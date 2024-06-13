package workers;

import algorithms.ECB;
import utils.Conversions;
import windows.VisualizationMagmaRound;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;


public class VisualizationExecutorMagmaRound extends SwingWorker<byte[], Object> {
    private final ECB algorithm;
    private final byte[] data;
    private final JDesktopPane parent;
    private final JDialog dialog;

    public VisualizationExecutorMagmaRound(JDesktopPane parentComponent, ECB algorithm, byte[] data) {
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
                String block = String.valueOf(Conversions.hex(data));
                String key = String.valueOf(Conversions.hex(algorithm.getKey()));
                VisualizationMagmaRound visRound = new VisualizationMagmaRound(parent, algorithm.getCipher().getState(), result, key, block);
                visRound.setVisible(true);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
