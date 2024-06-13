package widgets;


import utils.Conversions;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static utils.OSInfo.*;


public class TextFileFrame extends FileFrame {
    private static final int MAX_WIDTH = 500;
    private static final int MAX_HEIGHT = 300;

    private JTextArea text;

    private TextFileFrame(byte[] content, String name) {
        super(name, "text");
        this.resizable = true;
        switchToFormat(content);
    }


    @Override
    protected void switchToFormat(byte[] content) {
        resizable = true;
        super.switchToFormat(content);
        text = new JTextArea(Conversions.raw(content), 20, 25);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setEditable(isEditable());
        container.add(text);
    }

    public static TextFileFrame createEmpty() {
        return new TextFileFrame(new byte[0], "new_file");
    }

    public static TextFileFrame createReadme() {
        TextFileFrame readme = new TextFileFrame(Conversions.raw("Добро пожаловать в  приложение Литорея (LitoreR), которое поможет\n" +
                "самостоятельно изучить отечественные криптопреобразования.\n" +
                "\n" +
                "Версия 0.2 - предварительная с функциональностью ограниченной только инструментами исследования российских стандартов шифрования Магма и Кузнечик во всех предусмотренных режимах работы с файлами данных."), "ReadMe");
        readme.setBackground(Color.RED);
        return readme;
    }

    public static TextFileFrame createRandom() {
        File textFile = getRandomFileFromResources("/text");
        return createFromFile(textFile.toPath(), textFile.getName(), false);
    }

    public static TextFileFrame createFromFile(Component parent) {
        File file = pickFile(null, parent);
        if (file != null) {
            return createFromFile(file.toPath(), file.toPath().getFileName().toString(), true);
        }
        else {
            return null;
        }
    }

    // TODO: main
    private static TextFileFrame createFromFile(Path file, String name, boolean addSource) {
        byte[] content = readBytesFromFile(file);
        if (content.length == 0) content = Conversions.raw(String.format("Не удалось прочитать файл '%s'", file.toAbsolutePath()));
        TextFileFrame frame = new TextFileFrame(content, name);
        if (addSource) frame.backup = file;
        return frame;
    }


    @Override
    public byte[] getByteContent() {
        if (isBinary) {
            return super.getByteContent();
        }
        else {
            return Conversions.raw(text.getText());
        }
    }

    @Override
    protected byte[] getAllByteContent() {
        return getByteContent();
    }

    @Override
    protected Dimension getDimension() {
        Dimension d = new Dimension();
        d.width = Math.min(text.getPreferredSize().width, MAX_WIDTH);
        d.height = Math.min(text.getPreferredSize().height, MAX_HEIGHT);
        return d;
    }

    @Override
    public TextFileFrame process(byte[] processed, String code) {
        return new TextFileFrame(processed, name += "_" + code);
    }
}
