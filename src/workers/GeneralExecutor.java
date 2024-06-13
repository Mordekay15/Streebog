package workers;

import utils.Conversions;
import widgets.FileFrame;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;


public class GeneralExecutor extends SwingWorker<byte[], Object> {
    private final Callable<byte[]> payload;
    private final JDesktopPane parent;
    private final Function<byte[], FileFrame> callback;
    private final String process;
    private final JDialog dialog;

    public GeneralExecutor(JDesktopPane parentComponent, String process, Callable<byte[]> payload, Function<byte[], FileFrame> callback) {
        this.parent = parentComponent;
        this.process = process;
        this.payload = payload;
        this.callback = callback;

        this.dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parentComponent), process + "...");
        JProgressBar progressBar = new JProgressBar(JProgressBar.HORIZONTAL);
        progressBar.setIndeterminate(true);
        dialog.setLayout(new FlowLayout(FlowLayout.LEFT));
        dialog.add(progressBar);
        dialog.pack();
    }

    @Override
    protected byte[] doInBackground() throws Exception {
        dialog.setVisible(true);
        return payload.call();
    }

//    @Override
//    protected void done() {
//        dialog.setVisible(false);
//        dialog.dispose();
//        try {
//            byte[] result = this.get();
//            if (result == null) {
//                JOptionPane.showMessageDialog(parent, process + " не удалось", "Выполнение невозможно", JOptionPane.ERROR_MESSAGE);
//            } else {
//                FileFrame frame = callback.apply(result);
//                if (frame != null) {
//                    frame.display(parent);
//                }
//            }
//        } catch (InterruptedException | ExecutionException e) {
//            throw new RuntimeException(e);
//        }
//    }
    @Override
    protected void done() {
        dialog.setVisible(false);
        dialog.dispose();
        try {
            byte[] result = this.get();
            if (result == null) {
                JOptionPane.showMessageDialog(parent, process + " не удалось", "Выполнение невозможно", JOptionPane.ERROR_MESSAGE);
            } else {
                FileFrame frame = callback.apply(result);
                if (frame != null) {
                    frame.display(parent);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            if (e.getMessage() == null && e.getMessage().contains("selected")) {
                System.out.println(e.getMessage().length());
                JOptionPane.showMessageDialog(parent, process + " " + e, "Выполнение невозможно", JOptionPane.ERROR_MESSAGE);
            }
            if (e.getMessage() != null && !e.getMessage().contains("algorithm")) {
                JOptionPane.showMessageDialog(parent, process + " невозможно, потому что файл данных не выбран", "Выполнение невозможно", JOptionPane.ERROR_MESSAGE);
            }
//            JOptionPane.showMessageDialog(parent, process + " " + e, "Выполнение невозможно", JOptionPane.ERROR_MESSAGE);
        }
    }
}
