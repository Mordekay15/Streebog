package windows;

import org.w3c.dom.ls.LSOutput;
import statement.State;
import utils.Conversions;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.awt.Desktop;

import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import java.util.Enumeration;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;
import static utils.OSInfo.*;
import static utils.OSInfo.isUnix;


public class VisualizationExpandKey extends JDialog {
    private int currentId = 1;
    private int cComputingIdx = 0;
    private int xorConstantIdx = 0;
    private int sIdx = 0;
    private int lIdx = 0;
    private int xorResultIdx = 0;
    //    private int swapIdx = 0;
    private int roundIdx = 1;
    private int roundArrIdx = 1;
    private int roundFeistel = 0;
    private int roundFeistelCounter = 1;
    private int keyIdx = 1;
    private int fontSize = 12;
    //    private final int[] indexRounds = new int[] {1,3,4,5,6,7,8,9,11,12,13,14,15,16,18,19,20,21,22,23,25,26,27,28,29,30,32,33,34,35,36,37,39,40,41,42,43,44,46,47,48,49,50,51,53,54,55,56,57,58,61,62,64,65,66,68,69,71,72,73,75,76,78,79,80,82,83,85,86,87,89,90,92,93,94,96,97,99,100,101,103,104,106,107,108,110,111,113,114,115,118,119,121,122,123,125,126,128,129,130,132,133,135,136,137,139,140,142,143,144,146,147,149,150,151,153,154,156,157,158,160,161,163,164,165,167,168,170,171,172,175,176,178,179,180,182,183,185,186,187,189,190,192,193,194,196,197,199,200,201,203,204,206,207,208,210,211,213,214,215,217,218,220,221,222,224,225,227,228,229};
    private final int[] indexRounds = new int[]{1, 50, 99, 148};
    private final int[] indexDropSubblocksFirst = new int[]{6, 55, 104, 153};
    private final int[] indexDropSubblocksSecond = new int[]{7, 56, 105, 154};

    private String roundKey1 = "";
    private String roundKey2 = "";
    private String textXORConstant = "";
    private String textS = "";
    private String textL = "";
    private String textXORResult = "";

    private int checkDoubleClickPartOne = 0;
    private int checkDoubleClickPartTwo = 0;


    private final List<State> stateList;
    private final JTree tree;

    private JTextPane textPaneBeforeL = new JTextPane();
    private JTextPane textPaneAfterL = new JTextPane();

    private JTextPane textPaneIterKeyL = new JTextPane();
    private JTextPane textPaneIterKeyR = new JTextPane();

    private JTextPane textPaneBeforeR = new JTextPane();
    private JTextPane textPaneAfterR = new JTextPane();

    private JTextPane textPaneConstant = new JTextPane();
    //    private JTextPane textPaneRotate = new JTextPane();
    private JTextPane textPaneXORConstant = new JTextPane();
    private JTextPane textPaneS = new JTextPane();
    private JTextPane textPaneL = new JTextPane();
    private JTextPane textPaneXORResult = new JTextPane();
    private JTextPane textPaneRoundKey = new JTextPane();


    private JTextPane textPaneViz11 = new JTextPane();
    private JTextPane textPaneViz12 = new JTextPane();
    private JTextPane textPaneViz13 = new JTextPane();
    private JTextPane textPaneViz14 = new JTextPane();
    private JTextPane textPaneViz15 = new JTextPane();

    private JTextPane textPaneViz21 = new JTextPane();
    private JTextPane textPaneViz22 = new JTextPane();
    private JTextPane textPaneViz23 = new JTextPane();
    private JTextPane textPaneViz24 = new JTextPane();
    private JTextPane textPaneViz25 = new JTextPane();


    private final JButton nextStep = new JButton(">");
    private final JButton nextRound = new JButton(">>");
    private final JButton prevRound = new JButton("<<");
    private final Color colorLightRed = Color.getHSBColor(0.016f, 0.11f, 1.f);
    private final Color colorLightBlue = Color.getHSBColor(0.5f, 0.12f, 1.f);
    private final Color colorViz = Color.getHSBColor(0.33f, 0.058f, 1.f);


    private final JLabel stageFeistel = new JLabel("№ итерации развертывания ключа 1", SwingConstants.CENTER);

