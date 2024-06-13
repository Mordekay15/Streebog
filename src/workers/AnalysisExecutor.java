package workers;

import analysis.Analysis;
import widgets.FileFrame;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;


public class AnalysisExecutor extends SwingWorker<byte[], Object> {
    private final Analysis analysis;
    private final byte[] data;
    private final JDesktopPane parent;
    private final ProgressMonitor monitor;
    private final Function<byte[], FileFrame> callback;

    public AnalysisExecutor(Analysis analysis, byte[] data, JDesktopPane parentComponent, Function<byte[], FileFrame> callback) {
        this.analysis = analysis;
        this.data = data;
        this.parent = parentComponent;
        this.monitor = new ProgressMonitor(parentComponent, "������� ���� ��������� ���������", "", 0, 100);
        this.callback = callback;
    }

    @Override
    public byte[] doInBackground() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date init = new Date();
        AtomicReference<Date> start = new AtomicReference<>(new Date());
        AtomicReference<Date> current = new AtomicReference<>(start.get());
        return analysis.analyze(data, (Double progress) -> {
            current.set(new Date());
            long elapsed = current.get().getTime() - init.getTime();
            monitor.setProgress((int) (progress * 100));
            monitor.setNote("��������� ����� �������� ���� ���������: " + formatter.format(new Date((long) (init.getTime() + elapsed / progress))));
            start.set(current.get());
            if (monitor.isCanceled()) throw new RuntimeException("������ ����� ������ ���� �������");
        });
    }

    @Override
    protected void done() {
        try {
            byte[] result = get();
            if (result == null)
                JOptionPane.showMessageDialog(parent, "���������� ������� ����������� �� ������", "������ ����� ������ ���� ��������", JOptionPane.ERROR_MESSAGE);
            else {
                FileFrame frame = callback.apply(result);
                if (frame != null) frame.display(parent);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
