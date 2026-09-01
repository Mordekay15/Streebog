package visuals;

import algorithms.*;
import analysis.Analysis;
import ciphers.CipherHolder;
import hashfunctions.Streebog;
import mac.HMAC;
import java.util.AbstractMap;
import utils.Conversions;
import utils.Generation;
import widgets.HexTextField;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;


public class InputDialogConstructor {
    private static InputDialogConstructor instance;
    private final JComponent parent;

    private InputDialogConstructor(JComponent parent) {
        this.parent = parent;
    }

    public static InputDialogConstructor get(JComponent parent) {
        if (instance == null) {
            instance = new InputDialogConstructor(parent);
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
                algoName = "ECB Mode Cipher 'Kuznyechik'";
                break;
            case "CBC<Kuznyechik>":
                algoName = "CBC Mode Cipher 'Kuznyechik'";
                break;
            case "CFB<Kuznyechik>":
                algoName = "CFB Mode Cipher 'Kuznyechik'";
                break;
            case "CTR<Kuznyechik>":
                algoName = "CTR Mode Cipher 'Kuznyechik'";
                break;
            case "OFB<Kuznyechik>":
                algoName = "OFB Mode Cipher 'Kuznyechik'";
                break;
            case "ECB<Magma>":
                algoName = "ECB Mode Cipher 'Magma'";
                break;
            case "CBC<Magma>":
                algoName = "CBC Mode Cipher 'Magma'";
                break;
            case "CFB<Magma>":
                algoName = "CFB Mode Cipher 'Magma'";
                break;
            case "CTR<Magma>":
                algoName = "CTR Mode Cipher 'Magma'";
                break;
            case "OFB<Magma>":
                algoName = "OFB Mode Cipher 'Magma'";
                break;
        }
        Initializer init = showInputDialog(algoName, keySize, defaultPadding, defaultInitialVector, false, null);
        return init == null ? null : algorithm.instantiate(Conversions.hex(init.key), init.initialVector, init.padding);
    }

    public MAC macDialog(CipherHolder cipher) {
        String name = cipher.getName();
        switch (name) {
            case "Kuznyechik":
                name = "CMAC Kuznyechik";
                break;
            case "Magma":
                name = "CMAC Magma";
                break;
        }
        Initializer init = showInputDialog(name, cipher.getKeySize(), null, null, false, null);
        return init == null ? null : new MAC(cipher, Conversions.hex(init.key));
    }

    public HMAC hmacDialog(Streebog hashfunction, byte[] mes){
        Initializer init = showInputDialogFile("HMAC Streebog");
        return init == null ? null : new HMAC(hashfunction, init.key.getBytes(),mes);
    }

    public AbstractMap.SimpleEntry<ECB, byte[]> visualizationDialog(CipherHolder cipher) {
        String name = cipher.getName();
        switch (name) {
            case "Kuznyechik":
                name = "Visualization of Cipher 'Kuznyechik' Operation";
                break;
            case "Magma":
                name = "Visualization of Cipher 'Magma' Operation";
                break;
        }
        byte[] data = new byte[cipher.getBlockSize()];
        Padding defaultPadding = ECB.defaultPadding;
        Initializer init = showInputDialog(name, cipher.getKeySize(), defaultPadding, null, false, data);
        return init == null ? null : new AbstractMap.SimpleEntry<>(ECB.withState(cipher, Conversions.hex(init.key), init.padding), data);
    }


    public Analysis analysisDialog(AlgorithmHolder algorithm) {
        String algoName = algorithm.getName();
        switch (algoName) {
            case "Kuznyechik":
                algoName = "Cryptographic Analysis of Cipher 'Kuznyechik'";
                break;
            case "Magma":
                algoName = "Cryptographic Analysis of Cipher 'Magma'";
                break;
        }
        int keySize = algorithm.getCipherHolder().getKeySize();
        Padding defaultPadding = algorithm.getDefaultPadding();
        byte[] defaultInitialVector = algorithm.getDefaultInitialVector();
        Initializer init = showInputDialog(algoName, keySize, defaultPadding, defaultInitialVector, true, null);
        return init == null ? null : new Analysis(algorithm, init.key, init.initialVector, init.padding);
    }

    private Initializer showInputDialogFile(String name){
        JTextField key = new JTextField();
        List<JComponent> inputs = new ArrayList<>(List.of(new JLabel("Enter key"), key));

        int result = JOptionPane.showConfirmDialog(parent, inputs.toArray(), name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String resultKey = key.getText();
            /*
            if (resultKey.split(" ").length != keyLength) {
                JOptionPane.showMessageDialog(parent, String.format("Secret key must be %d bytes long", keyLength), "Invalid data", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            byte[] resultInitialVector = defaultInitialVector == null ? null : initialVector.getBytes();
            if (resultInitialVector != null && resultInitialVector.length != defaultInitialVector.length) {
                JOptionPane.showMessageDialog(parent, String.format("Initialization vector must be %d bytes long", defaultInitialVector.length), "Invalid data", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            Padding resultPadding = defaultPadding == null ? null : (Padding) padding.getSelectedItem();
            if (data != null) {
                System.arraycopy(dataInput.getBytes(), 0, data, 0, data.length);
            }*/
            return new Initializer(resultKey, null, null);
        } else return null;
    }

    private Initializer showInputDialog(String name, int keyLength, Padding defaultPadding, byte[] defaultInitialVector, boolean keyUnknown, byte[] data) {
//        byte[] randomKey = Generation.randomBytes(keyLength);
        String randomKey = "88 99 AA BB CC DD EE FF 00 11 22 33 44 55 66 77 FE DC BA 98 76 54 32 10 01 23 45 67 89 AB CD EF";

        JTextField key = new HexTextField(Conversions.rawS(randomKey), keyLength, keyUnknown);
        HexTextField initialVector = new HexTextField(false);
        JComboBox<Padding> padding = new JComboBox<>(new Padding[]{Padding._OOOOOO, Padding._8OOOOO});
        HexTextField dataInput = new HexTextField(false);

        List<JComponent> inputs = new ArrayList<>(List.of(new JLabel("Secret key"), key));

        if (defaultPadding != null) {
            inputs.add(new JLabel("Padding method"));
            padding.setSelectedItem(defaultPadding);
            inputs.add(padding);
        }

        if (defaultInitialVector != null) {
            inputs.add(new JLabel("Initialization vector"));
            initialVector.setBytesLength(defaultInitialVector.length);
            initialVector.setText(Conversions.hex(defaultInitialVector));
            inputs.add(initialVector);
        }

        if (data != null) {
            inputs.add(new JLabel(String.format("Data block (%s bytes)", data.length)));
            dataInput.setBytesLength(data.length);
            dataInput.setText(Conversions.hex(Generation.randomBytes(data.length)));
            inputs.add(dataInput);
        }

        int result = JOptionPane.showConfirmDialog(parent, inputs.toArray(), name, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String resultKey = key.getText();
            if (resultKey.split(" ").length != keyLength) {
                JOptionPane.showMessageDialog(parent, String.format("Secret key must be %d bytes long", keyLength), "Invalid data", JOptionPane.WARNING_MESSAGE);
                return null;
            }
            byte[] resultInitialVector = defaultInitialVector == null ? null : initialVector.getBytes();
            if (resultInitialVector != null && resultInitialVector.length != defaultInitialVector.length) {
                JOptionPane.showMessageDialog(parent, String.format("Initialization vector must be %d bytes long", defaultInitialVector.length), "Invalid data", JOptionPane.WARNING_MESSAGE);
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
