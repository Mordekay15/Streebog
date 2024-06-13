package widgets;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;


public class BinaryFileFrame extends FileFrame {
    private static final int MAX_WIDTH = 300;
    private static final int MAX_HEIGHT = 300;

    public BinaryFileFrame(byte[] content, String name) {
        super(name, "binary");
        this.iconable = false;
        this.isBinary = true;
        switchToBinary(content);
    }


    public static BinaryFileFrame createEmpty() {
        return new BinaryFileFrame(new byte[0], "new_file.bin");
    }

    public static BinaryFileFrame createRandom() {
        File binaryFile = getRandomFileFromResources("/binary");
        return createFromFile(binaryFile.toPath(), binaryFile.getName(), false);
    }

    public static BinaryFileFrame createFromFile(Component parent) {
        File file = pickFile(null, parent);
        if (file != null) {
            return createFromFile(file.toPath(), file.toPath().getFileName().toString(), true);
        } else {
            return null;
        }
    }

    private static BinaryFileFrame createFromFile(Path file, String name, boolean addSource) {
        BinaryFileFrame frame = new BinaryFileFrame(readBytesFromFile(file), name);
        if (addSource) {
            frame.backup = file;
        }
        return frame;
    }


    @Override
    protected Dimension getDimension() {
        return new Dimension(MAX_WIDTH, MAX_HEIGHT);
    }

    @Override
    protected void switchToFormat(byte[] content) {
    }

    @Override
    public BinaryFileFrame process(byte[] processed, String code) {
        return new BinaryFileFrame(processed, name += "_" + code);
    }
}