    public VisualizationExpandKey(Component owner, State state, byte[] result, String key, byte[] algorithmKey) {
        super((Frame) SwingUtilities.getWindowAncestor(owner), "Визуализация развертывания ключа шифра 'Кузнечик'", false);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        stateList = createStateList(state);
        prevRound.setEnabled(false);
        nextStep.setForeground(Color.BLUE);
        nextRound.setForeground(Color.RED);
        prevRound.setForeground(Color.RED);

        System.out.println("KEY[] MAIN:" + Arrays.toString(algorithmKey));
        System.out.println("KEY[] MAIN:" + Arrays.toString(Conversions.hex(key)));

        roundKey1 = String.valueOf(key).substring(0, 47);
        roundKey2 = String.valueOf(key).substring(48, 95);


        textPaneBeforeL.setEditable(false);
        textPaneAfterL.setEditable(false);
        textPaneBeforeR.setEditable(false);
        textPaneAfterR.setEditable(false);
        textPaneConstant.setEditable(false);
        textPaneIterKeyL.setEditable(false);
        textPaneIterKeyR.setEditable(false);
        textPaneL.setEditable(false);
        textPaneRoundKey.setEditable(false);
        textPaneConstant.setEditable(false);
        textPaneS.setEditable(false);
        textPaneViz11.setEditable(false);
        textPaneViz12.setEditable(false);
        textPaneViz13.setEditable(false);
        textPaneViz14.setEditable(false);
        textPaneViz15.setEditable(false);
        textPaneViz21.setEditable(false);
        textPaneViz22.setEditable(false);
        textPaneViz23.setEditable(false);
        textPaneViz24.setEditable(false);
        textPaneViz25.setEditable(false);
        textPaneXORConstant.setEditable(false);
        textPaneXORResult.setEditable(false);


        nextStep.setFont(font);
        nextRound.setFont(font);
        prevRound.setFont(font);


        textPaneIterKeyL.setBackground(colorLightBlue);
        textPaneIterKeyR.setBackground(colorLightBlue);

        textPaneAfterL.setBackground(colorLightRed);
        textPaneAfterR.setBackground(colorLightRed);


        textPaneViz11.setBackground(colorViz);
        textPaneViz12.setBackground(colorViz);
        textPaneViz13.setBackground(colorViz);
        textPaneViz14.setBackground(colorViz);
        textPaneViz15.setBackground(colorViz);
        textPaneViz21.setBackground(colorViz);
        textPaneViz22.setBackground(colorViz);
        textPaneViz24.setBackground(colorViz);
        textPaneViz23.setBackground(colorViz);
        textPaneViz25.setBackground(colorViz);


        textPaneRoundKey.setBackground(colorLightRed);
        textPaneBeforeL.setBackground(colorLightRed);
        textPaneBeforeR.setBackground(colorLightRed);

        textPaneConstant.setBackground(colorLightBlue);
        textPaneXORConstant.setBackground(colorLightBlue);
        textPaneS.setBackground(colorLightBlue);
        textPaneL.setBackground(colorLightBlue);
        textPaneXORResult.setBackground(colorLightBlue);


        tree = createStateTree(state, key);
        tree.setScrollsOnExpand(true);
        tree.setExpandsSelectedPaths(true);

        stageFeistel.setFont(font);
        stageFeistel.setForeground(Color.BLUE);

        nextStep.addActionListener(e -> setCurrentIdNextStep(currentId + 1, key));
//        prevStep.addActionListener(e -> setCurrentIdPrevStep(currentId - 1, key));
        nextRound.addActionListener(e -> setCurrentIdNextRound(indexRounds[roundArrIdx], key));
        if (indexRounds[roundArrIdx - 1] > 0) {
            prevRound.addActionListener(e -> setCurrentIdPrevRound(indexRounds[roundArrIdx - 1], key));
        }
        else{
            prevRound.setEnabled(false);
        }
//        hintBlocks.addActionListener(e -> {
//            try {
//                HintButton();
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
//        roundKeyHint.addActionListener(e -> {
//            try {
//                roundKeyHint();
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//        });

        if (currentId == 1) {
            scipStartSteps(key);
            prevRound.setEnabled(false);
        }

        getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints textPaneRoundKeyConstraints = createConstants(1, 1, 1, 1, 0, 0, new Insets(5, 5, 5, 0));

        GridBagConstraints textPaneBeforeLConstraints = createConstants(1, 1, 1, 1, 0, 1, new Insets(5, 5, 5, 0));

        GridBagConstraints textPanaIterKeyLConstraints = createConstants(1, 1, 1, 1, 0, 2, new Insets(5, 5, 5, 0));

        GridBagConstraints textPaneViz11Constraints = createConstants(1, 1, 1, 1, 0, 3, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneViz12Constraints = createConstants(1, 1, 1, 1, 0, 4, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneViz13Constraints = createConstants(1, 1, 1, 1, 0, 5, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneViz14Constraints = createConstants(1, 1, 1, 1, 0, 6, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneViz15Constraints = createConstants(1, 1, 1, 1, 0, 7, new Insets(5, 5, 5, 0));

        GridBagConstraints textPaneBeforeRConstraints = createConstants(1, 1, 1, 1, 1, 1, new Insets(5, 5, 5, 0));
        GridBagConstraints textPanaIterKeyRConstraints = createConstants(1, 1, 1, 1, 1, 2, new Insets(5, 5, 5, 0));

        GridBagConstraints textPaneViz21Constraints = createConstants(1, 1, 1, 1, 1, 3, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneViz22Constraints = createConstants(1, 1, 1, 1, 1, 4, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneViz23Constraints = createConstants(1, 1, 1, 1, 1, 5, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneViz24Constraints = createConstants(1, 1, 1, 1, 1, 6, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneViz25Constraints = createConstants(1, 1, 1, 1, 1, 7, new Insets(5, 5, 5, 0));

        GridBagConstraints textPaneAfterLConstraints = createConstants(1, 1, 1, 1, 0, 8, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneAfterRConstraints = createConstants(1, 1, 1, 1, 1, 8, new Insets(5, 5, 5, 0));

//        GridBagConstraints textPaneRotateConstraints = createConstants(1, 1, 1, 1, 0, 1, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneConstantConstraints = createConstants(1, 1, 1, 1, 2, 3, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneXORConstantConstraints = createConstants(1, 1, 1, 1, 2, 4, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneSConstraints = createConstants(1, 1, 1, 1, 2, 5, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneLConstraints = createConstants(1, 1, 1, 1, 2, 6, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneXORResultConstraints = createConstants(1, 1, 1, 1, 2, 7, new Insets(5, 5, 5, 0));


        getContentPane().add(textPaneConstant, textPaneConstantConstraints);
//        getContentPane().add(textPaneRotate, textPaneRotateConstraints);
        getContentPane().add(textPaneRoundKey, textPaneRoundKeyConstraints);
        getContentPane().add(textPaneXORConstant, textPaneXORConstantConstraints);
        getContentPane().add(textPaneS, textPaneSConstraints);
        getContentPane().add(textPaneL, textPaneLConstraints);
        getContentPane().add(textPaneXORResult, textPaneXORResultConstraints);
        getContentPane().add(textPaneBeforeL, textPaneBeforeLConstraints);

        getContentPane().add(textPaneIterKeyL, textPanaIterKeyLConstraints);
        getContentPane().add(textPaneIterKeyR, textPanaIterKeyRConstraints);

        getContentPane().add(textPaneViz11, textPaneViz11Constraints);
        getContentPane().add(textPaneViz12, textPaneViz12Constraints);
        getContentPane().add(textPaneViz13, textPaneViz13Constraints);
        getContentPane().add(textPaneViz14, textPaneViz14Constraints);
        getContentPane().add(textPaneViz15, textPaneViz15Constraints);

        getContentPane().add(textPaneViz21, textPaneViz21Constraints);
        getContentPane().add(textPaneViz22, textPaneViz22Constraints);
        getContentPane().add(textPaneViz23, textPaneViz23Constraints);
        getContentPane().add(textPaneViz24, textPaneViz24Constraints);
        getContentPane().add(textPaneViz25, textPaneViz25Constraints);

        getContentPane().add(textPaneBeforeR, textPaneBeforeRConstraints);
        getContentPane().add(textPaneAfterL, textPaneAfterLConstraints);
        getContentPane().add(textPaneAfterR, textPaneAfterRConstraints);

        GridBagConstraints stageFeistelConstraints = createConstants(1, 1, 1, 1, 6, 0, new Insets(0, 5, 5, 0));
//        GridBagConstraints sHintConstraints = createConstants(1, 1, 1, 1, 2, 0, new Insets(0, 5, 5, 0));
//        GridBagConstraints roundKeyHintConstraints = createConstants(1, 1, 1, 1, 3, 3, new Insets(0, 5, 5, 0));
        GridBagConstraints nextStepConstraints = createConstants(1, 1, 1, 1, 6, 9, new Insets(0, 5, 5, 0));
//        GridBagConstraints prevStepConstraints = createConstants(1, 1, 1, 1, 4, 8, new Insets(0, 5, 5, 0));
//        GridBagConstraints hintBlocksConstraints = createConstants(1, 1, 1, 1, 4, 8, new Insets(0, 5, 5, 0));
        GridBagConstraints nextRoundConstraints = createConstants(1, 1, 1, 1, 5, 9, new Insets(0, 5, 5, 0));
        GridBagConstraints prevRoundConstraints = createConstants(1, 1, 1, 1, 4, 9, new Insets(0, 5, 5, 0));


        getContentPane().add(stageFeistel, stageFeistelConstraints);
//        getContentPane().add(prevStep, prevStepConstraints);
//        getContentPane().add(hintBlocks, hintBlocksConstraints);
        getContentPane().add(nextStep, nextStepConstraints);
        getContentPane().add(nextRound, nextRoundConstraints);
        getContentPane().add(prevRound, prevRoundConstraints);

//        JScrollPane treePane = new JScrollPane(tree);
//        treePane.setPreferredSize(new Dimension(200, treePane.getPreferredSize().height));
//        GridBagConstraints treeConstraints = createConstants(1, 2, 1, 8, 12, 0, new Insets(5, 5, 5, 5));
//        getContentPane().add(treePane, treeConstraints);


        setResizable(true);
        setPreferredSize(new Dimension(1024, 768));
        pack();
    }

    private void sleep(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }

    private void visualizeSwapKeyPartOne() {
        // Визуализация маршрута первого субблока
        Font font = new Font("Arial", Font.BOLD, fontSize);
        textPaneViz21.setFont(font);
        textPaneViz13.setFont(font);
        textPaneViz14.setFont(font);
        textPaneViz15.setFont(font);
        textPaneViz12.setFont(font);
        textPaneViz11.setFont(font);
        textPaneViz22.setFont(font);
        textPaneViz23.setFont(font);
        textPaneViz24.setFont(font);
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                int ms = 1000;
                Style styleRed = getStyleText(textPaneXORResult, Color.RED);
                Style styleBlue = getStyleText(textPaneXORResult, Color.BLUE);
                Style styleBlack = getStyleText(textPaneXORResult, Color.BLACK);

                nextStep.setEnabled(false);
                nextRound.setEnabled(false);
                prevRound.setEnabled(false);

                textPaneViz21.setText(roundKey1);
                sleep(ms);

                textPaneViz21.setText(null);
                textPaneViz12.setText(roundKey1);
                sleep(ms);

                textPaneViz13.setText(roundKey1);
                textPaneViz12.setText(null);
                sleep(ms);

                textPaneViz14.setText(roundKey1);
                textPaneViz13.setText(null);
                sleep(ms);

                textPaneViz15.setText(roundKey1);
                textPaneViz14.setText(null);
                sleep(ms);

                textPaneViz15.setText(null);
                insertColorText(textPaneAfterL, styleBlue, "Субблок L'\n");
                insertColorText(textPaneAfterL, styleBlack, roundKey1);
                textPaneViz15.setText(null);
                sleep(ms);


                textPaneViz11.setText(roundKey2);
                sleep(ms);

                textPaneViz11.setText(null);
                textPaneViz22.setText(textXORConstant);
                sleep(ms);

                textPaneViz23.setText(textS);
                textPaneViz22.setText(null);
                sleep(ms);


                textPaneViz24.setText(textL); // Остановка перед L
                textPaneViz23.setText(null);
                sleep(ms - 700);


                nextStep.setEnabled(true);
                return null;
            }
        };
        worker.execute();
    }

    private void visualizeSwapKeyPartTwo(String dataXOR) {
        Font font = new Font("Arial", Font.BOLD, fontSize);
        textPaneXORResult.setFont(font);
        textPaneViz25.setFont(font);
        textPaneIterKeyL.setFont(font);
        textPaneIterKeyR.setFont(font);
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                int ms = 700;
                Style styleBlue = getStyleText(textPaneXORResult, Color.BLUE);
                Style styleRed = getStyleText(textPaneXORResult, Color.RED);
                Style styleBlack = getStyleText(textPaneXORResult, Color.BLACK);
                nextStep.setEnabled(false);

                textPaneXORResult.setText(null);
                textPaneXORResult.setText("Вычисление.");
                sleep(ms);
                textPaneXORResult.setText("Вычисление..");
                sleep(ms);
                textPaneXORResult.setText("Вычисление...");
                sleep(ms);
                textPaneXORResult.setText(null);

                insertColorText(textPaneXORResult, styleBlue, "Преобразование: 'сложение XOR'\n");
                insertColorText(textPaneXORResult, styleBlack, dataXOR);
//                textPaneXORResult.setText(dataXOR);
                textPaneViz25.setText(dataXOR);
                textPaneViz24.setText(null);

                sleep(ms + 300);

                insertColorText(textPaneAfterR, styleBlue, "Субблок R'\n");
                insertColorText(textPaneAfterR, styleBlack, dataXOR);
                textPaneViz25.setText(null);

                sleep(ms);
                textPaneIterKeyL.setText(null);
                insertColorText(textPaneIterKeyL, styleBlue, "Субблок L\n");
                insertColorText(textPaneIterKeyL, styleBlack, roundKey1);

                textPaneIterKeyR.setText(null);
                insertColorText(textPaneIterKeyR, styleBlue, "Субблок R\n");
                insertColorText(textPaneIterKeyR, styleBlack, dataXOR);

                nextStep.setEnabled(true);
                nextRound.setEnabled(true);
                prevRound.setEnabled(true);

                // --------------------------------

                // swap
//                textPaneAfterL.setText(null);
//                textPaneAfterL.setText(textXORResult);
//
//                textPaneAfterR.setText(null);
//                textPaneAfterR.setText(roundKey1);
//                sleep(ms);

                return null;
            }
        };
        worker.execute();
    }


    private void scipStartSteps(String key) {
        State current = stateList.get(1);
        textPaneRoundKey.setText(null);
        textPaneBeforeL.setText(null);
        textPaneBeforeR.setText(null);
        textPaneAfterL.setText(null);
        textPaneAfterR.setText(null);
//        stage.setText("Раунд №"+(roundIdx+1));
        Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
        Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
        Style styleBlue = getStyleText(textPaneIterKeyR, Color.BLUE);
        insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n");
        insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
        insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ 1\n");
        insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47));
//        insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0, 47));
        insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ 2\n");
        insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95));
//        insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));

        textPaneIterKeyL.setText(null);
        insertColorText(textPaneIterKeyL, styleBlue, "Субблок L\n");
        insertColorText(textPaneIterKeyL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47));

        textPaneIterKeyR.setText(null);
        insertColorText(textPaneIterKeyR, styleBlue, "Субблок R\n");
        insertColorText(textPaneIterKeyR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95));
//        JOptionPane.showMessageDialog(
//                VisualizationExpandKey.this,
//                new String[]{"Уведомление, что нужно кликнуть 3 раза"},
//                "Уведомление о кликах",
//                JOptionPane.INFORMATION_MESSAGE);
        tree.setSelectionPath(findTreePath(current));

//        prevStep.setEnabled(false);
        prevRound.setEnabled(false);
    }

    private Style getStyleText(JTextPane textPane, Color color) {
        Style style = textPane.addStyle("bold", null);
        StyleConstants.setBold(style, true);
        StyleConstants.setFontSize(style, fontSize);
        StyleConstants.setForeground(style, color);
        return style;
    }

    private void insertColorText(JTextPane textPane, Style style, String str) {
        StyledDocument doc = textPane.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), str, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isNumberInArray(int digit, int[] arr) {
        for (int num : arr) {
            if (num == digit) {
                return true;
            }
        }
        return false;
    }


    private void HintButton() throws IOException {
        String fileName = "swap_keys.png";
        Desktop dt = Desktop.getDesktop();
        String user = getUser();
        if (isWindows()) {
            File fWin = new File("C:\\Users\\" + user + "\\Documents\\CryptoETU\\" + fileName);
            dt.open(fWin);
        } else if (isMac()) {
            File fMac = new File("/Users/" + user + "/Documents/CryptoETU/" + fileName);
            dt.open(fMac);
        } else if (isUnix()) {
            File fUnix = new File("/home/" + user + "/Documents/CryptoETU/" + fileName);
            dt.open(fUnix);
        }
        JOptionPane.showMessageDialog(
                VisualizationExpandKey.this,
                new String[]{"Описание...",
                        "",
                        ""},
                "Перемещение субблоков",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void roundKeyHint() throws IOException {
//        File f = new File("src/main/resources/files/images/round_key_kuznyechik.png");
//        Desktop dt = Desktop.getDesktop();
//        dt.open(f);

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop()
                    .isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop()
                        .browse(new URI("https://drive.google.com/file/d/1PV9bahO7mmsrnM6BpzcCr1vBFnYkQmpr/view?usp=share_link"));
            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        JOptionPane.showMessageDialog(
                VisualizationExpandKey.this,
                new String[]{"Раундовые ключи получаются путем определенных преобразований на основе мастер-ключа.",
                        "Процесс начинается с разбиения мастер-ключа пополам, так получается первая пара раундовых ключей.",
                        " ",
                        "Для генерации каждой последующей пары раундовых ключей применяется 8 итераций сети Фейстеля,",
                        "в каждой итерации используется константа, которая вычисляется путем применения линейного преобразоваия алгоритма к значению номера итерации",
                        ""},
                "Развертывания ключей",
                JOptionPane.INFORMATION_MESSAGE);
    }


    private void setCurrentIdNextStep(int id, String key) {
        SwingUtilities.invokeLater(() -> {
            Style styleRed_ = getStyleText(textPaneXORConstant, Color.RED);
            State current = stateList.get(id);
            prevRound.setEnabled(id != 1);
            System.out.println("subblock id: " + id);


//            if ((id == 50) & (checkDoubleClick == 0)){
//                System.out.println("DEBUG FOR VIS: "+id);
//                checkDoubleClick += 1;
//                try {
//                    visualizeSwapKey();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }
            System.out.println("ID step = " + id);
            if (current.getName().equals("Round key parts 2 and 3")) {
//                prevStep.setEnabled(false);
                prevRound.setEnabled(true);
                nextStep.setEnabled(true);
                roundFeistelCounter = 1;


            } else if (current.getName().equals("Constant computing " + cComputingIdx)) {
//                prevRound.setEnabled(true);
////                nextRound.setEnabled(true);
////                System.out.println("DEBUG id: "+ id);
//                System.out.println("DEBUG for Round: " + id);
//                System.out.println("DEBUG roundIDX: " + roundIdx);
//                textPaneConstant.setText(null);
//                Style styleBlue = getStyleText(textPaneConstant, Color.BLUE);
//                Style styleBlack = getStyleText(textPaneConstant, Color.BLACK);
//                insertColorText(textPaneConstant, styleBlue, "Формирование ключа итерации:\n\n");
//                insertColorText(textPaneConstant, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
//                textConstant = String.valueOf(Conversions.hex(current.getAfter()));
//                System.out.println("DEBUG jtextpane: "+textPaneBeforeL.getText());

//                insertColorText(textPaneConstant, styleRed_, "\n\nДанные до");
//                insertColorText(textPaneConstant, styleRed_, String.valueOf(Conversions.hex(current.getBefore())));
//                insertColorText(textPaneConstant, styleRed_, "\n\nДоп. данные:");
//                insertColorText(textPaneConstant, styleRed_, String.valueOf((Arrays.toString(current.getAdditional()))));
//                System.out.println(
//                                "\nВычисление константы:\n\n"+
//                                "Данные до:\n"+String.valueOf(Conversions.hex(current.getBefore()))+
//                                "\n\nДоп.данные:\n"+String.valueOf((Arrays.toString(current.getAdditional()))));
//                System.out.println("\n\n\n--------------------------\n\n\n");
                cComputingIdx++;
            } else if (current.getName().equals("Feistel round " + roundFeistel)) {
                stageFeistel.setText("№ итерации развертывания ключа " + (roundFeistelCounter));
                roundFeistel++;
                roundFeistelCounter++;
                textPaneAfterL.setText(null);
                textPaneAfterR.setText(null);

//                prevStep.setEnabled(true);

//                textPaneRoundKey.setText(null);
//                textPaneBeforeL.setText(null);
//                textPaneBeforeR.setText(null);
//                textPaneAfterL.setText(null);
//                textPaneAfterR.setText(null);
//                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
//                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
//                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n\n");
//                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
//                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ "+String.valueOf(roundIdx)+"\n\n");
//                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0,47));
//                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0,47));
//                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ "+String.valueOf(roundIdx+1)+"\n\n");
//                insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48,95));
//                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48,95));

//                textPaneS.setText(null);
//                textPaneL.setText(null);
//                textPaneRotate.setText(null);
//                textPaneSwap.setText(null);
//                textPaneXORResult.setText(null);
//                textPaneXORConstant.setText(null);
//                textPaneConstant.setText(null);

            } else if (current.getName().equals("XOR key 1 with constant " + xorConstantIdx)) {
//                System.out.println("DEBUG id: " + id);

                textPaneXORConstant.setText(null);

                textPaneBeforeL.setText(null);
                textPaneBeforeR.setText(null);
                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ " + keyIdx + "\n");
                insertColorText(textPaneBeforeL, styleBlack, roundKey2);
                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ " + (keyIdx + 1) + "\n");
                insertColorText(textPaneBeforeR, styleBlack, roundKey1);

                Style styleBlue = getStyleText(textPaneXORConstant, Color.BLUE);
//                Style styleBlack = getStyleText(textPaneXORConstant, Color.BLACK);

                insertColorText(textPaneXORConstant, styleBlue, "Преобразование: 'сложение XOR'\n");
                insertColorText(textPaneXORConstant, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));

                textPaneConstant.setText(null);
                insertColorText(textPaneConstant, styleBlue, "Формирование ключа итерации:\n");
                insertColorText(textPaneConstant, styleBlack, String.valueOf(Conversions.hex(current.getAdditional())));

                textXORConstant = String.valueOf(Conversions.hex(current.getAfter()));

//                System.out.println(
//                                "XOR with constant:\n\n"+
//                                "Данные до:\n"+String.valueOf(Conversions.hex(current.getBefore()))+
//                                "\n\nДоп.данные:\n"+String.valueOf(Conversions.hex(current.getAdditional())));
//                System.out.println("\n\n\n--------------------------\n\n\n");

//                insertColorText(textPaneXORConstant, styleRed_, "\n\nДанные до");
//                insertColorText(textPaneXORConstant, styleRed_, String.valueOf(Conversions.hex(current.getBefore())));
//                insertColorText(textPaneXORConstant, styleRed_, "\n\nДоп.данные:");
//                insertColorText(textPaneXORConstant, styleRed_, String.valueOf(Conversions.hex(current.getAdditional())));

//                insertColorText(textPaneXORConstant, styleBlue, "\n\nС чем производится XOR:");
//                insertColorText(textPaneXORConstant, styleBlack, String.valueOf(Conversions.hex(current.getAdditional())));
                xorConstantIdx++;
            } else if (current.getName().equals("Substitute bytes " + sIdx)) {
//                System.out.println("DEBUG id: " + id);
                textPaneS.setText(null);
                Style styleBlue = getStyleText(textPaneS, Color.BLUE);
                Style styleBlack = getStyleText(textPaneXORConstant, Color.BLACK);
                insertColorText(textPaneS, styleBlue, "Преобразование: 'подстановка S'\n");
                insertColorText(textPaneS, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
                textS = String.valueOf(Conversions.hex(current.getAfter()));

//                insertColorText(textPaneS, styleRed_, "\n\nДанные до");
//                insertColorText(textPaneS, styleRed_, String.valueOf(Conversions.hex(current.getBefore())));
//                insertColorText(textPaneS, styleRed_, "\n\nДоп. данные:");
//                insertColorText(textPaneS, styleRed_, Arrays.toString(current.getAdditional()));
                sIdx++;

//                System.out.println(
//                                "S:\n\n"+
//                                "Данные до:\n"+String.valueOf(Conversions.hex(current.getBefore()))+
//                                "\n\nДоп.данные:\n"+"null");
//                System.out.println("\n\n\n--------------------------\n\n\n");

            } else if (current.getName().equals("Rotate bytes " + lIdx)) {
//                System.out.println("DEBUG id: " + id);
                textPaneL.setText(null);
//                textPaneRotate.setText(null);
                Style styleBlue = getStyleText(textPaneL, Color.BLUE);
                Style styleBlack = getStyleText(textPaneL, Color.BLACK);
                insertColorText(textPaneL, styleBlue, "Преобразование: 'регистр сдвига L'\n");
                insertColorText(textPaneL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
                textL = String.valueOf(Conversions.hex(current.getAfter()));

                if (isNumberInArray(id, indexDropSubblocksFirst)) {
                    visualizeSwapKeyPartOne();
//                    checkDoubleClickPartOne += 1;
                }


//                insertColorText(textPaneL, styleRed_, "\n\nДанные до");
//                insertColorText(textPaneL, styleRed_, String.valueOf(Conversions.hex(current.getBefore())));
//                insertColorText(textPaneL, styleRed_, "\n\nДоп. данные:");
//                insertColorText(textPaneL, styleRed_, Arrays.toString(current.getAdditional()));

//                insertColorText(textPaneRotate, styleBlue, "Преобразование R:\n\n");
//                insertColorText(textPaneRotate, styleBlack, String.valueOf(Conversions.hex(current.getBefore())));
                lIdx++;
            } else if (current.getName().equals("XOR key 1 with result " + xorResultIdx)) {
//                System.out.println("DEBUG id: " + id);
                textPaneXORResult.setText(null);
                Style styleBlue = getStyleText(textPaneXORResult, Color.BLUE);
                Style styleBlack = getStyleText(textPaneXORResult, Color.BLACK);
//                insertColorText(textPaneXORResult, styleBlue, "Преобразование: Сложение XOR\n\n");
//                insertColorText(textPaneXORResult, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));
//                textXORResult = String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95);

//                insertColorText(textPaneXORResult, styleRed_, "\n\nДанные до");
//                insertColorText(textPaneXORResult, styleRed_, String.valueOf(Conversions.hex(current.getBefore())));
//                insertColorText(textPaneXORResult, styleRed_, "\n\nДоп.данные:");
//                insertColorText(textPaneXORResult, styleRed_, String.valueOf(Conversions.hex(current.getAdditional())));
//                insertColorText(textPaneXORResult, styleBlue, "Преобразование: Сложение XOR\n\n");
//                insertColorText(textPaneXORResult, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));

//                System.out.println(
//                                "XOR with result:\n\n"+
//                                "Данные до:\n"+String.valueOf(Conversions.hex(current.getBefore()))+
//                                "\n\nДоп.данные:\n"+String.valueOf(Conversions.hex(current.getAdditional())));
//                System.out.println("\n\n\n--------------------------\n\n\n");
//                textXORResult = String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95);

                if (isNumberInArray(id, indexDropSubblocksSecond)) {
                    visualizeSwapKeyPartTwo(String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));
//                    insertColorText(textPaneXORResult, styleBlue, "Преобразование: Сложение XOR\n\n");
//                    insertColorText(textPaneXORResult, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));
                    textXORResult = String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95);
//                    checkDoubleClickPartTwo += 1;

//                    textPaneIterKeyL.setText(null);
//                    textPaneIterKeyR.setText(null);
//                    insertColorText(textPaneIterKeyL, styleBlue, "Субблок L\n\n");
//                    insertColorText(textPaneIterKeyL, styleBlack, textPaneAfterL.getText());
//
//                    insertColorText(textPaneIterKeyR, styleBlue, "Субблок R\n\n");
//                    insertColorText(textPaneIterKeyR, styleBlack, textPaneAfterR.getText());


//                    textPaneAfterL.setText(null);
//                    textPaneAfterR.setText(null);
                } else {
                    insertColorText(textPaneXORResult, styleBlue, "Преобразование: 'сложение XOR'\n");
                    insertColorText(textPaneXORResult, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));
                    textXORResult = String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95);

                    textPaneIterKeyL.setText(null);
                    insertColorText(textPaneIterKeyL, styleBlue, "Субблок L\n");
                    insertColorText(textPaneIterKeyL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0, 47));

                    textPaneIterKeyR.setText(null);
                    insertColorText(textPaneIterKeyR, styleBlue, "Субблок R\n");
                    insertColorText(textPaneIterKeyR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));

                }


//                insertColorText(textPaneXORResult, styleBlue, "\n\nС чем производится XOR:");
//                insertColorText(textPaneXORResult, styleBlack, String.valueOf(Conversions.hex(current.getAdditional())));
                xorResultIdx++;
            }
