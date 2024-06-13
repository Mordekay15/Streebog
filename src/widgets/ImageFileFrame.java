package widgets;

import utils.Conversions;
import windows.MainWindow;
import windows.VisualizationExpandKey;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.*;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;


public class ImageFileFrame extends FileFrame {
    private static final Set<String> formats = Set.of("gif", "png", "jpg");
//    TODO: при работе с сохранением изображений --> использовать ТОЛЬКО .png
    private static final FileFilter filter = new FileNameExtensionFilter("Image file filter", "png", "bmp");

    private final BufferedImage image;
    private final String format;

    protected ImageFileFrame(BufferedImage content, String name, String format) {
        super(name, "image");
        this.format = format;
        this.image = content;
        this.resizable = true;
        container.add(new JLabel(new ImageIcon(content)));
    }


    private BufferedImage copyImageReplacingData(byte[] data) {
        ColorModel cm = image.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = image.copyData(null);
        int width = image.getWidth();
        int height = image.getHeight();
        raster.setDataElements(0, 0, width, height, data);
        System.out.println(width +"x"+ width);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    @Override
    protected void switchToFormat(byte[] content) {
        super.switchToFormat(content);
        resizable = true;
        setSize(getDimension());
        setBytesContent(content);
        container.add(new JLabel(new ImageIcon(image)));
    }

    @Override
    protected void switchToBinary(byte[] content) {
        super.switchToBinary(content);
        resizable = true;
    }

    private static String getFormat(File file) {
        String name = file.getName();
        Optional<String> extension = Optional.of(name).filter(f -> f.contains(".")).map(f -> f.substring(name.lastIndexOf(".") + 1));
        String format = extension.orElse("png").toLowerCase();
        return formats.contains(format) ? format : "png";
    }

    private byte[] getBytesContent() {
        byte[] data = new byte[image.getWidth() * image.getHeight() * image.getRaster().getNumDataElements()];
        image.getRaster().getDataElements(0, 0, image.getWidth(), image.getHeight(), data);
        return data;
    }

    private void setBytesContent(byte[] bytes) {
        image.getRaster().setDataElements(0, 0, image.getWidth(), image.getHeight(), bytes);
    }

    private static byte[] toBytes(BufferedImage image, String format) {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, format, stream);
            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[0];
        }
    }

    private static BufferedImage toImage(byte[] bytes) {
        InputStream inputStream = new ByteArrayInputStream(bytes);
        BufferedImage inputPicture;
        try {
            if (bytes.length == 0) {
                throw new IOException("Входной массив пуст");
            }
            inputPicture = ImageIO.read(inputStream);
        } catch (IOException e) {
            inputPicture = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
        }
        return inputPicture;
    }


    public static ImageFileFrame createRandom() {
        File imageFile = getRandomFileFromResources("/images");
        return createFromFile(imageFile.toPath(), imageFile.getName(), getFormat(imageFile), false);
    }

    public static ImageFileFrame createFromFile(Component parent) {
        File file = pickFile(filter, parent);
        if (file != null) {
            return createFromFile(file.toPath(), file.toPath().getFileName().toString(), getFormat(file), true);
        } else {
            return null;
        }
    }

    private static ImageFileFrame createFromFile(Path file, String name, String format, boolean addSource) {
        byte[] content = readBytesFromFile(file);
        ImageFileFrame frame = new ImageFileFrame(toImage(content), name, format);
        if (frame.image.getWidth() * frame.image.getHeight() > 100000) {
            JOptionPane.showMessageDialog(frame, "При выборе изображения >100K байт без сжатия обработка может быть медленной.");
        }
        if (addSource) {
            frame.backup = file;
        }
        return frame;
    }


    @Override
    public byte[] getByteContent() {
        if (isBinary) {
            return super.getByteContent();
        } else {
            return getBytesContent();
        }
    }

    @Override
    protected byte[] getAllByteContent() {
        if (isBinary) {
            setBytesContent(super.getByteContent());
        }
        return toBytes(image, format);
    }

    @Override
    protected Dimension getDimension() {
        Dimension d = new Dimension();
        d.width = image.getWidth() + 10;
        d.height = image.getHeight() + 10;
        return d;
    }

    @Override
    public ImageFileFrame process(byte[] processed, String code) {
        return new ImageFileFrame(copyImageReplacingData(processed), name += "_" + code, format);
    }
}
