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
        super("�������");
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
            JOptionPane.showMessageDialog(this, e.getMessage(), "������ ������������� ����������", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void setupContents() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(new EmptyBorder(5, 5, 5, 5));

        desktop = new JDesktopPane();
        // TODO: ������� ����������� (+ : �������� ������� createEmpty() �� createReadme())
        Objects.requireNonNull(TextFileFrame.createReadme()).display(desktop);

        mainContainer.add(desktop);
        setContentPane(mainContainer);
    }


    private void setupMenu() {
        support = new JButton("?"); // TODO use image
        support.setFocusPainted(false);
        support.setBorder(new RoundedBorder(5));

        JMenuItem openSystem = new JMenu("������� ����");
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("������� ��� ��������� (.txt)", () -> TextFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("������� ��� �������� (.bin)", () -> BinaryFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("������� ��� ����������� (.png, .bmp)", () -> ImageFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("������� ���������� ���� (ReadMe.txt)", TextFileFrame::createReadme));

        JMenu createFile = new JMenu("������� ����");
        createFile.add(MenuItemConstructor.get(desktop).fileCreateItem("������� ��������� (.txt)", TextFileFrame::createEmpty));
        createFile.add(MenuItemConstructor.get(desktop).fileCreateItem("������� �������� (.bin)", BinaryFileFrame::createEmpty));

        JMenu file = new JMenu("����");
        file.add(createFile);
        file.add(openSystem);



        JMenu encrypt = new JMenu("������������");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "��������";
                    break;
                case "Magma":
                    name = "�����";
                    break;
            }
            JMenu encryptor = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "������� ������";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "������� ������ � �����������";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "������������ � �������� ������ �� ����������";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "������������";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "������������ � �������� ������ �� ������";
                        break;
                    case "ECB<Magma>":
                        holderName = "������� ������";
                        break;
                    case "CBC<Magma>":
                        holderName = "������� ������ � �����������";
                        break;
                    case "CFB<Magma>":
                        holderName = "������������ � �������� ������ �� ����������";
                        break;
                    case "CTR<Magma>":
                        holderName = "������������";
                        break;
                    case "OFB<Magma>":
                        holderName = "������������ � �������� ������ �� ������";
                        break;
                }
                encryptor.add(MenuItemConstructor.get(desktop).encodeDecodeItem(holderName, holder, MenuItemConstructor.Mode.ENCRYPT));
            }
            encrypt.add(encryptor);
        }

        JMenu decrypt = new JMenu("�������������");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "��������";
                    break;
                case "Magma":
                    name = "�����";
                    break;
            }
            JMenu decryptor = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "������� ������";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "������� ������ � �����������";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "������������ � �������� ������ �� ����������";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "������������";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "������������ � �������� ������ �� ������";
                        break;
                    case "ECB<Magma>":
                        holderName = "������� ������";
                        break;
                    case "CBC<Magma>":
                        holderName = "������� ������ � �����������";
                        break;
                    case "CFB<Magma>":
                        holderName = "������������ � �������� ������ �� ����������";
                        break;
                    case "CTR<Magma>":
                        holderName = "������������";
                        break;
                    case "OFB<Magma>":
                        holderName = "������������ � �������� ������ �� ������";
                        break;
                }
                decryptor.add(MenuItemConstructor.get(desktop).encodeDecodeItem(holderName, holder, MenuItemConstructor.Mode.DECRYPT));
            }
            decrypt.add(decryptor);
        }

        JMenu authentication = new JMenu("��������������");

        JMenu subscription = new JMenu("������������");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "��������";
                    break;
                case "Magma":
                    name = "�����";
                    break;
            }
            subscription.add(MenuItemConstructor.get(desktop).subscribeItem(name, cipher));
        }
        authentication.add(subscription);

        JMenu streebog = new JMenu("�����������");

        for (HashFunctionHolder hash : hashfunctions) {
            String name = hash.getName();
            switch (name){
                case "Streebog":
                    streebog.add(MenuItemConstructor.get(desktop).streebogItem("�������-256", 256, hash));
                    //streebog.add(MenuItemConstructor.get(desktop).streebogItem("�������-512", 512, hash));//.setEnabled(false);
            }
        }
        // TODO: ����������� ����� Streebog, ��� ������-������

        authentication.add(streebog);


        JMenu hmac = new JMenu("HMAC ���");
        //JMenuItem hmac256 = new JMenuItem("HMAC-256");
        //JMenuItem hmac512 = new JMenuItem("HMAC-512");
        hmac.add(MenuItemConstructor.get(desktop).HMACItem("HMAC-256", 256));
        //hmac.add(MenuItemConstructor.get(desktop).HMACItem("HMAC-512", 512)).setEnabled(false);
        //hmac.add(hmac256);
        //hmac.add(hmac512);


        authentication.add(hmac).setEnabled(true);

        JMenu analysis = new JMenu("������");
        analysis.setEnabled(true);
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "��������";
                    break;
                case "Magma":
                    name = "�����";
                    break;
            }
            JMenu analyzer = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "������� ������";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "������� ������ � �����������";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "������������ � �������� ������ �� ����������";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "������������";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "������������ � �������� ������ �� ������";
                        break;
                    case "ECB<Magma>":
                        holderName = "������� ������";
                        break;
                    case "CBC<Magma>":
                        holderName = "������� ������ � �����������";
                        break;
                    case "CFB<Magma>":
                        holderName = "������������ � �������� ������ �� ����������";
                        break;
                    case "CTR<Magma>":
                        holderName = "������������";
                        break;
                    case "OFB<Magma>":
                        holderName = "������������ � �������� ������ �� ������";
                        break;
                }
                analyzer.add(MenuItemConstructor.get(desktop).analyzeItem(holderName, holder));
            }
            analysis.add(analyzer).setEnabled(false);
        }

        JMenu analysisBlockCipherAttack = new JMenu("����� �� ������� ����");
        JMenuItem diffAnalysisAttack = new JMenuItem("���������������� ������");
        JMenuItem linearAnalysisAttack = new JMenuItem("�������� ������");
        analysisBlockCipherAttack.add(diffAnalysisAttack);
        analysisBlockCipherAttack.add(linearAnalysisAttack);

        JMenu analysisCipherModeAttack = new JMenu("����� �� ����� ������ �����");
        JMenuItem ecbAttack = new JMenuItem("������� ������");
        JMenuItem cbcAttack = new JMenuItem("������� ������ � �����������");
        analysisCipherModeAttack.add(ecbAttack);
        analysisCipherModeAttack.add(cbcAttack);

        JMenu analysisHashFunctionAttack = new JMenu("����� �� ���-�������");
        JMenuItem rainbowAttack = new JMenuItem("�� ������ \"�������� ������\"");
        JMenuItem birthDayAttack = new JMenuItem("�� ������ \"���� ��������\"");
        analysisHashFunctionAttack.add(rainbowAttack);
        analysisHashFunctionAttack.add(birthDayAttack);


        JMenu analysisTools = new JMenu("�����������");
        JMenuItem hammingDist = new JMenuItem("���������� ��������");
        analysisTools.add(hammingDist);

        analysis.add(analysisBlockCipherAttack);
        analysis.add(analysisCipherModeAttack);
        analysis.add(analysisHashFunctionAttack);
        analysis.add(analysisTools);

        JMenu visualization = new JMenu("������������");
        JMenu visKuznyechik = new JMenu("���� '��������'");
        JMenu visMagma = new JMenu("���� '�����'");
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeKuznyechikRoundItem("��������� ��������������", ciphers.get(0)));
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeExpandKeyItem("������������� �����", ciphers.get(0)));
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeRegister("������� ������", ciphers.get(0))).setEnabled(false);

        visMagma.add(MenuItemConstructor.get(desktop).visualizeMagmaRoundItem("��������� ��������������", ciphers.get(1)));
        visMagma.add(MenuItemConstructor.get(desktop).visualizeMagmaKeyItem("������������� �����", ciphers.get(1)));


        JMenu visHashFunction = new JMenu("���-�������");
        //JMenuItem visStreebog256 = new JMenuItem("�������-256");
        //JMenuItem visStreebog512 = new JMenuItem("�������-512");

        visHashFunction.add(MenuItemConstructor.get(desktop).visualizeStreebogItem("�������-256", 256, hashfunctions.get(0)));
        //visHashFunction.add(MenuItemConstructor.get(desktop).visualizeStreebogItem("�������-512", 512, hashfunctions.get(0))).setEnabled(false);
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