//            else if (current.getName().equals("Swap keys " + swapIdx)){
//                System.out.println("DEBUG id: "+ id);
//                prevStep.setEnabled(true);
//                textPaneSwap.setText(null);
//
//                Style styleBlue = getStyleText(textPaneSwap, Color.BLUE);
//                Style styleBlack = getStyleText(textPaneSwap, Color.BLACK);
//                insertColorText(textPaneSwap, styleBlue, "Преобразование Swap:\n\n");
//                insertColorText(textPaneSwap, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
//                swapIdx++;
//
//
//            }

            if (current.getName().equals("Round key parts 0 and 1")) {
//                System.out.println("DEBUG id: " + id);
//                stage.setText("Раунд №1");
                if (roundIdx == 0) {
                    roundIdx += 2;
                    roundArrIdx++;
                }
                textPaneRoundKey.setText(null);
                textPaneBeforeL.setText(null);
                textPaneBeforeR.setText(null);
                textPaneAfterL.setText(null);
                textPaneAfterR.setText(null);
                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n");
                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ 1\n");
                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47));
                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0, 47));
                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ 2\n");
                insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95));
                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));


            } else if (current.getName().equals("Round key parts " + roundIdx + " and " + (roundIdx + 1))) {
                nextRound.setEnabled(true);
                nextStep.setEnabled(true);
//                System.out.println("DEBUG id: " + id);
                xorResultIdx = 0;
                xorConstantIdx = 0;
//                swapIdx = 0;
                cComputingIdx = 0;
                lIdx = 0;
                roundIdx++;
                roundArrIdx++;

                textPaneRoundKey.setText(null);
                textPaneBeforeL.setText(null);
                textPaneBeforeR.setText(null);
                textPaneAfterL.setText(null);
                textPaneAfterR.setText(null);
                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n");
                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ " + roundIdx + "\n");
                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47));
                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0, 47));
                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ " + (roundIdx + 1) + "\n");
                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));

