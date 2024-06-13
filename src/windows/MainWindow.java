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
        super("Литорея");
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
            JOptionPane.showMessageDialog(this, e.getMessage(), "Ошибка инициализации приложения", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void setupContents() {
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBorder(new EmptyBorder(5, 5, 5, 5));

        desktop = new JDesktopPane();
        // TODO: русская локализация (+ : поменять функцию createEmpty() на createReadme())
        Objects.requireNonNull(TextFileFrame.createReadme()).display(desktop);

        mainContainer.add(desktop);
        setContentPane(mainContainer);
    }


    private void setupMenu() {
        support = new JButton("?"); // TODO use image
        support.setFocusPainted(false);
        support.setBorder(new RoundedBorder(5));

        JMenuItem openSystem = new JMenu("Открыть файл");
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("Открыть как текстовый (.txt)", () -> TextFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("Открыть как бинарный (.bin)", () -> BinaryFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("Открыть как изображение (.png, .bmp)", () -> ImageFileFrame.createFromFile(this)));
        openSystem.add(MenuItemConstructor.get(desktop).fileCreateItem("Открыть справочный файл (ReadMe.txt)", TextFileFrame::createReadme));

        JMenu createFile = new JMenu("Создать файл");
        createFile.add(MenuItemConstructor.get(desktop).fileCreateItem("Создать текстовый (.txt)", TextFileFrame::createEmpty));
        createFile.add(MenuItemConstructor.get(desktop).fileCreateItem("Создать бинарный (.bin)", BinaryFileFrame::createEmpty));

        JMenu file = new JMenu("Файл");
        file.add(createFile);
        file.add(openSystem);



        JMenu encrypt = new JMenu("Зашифрование");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "Кузнечик";
                    break;
                case "Magma":
                    name = "Магма";
                    break;
            }
            JMenu encryptor = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "Простая замена";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "Простая замена с зацеплением";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "Гаммирование с обратной связью по шифртексту";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "Гаммирование";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "Гаммирование с обратной связью по выходу";
                        break;
                    case "ECB<Magma>":
                        holderName = "Простая замена";
                        break;
                    case "CBC<Magma>":
                        holderName = "Простая замена с зацеплением";
                        break;
                    case "CFB<Magma>":
                        holderName = "Гаммирование с обратной связью по шифртексту";
                        break;
                    case "CTR<Magma>":
                        holderName = "Гаммирование";
                        break;
                    case "OFB<Magma>":
                        holderName = "Гаммирование с обратной связью по выходу";
                        break;
                }
                encryptor.add(MenuItemConstructor.get(desktop).encodeDecodeItem(holderName, holder, MenuItemConstructor.Mode.ENCRYPT));
            }
            encrypt.add(encryptor);
        }

        JMenu decrypt = new JMenu("Расшифрование");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "Кузнечик";
                    break;
                case "Magma":
                    name = "Магма";
                    break;
            }
            JMenu decryptor = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "Простая замена";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "Простая замена с зацеплением";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "Гаммирование с обратной связью по шифртексту";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "Гаммирование";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "Гаммирование с обратной связью по выходу";
                        break;
                    case "ECB<Magma>":
                        holderName = "Простая замена";
                        break;
                    case "CBC<Magma>":
                        holderName = "Простая замена с зацеплением";
                        break;
                    case "CFB<Magma>":
                        holderName = "Гаммирование с обратной связью по шифртексту";
                        break;
                    case "CTR<Magma>":
                        holderName = "Гаммирование";
                        break;
                    case "OFB<Magma>":
                        holderName = "Гаммирование с обратной связью по выходу";
                        break;
                }
                decryptor.add(MenuItemConstructor.get(desktop).encodeDecodeItem(holderName, holder, MenuItemConstructor.Mode.DECRYPT));
            }
            decrypt.add(decryptor);
        }

        JMenu authentication = new JMenu("Аутентификация");

        JMenu subscription = new JMenu("Имитовставка");
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "Кузнечик";
                    break;
                case "Magma":
                    name = "Магма";
                    break;
            }
            subscription.add(MenuItemConstructor.get(desktop).subscribeItem(name, cipher));
        }
        authentication.add(subscription);

        JMenu streebog = new JMenu("Хэширование");

        for (HashFunctionHolder hash : hashfunctions) {
            String name = hash.getName();
            switch (name){
                case "Streebog":
                    streebog.add(MenuItemConstructor.get(desktop).streebogItem("Стрибог-256", 256, hash));
                    //streebog.add(MenuItemConstructor.get(desktop).streebogItem("Стрибог-512", 512, hash));//.setEnabled(false);
            }
        }
        // TODO: Реализовать класс Streebog, его бизнес-логику

        authentication.add(streebog);


        JMenu hmac = new JMenu("HMAC код");
        //JMenuItem hmac256 = new JMenuItem("HMAC-256");
        //JMenuItem hmac512 = new JMenuItem("HMAC-512");
        hmac.add(MenuItemConstructor.get(desktop).HMACItem("HMAC-256", 256));
        //hmac.add(MenuItemConstructor.get(desktop).HMACItem("HMAC-512", 512)).setEnabled(false);
        //hmac.add(hmac256);
        //hmac.add(hmac512);


        authentication.add(hmac).setEnabled(true);

        JMenu analysis = new JMenu("Анализ");
        analysis.setEnabled(true);
        for (CipherHolder cipher : ciphers) {
            String name = cipher.getName();
            switch (name) {
                case "Kuznyechik":
                    name = "Кузнечик";
                    break;
                case "Magma":
                    name = "Магма";
                    break;
            }
            JMenu analyzer = new JMenu(name);
            for (Class<? extends Algorithm> algorithm : algorithms) {
                AlgorithmHolder holder = new AlgorithmHolder(algorithm, cipher);
                String holderName = holder.getName();
                switch (holderName) {
                    case "ECB<Kuznyechik>":
                        holderName = "Простая замена";
                        break;
                    case "CBC<Kuznyechik>":
                        holderName = "Простая замена с зацеплением";
                        break;
                    case "CFB<Kuznyechik>":
                        holderName = "Гаммирование с обратной связью по шифртексту";
                        break;
                    case "CTR<Kuznyechik>":
                        holderName = "Гаммирование";
                        break;
                    case "OFB<Kuznyechik>":
                        holderName = "Гаммирование с обратной связью по выходу";
                        break;
                    case "ECB<Magma>":
                        holderName = "Простая замена";
                        break;
                    case "CBC<Magma>":
                        holderName = "Простая замена с зацеплением";
                        break;
                    case "CFB<Magma>":
                        holderName = "Гаммирование с обратной связью по шифртексту";
                        break;
                    case "CTR<Magma>":
                        holderName = "Гаммирование";
                        break;
                    case "OFB<Magma>":
                        holderName = "Гаммирование с обратной связью по выходу";
                        break;
                }
                analyzer.add(MenuItemConstructor.get(desktop).analyzeItem(holderName, holder));
            }
            analysis.add(analyzer).setEnabled(false);
        }

        JMenu analysisBlockCipherAttack = new JMenu("Атака на блочный шифр");
        JMenuItem diffAnalysisAttack = new JMenuItem("Дифференциальный анализ");
        JMenuItem linearAnalysisAttack = new JMenuItem("Линейный анализ");
        analysisBlockCipherAttack.add(diffAnalysisAttack);
        analysisBlockCipherAttack.add(linearAnalysisAttack);

        JMenu analysisCipherModeAttack = new JMenu("Атака на режим работы шифра");
        JMenuItem ecbAttack = new JMenuItem("Простая замена");
        JMenuItem cbcAttack = new JMenuItem("Простая замена с зацеплением");
        analysisCipherModeAttack.add(ecbAttack);
        analysisCipherModeAttack.add(cbcAttack);

        JMenu analysisHashFunctionAttack = new JMenu("Атака на хэш-функцию");
        JMenuItem rainbowAttack = new JMenuItem("На основе \"радужных таблиц\"");
        JMenuItem birthDayAttack = new JMenuItem("На основе \"дней рождения\"");
        analysisHashFunctionAttack.add(rainbowAttack);
        analysisHashFunctionAttack.add(birthDayAttack);


        JMenu analysisTools = new JMenu("Инструменты");
        JMenuItem hammingDist = new JMenuItem("Расстояние Хэмминга");
        analysisTools.add(hammingDist);

        analysis.add(analysisBlockCipherAttack);
        analysis.add(analysisCipherModeAttack);
        analysis.add(analysisHashFunctionAttack);
        analysis.add(analysisTools);

        JMenu visualization = new JMenu("Визуализация");
        JMenu visKuznyechik = new JMenu("Шифр 'Кузнечик'");
        JMenu visMagma = new JMenu("Шифр 'Магма'");
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeKuznyechikRoundItem("Раундовые преобразования", ciphers.get(0)));
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeExpandKeyItem("Развертывание ключа", ciphers.get(0)));
        visKuznyechik.add(MenuItemConstructor.get(desktop).visualizeRegister("Регистр сдвига", ciphers.get(0))).setEnabled(false);

        visMagma.add(MenuItemConstructor.get(desktop).visualizeMagmaRoundItem("Раундовые преобразования", ciphers.get(1)));
        visMagma.add(MenuItemConstructor.get(desktop).visualizeMagmaKeyItem("Развертывание ключа", ciphers.get(1)));


        JMenu visHashFunction = new JMenu("Хэш-функция");
        //JMenuItem visStreebog256 = new JMenuItem("Стрибог-256");
        //JMenuItem visStreebog512 = new JMenuItem("Стрибог-512");

        visHashFunction.add(MenuItemConstructor.get(desktop).visualizeStreebogItem("Стрибог-256", 256, hashfunctions.get(0)));
        //visHashFunction.add(MenuItemConstructor.get(desktop).visualizeStreebogItem("Стрибог-512", 512, hashfunctions.get(0))).setEnabled(false);
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
