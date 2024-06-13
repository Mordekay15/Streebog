package workers;

import algorithms.ECB;
import windows.VisualizationMagmaKey;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;


public class VisualizationExecutorMagmaKey extends SwingWorker<byte[], Object> {
    private final ECB algorithm;
    private final byte[] data;
    private final JDesktopPane parent;
    private final JDesktopPane parentK;
    private final JDialog dialog;
    private final JDialog dialogK;

    public VisualizationExecutorMagmaKey(JDesktopPane parentComponent, ECB algorithm, byte[] data) {
        this.parent = parentComponent;
        this.parentK = parentComponent;
        this.algorithm = algorithm;
        this.data = data;

        this.dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parentComponent), "Зашифрование...");
        this.dialogK = new JDialog((Frame) SwingUtilities.getWindowAncestor(parentComponent), "Зашифрование...");
        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setIndeterminate(true);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialog.add(progressBar);
        dialog.pack();

        dialogK.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialogK.add(progressBar);
        dialogK.pack();
    }

    @Override
    protected byte[] doInBackground() {
        dialog.setVisible(true);
        dialogK.setVisible(true);
        return algorithm.encrypt(data);
    }

    @Override
    protected void done() {
        dialog.setVisible(false);
        dialog.dispose();

        dialogK.setVisible(false);
        dialogK.dispose();
        try {
            byte[] result = this.get();
            if (result == null)
                JOptionPane.showMessageDialog(parent, "Выполнение невозможно", "Выполнение невозможно", JOptionPane.ERROR_MESSAGE);
            else {
                VisualizationMagmaKey visKey = new VisualizationMagmaKey(parentK, algorithm.getCipher().getState(), result);
                visKey.setVisible(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