//                JOptionPane.showMessageDialog(
//                        VisualizationExpandKey.this,
//                        new String[]{"Уведомление, что нужно кликнуть 3 раза"},
//                        "Уведомление о кликах",
//                        JOptionPane.INFORMATION_MESSAGE);

            }
            if (current.getName().equals("XOR key 1 with result 7")) {
                nextStep.setEnabled(false);
                textPaneAfterL.setText(null);
                textPaneAfterR.setText(null);
                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
                Style styleBlue = getStyleText(textPaneRoundKey, Color.BLUE);
                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
//                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0, 47));
                if (id == 196) {
                    insertColorText(textPaneAfterL, styleRed, "Раундовый ключ 10\n");
                    insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0, 47));
                    insertColorText(textPaneAfterR, styleRed, "Раундовый ключ 9\n");
                    insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));
                }
                else{
                    insertColorText(textPaneAfterL, styleBlue, "Субблок L'\n");
                    insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0, 47));
                    insertColorText(textPaneAfterR, styleBlue, "Субблок R'\n");
                    insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));
                }

            }
            if (id == 197) {
                nextStep.setEnabled(false);
            }
            tree.setSelectionPath(findTreePath(current));


            currentId = id;
        });
    } // nextStep


//    private void setCurrentIdPrevStep(int id, String key) {
//        SwingUtilities.invokeLater(() -> {
//
//            State current = stateList.get(id);
//            if(current.getName().equals("Round key parts 2 and 3")){
//                prevStep.setEnabled(false);
//            }
//
//            else if (current.getName().equals("Constant computing " + (cComputingIdx-2))){
//                System.out.println("DEBUG id: "+ id);
//                textPaneConstant.setText(null);
//
//                Style styleBlue = getStyleText(textPaneConstant, Color.BLUE);
//                Style styleBlack = getStyleText(textPaneConstant, Color.BLACK);
//                insertColorText(textPaneConstant, styleBlue, "Результат вычисления константы\n\n");
//                insertColorText(textPaneConstant, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
//
//                cComputingIdx--;
//            }
//            else if (current.getName().equals("Feistel round " + (roundFeistel-2)) | current.getName().equals("Feistel round " + (roundFeistel-1))){
//                stageFeistel.setText("№ итерации развертывания ключа "+(roundFeistel-1));
//
////                textPaneRoundKey.setText(null);
////                textPaneBeforeL.setText(null);
////                textPaneBeforeR.setText(null);
////                textPaneAfterL.setText(null);
////                textPaneAfterR.setText(null);
////                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
////                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
////                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n\n");
////                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
////                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ "+String.valueOf(roundIdx)+"\n\n");
////                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0,47));
////                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0,47));
////                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ "+String.valueOf(roundIdx+1)+"\n\n");
////                insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48,95));
////                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48,95));
//
//                nextStep.setEnabled(true);
//                roundFeistel--;
//            }
//            else if (current.getName().equals("XOR key 1 with constant "+(xorConstantIdx-2))) {
//                System.out.println("DEBUG id: "+ id);
//                textPaneXORConstant.setText(null);
//                Style styleBlue = getStyleText(textPaneXORConstant, Color.BLUE);
//                Style styleBlack = getStyleText(textPaneXORConstant, Color.BLACK);
//                insertColorText(textPaneXORConstant, styleBlue, "Результат X:\n\n");
//                insertColorText(textPaneXORConstant, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
////                insertColorText(textPaneXORConstant, styleBlue, "\n\nС чем производится XOR:\n\n");
////                insertColorText(textPaneXORConstant, styleBlack, String.valueOf(Conversions.hex(current.getAdditional())));
//                xorConstantIdx--;
//            }
//            else if (current.getName().equals("Substitute bytes "+ (sIdx-2))){
//                System.out.println("DEBUG id: "+ id);
//                textPaneS.setText(null);
//                Style styleBlue = getStyleText(textPaneS, Color.BLUE);
//                Style styleBlack = getStyleText(textPaneS, Color.BLACK);
//                insertColorText(textPaneS, styleBlue, "Результат S:\n\n");
//                insertColorText(textPaneS, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
//                sIdx--;
//
//            }
//            else if (current.getName().equals("Rotate bytes " + (lIdx-2))){
//                System.out.println("DEBUG id: "+ id);
//                textPaneL.setText(null);
////                textPaneRotate.setText(null);
//                Style styleBlue = getStyleText(textPaneL, Color.BLUE);
//                Style styleBlack = getStyleText(textPaneL, Color.BLACK);
//                insertColorText(textPaneL, styleBlue, "Результат L:\n\n");
//                insertColorText(textPaneL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
//
////                insertColorText(textPaneRotate, styleBlue, "Результат R:\n\n");
////                insertColorText(textPaneRotate, styleBlack, String.valueOf(Conversions.hex(current.getBefore())));
//                lIdx--;
//            }
//            else if (current.getName().equals("XOR key 1 with result "+(xorResultIdx-2))){
//                System.out.println("DEBUG id: "+ id);
//                textPaneXORResult.setText(null);
//                Style styleBlue = getStyleText(textPaneXORResult, Color.BLUE);
//                Style styleBlack = getStyleText(textPaneXORResult, Color.BLACK);
//                insertColorText(textPaneXORResult, styleBlue, "Результат X:\n\n");
//                insertColorText(textPaneXORResult, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
////                insertColorText(textPaneXORResult, styleBlue, "\n\nС чем производится XOR:\n\n");
////                insertColorText(textPaneXORResult, styleBlack, String.valueOf(Conversions.hex(current.getAdditional())));
//                xorResultIdx--;
//            }
////            else if (current.getName().equals("Swap keys " + (swapIdx-2))){
////                System.out.println("DEBUG id: "+ id);
////                textPaneSwap.setText(null);
////
////                Style styleBlue = getStyleText(textPaneSwap, Color.BLUE);
////                Style styleBlack = getStyleText(textPaneSwap, Color.BLACK);
////                insertColorText(textPaneSwap, styleBlue, "Результат Swap:\n\n");
////                insertColorText(textPaneSwap, styleBlack, String.valueOf(Conversions.hex(current.getAfter())));
////                swapIdx--;
////            }
//
//            if (current.getName().equals("Round key parts 0 and 1")){
//                System.out.println("DEBUG id: "+ id);
//                prevRound.setEnabled(false);
////                stage.setText("Раунд №1");
//                if (roundIdx == 0){
//                    roundIdx -= 2;
//                    xorResultIdx = roundIdx-3;
//                    xorConstantIdx = roundIdx-3;
////                    swapIdx = roundIdx-3;
//                    cComputingIdx = roundIdx-3;
//                    lIdx = roundIdx-3;
//                }
//                textPaneRoundKey.setText(null);
//                textPaneBeforeL.setText(null);
//                textPaneBeforeR.setText(null);
//                textPaneAfterL.setText(null);
//                textPaneAfterR.setText(null);
//                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
//                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
//                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n\n");
//                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
//                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ 1\n\n");
//                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0,47));
//                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0,47));
//                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ 2\n\n");
//                insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48,95));
//                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48,95));
//            }
//            else if (current.getName().equals("Round key parts "+(roundIdx-1)+" and "+roundIdx)){
//                prevStep.setEnabled(false);
//                nextStep.setEnabled(true);
//                System.out.println("DEBUG Round id: "+ id);
//                xorResultIdx = roundIdx-1;
//                xorConstantIdx = roundIdx-1;
////                swapIdx = roundIdx-1;
//                cComputingIdx = roundIdx-1;
//                lIdx = roundIdx-1;
//                roundIdx--;
//
//                textPaneRoundKey.setText(null);
//                textPaneBeforeL.setText(null);
//                textPaneBeforeR.setText(null);
//                textPaneAfterL.setText(null);
//                textPaneAfterR.setText(null);
//                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
//                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
//                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n\n");
//                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
//                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ "+String.valueOf(roundIdx)+"\n\n");
//                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0,47));
//                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0,47));
//                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ "+String.valueOf(roundIdx+1)+"\n\n");
//                insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48,95));
//                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48,95));
//
//            }
//
//            tree.setSelectionPath(findTreePath(current));
//            currentId = id;
//        });
//    } // prevStep


    private void setCurrentIdNextRound(int id, String key) {
        SwingUtilities.invokeLater(() -> {
            State current = stateList.get(id);
            System.out.println("DEBUG id nextRound: " + id);
            roundFeistel = 0;
            nextRound.setEnabled(id < 148);
            prevRound.setEnabled(id != 1);

            // {1, 50, 99, 148}

            switch (current.getName()) {
                case "Round key parts 2 and 3":
                    roundFeistelCounter = 1;
                    roundIdx = 0;
                    roundArrIdx = 0;
                    keyIdx = 1;
                    prevRound.setEnabled(false);
                    break;
                case "Round key parts 3 and 4":
                    roundFeistelCounter = 9;
                    roundIdx = 1;
                    roundArrIdx = 1;
                    keyIdx = 3;
                    prevRound.setEnabled(true);
                    break;
                case "Round key parts 4 and 5":
                    roundFeistelCounter = 17;
                    roundIdx = 2;
                    roundArrIdx = 2;
                    keyIdx = 5;
                    nextRound.setEnabled(true);
                    break;
                case "Round key parts 5 and 6":
                    roundFeistelCounter = 25;
                    roundIdx = 3;
                    roundArrIdx = 3;
                    keyIdx = 7;
                    nextRound.setEnabled(false);
                    break;
            }



            if (current.getName().equals("Round key parts 0 and 1")) {
//                stage.setText("Раунд №"+(roundIdx+1));
                stageFeistel.setText("№ итерации развертывания ключа 0");
                keyIdx = 1;
//                roundIdx += 2;
//                roundArrIdx++;
                textPaneBeforeL.setText(null);
                textPaneAfterL.setText(null);
                textPaneBeforeR.setText(null);
                textPaneAfterR.setText(null);
                textPaneIterKeyL.setText(null);
                textPaneIterKeyR.setText(null);

                textPaneViz11.setText(null);
                textPaneViz12.setText(null);
                textPaneViz13.setText(null);
                textPaneViz14.setText(null);
                textPaneViz15.setText(null);
                textPaneViz21.setText(null);
                textPaneViz22.setText(null);
                textPaneViz23.setText(null);
                textPaneViz24.setText(null);
                textPaneViz25.setText(null);


                textPaneConstant.setText(null);
                textPaneL.setText(null);
                textPaneXORConstant.setText(null);
                textPaneXORResult.setText(null);
                textPaneS.setText(null);
//            textPaneRotate.setText(null);
                textPaneS.setText(null);
                textPaneRoundKey.setText(null);

                roundKey1 = String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47);
                roundKey2 = String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95);

//                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
//                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
//                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n\n");
//                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
//                insertColorText(textPaneAfterL, styleRed, "Раундовый ключ 1\n\n");
//                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47));
//                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0, 47));
//                insertColorText(textPaneAfterR, styleRed, "Раундовый ключ 2\n\n");
//                insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95));
//                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48, 95));
            } else if (current.getName().equals("Round key parts " + (roundIdx+2) + " and " + (roundIdx + 3))) {
//                textPaneBeforeL.setText(null);
//                textPaneAfterL.setText(null);
//                textPaneBeforeR.setText(null);
//                textPaneAfterR.setText(null);
//
//                textPaneConstant.setText(null);
//                textPaneL.setText(null);
//                textPaneXORConstant.setText(null);
//                textPaneXORResult.setText(null);
//                textPaneS.setText(null);
////            textPaneRotate.setText(null);
//                textPaneS.setText(null);
//                JOptionPane.showMessageDialog(
//                        VisualizationExpandKey.this,
//                        new String[]{"Уведомление, что нужно кликнуть 3 раза"},
//                        "Уведомление о кликах",
//                        JOptionPane.INFORMATION_MESSAGE);
                roundKey2 = String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47);
                roundKey1 = String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95);

