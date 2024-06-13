package workers;

import algorithms.ECB;
import utils.Conversions;
import windows.VisualizationExpandKey;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;


public class VisualizationExpandKeyExecutor extends SwingWorker<byte[], Object> {
    private final ECB algorithm;
    private final byte[] data;
    private final JDesktopPane parent;
    private final JDialog dialog;

    public VisualizationExpandKeyExecutor(JDesktopPane parentComponent, ECB algorithm, byte[] data) {
        this.parent = parentComponent;
        this.algorithm = algorithm;
        this.data = data;

        this.dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parentComponent), "Зашифрование...");
        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setIndeterminate(true);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialog.add(progressBar);
        dialog.pack();
    }

    @Override
    protected byte[] doInBackground() {
        dialog.setVisible(true);
        return algorithm.encrypt(data);
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
                String key = String.valueOf(Conversions.hex(algorithm.getKey()));
                VisualizationExpandKey visExpKey = new VisualizationExpandKey(parent, algorithm.getCipher().getState(), result, key, algorithm.getKey());
                visExpKey.setVisible(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
