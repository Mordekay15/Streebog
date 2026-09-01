package windows;

import algorithms.*;
import ciphers.CipherHolder;
import ciphers.Kuznyechik;
import hashfunctions.HashFunction;
import hashfunctions.HashFunctionHolder;
import hashfunctions.Streebog;
import utils.RoundedBorder;
import visuals.MenuItemConstructor;
import widgets.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MainWindow extends JFrame {
    private JButton support;
    private JDesktopPane desktop;

    private final List<CipherHolder> ciphers;
    private final List<HashFunctionHolder> hashfunctions;

    //private List<Class<? extends HashFunction>> hashfunctions;
    private final List<Class<? extends Algorithm>> algorithms = List.of(ECB.class, CBC.class, CFB.class, CTR.class, OFB.class);

    public MainWindow(String name, CipherHolder... holders) {
//        super(String.format("%s", Arrays.stream(holders).map(CipherHolder::getName).collect(Collectors.joining(", "))));
        super("Litorea");
        ciphers = Arrays.stream(holders).collect(Collectors.toList());


        HashFunctionHolder[] hashFunctionHolders1 = new HashFunctionHolder[] {new HashFunctionHolder(Streebog.class)};
        hashfunctions = Arrays.stream(hashFunctionHolders1).collect(Collectors.toList());

        try {
            setupContents();
            setupMenu();

            setDefaultCloseOperation(EXIT_ON_CLOSE);
//            setIconImage(new ImageIcon("files/images/183993.png").getImage()); // TODO add favicon image
            getRootPane().setDefaultButton(support);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Application initialization error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void setupContents() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(new EmptyBorder(5, 5, 5, 5));

        desktop = new JDesktopPane();
        // TODO: add a welcome screen (+ : replace the createEmpty() call with createReadme())
        Objects.requireNonNull(TextFileFrame.createReadme()).display(desktop);

        mainContainer.add(desktop);
        setContentPane(mainContainer);
    }


    private void setupMenu() {
        support = new JButton("?"); // TODO use image
        support.setFocusPainted(false);
        support.setBorder(new RoundedBorder(5));

        JMenuItem openSystem = new JMenu("Open file");
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("Open as text (.txt)", () -> TextFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("Open as binary (.bin)", () -> BinaryFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("Open as image (.png, .bmp)", () -> ImageFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("Open help file (ReadMe.txt)", TextFileFrame::createReadme));

        JMenu createFile = new JMenu("New file");
        createFile.add(MenuItemConstructor.get(desktop).fileCreateItem("Create text file (.txt)", TextFileFrame::createEmpty));
        createFile.add(MenuItemConstructor.get(desktop).fileCreateItem("Create binary file (.bin)", BinaryFileFrame::createEmpty));

        JMenu file = new JMenu("File");
        file.add(createFile);
        file.add(openSystem);



        JMenu encrypt = new JMenu("Encryption");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "Kuznyechik";
                    break;
                case "Magma":
                    name = "Magma";
                    break;
            }
            JMenu encryptor = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "Electronic Codebook (ECB)";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "Cipher Block Chaining (CBC)";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "Cipher Feedback (CFB)";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "Counter (CTR)";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "Output Feedback (OFB)";
                        break;
                    case "ECB<Magma>":
                        holderName = "Electronic Codebook (ECB)";
                        break;
                    case "CBC<Magma>":
                        holderName = "Cipher Block Chaining (CBC)";
                        break;
                    case "CFB<Magma>":
                        holderName = "Cipher Feedback (CFB)";
                        break;
                    case "CTR<Magma>":
                        holderName = "Counter (CTR)";
                        break;
                    case "OFB<Magma>":
                        holderName = "Output Feedback (OFB)";
                        break;
                }
                encryptor.add(MenuItemConstructor.get(desktop).encodeDecodeItem(holderName, holder, MenuItemConstructor.Mode.ENCRYPT));
            }
            encrypt.add(encryptor);
        }

        JMenu decrypt = new JMenu("Decryption");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "Kuznyechik";
                    break;
                case "Magma":
                    name = "Magma";
                    break;
            }
            JMenu decryptor = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "Electronic Codebook (ECB)";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "Cipher Block Chaining (CBC)";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "Cipher Feedback (CFB)";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "Counter (CTR)";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "Output Feedback (OFB)";
                        break;
                    case "ECB<Magma>":
                        holderName = "Electronic Codebook (ECB)";
                        break;
                    case "CBC<Magma>":
                        holderName = "Cipher Block Chaining (CBC)";
                        break;
                    case "CFB<Magma>":
                        holderName = "Cipher Feedback (CFB)";
                        break;
                    case "CTR<Magma>":
                        holderName = "Counter (CTR)";
                        break;
                    case "OFB<Magma>":
                        holderName = "Output Feedback (OFB)";
                        break;
                }
                decryptor.add(MenuItemConstructor.get(desktop).encodeDecodeItem(holderName, holder, MenuItemConstructor.Mode.DECRYPT));
            }
            decrypt.add(decryptor);
        }

        JMenu authentication = new JMenu("Authentication");

        JMenu subscription = new JMenu("Signing");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "Kuznyechik";
                    break;
                case "Magma":
                    name = "Magma";
                    break;
            }
            subscription.add(MenuItemConstructor.get(desktop).subscribeItem(name, cipher));
        }
        authentication.add(subscription);

        JMenu streebog = new JMenu("Hashing");

        for (HashFunctionHolder hash : hashfunctions) {
            String name = hash.getName();
            switch (name){
                case "Streebog":
                    streebog.add(MenuItemConstructor.get(desktop).streebogItem("Streebog-256", 256, hash));
                    //streebog.add(MenuItemConstructor.get(desktop).streebogItem("Streebog-512", 512, hash));//.setEnabled(false);
            }
        }
        // TODO: implement Streebog output for the data stream

        authentication.add(streebog);


        JMenu hmac = new JMenu("HMAC code");
        //JMenuItem hmac256 = new JMenuItem("HMAC-256");
        //JMenuItem hmac512 = new JMenuItem("HMAC-512");
        hmac.add(MenuItemConstructor.get(desktop).HMACItem("HMAC-256", 256));
        //hmac.add(MenuItemConstructor.get(desktop).HMACItem("HMAC-512", 512)).setEnabled(false);
        //hmac.add(hmac256);
        //hmac.add(hmac512);


        authentication.add(hmac).setEnabled(true);

        JMenu analysis = new JMenu("Analysis");
        analysis.setEnabled(true);
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "Kuznyechik";
                    break;
                case "Magma":
                    name = "Magma";
                    break;
            }
            JMenu analyzer = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "Electronic Codebook (ECB)";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "Cipher Block Chaining (CBC)";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "Cipher Feedback (CFB)";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "Counter (CTR)";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "Output Feedback (OFB)";
                        break;
                    case "ECB<Magma>":
                        holderName = "Electronic Codebook (ECB)";
                        break;
                    case "CBC<Magma>":
                        holderName = "Cipher Block Chaining (CBC)";
                        break;
                    case "CFB<Magma>":
                        holderName = "Cipher Feedback (CFB)";
                        break;
                    case "CTR<Magma>":
                        holderName = "Counter (CTR)";
                        break;
                    case "OFB<Magma>":
                        holderName = "Output Feedback (OFB)";
                        break;
                }
                analyzer.add(MenuItemConstructor.get(desktop).analyzeItem(holderName, holder));
            }
            analysis.add(analyzer).setEnabled(false);
        }

        JMenu analysisBlockCipherAttack = new JMenu("Attacks on the block cipher");
        JMenuItem diffAnalysisAttack = new JMenuItem("Differential cryptanalysis");
        JMenuItem linearAnalysisAttack = new JMenuItem("Linear cryptanalysis");
        analysisBlockCipherAttack.add(diffAnalysisAttack);
        analysisBlockCipherAttack.add(linearAnalysisAttack);

        JMenu analysisCipherModeAttack = new JMenu("Attacks on the cipher mode");
        JMenuItem ecbAttack = new JMenuItem("Electronic Codebook (ECB)");
        JMenuItem cbcAttack = new JMenuItem("Cipher Block Chaining (CBC)");
        analysisCipherModeAttack.add(ecbAttack);
        analysisCipherModeAttack.add(cbcAttack);

        JMenu analysisHashFunctionAttack = new JMenu("Attacks on hash functions");
        JMenuItem rainbowAttack = new JMenuItem("Rainbow table attack");
        JMenuItem birthDayAttack = new JMenuItem("Birthday attack");
        analysisHashFunctionAttack.add(rainbowAttack);
        analysisHashFunctionAttack.add(birthDayAttack);


        JMenu analysisTools = new JMenu("Tools");
        JMenuItem hammingDist = new JMenuItem("Hamming distance");
        analysisTools.add(hammingDist);

        analysis.add(analysisBlockCipherAttack);
        analysis.add(analysisCipherModeAttack);
        analysis.add(analysisHashFunctionAttack);
        analysis.add(analysisTools);

        JMenu visualization = new JMenu("Visualization");
        JMenu visKuznyechik = new JMenu("Kuznyechik cipher");
        JMenu visMagma = new JMenu("Magma cipher");
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeKuznyechikRoundItem("Round transformation", ciphers.get(0)));
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeExpandKeyItem("Key expansion", ciphers.get(0)));
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeRegister("Shift register", ciphers.get(0))).setEnabled(false);

        visMagma.add(MenuItemConstructor.get(desktop).visualizeMagmaRoundItem("Round transformation", ciphers.get(1)));
        visMagma.add(MenuItemConstructor.get(desktop).visualizeMagmaKeyItem("Key expansion", ciphers.get(1)));


        JMenu visHashFunction = new JMenu("Hash functions");
        //JMenuItem visStreebog256 = new JMenuItem("Streebog-256");
        //JMenuItem visStreebog512 = new JMenuItem("Streebog-512");

        visHashFunction.add(MenuItemConstructor.get(desktop).visualizeStreebogItem("Streebog-256", 256, hashfunctions.get(0)));
        //visHashFunction.add(MenuItemConstructor.get(desktop).visualizeStreebogItem("Streebog-512", 512, hashfunctions.get(0))).setEnabled(false);
        //visHashFunction.add(visStreebog256);
        //visHashFunction.add(visStreebog512);

        visualization.add(visKuznyechik);
        visualization.add(visMagma);
        visualization.add(visHashFunction);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        menuBar.add(encrypt);
        menuBar.add(decrypt);
        menuBar.add(authentication);
        menuBar.add(visualization);
        menuBar.add(analysis);

        setJMenuBar(menuBar);
    }

    public void display(int minWidth, int minHeight) {
        setMinimumSize(new Dimension(minWidth, minHeight));
        pack();
        setLocationByPlatform(true);
        setVisible(true);
    }
}