//                keyIdx += 2;
                prevRound.setEnabled(true);
                nextStep.setEnabled(true);

                textPaneBeforeL.setText(null);
                textPaneAfterL.setText(null);
                textPaneBeforeR.setText(null);
                textPaneAfterR.setText(null);
                textPaneIterKeyL.setText(null);
                textPaneIterKeyR.setText(null);

                textPaneViz11.setText(null);
                textPaneViz12.setText(null);
                textPaneViz13.setText(null);
                textPaneViz14.setText(null);
                textPaneViz15.setText(null);
                textPaneViz21.setText(null);
                textPaneViz22.setText(null);
                textPaneViz23.setText(null);
                textPaneViz24.setText(null);
                textPaneViz25.setText(null);

                textPaneConstant.setText(null);
                textPaneL.setText(null);
                textPaneXORConstant.setText(null);
                textPaneXORResult.setText(null);
                textPaneS.setText(null);
//            textPaneRotate.setText(null);
                textPaneS.setText(null);
                textPaneRoundKey.setText(null);

//                stage.setText("Раунд №"+(roundIdx+1));
                stageFeistel.setText("№ итерации развертывания ключа " + (roundFeistelCounter));
                nextRound.setEnabled(id < 148);
                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
                Style styleBlue = getStyleText(textPaneRoundKey, Color.BLUE);
                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n");
                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
