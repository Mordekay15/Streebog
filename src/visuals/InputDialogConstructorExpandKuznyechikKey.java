package visuals;

import algorithms.*;
import analysis.Analysis;
import ciphers.CipherHolder;
import org.apache.commons.math3.util.Pair;
import utils.Conversions;
import utils.Generation;
import widgets.HexTextField;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class InputDialogConstructorExpandKuznyechikKey {
    private static InputDialogConstructorExpandKuznyechikKey instance;
    private final JComponent parent;

    private InputDialogConstructorExpandKuznyechikKey(JComponent parent) {
        this.parent = parent;
    }

    public static InputDialogConstructorExpandKuznyechikKey get(JComponent parent) {
        if (instance == null) {
            instance = new InputDialogConstructorExpandKuznyechikKey(parent);
        }
        return instance;
    }


    private static class Initializer {
        public String key;
        public byte[] initialVector;
        public Padding padding;

        Initializer(String key, byte[] initialVector, Padding padding) {
            this.key = key;
            this.initialVector = initialVector;
            this.padding = padding;
        }
    }


    public Algorithm algorithmDialog(AlgorithmHolder algorithm) {
        int keySize = algorithm.getCipherHolder().getKeySize();
        Padding defaultPadding = algorithm.getDefaultPadding();
        byte[] defaultInitialVector = algorithm.getDefaultInitialVector();
        System.out.println(algorithm.getName());
        String algoName = algorithm.getName();
        switch (algoName) {
            case "ECB<Kuznyechik>":
                algoName = "����� ������� ������ ����� '��������'";
                break;
            case "CBC<Kuznyechik>":
                algoName = "����� ������� ������ � ����������� ����� '��������'";
                break;
            case "CFB<Kuznyechik>":
                algoName = "����� ������������ � �������� ������ �� ���������� ����� '��������'";
                break;
            case "CTR<Kuznyechik>":
                algoName = "����� ������������ ����� '��������'";
                break;
            case "OFB<Kuznyechik>":
                algoName = "����� ������������ � �������� ������ �� ������ ����� '��������'";
                break;
            case "ECB<Magma>":
                algoName = "����� ������� ������ ����� '�����'";
                break;
            case "CBC<Magma>":
                algoName = "����� ������� ������ � ����������� ����� '�����'";
                break;
            case "CFB<Magma>":
                algoName = "����� ������������ � �������� ������ �� ���������� ����� '�����'";
                break;
            case "CTR<Magma>":
                algoName = "����� ������������ ����� '�����'";
                break;
            case "OFB<Magma>":
                algoName = "����� ������������ � �������� ������ �� ������ ����� '�����'";
                break;
        }
        Initializer init = showInputDialog(algoName, keySize, defaultPadding, defaultInitialVector, false, null);
        return init == null ? null : algorithm.instantiate(Conversions.hex(init.key), init.initialVector, init.padding);
    }

    public MAC macDialog(CipherHolder cipher) {
        String name = cipher.getName();
        switch (name) {
            case "Kuznyechik":
                name = "CMAC ��������";
                break;
            case "Magma":
                name = "CMAC �����";
                break;
        }
        Initializer init = showInputDialog(name, cipher.getKeySize(), null, null, false, null);
        return init == null ? null : new MAC(cipher, Conversions.hex(init.key));
    }

    public Pair<ECB, byte[]> visualizationDialog(CipherHolder cipher) {
        String name = cipher.getName();
        switch (name) {
            case "Kuznyechik":
                name = "������������ ������ ����� '��������'";
                break;
            case "Magma":
                name = "������������ ������ ����� '�����'";
                break;
        }
        byte[] data = new byte[cipher.getBlockSize()];
        Padding defaultPadding = ECB.defaultPadding;
        Initializer init = showInputDialog(name, cipher.getKeySize(), defaultPadding, null, false, data);
        return init == null ? null : new Pair<>(ECB.withState(cipher, Conversions.hex(init.key), init.padding), data);
    }


    public Analysis analysisDialog(AlgorithmHolder algorithm) {
        String algoName = algorithm.getName();
        switch (algoName) {
            case "Kuznyechik":
                algoName = "������ ��������������� ����� '��������'";
                break;
            case "Magma":
                algoName = "������ ��������������� ����� '�����'";
                break;
        }
        int keySize = algorithm.getCipherHolder().getKeySize();
        Padding defaultPadding = algorithm.getDefaultPadding();
        byte[] defaultInitialVector = algorithm.getDefaultInitialVector();
        Initializer init = showInputDialog(algoName, keySize, defaultPadding, defaultInitialVector, true, null);
        return init == null ? null : new Analysis(algorithm, init.key, init.initialVector, init.padding);
    }


    private Initializer showInputDialog(String name, int keyLength, Padding defaultPadding, byte[] defaultInitialVector, boolean keyUnknown, byte[] data) {
//        byte[] randomKey = Generation.randomBytes(keyLength);
        String randomKey = "88 99 AA BB CC DD EE FF 00 11 22 33 44 55 66 77 FE DC BA 98 76 54 32 10 01 23 45 67 89 AB CD EF";

        JTextField key = new HexTextField(Conversions.rawS(randomKey), keyLength, keyUnknown);
        HexTextField initialVector = new HexTextField(false);
        JComboBox<Padding> padding = new JComboBox<>(new Padding[]{Padding._OOOOOO, Padding._8OOOOO});
        HexTextField dataInput = new HexTextField(false);

        List<JComponent> inputs = new ArrayList<>(List.of(new JLabel("��������� ����"), key));

        if (defaultPadding != null) {
//            inputs.add(new JLabel("������ ����������"));
            padding.setSelectedItem(defaultPadding);
//            inputs.add(padding);
        }

        if (defaultInitialVector != null) {
//            inputs.add(new JLabel("�������������"));
            initialVector.setBytesLength(defaultInitialVector.length);
            initialVector.setText(Conversions.hex(defaultInitialVector));
//            inputs.add(initialVector);
        }

        if (data != null) {
//            inputs.add(new JLabel(String.format("���� ������ ����� ������ (%s ����)", data.length)));
            dataInput.setBytesLength(data.length);
            dataInput.setText(Conversions.hex(Generation.randomBytes(data.length)));
//            inputs.add(dataInput);
        }

        int result = JOptionPane.showConfirmDialog(parent, inputs.toArray(), name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String resultKey = key.getText();
            if (resultKey.split(" ").length != keyLength) {
                JOptionPane.showMessageDialog(parent, String.format("���� ������ ���� ������ %d ����", keyLength), "�������� ������", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            byte[] resultInitialVector = defaultInitialVector == null ? null : initialVector.getBytes();
            if (resultInitialVector != null && resultInitialVector.length != defaultInitialVector.length) {
                JOptionPane.showMessageDialog(parent, String.format("��������� ������ ������ ���� ������ %d ����", defaultInitialVector.length), "�������� ������", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            Padding resultPadding = defaultPadding == null ? null : (Padding) padding.getSelectedItem();
            if (data != null) {
                System.arraycopy(dataInput.getBytes(), 0, data, 0, data.length);
            }
            return new Initializer(resultKey, resultInitialVector, resultPadding);
        } else return null;
    }
}
