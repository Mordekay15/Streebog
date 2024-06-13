package widgets;

import utils.Conversions;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Random;


public abstract class FileFrame extends JInternalFrame implements PropertyChangeListener, InternalFrameListener {
    private static final int STEP_X = 30;
    private static final int STEP_Y = 20;
    private static Point latest = new Point();
    protected static final Random generator = new Random();

    private HexTextArea bytes;
    protected JPanel container;

    protected boolean isBinary = false;
    protected Path backup = null;
    protected String name, type;

    protected FileFrame(String name, String type) {
        this.name = name;
        this.type = type;
        this.closable = true;
        this.maximizable = true;
        this.iconable = true;

        this.container = new JPanel(new BorderLayout());
        this.container.setBorder(new EmptyBorder(2, 2, 2, 2));
        JScrollPane scroll = new JScrollPane(container);
        add(scroll);

        addPropertyChangeListener(this);
        addInternalFrameListener(this);
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
    }


    protected static File getRandomFileFromResources(String path) {
        URL textFilesUrl = BinaryFileFrame.class.getResource("/files" + path);
        File[] textFiles = new File(Objects.requireNonNull(textFilesUrl).getFile()).listFiles();

        int textFileIndex = generator.nextInt(Objects.requireNonNull(textFiles).length);
        return Objects.requireNonNull(textFiles)[textFileIndex];
    }

    protected static File pickFile(FileFilter filter, Component parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите файл, который нужно открыть");
        if (filter != null) {
            fileChooser.setFileFilter(filter);
        }
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else return null;
    }

    protected static byte[] readBytesFromFile(Path path) {
        byte[] content;
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
            content = new byte[0];
        }
        return content;
    }

    public byte[] getByteContent() {
        return bytes.getBytes();
    }

    protected byte[] getAllByteContent() {
        return bytes.getBytes();
    }

    protected void switchToBinary(byte[] content) {
        isBinary = true;
        resizable = false;
        container.removeAll();
        bytes = new HexTextArea(Conversions.hex(content), 25, 20);
        bytes.setEditable(isEditable());
        bytes.setLineWrap(true);
        bytes.setWrapStyleWord(true);
        container.add(bytes);
    }

    protected void switchToFormat(byte[] content) {
        isBinary = false;
        container.removeAll();
        String textContent = new String(content, StandardCharsets.UTF_8);
        JTextArea textArea = new JTextArea(textContent);
        textArea.setEditable(isEditable());
        container.add(textArea);
    }



    public abstract FileFrame process(byte[] processed, String code);

    protected abstract Dimension getDimension();


    protected boolean isEditable() {
        return backup == null || Files.isWritable(backup);
    }

    private void display(int parentWidth, int parentHeight) {
        setSize(getDimension());
        title = name + " (" + (isEditable() ? "editable" : "read-only") + ") - " + type;

        int newWidth = latest.x + getWidth();
        int newHeight = latest.y + getHeight();

        if (newWidth > parentWidth || newHeight > parentHeight) latest = new Point(STEP_X, STEP_Y);
        else latest = new Point(latest.x + STEP_X, latest.y + STEP_Y);

        setLocation(latest);
        setVisible(true);
    }

    public void display(JDesktopPane parent) {
        try {
            display(parent.getWidth(), parent.getHeight());
            parent.add(this);
            parent.setSelectedFrame(this);
            setSelected(true);
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }

//    TODO: предыдущий вариант (изначальный вариант (с сохранением с новым именем))

//    private void save() throws IOException {
//        if (backup == null || !Files.isWritable(backup)) {
//            JFileChooser fileChooser = new JFileChooser();
//            fileChooser.setDialogTitle("Выберите место для сохранения файла");
//            fileChooser.setApproveButtonText("Save file");
//            fileChooser.setSelectedFile(Path.of(System.getProperty("user.home"), this.name).toFile());
//            int result = fileChooser.showOpenDialog(this);
//            if (result == JFileChooser.APPROVE_OPTION) {
//                backup = fileChooser.getSelectedFile().toPath();
//            } else throw new IOException("Файл не выбран");
//        }
//        if (Files.isWritable(backup)) {
//            Files.write(backup, getAllByteContent());
//        } else throw new IOException("Файл невозможно записать");
//    }

//    TODO: основной вариант сохранения (сделанный)
    private void save() throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Выберите место для сохранения файла");
        fileChooser.setApproveButtonText("Save file");

        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            String filePath = selectedFile.getParent();

            String newFileName = fileName.substring(0, fileName.lastIndexOf(".")) + getFileExtension(fileName);
            File newFile = new File(filePath, newFileName);

            backup = newFile.toPath();
            Files.write(backup, getAllByteContent());
        } else {
            throw new IOException("Файл не выбран");
        }
    }

    private String getFileExtension(String fileName) {
        int extensionIndex = fileName.lastIndexOf(".");
        if (extensionIndex >= 0) {
            return fileName.substring(extensionIndex);
        }
        return "";
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            if (JInternalFrame.IS_MAXIMUM_PROPERTY.equals(evt.getPropertyName()) && (boolean) evt.getNewValue()) {
                setMaximum(false);
                save();
            } else if (JInternalFrame.IS_ICON_PROPERTY.equals(evt.getPropertyName()) && (boolean) evt.getNewValue()) {
                setIcon(false);
                if (isBinary) {
                    switchToFormat(getByteContent());
                } else switchToBinary(getByteContent());
            }
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Файл невозможно сохранить\n" + ex.getMessage());
        }
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        if (!isEditable()) {
            dispose();
            return;
        }

        int close = JOptionPane.showConfirmDialog(this, "Сохранить файл перед закрытием?", "Сохранить файл", JOptionPane.YES_NO_OPTION);
        if (close == JOptionPane.NO_OPTION) {
            dispose();
            return;
        }

        try {
            save();
            dispose();
        } catch (IOException ex) {
            int closeForce = JOptionPane.showConfirmDialog(this, ex, "Закрыть в любом случае", JOptionPane.YES_NO_OPTION);
            if (closeForce == JOptionPane.YES_OPTION) {
                dispose();
            }
        }
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }
}