//                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ "+String.valueOf(keyIdx)+"\n\n");
                textPaneIterKeyL.setText(null);
                insertColorText(textPaneIterKeyL, styleBlue, "Субблок L\n");
                insertColorText(textPaneIterKeyL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47));

                textPaneIterKeyR.setText(null);
                insertColorText(textPaneIterKeyR, styleBlue, "Субблок R\n");
                insertColorText(textPaneIterKeyR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95));

                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ " + String.valueOf(keyIdx) + "\n");
                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47));
//                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0,47));
                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ " + String.valueOf(keyIdx + 1) + "\n");
                insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95));
//                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48,95));

                xorResultIdx = 0;
                xorConstantIdx = 0;
//                swapIdx = 0;
                cComputingIdx = 0;
                lIdx = 0;
                sIdx = 0;
//                roundIdx++;
//                roundArrIdx++;
                roundFeistel = 0;
//                roundFeistelCounter += 7;
            } //else if (current.getName().equals("Round key parts 2 and 3")) {
//                roundFeistelCounter = 1;
//            }


            tree.setSelectionPath(findTreePath(current));
            roundArrIdx++;
            currentId = indexRounds[roundArrIdx]; // массив
        });
    } // nextRound

    private void setCurrentIdPrevRound(int id, String key) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("DEBUG id prevRound: " + id);
            System.out.println("DEBUG roundIdx prevRound: " + roundIdx);
            State current = stateList.get(id);

            if (id == 1 | id == 1 || id == 2) {
                prevRound.setEnabled(false);
            }

            roundArrIdx--;
            checkDoubleClickPartOne = 0;
            checkDoubleClickPartTwo = 0;
