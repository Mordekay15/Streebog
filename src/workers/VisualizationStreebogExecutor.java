package workers;

import algorithms.ECB;
import hashfunctions.HashFunction;
import hashfunctions.Streebog;
import windows.VisualizationStreebog;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class VisualizationStreebogExecutor extends SwingWorker<byte[], Object> {
    private final byte[] data;
    private final HashFunction streebog;
    private final JDesktopPane parent;
    private final JDialog dialog;

    public VisualizationStreebogExecutor(JDesktopPane parentComponent, byte[] data, HashFunction streebog) {
        this.parent = parentComponent;
        this.streebog = streebog;
        this.data = data;

        this.dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parentComponent), "Хеширование");

        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setIndeterminate(true);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialog.add(progressBar);
        dialog.pack();

    }
    @Override
    protected byte[] doInBackground() {
        dialog.setVisible(true);
        return streebog.get_hash(data);
    }
    @Override
    protected void done() {
        dialog.setVisible(false);
        dialog.dispose();

        try {
            byte[] result = this.get();
            if (result == null) {
                JOptionPane.showMessageDialog(parent, "Выполнение невозможно", "Выполнение невозможно", JOptionPane.ERROR_MESSAGE);
            } else {
                //String key = String.valueOf(Conversions.hex(algorithm.getKey()));
                //VisualizationExpandKey visExpKey = new VisualizationExpandKey(parent, algorithm.getCipher().getState(), result, key, algorithm.getKey());
                //visExpKey.setVisible(true);
                VisualizationStreebog visStreebog = new VisualizationStreebog(parent, streebog.getState(), result);
                visStreebog.setVisible(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}