//            prevRound.setEnabled(id != 1);
            nextStep.setEnabled(true);
            keyIdx -= 1;
            textPaneRoundKey.setText(null);
            textPaneBeforeL.setText(null);
            textPaneAfterL.setText(null);
            textPaneIterKeyL.setText(null);
            textPaneIterKeyR.setText(null);
            textPaneIterKeyL.setText(null);
            textPaneIterKeyR.setText(null);

            textPaneBeforeR.setText(null);
            textPaneAfterR.setText(null);
            textPaneConstant.setText(null);
            textPaneL.setText(null);
            textPaneXORConstant.setText(null);
            textPaneXORResult.setText(null);
            textPaneS.setText(null);

            switch (current.getName()) {
                case "Round key parts 2 and 3":
                    roundFeistelCounter = 1;
                    roundIdx = 0;
                    keyIdx = 1;
                    roundArrIdx = 0;
                    prevRound.setEnabled(false);
                    break;
                case "Round key parts 3 and 4":
                    roundFeistelCounter = 9;
                    roundIdx = 1;
                    keyIdx = 3;
                    roundArrIdx = 1;
                    prevRound.setEnabled(true);
                    break;
                case "Round key parts 4 and 5":
                    roundFeistelCounter = 17;
                    roundIdx = 2;
                    roundArrIdx = 2;
                    keyIdx = 5;
                    nextRound.setEnabled(true);
                    break;
                case "Round key parts 5 and 6":
                    roundFeistelCounter = 25;
                    roundIdx = 3;
                    roundArrIdx = 3;
                    keyIdx = 7;
                    nextRound.setEnabled(false);
                    break;
            }

            textPaneViz11.setText(null);
            textPaneViz12.setText(null);
            textPaneViz13.setText(null);
            textPaneViz14.setText(null);
            textPaneViz15.setText(null);
            textPaneViz21.setText(null);
            textPaneViz22.setText(null);
            textPaneViz23.setText(null);
            textPaneViz24.setText(null);
            textPaneViz25.setText(null);
//            textPaneRotate.setText(null);
            roundFeistel = 7;
            nextRound.setEnabled(true);

            System.out.println("keyIdx: "+keyIdx);

            Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
            Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
            insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n");
            insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
            insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ " + (keyIdx) + "\n");
            insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0, 47));
//            insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0,47));

            insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ " + (keyIdx + 1) + "\n");
            insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48, 95));
//            insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48,95));
//            switch (current.getName()) {
//                case "Round key parts 2 and 3":
//                    keyIdx = 3;
//                    break;
//                case "Round key parts 3 and 4":
//                    keyIdx = 5;
//                    break;
//                case "Round key parts 4 and 5":
//                    keyIdx = 7;
//                    break;
//                case "Round key parts 5 and 6":
//                    keyIdx = 9;
//                    break;
//            }

            if (current.getName().equals("Round key parts 0 and 1")) {
//                stage.setText("Раунд №1");
//                prevStep.setEnabled(false);
                stageFeistel.setText("№ итерации развертывания ключа 0");
                roundIdx -= 2;
                if (roundIdx < 0) {
                    roundIdx = 0;
                    keyIdx = 1;
                }
                roundFeistel = 0;
                roundFeistelCounter = 1;
                keyIdx = 1;

//                textPaneRoundKey.setText(null);
//                textPaneBeforeL.setText(null);
//                textPaneBeforeR.setText(null);
//                textPaneAfterL.setText(null);
//                textPaneAfterR.setText(null);
//
//                textPaneViz11.setText(null);
//                textPaneViz12.setText(null);
//                textPaneViz13.setText(null);
//                textPaneViz14.setText(null);
//                textPaneViz15.setText(null);
//                textPaneViz21.setText(null);
//                textPaneViz22.setText(null);
//                textPaneViz23.setText(null);
//                textPaneViz24.setText(null);
//                textPaneViz25.setText(null);

            } else if (current.getName().equals("Round key parts " + (roundIdx +2) + " and " + (roundIdx+3))) {
//                stage.setText("Раунд №"+(roundIdx-1));

//                prevStep.setEnabled(false);
                stageFeistel.setText("№ итерации развертывания ключа " + (roundFeistelCounter));
//                textPaneRoundKey.setText(null);
//                textPaneBeforeL.setText(null);
//                textPaneBeforeR.setText(null);
//                textPaneAfterL.setText(null);
//                textPaneAfterR.setText(null);
//                Style styleRed = getStyleText(textPaneRoundKey, Color.RED);
//                Style styleBlack = getStyleText(textPaneRoundKey, Color.BLACK);
//                insertColorText(textPaneRoundKey, styleRed, "Секретный ключ:\n\n");
//                insertColorText(textPaneRoundKey, styleBlack, String.valueOf(key));
//                insertColorText(textPaneBeforeL, styleRed, "Раундовый ключ "+String.valueOf(keyIdx)+"\n\n");
//                insertColorText(textPaneBeforeL, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(0,47));
//                insertColorText(textPaneAfterL, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(0,47));
//                insertColorText(textPaneBeforeR, styleRed, "Раундовый ключ "+String.valueOf(keyIdx+1)+"\n\n");
//                insertColorText(textPaneBeforeR, styleBlack, String.valueOf(Conversions.hex(current.getBefore())).substring(48,95));
//                insertColorText(textPaneAfterR, styleBlack, String.valueOf(Conversions.hex(current.getAfter())).substring(48,95));
                xorResultIdx = 0;
                xorConstantIdx = 0;
//                swapIdx = 0;
                cComputingIdx = 0;
                lIdx = 0;
                sIdx = 0;
//                keyIdx -= 1;

//                roundArrIdx--;
//                roundIdx--;
                roundFeistel = 0;
//                roundFeistelCounter -= 7;

            } else if (current.getName().equals("Feistel round" + (roundFeistel))) {
                stageFeistel.setText("№ итерации развертывания ключа " + (roundFeistelCounter - 1));
            } else if (current.getName().equals("Round key parts 2 and 3")) {
                roundFeistelCounter = 1;
            }
            if (id == 1) {
//                stage.setText("Раунд №1");
                stageFeistel.setText("№ итерации развертывания ключа 0");
                prevRound.setEnabled(false);
            }


            tree.setSelectionPath(findTreePath(current));
//            currentId = id;
            currentId = indexRounds[roundArrIdx];
        });
    } // prevRound

    private List<State> createStateList(State state) {
        ArrayList<String> test = new ArrayList<String>(Arrays.asList(
                "Feistel round 0",
                "Feistel round 1",
                "Feistel round 2",
                "Feistel round 3",
                "Feistel round 4",
                "Feistel round 5",
                "Feistel round 6",
                "Feistel round 7",
                "Constant computing 0",
                "Constant computing 1",
                "Constant computing 2",
                "Constant computing 3",
                "Constant computing 4",
                "Constant computing 5",
                "Constant computing 6",
                "Constant computing 7",
                "XOR key 1 with constant 0",
                "XOR key 1 with constant 1",
                "XOR key 1 with constant 2",
                "XOR key 1 with constant 3",
                "XOR key 1 with constant 4",
                "XOR key 1 with constant 5",
                "XOR key 1 with constant 6",
                "XOR key 1 with constant 7",
                "XOR key 1 with result 0",
                "XOR key 1 with result 1",
                "XOR key 1 with result 2",
                "XOR key 1 with result 3",
                "XOR key 1 with result 4",
                "XOR key 1 with result 5",
                "XOR key 1 with result 6",
                "XOR key 1 with result 7",
//                "Round key parts 0 and 1",
                "Round key parts 2 and 3",
                "Round key parts 3 and 4",
                "Round key parts 4 and 5",
                "Round key parts 5 and 6",
                "XOR key 1 with result",
                "Substitute bytes 0",
                "Substitute bytes 1",
                "Substitute bytes 2",
                "Substitute bytes 3",
                "Substitute bytes 4",
                "Substitute bytes 5",
                "Substitute bytes 6",
                "Substitute bytes 7",
                "Substitute bytes 8",
//                "Swap keys 0",
//                "Swap keys 1",
//                "Swap keys 2",
//                "Swap keys 3",
//                "Swap keys 4",
//                "Swap keys 5",
//                "Swap keys 6",
//                "Swap keys 7",
                "Rotate bytes 0",
                "Rotate bytes 1",
                "Rotate bytes 2",
                "Rotate bytes 3",
                "Rotate bytes 4",
                "Rotate bytes 5",
                "Rotate bytes 6",
                "Rotate bytes 7",
                "Rotate bytes 8"
        ));
        List<State> list = new ArrayList<>();
        list.add(state);
        for (State sub : state.getInner()) {
            for (String s : test) {
                if (sub.getName().equals(s)) {
                    list.addAll(createStateList(sub));
                }
            }
        }
        return list;
    }

    private GridBagConstraints createConstants(int width, int height, int weightX, int weightY, int x, int y, Insets insets) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = weightX;
        constraints.weighty = weightY;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = insets;
        return constraints;
    }

    private JTree createStateTree(State state, String key) {
        JTree tree = new JTree(createStateTreeNodes(state));
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
            setCurrentIdNextStep(stateList.indexOf((State) node.getUserObject()), key);
        });
        return tree;
    }

    private DefaultMutableTreeNode createStateTreeNodes(State state) {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(state.getName());
        top.setUserObject(state);
        for (State sub : state.getInner()) {
            top.add(createStateTreeNodes(sub));
        }
        return top;
    }

    //
    private TreePath findTreePath(State state) {
        Enumeration<TreeNode> e = ((DefaultMutableTreeNode) tree.getModel().getRoot()).depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.getUserObject() == state) return new TreePath(node.getPath());
        }
        return null;
    }
}
