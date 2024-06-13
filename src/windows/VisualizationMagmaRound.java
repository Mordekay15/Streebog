package windows;

import statement.State;
import utils.Conversions;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static utils.OSInfo.*;
import static utils.OSInfo.isUnix;

public class VisualizationMagmaRound extends JDialog {

//    private final JTree tree;
    private int currentId = 5;
    private int roundCounter = 1;

    private String roundKey1 = "";
    private String roundKey2 = "";

    private int roundArrIdx = 0;
    private int fontSize = 14;

    private String valueRoundBeforeL = "";
    private String valueRoundBeforeR = "";
    private String valueRoundAfterL = "";
    private String valueRoundAfterR = "";

    private boolean checkDoubleClick = true;
    private boolean firstStep = false;
    private final int[] indexRounds = new int[]{1, 6, 11, 16, 21, 26, 31, 36, 41, 46, 51, 56, 61, 66, 71, 76, 81, 86, 91, 96, 101, 106, 111, 116, 121, 126, 131, 136, 141, 146, 151, 156};
    private final int[] indexVisFeistel = new int[]{10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120, 125, 130, 135, 140, 145, 150, 155, 160};
    private final List<State> stateList;

    private final JButton nextStep = new JButton(">");
    private final JButton nextRound = new JButton(">>");
    private final JButton previousRound = new JButton("<<");
    private final JLabel stage = new JLabel("Раунд _", SwingConstants.CENTER);


    private JTextPane fieldRoundBeforeL = new JTextPane();
    private JTextPane fieldRoundBeforeR = new JTextPane();
    private JTextPane fieldRoundKey = new JTextPane();
    private JTextPane fieldAddRoundKey = new JTextPane();
    private JTextPane fieldTransform = new JTextPane();
    private JTextPane fieldShiftLeft = new JTextPane();
    private JTextPane fieldXOR = new JTextPane();
    private JTextPane fieldRoundAfterL = new JTextPane();
    private JTextPane fieldRoundAfterR = new JTextPane();

    private String textTransform = "";
    private String textShiftLeft = "";
    private String textXOR = "";
    private String textAddRoundKey = "";


    private JTextPane fieldViz11 = new JTextPane();
    private JTextPane fieldViz12 = new JTextPane();
    private JTextPane fieldViz13 = new JTextPane();
    private JTextPane fieldViz14 = new JTextPane();
    private JTextPane fieldViz15 = new JTextPane();
    private JTextPane fieldViz16 = new JTextPane();
    private JTextPane fieldViz17 = new JTextPane();

    private JTextPane fieldViz21 = new JTextPane();
    private JTextPane fieldViz22 = new JTextPane();
    private JTextPane fieldViz23 = new JTextPane();
    private JTextPane fieldViz24 = new JTextPane();
    private JTextPane fieldViz25 = new JTextPane();
    private JTextPane fieldViz26 = new JTextPane();
    private JTextPane fieldViz27 = new JTextPane();

    private final JButton addRoundKeyHint = new JButton("Преобразование: 'сложение по модулю 2^32'");

    private final JButton tableHint = new JButton("Преобразование: 'подстановка S'");
    private final JButton shiftHint = new JButton("Преобразование: 'циклический сдвиг <<11'");
    private final JButton xorHint = new JButton("Преобразование: 'сложение XOR'");

    private final Color colorViz_ij = Color.getHSBColor(0.068f, 0.06f, 1.f);
    private final Color colorLightRed = Color.getHSBColor(0.016f, 0.11f, 1.f);
    private final Color colorLightBlue = Color.getHSBColor(0.5f, 0.12f, 1.f);

    public VisualizationMagmaRound(Component owner, State state, byte[] result, String key, String block) {
        super((Frame) SwingUtilities.getWindowAncestor(owner), "Визуализация раундовых преобразований шифра 'Магма'", false);

//        tree = createStateTree(state, key);
//        tree.setScrollsOnExpand(true);
//        tree.setExpandsSelectedPaths(true);

        fieldAddRoundKey.setEditable(false);
        fieldRoundKey.setEditable(false);
        fieldRoundAfterL.setEditable(false);
        fieldRoundAfterR.setEditable(false);
        fieldRoundBeforeL.setEditable(false);
        fieldRoundBeforeR.setEditable(false);
        fieldShiftLeft.setEditable(false);
        fieldTransform.setEditable(false);
        fieldViz11.setEditable(false);
        fieldViz12.setEditable(false);
        fieldViz13.setEditable(false);
        fieldViz14.setEditable(false);
        fieldViz15.setEditable(false);
        fieldViz16.setEditable(false);
        fieldViz17.setEditable(false);
        fieldViz21.setEditable(false);
        fieldViz22.setEditable(false);
        fieldViz23.setEditable(false);
        fieldViz24.setEditable(false);
        fieldViz25.setEditable(false);
        fieldViz26.setEditable(false);
        fieldViz27.setEditable(false);
        fieldXOR.setEditable(false);


        stage.setForeground(Color.RED);

        fieldAddRoundKey.setBackground(colorLightBlue);
        fieldTransform.setBackground(colorLightBlue);
        fieldShiftLeft.setBackground(colorLightBlue);
        fieldXOR.setBackground(colorLightBlue);

        fieldRoundBeforeL.setBackground(colorLightRed);
        fieldRoundBeforeR.setBackground(colorLightRed);
        fieldRoundAfterL.setBackground(colorLightRed);
        fieldRoundAfterR.setBackground(colorLightRed);
        fieldRoundKey.setBackground(colorLightRed);


        fieldViz11.setBackground(colorViz_ij);
        fieldViz12.setBackground(colorViz_ij);
        fieldViz13.setBackground(colorViz_ij);
        fieldViz14.setBackground(colorViz_ij);
        fieldViz15.setBackground(colorViz_ij);
        fieldViz16.setBackground(colorViz_ij);
        fieldViz17.setBackground(colorViz_ij);

        fieldViz21.setBackground(colorViz_ij);
        fieldViz22.setBackground(colorViz_ij);
        fieldViz23.setBackground(colorViz_ij);
        fieldViz24.setBackground(colorViz_ij);
        fieldViz25.setBackground(colorViz_ij);
        fieldViz26.setBackground(colorViz_ij);
        fieldViz27.setBackground(colorViz_ij);

//        float[] hsbValues = Color.RGBtoHSB(224, 255, 255, null);
//        float hue = hsbValues[0]; // оттенок
//        float saturation = hsbValues[1]; // насыщенность
//        float brightness = hsbValues[2]; // яркость
//        System.out.println("hue: "+hue);
//        System.out.println("saturation: "+saturation);
//        System.out.println("brightness: "+brightness);


//         .setBackground(Color.getHSBColor(0.016f, 0.11f, 1.f)); // красноватый
//         .setBackground(Color.getHSBColor(0.5f, 0.12f, 1.f)); // синеватый



        Font font = new Font("Arial", Font.BOLD, fontSize);
        addRoundKeyHint.setFont(font);
        addRoundKeyHint.setForeground(Color.BLUE);

        tableHint.setFont(font);
        tableHint.setForeground(Color.BLUE);

        shiftHint.setFont(font);
        shiftHint.setForeground(Color.BLUE);

        xorHint.setFont(font);
        xorHint.setForeground(Color.BLUE);

        nextRound.setFont(font);
        previousRound.setFont(font);
        nextStep.setFont(font);




        stateList = createStateList(state);
        System.out.println(stateList);
        nextRound.setForeground(Color.RED);
        previousRound.setForeground(Color.RED);

        nextStep.setForeground(Color.BLUE);


        State currentStart = stateList.get(2);
        fieldAddRoundKey.setText(null);

        Style styleBlueARK = getStyleText(fieldAddRoundKey, Color.BLUE);
        insertColorText(fieldAddRoundKey, styleBlueARK, "Результат:");
        insertColorText(fieldAddRoundKey, styleBlueARK, " " + Conversions.hex(currentStart.getAfter()));

        currentStart = stateList.get(3);
        fieldTransform.setText(null);
        Style styleBlueT = getStyleText(fieldTransform, Color.BLUE);
        insertColorText(fieldTransform, styleBlueT, "Результат:");
        insertColorText(fieldTransform, styleBlueT, " " + Conversions.hex(currentStart.getAfter()));


        currentStart = stateList.get(4);
        fieldShiftLeft.setText(null);
        // TODO: костыль <<11 :(
        int word = Conversions.word(currentStart.getBefore());
        byte[] ee = Conversions.word(((word << 11) & 0xFFFFF800) | ((word >> 21) & 0x7FF));
        Style styleBlueSL = getStyleText(fieldShiftLeft, Color.BLUE);
        insertColorText(fieldShiftLeft, styleBlueSL, "Результат:");
        insertColorText(fieldShiftLeft, styleBlueSL, " " + Conversions.hex(ee));

        currentStart = stateList.get(5);
        fieldXOR.setText(null);

        Style styleBlueXOR = getStyleText(fieldXOR, Color.RED);
        insertColorText(fieldXOR, styleBlueXOR, "Результат:");
        insertColorText(fieldXOR, styleBlueXOR, " " + Conversions.hex(currentStart.getAfter()));

        stage.setText("Раунд №1");
        stage.setFont(font);
        stage.setForeground(Color.RED);

        currentStart = stateList.get(1);
        firstStep = false;
        fieldRoundBeforeL.setText(null);
        Style styleRedBL = getStyleText(fieldRoundBeforeL, Color.RED);
        insertColorText(fieldRoundBeforeL, styleRedBL, "Субблок L:");
        insertColorText(fieldRoundBeforeL, styleRedBL, " " + String.valueOf(Conversions.hex(currentStart.getBefore())).substring(0, 11));

        fieldRoundBeforeR.setText(null);
        Style styleRedBR = getStyleText(fieldRoundBeforeR, Color.RED);

        insertColorText(fieldRoundBeforeR, styleRedBR, "Субблок R:");
        insertColorText(fieldRoundBeforeR, styleRedBR, " " + String.valueOf(Conversions.hex(currentStart.getBefore())).substring(12, 23));

        fieldRoundAfterL.setText(null);
        Style styleRedAL = getStyleText(fieldRoundAfterL, Color.RED);
        insertColorText(fieldRoundAfterL, styleRedAL, "Субблок L':");
        insertColorText(fieldRoundAfterL, styleRedAL, " " + String.valueOf(Conversions.hex(currentStart.getAfter())).substring(0, 11));

        fieldRoundAfterR.setText(null);
        Style styleRedAR = getStyleText(fieldRoundAfterR, Color.RED);
        insertColorText(fieldRoundAfterR, styleRedAR, "Субблок R':");
        insertColorText(fieldRoundAfterR, styleRedAR, " " + String.valueOf(Conversions.hex(currentStart.getAfter())).substring(12, 23));

        fieldRoundKey.setText(null);
        Style styleRedRK = getStyleText(fieldRoundKey, Color.RED);
        insertColorText(fieldRoundKey, styleRedRK, "Ключ раунда:");
        insertColorText(fieldRoundKey, styleRedRK, " " + String.valueOf(Conversions.hex(currentStart.getAdditional())));
        nextStep.addActionListener(e -> setCurrentIdNextStep(currentId + 1));
        nextRound.addActionListener(e -> setCurrentIdNextRound(indexRounds[roundArrIdx + 1]));
        previousRound.addActionListener(e -> setCurrentIdPrevRound(indexRounds[roundArrIdx - 1]));

        addRoundKeyHint.addActionListener(e -> hintAddRoundKey());
        tableHint.addActionListener(e -> hintTable());
        shiftHint.addActionListener(e -> hintShift());
        xorHint.addActionListener(e -> hintXOR());
        setCurrentIdNextStep(5);
//        tree.setSelectionPath(findTreePath(stateList.get(5)));
        getContentPane().setLayout(new GridBagLayout());


        GridBagConstraints fieldRoundBeforeLConstraints = createConstants(1, 1, 1, 1, 0, 0, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldRoundBeforeL, fieldRoundBeforeLConstraints);

        GridBagConstraints fieldRoundBeforeRConstraints = createConstants(1, 1, 1, 1, 1, 0, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldRoundBeforeR, fieldRoundBeforeRConstraints);


        GridBagConstraints fieldViz11Constraints = createConstants(1, 1, 1, 1, 0, 1, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz12Constraints = createConstants(1, 1, 1, 1, 0, 2, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz13Constraints = createConstants(1, 1, 1, 1, 0, 3, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz14Constraints = createConstants(1, 1, 1, 1, 0, 4, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz15Constraints = createConstants(1, 1, 1, 1, 0, 5, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz16Constraints = createConstants(1, 1, 1, 1, 0, 6, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz17Constraints = createConstants(1, 1, 1, 1, 0, 7, new Insets(5, 5, 5, 0));

        getContentPane().add(fieldViz11, fieldViz11Constraints);
        getContentPane().add(fieldViz12, fieldViz12Constraints);
        getContentPane().add(fieldViz13, fieldViz13Constraints);
        getContentPane().add(fieldViz14, fieldViz14Constraints);
        getContentPane().add(fieldViz15, fieldViz15Constraints);
        getContentPane().add(fieldViz16, fieldViz16Constraints);
        getContentPane().add(fieldViz17, fieldViz17Constraints);


        GridBagConstraints fieldViz21Constraints = createConstants(1, 1, 1, 1, 1, 1, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz22Constraints = createConstants(1, 1, 1, 1, 1, 2, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz23Constraints = createConstants(1, 1, 1, 1, 1, 3, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz24Constraints = createConstants(1, 1, 1, 1, 1, 4, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz25Constraints = createConstants(1, 1, 1, 1, 1, 5, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz26Constraints = createConstants(1, 1, 1, 1, 1, 6, new Insets(5, 5, 5, 0));
        GridBagConstraints fieldViz27Constraints = createConstants(1, 1, 1, 1, 1, 7, new Insets(5, 5, 5, 0));

        getContentPane().add(fieldViz21, fieldViz21Constraints);
        getContentPane().add(fieldViz22, fieldViz22Constraints);
        getContentPane().add(fieldViz23, fieldViz23Constraints);
        getContentPane().add(fieldViz24, fieldViz24Constraints);
        getContentPane().add(fieldViz25, fieldViz25Constraints);
        getContentPane().add(fieldViz26, fieldViz26Constraints);
        getContentPane().add(fieldViz27, fieldViz27Constraints);


        GridBagConstraints fieldRoundKeyConstraints = createConstants(1, 1, 1, 1, 4, 0, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldRoundKey, fieldRoundKeyConstraints);

        GridBagConstraints fieldAddRoundKeyConstraints = createConstants(1, 1, 1, 1, 2, 2, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldAddRoundKey, fieldAddRoundKeyConstraints);

        GridBagConstraints fieldTransformConstraints = createConstants(1, 1, 1, 1, 2, 4, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldTransform, fieldTransformConstraints);

        GridBagConstraints fieldShiftLeftConstraints = createConstants(1, 1, 1, 1, 2, 6, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldShiftLeft, fieldShiftLeftConstraints);

        GridBagConstraints fieldXORConstraints = createConstants(1, 1, 1, 1, 2, 8, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldXOR, fieldXORConstraints);

        GridBagConstraints fieldRoundAfterLConstraints = createConstants(1, 1, 1, 1, 0, 8, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldRoundAfterL, fieldRoundAfterLConstraints);

        GridBagConstraints fieldRoundAfterRConstraints = createConstants(1, 1, 1, 1, 1, 8, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldRoundAfterR, fieldRoundAfterRConstraints);

        GridBagConstraints stageConstraints = createConstants(1, 1, 2, 1, 2, 9, new Insets(0, 5, 5, 0));
        getContentPane().add(stage, stageConstraints);


        GridBagConstraints nextStepConstraints = createConstants(1, 1, 1, 1, 4, 9, new Insets(0, 5, 5, 0));
        getContentPane().add(nextStep, nextStepConstraints);


        GridBagConstraints previousRoundConstraints = createConstants(1, 1, 1, 1, 0, 10, new Insets(0, 5, 5, 0));
        getContentPane().add(previousRound, previousRoundConstraints);

        GridBagConstraints nextRoundConstraints = createConstants(1, 1, 1, 1, 4, 10, new Insets(0, 5, 5, 0));
        getContentPane().add(nextRound, nextRoundConstraints);


        GridBagConstraints addRoundKeyHintConstraints = createConstants(1, 1, 1, 1, 2, 1, new Insets(0, 5, 5, 0));
        getContentPane().add(addRoundKeyHint, addRoundKeyHintConstraints);

        GridBagConstraints tableHintConstraints = createConstants(1, 1, 1, 1, 2, 3, new Insets(0, 5, 5, 0));
        getContentPane().add(tableHint, tableHintConstraints);

        GridBagConstraints shiftHintConstraints = createConstants(1, 1, 1, 1, 2, 5, new Insets(0, 5, 5, 0));
        getContentPane().add(shiftHint, shiftHintConstraints);

        GridBagConstraints xorHintConstraints = createConstants(1, 1, 1, 1, 2, 7, new Insets(0, 5, 5, 0));
        getContentPane().add(xorHint, xorHintConstraints);


//        JScrollPane treePane = new JScrollPane(tree);
//        treePane.setPreferredSize(new Dimension(200, treePane.getPreferredSize().height));
//        GridBagConstraints treeConstraints = createConstants(1, 2, 1, 8, 12, 0, new Insets(5, 5, 5, 5));
//        getContentPane().add(treePane, treeConstraints);


        setResizable(true);
        setPreferredSize(new Dimension(800, 600));
        pack();
    }

    private void hintAddRoundKey() {
        String textFileName = "AddRoundKey_hint.txt";
        String title = "Преобразование: сложение по модулю 2^32";
        String user = getUser();

        if (isWindows()) {
            String path = "C:\\Users\\" + user + "\\Documents\\CryptoETU\\"+ textFileName;
            try {
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isMac()) {
            String path = "/Users/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isUnix()) {
            String path = "/home/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hintTable() {
        String textFileName = "Table_hint.txt";
        String title = "Преобразование: подстановка S";
        String user = getUser();

        if (isWindows()) {
            String path = "C:\\Users\\" + user + "\\Documents\\CryptoETU\\"+ textFileName;
            try {
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isMac()) {
            String path = "/Users/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isUnix()) {
            String path = "/home/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hintShift() {
        String textFileName = "Shift_hint.txt";
        String title = "Преобразование: циклический сдвиг <<11";
        String user = getUser();

        if (isWindows()) {
            String path = "C:\\Users\\" + user + "\\Documents\\CryptoETU\\"+ textFileName;
            try {
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isMac()) {
            String path = "/Users/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isUnix()) {
            String path = "/home/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hintXOR() {
        String textFileName = "XOR_hint.txt";
        String title = "Преобразование: сложение XOR";
        String user = getUser();


        if (isWindows()) {
            String path = "C:\\Users\\" + user + "\\Documents\\CryptoETU\\"+ textFileName;
            try {
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isMac()) {
            String path = "/Users/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isUnix()) {
            String path = "/home/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationMagmaRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void Hint2() throws Exception {
        File f = new File("src/main/resources/images/iteration.png");
        Desktop dt = Desktop.getDesktop();
        dt.open(f);

        JOptionPane.showMessageDialog(
                VisualizationMagmaRound.this,
                new String[]{"В ходе последней (32-й) итерации так же, как и в остальных, преобразуется правая половина,",
                        "после чего полученный результат пишется в левую часть исходного блока, а правая сохраняет свое значение.",
                        ""},
                "Схема одной итерации",
                JOptionPane.INFORMATION_MESSAGE);
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

    private boolean numberCheck(int digit, int[] arr) {
        for (int i : arr) {
            if (i == digit) {
                return true;
            }
        }
        return false;
    }

    private void sleep(int ms) throws InterruptedException {
        Thread.sleep(ms);
    }

    private void visualizeFeistel(int id, String textXOR) {
        Font font = new Font("Arial", Font.BOLD, fontSize);
        fieldViz21.setFont(font);
        fieldViz21.setForeground(Color.BLUE);
        fieldViz12.setFont(font);
        fieldViz12.setForeground(Color.BLUE);
        fieldViz13.setFont(font);
        fieldViz13.setForeground(Color.BLUE);
        fieldViz14.setFont(font);
        fieldViz14.setForeground(Color.BLUE);
        fieldViz15.setFont(font);
        fieldViz15.setForeground(Color.BLUE);
        fieldViz16.setFont(font);
        fieldViz16.setForeground(Color.BLUE);
        fieldViz17.setFont(font);
        fieldViz17.setForeground(Color.BLUE);
        fieldViz11.setFont(font);
        fieldViz11.setForeground(Color.BLUE);
        fieldViz22.setFont(font);
        fieldViz22.setForeground(Color.BLUE);
        fieldViz23.setFont(font);
        fieldViz23.setForeground(Color.BLUE);
        fieldViz24.setFont(font);
        fieldViz24.setForeground(Color.BLUE);
        fieldViz25.setFont(font);
        fieldViz25.setForeground(Color.BLUE);
        fieldViz26.setFont(font);
        fieldViz26.setForeground(Color.BLUE);
        fieldViz27.setFont(font);
        fieldViz27.setForeground(Color.BLUE);
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                int ms = 1000;
                Style styleBlueXOR = getStyleText(fieldXOR, Color.BLUE);

                fieldRoundAfterL.setText(null);
                fieldRoundAfterR.setText(null);

                nextRound.setEnabled(false);
                previousRound.setEnabled(false);
                nextStep.setEnabled(false);

                fieldViz21.setText(roundKey2);
                sleep(ms);

                fieldViz12.setText(roundKey2);
                fieldViz21.setText(null);
                sleep(ms);

                fieldViz13.setText(roundKey2);
                fieldViz12.setText(null);
                sleep(ms);

                fieldViz14.setText(roundKey2);
                fieldViz13.setText(null);
                sleep(ms);

                fieldViz15.setText(roundKey2);
                fieldViz14.setText(null);
                sleep(ms);



//                fieldViz16.setFont(font);
//                fieldViz16.setForeground(Color.BLUE);
                fieldViz16.setText(roundKey2);
                fieldViz15.setText(null);
                sleep(ms);

                fieldViz17.setText(roundKey2);
                fieldViz16.setText(null);
                sleep(ms);

                fieldViz17.setText(null);
                Style styleRedAL = getStyleText(fieldRoundAfterL, Color.RED);
                insertColorText(fieldRoundAfterL, styleRedAL, "Субблок L':");
                insertColorText(fieldRoundAfterL, styleRedAL, " " + roundKey2);


                // -----------------

                fieldViz11.setText(roundKey1);
                sleep(ms);

                fieldViz22.setText(textAddRoundKey);
                fieldViz11.setText(null);
                sleep(ms);

                fieldViz23.setText(textAddRoundKey);
                fieldViz22.setText(null);
                sleep(ms);

                fieldViz24.setText(textTransform);
                fieldViz23.setText(null);
                sleep(ms);

                fieldViz25.setText(textTransform);
                fieldViz24.setText(null);
                sleep(ms);

                fieldViz26.setText(textShiftLeft);
                fieldViz25.setText(null);
                sleep(ms);

                fieldViz27.setText(textShiftLeft);
                fieldViz26.setText(null);

//                fieldXOR.setText(null);
//                fieldXOR.setText("Вычисление.");
//                sleep(ms - 700);
//                fieldXOR.setText(null);
//                fieldXOR.setText("Вычисление..");
//                sleep(ms - 700);
//                fieldXOR.setText(null);
//                fieldXOR.setText("Вычисление...");
//                sleep(ms - 700);
//                fieldXOR.setText(null);
//                fieldXOR.setText("Вычисление.");
//                sleep(ms - 700);
//                fieldXOR.setText(null);
//                fieldXOR.setText("Вычисление..");
//                sleep(ms - 700);
//                fieldXOR.setText(null);
//                fieldXOR.setText("Вычисление...");
//                sleep(ms - 700);
//                fieldXOR.setText(null);
//
//                fieldViz27.setText(null);
//                insertColorText(fieldXOR, styleBlueXOR, "Результат:");
//                insertColorText(fieldXOR, styleBlueXOR, " " + textXOR);
//                insertColorText(fieldRoundAfterR, styleRedAL, "Субблок R':");
//                insertColorText(fieldRoundAfterR, styleRedAL, " " + textXOR);
//
//                nextRound.setEnabled(true);
//                previousRound.setEnabled(true);

                nextStep.setEnabled(true);
                return null;
            }
        };
        worker.execute();
    }


    private void visualizeFeistelPartTwo(int id, String textXOR) {
        Font font = new Font("Arial", Font.BOLD, fontSize);
        fieldViz21.setFont(font);
        fieldViz21.setForeground(Color.BLUE);
        fieldViz12.setFont(font);
        fieldViz12.setForeground(Color.BLUE);
        fieldViz13.setFont(font);
        fieldViz13.setForeground(Color.BLUE);
        fieldViz14.setFont(font);
        fieldViz14.setForeground(Color.BLUE);
        fieldViz15.setFont(font);
        fieldViz15.setForeground(Color.BLUE);
        fieldViz16.setFont(font);
        fieldViz16.setForeground(Color.BLUE);
        fieldViz17.setFont(font);
        fieldViz17.setForeground(Color.BLUE);
        fieldViz11.setFont(font);
        fieldViz11.setForeground(Color.BLUE);
        fieldViz22.setFont(font);
        fieldViz22.setForeground(Color.BLUE);
        fieldViz23.setFont(font);
        fieldViz23.setForeground(Color.BLUE);
        fieldViz24.setFont(font);
        fieldViz24.setForeground(Color.BLUE);
        fieldViz25.setFont(font);
        fieldViz25.setForeground(Color.BLUE);
        fieldViz26.setFont(font);
        fieldViz26.setForeground(Color.BLUE);
        fieldViz27.setFont(font);
        fieldViz27.setForeground(Color.BLUE);
        SwingWorker<Void, String> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                int ms = 1000;
                Style styleBlueXOR = getStyleText(fieldXOR, Color.BLUE);

                Style styleRedAL = getStyleText(fieldRoundAfterL, Color.RED);


                // -----------------

                fieldXOR.setText(null);
                fieldXOR.setText("Вычисление.");
                sleep(ms - 700);
                fieldXOR.setText(null);
                fieldXOR.setText("Вычисление..");
                sleep(ms - 700);
                fieldXOR.setText(null);
                fieldXOR.setText("Вычисление...");
                sleep(ms - 700);
                fieldXOR.setText(null);
                fieldXOR.setText("Вычисление.");
                sleep(ms - 700);
                fieldXOR.setText(null);
                fieldXOR.setText("Вычисление..");
                sleep(ms - 700);
                fieldXOR.setText(null);
                fieldXOR.setText("Вычисление...");
                sleep(ms - 700);
                fieldXOR.setText(null);

                fieldViz27.setText(null);
                insertColorText(fieldXOR, styleBlueXOR, "Результат:");
                insertColorText(fieldXOR, styleBlueXOR, " " + textXOR);
                insertColorText(fieldRoundAfterR, styleRedAL, "Субблок R':");
                insertColorText(fieldRoundAfterR, styleRedAL, " " + textXOR);

                nextRound.setEnabled(true);
                previousRound.setEnabled(true);
//                nextStep.setEnabled(true);
                return null;
            }
        };
        worker.execute();
    }

//    private void setCurrentIdStepBack(int id) {
//        SwingUtilities.invokeLater(() -> {
////            System.out.println("DEBUG id: " + id);
//            State current = stateList.get(id);
//
////            for (int i = 0; i < stateList.size(); i++) {
////                for (String s : test) {
////                    if (current.getName().equals(s)) {
//////                        System.out.println(id);
////                        diagram.setDiagram(current);
////                        stage.setText(current.getName());
////                        tree.setSelectionPath(findTreePath(current));
////                        System.out.println(current);
////                    }
////
////                }
////                tree.setSelectionPath(findTreePath(current));
//            currentId = id;
////            }
//            switch (current.getName()) {
//                case "Add round key":
//                    fieldAddRoundKey.setText(null);
////                    fieldAddRoundKey.append(
//////                                    "(Шаг назад) Сложение по модулю\n\n\n"+
//////                                    "Блок данных: " + String.valueOf(Conversions.hex(current.getBefore())) +
//////                                    "\n\n---Ключ---: " + String.valueOf(Conversions.hex(current.getAdditional())) +
////                                    "(Шаг назад) Результат: " + String.valueOf(Conversions.hex(current.getAfter()))
////                    );
//                    Style styleRedARK= getStyleText(fieldAddRoundKey, Color.RED);
//                    Style styleBlackARK = getStyleText(fieldAddRoundKey, Color.BLACK);
//                    insertColorText(fieldAddRoundKey, styleBlackARK, "Результат:");
//                    insertColorText(fieldAddRoundKey, styleRedARK, " "+String.valueOf(Conversions.hex(current.getAfter())));
//                    break;
//                case "Transform":
//                    fieldTransform.setText(null);
//                    Style styleRedT = getStyleText(fieldTransform, Color.RED);
//                    Style styleBlackT = getStyleText(fieldTransform, Color.BLACK);
//                    insertColorText(fieldTransform, styleBlackT, "Результат:");
//                    insertColorText(fieldTransform, styleRedT, " "+String.valueOf(Conversions.hex(current.getAfter())));
//                    break;
//                case "Shift left":
//                    fieldShiftLeft.setText(null);
//                    Style styleRedSL = getStyleText(fieldShiftLeft, Color.RED);
//                    Style styleBlackSL = getStyleText(fieldShiftLeft, Color.BLACK);
//                    insertColorText(fieldShiftLeft, styleBlackSL, "Результат:");
//                    insertColorText(fieldShiftLeft, styleRedSL, " "+String.valueOf(Conversions.hex(current.getAfter())));
//                    break;
//                case "XOR with round key":
//                    fieldXOR.setText(null);
//                    Style styleBlackXOR = getStyleText(fieldXOR, Color.BLACK);
//                    Style styleRedXOR = getStyleText(fieldXOR, Color.RED);
//                    insertColorText(fieldXOR, styleBlackXOR, "Результат:");
//                    insertColorText(fieldXOR, styleRedXOR, " "+String.valueOf(Conversions.hex(current.getAfter())));
//                    break;
//
//            }
//
//            if (current.getName().equals("Round "+ (roundCounter-2))){
//                stage.setText("Раунд №"+ (roundCounter-2));
//                roundCounter--;
//
//                fieldRoundKey.setText(null);
//                Style styleBlackRK = getStyleText(fieldRoundKey, Color.BLACK);
//                Style styleBlueRK = getStyleText(fieldRoundKey, Color.BLUE);
//                insertColorText(fieldRoundKey, styleBlackRK, "Ключ раунда:");
//                insertColorText(fieldRoundKey, styleBlueRK, " "+String.valueOf(Conversions.hex(current.getAdditional())));
//
//                fieldRoundBeforeL.setText(null);
//                Style styleRedBL = getStyleText(fieldRoundBeforeL, Color.RED);
//                Style styleBlackBL = getStyleText(fieldRoundBeforeL, Color.BLACK);
//                insertColorText(fieldRoundBeforeL, styleBlackBL, "Субблок L:");
//                insertColorText(fieldRoundBeforeL, styleRedBL, " "+String.valueOf(Conversions.hex(current.getBefore())).substring(0,11));
//                fieldRoundBeforeR.setText(null);
//                Style styleBlackBR = getStyleText(fieldRoundBeforeR, Color.BLACK);
//                Style styleRedBR = getStyleText(fieldRoundBeforeR, Color.RED);
//
//                insertColorText(fieldRoundBeforeR, styleBlackBR, "Субблок R:");
//                insertColorText(fieldRoundBeforeR, styleRedBR, " "+String.valueOf(Conversions.hex(current.getBefore())).substring(12,23));
//
//                fieldRoundAfterL.setText(null);
//                Style styleGreenAL = getStyleText(fieldRoundAfterL, Color.GREEN);
//                Style styleBlackAL = getStyleText(fieldRoundAfterL, Color.BLACK);
//                insertColorText(fieldRoundAfterL, styleBlackAL, "Субблок L':");
//                insertColorText(fieldRoundAfterL, styleGreenAL, " "+String.valueOf(Conversions.hex(current.getBefore())).substring(12,23));
//
//                fieldRoundAfterR.setText(null);
//                Style styleGreenAR = getStyleText(fieldRoundAfterR, Color.GREEN);
//                Style styleBlackAR = getStyleText(fieldRoundAfterR, Color.BLACK);
//                insertColorText(fieldRoundAfterR, styleBlackAR, "Субблок R':");
//                insertColorText(fieldRoundAfterR, styleGreenAR, " "+String.valueOf(Conversions.hex(current.getAfter())).substring(12,23));
//            }
//
////            tree.setSelectionPath(findTreePath(current));
//            currentId = id;
//        });
//    }

    private void setCurrentIdNextStep(int id) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("(nextStep) DEBUG id: " + id);
            State current = stateList.get(id);
            System.out.println("checkDoubleClick nextStep: " + checkDoubleClick);
            System.out.println("nextStep roundCounter: " + roundCounter);
            fieldViz11.setText(null);
            fieldViz12.setText(null);
            fieldViz13.setText(null);
            fieldViz14.setText(null);
            fieldViz15.setText(null);
            fieldViz16.setText(null);
            fieldViz17.setText(null);
            fieldViz21.setText(null);
            fieldViz22.setText(null);
            fieldViz23.setText(null);
            fieldViz24.setText(null);
            fieldViz25.setText(null);
            fieldViz26.setText(null);
            fieldViz27.setText(null);
            switch (current.getName()) {
                case "Add round key":
                    fieldAddRoundKey.setText(null);
                    Style styleBlueARK = getStyleText(fieldAddRoundKey, Color.BLUE);
                    insertColorText(fieldAddRoundKey, styleBlueARK, "Результат:");
                    insertColorText(fieldAddRoundKey, styleBlueARK, " " + Conversions.hex(current.getAfter()));
                    textAddRoundKey = String.valueOf(Conversions.hex(current.getAfter()));
                    nextStep.setEnabled(true);
                    break;
                case "Transform":
                    fieldTransform.setText(null);
                    Style styleBlueT = getStyleText(fieldTransform, Color.BLUE);
                    insertColorText(fieldTransform, styleBlueT, "Результат:");
                    insertColorText(fieldTransform, styleBlueT, " " + Conversions.hex(current.getAfter()));
                    textTransform = String.valueOf(Conversions.hex(current.getAfter()));
                    break;
                case "Shift left":
                    if (checkDoubleClick && roundCounter != 1) {
                        visualizeFeistel(id, textXOR);
                        int word = Conversions.word(current.getBefore());
                        byte[] e = Conversions.word(((word << 11) & 0xFFFFF800) | ((word >> 21) & 0x7FF));
                        fieldShiftLeft.setText(null);
                        Style styleBlueSL = getStyleText(fieldShiftLeft, Color.BLUE);
                        insertColorText(fieldShiftLeft, styleBlueSL, "Результат:");
                        insertColorText(fieldShiftLeft, styleBlueSL, " " + Conversions.hex(e));
                        textShiftLeft = String.valueOf(Conversions.hex(e));
//                        checkDoubleClick = false;
//                        nextStep.setEnabled(false);
//                        firstStep = true;
                    }
                    else if (roundCounter == 1 && firstStep) {
                        visualizeFeistel(id, textXOR);
                        fieldShiftLeft.setText(null);
                        int word = Conversions.word(current.getBefore());
                        byte[] e = Conversions.word(((word << 11) & 0xFFFFF800) | ((word >> 21) & 0x7FF));
                        Style styleBlueSL = getStyleText(fieldShiftLeft, Color.BLUE);
                        insertColorText(fieldShiftLeft, styleBlueSL, "Результат:");
                        insertColorText(fieldShiftLeft, styleBlueSL, " " + Conversions.hex(e));
                        textShiftLeft = String.valueOf(Conversions.hex(current.getAfter()));
//                        checkDoubleClick = false;
//                        nextStep.setEnabled(false);
//                        firstStep = false;
                    }
                    else {
                        int word = Conversions.word(current.getBefore());
                        byte[] e = Conversions.word(((word << 11) & 0xFFFFF800) | ((word >> 21) & 0x7FF));
                        System.out.println("\n\nDEBUG <<11 (now): " + Conversions.hex(e));
                        System.out.println("DEBUG <<11 (after): " + Conversions.hex(current.getAfter()));
                        fieldShiftLeft.setText(null);
                        Style styleBlueSL = getStyleText(fieldShiftLeft, Color.BLUE);
                        insertColorText(fieldShiftLeft, styleBlueSL, "Результат:");
                        insertColorText(fieldShiftLeft, styleBlueSL, " " + Conversions.hex(e));
                        textShiftLeft = String.valueOf(Conversions.hex(current.getAfter()));
                    }
                    break;
                case "XOR with round key":
                    System.out.println("debug id subblock: " + id);
                    textXOR = String.valueOf(Conversions.hex(current.getAfter()));
                    fieldXOR.setText(null);
                    Style styleBlueXOR = getStyleText(fieldXOR, Color.BLUE);
                    if (checkDoubleClick && roundCounter != 1) {
                        visualizeFeistelPartTwo(id, textXOR);
                        checkDoubleClick = false;
                        nextStep.setEnabled(false);
                        firstStep = true;
                    }
                    else if (roundCounter == 1 && firstStep) {
                        visualizeFeistelPartTwo(id, textXOR);
                        checkDoubleClick = false;
                        nextStep.setEnabled(false);
                        firstStep = false;
                    }
                    else {
                        System.out.println("debug id subblock: " + id);
                        insertColorText(fieldXOR, styleBlueXOR, "Результат:");
                        insertColorText(fieldXOR, styleBlueXOR, " " + Conversions.hex(current.getAfter()));
                        nextStep.setEnabled(false);
                        if (id != 5) {
                            fieldRoundAfterL.setText(null);
                            fieldRoundAfterL.setText(null);

                            Style styleRedAL = getStyleText(fieldRoundAfterL, Color.RED);
                            insertColorText(fieldRoundAfterL, styleRedAL, "Субблок L':");
                            insertColorText(fieldRoundAfterL, styleRedAL, " " + valueRoundAfterL);

                            Style styleRedAR = getStyleText(fieldRoundAfterR, Color.RED);
                            insertColorText(fieldRoundAfterR, styleRedAR, "Субблок R':");
                            insertColorText(fieldRoundAfterR, styleRedAR, " " + valueRoundAfterR);
                        }

                    }
                    break;

            }
            if (current.getName().equals("Round " + roundCounter)) {
                stage.setText("Раунд №" + roundCounter);
                roundCounter++;
//                System.out.println("DEBUG id to ARR: " + id);
                fieldRoundKey.setText(null);
                Style styleRedRK = getStyleText(fieldRoundKey, Color.RED);
                insertColorText(fieldRoundKey, styleRedRK, "Ключ раунда:");
                insertColorText(fieldRoundKey, styleRedRK, " " + String.valueOf(Conversions.hex(current.getAdditional())));
                fieldRoundBeforeL.setText(null);


                valueRoundBeforeL = String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11);
                valueRoundBeforeR = String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23);
                valueRoundAfterL = String.valueOf(Conversions.hex(current.getAfter())).substring(0, 11);
                valueRoundAfterR = String.valueOf(Conversions.hex(current.getAfter())).substring(12, 23);
                Style styleRedBL = getStyleText(fieldRoundBeforeL, Color.RED);
                insertColorText(fieldRoundBeforeL, styleRedBL, "Субблок L:");
                insertColorText(fieldRoundBeforeL, styleRedBL, " " + String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11));
                fieldRoundBeforeR.setText(null);

                Style styleRedBR = getStyleText(fieldRoundBeforeR, Color.RED);
                insertColorText(fieldRoundBeforeR, styleRedBR, "Субблок R:");
                insertColorText(fieldRoundBeforeR, styleRedBR, " " + String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23));
//                fieldRoundAfterL.setText(null);
//
//                Style styleRedAL = getStyleText(fieldRoundAfterL, Color.RED);
//                insertColorText(fieldRoundAfterL, styleRedAL, "Субблок L':");
//                insertColorText(fieldRoundAfterL, styleRedAL, " " + String.valueOf(Conversions.hex(current.getAfter())).substring(0, 11));
//                fieldRoundAfterR.setText(null);
//
//                Style styleRedAR = getStyleText(fieldRoundAfterR, Color.RED);
//                insertColorText(fieldRoundAfterR, styleRedAR, "Субблок R':");
//                insertColorText(fieldRoundAfterR, styleRedAR, " " + String.valueOf(Conversions.hex(current.getAfter())).substring(12, 23));

            }
            if (current.getName().equals("XOR with round key") & id <= 5) {
                previousRound.setEnabled(false);
            }

//            tree.setSelectionPath(findTreePath(current));
            currentId = id;
        });
    }


    private void setCurrentIdNextRound(int id) {
        SwingUtilities.invokeLater(() -> {
//            System.out.println("DEBUG id: " + id);
            State current = stateList.get(id);
            System.out.println("roundCounter (nextRound): " + roundCounter);
            System.out.println("checkDoubleClick nextRound: " + checkDoubleClick);
            roundKey1 = String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11);
            roundKey2 = String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23);
            fieldRoundAfterL.setText(null);
            fieldRoundAfterR.setText(null);
            fieldTransform.setText(null);
            fieldShiftLeft.setText(null);
            fieldAddRoundKey.setText(null);
            fieldXOR.setText(null);

            fieldViz11.setText(null);
            fieldViz12.setText(null);
            fieldViz13.setText(null);
            fieldViz14.setText(null);
            fieldViz15.setText(null);
            fieldViz16.setText(null);
            fieldViz17.setText(null);
            fieldViz21.setText(null);
            fieldViz22.setText(null);
            fieldViz23.setText(null);
            fieldViz24.setText(null);
            fieldViz25.setText(null);
            fieldViz26.setText(null);
            fieldViz27.setText(null);

            nextStep.setEnabled(true);
            if (roundCounter >= 18){
                checkDoubleClick = true;
            }
            if (current.getName().equals("Round " + roundCounter)) {
                fieldViz11.setText(null);
                fieldViz12.setText(null);
                fieldViz13.setText(null);
                fieldViz14.setText(null);
                fieldViz15.setText(null);
                fieldViz16.setText(null);
                fieldViz17.setText(null);
                fieldViz21.setText(null);
                fieldViz22.setText(null);
                fieldViz23.setText(null);
                fieldViz24.setText(null);
                fieldViz25.setText(null);
                fieldViz26.setText(null);
                fieldViz27.setText(null);

                fieldRoundAfterL.setText(null);
                fieldRoundAfterR.setText(null);
                stage.setText("Раунд №" + (roundCounter + 1));
                roundCounter++;
                roundArrIdx++;
                nextRound.setEnabled(true);
                previousRound.setEnabled(true);

                roundKey1 = String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11);
                roundKey2 = String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23);

                fieldRoundKey.setText(null);
                Style styleRedRK = getStyleText(fieldRoundKey, Color.RED);
                insertColorText(fieldRoundKey, styleRedRK, "Ключ раунда:");
                insertColorText(fieldRoundKey, styleRedRK, " " + String.valueOf(Conversions.hex(current.getAdditional())));
                fieldRoundBeforeL.setText(null);
                Style styleRedBL = getStyleText(fieldRoundBeforeL, Color.RED);
                insertColorText(fieldRoundBeforeL, styleRedBL, "Субблок L:");
                insertColorText(fieldRoundBeforeL, styleRedBL, " " + String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11));
                fieldRoundBeforeR.setText(null);
                Style styleRedBR = getStyleText(fieldRoundBeforeR, Color.RED);


                valueRoundBeforeL = String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11);
                valueRoundBeforeR = String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23);
                valueRoundAfterL = String.valueOf(Conversions.hex(current.getAfter())).substring(0, 11);
                valueRoundAfterR = String.valueOf(Conversions.hex(current.getAfter())).substring(12, 23);

                insertColorText(fieldRoundBeforeR, styleRedBR, "Субблок R:");
                insertColorText(fieldRoundBeforeR, styleRedBR, " " + String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23));
//                fieldRoundAfterL.setText(null);
//
//
//                Style styleRedAL = getStyleText(fieldRoundAfterL, Color.RED);
//                insertColorText(fieldRoundAfterL, styleRedAL, "Субблок L':");
//                insertColorText(fieldRoundAfterL, styleRedAL, " " + String.valueOf(Conversions.hex(current.getAfter())).substring(0, 11));
//                fieldRoundAfterR.setText(null);
//                Style styleRedAR = getStyleText(fieldRoundAfterR, Color.RED);
//                insertColorText(fieldRoundAfterR, styleRedAR, "Субблок R':");
//                insertColorText(fieldRoundAfterR, styleRedAR, " " + String.valueOf(Conversions.hex(current.getAfter())).substring(12, 23));


            }
            nextRound.setEnabled(!current.getName()
                    .equals("Round 31"));
            if (current.getName().equals("Round 0")) {
                previousRound.setEnabled(false);
                nextStep.setEnabled(true);
            }
//            tree.setSelectionPath(findTreePath(current));
            currentId = id;
        });
    }

    private void setCurrentIdPrevRound(int id) {
        SwingUtilities.invokeLater(() -> {
//            System.out.println("(prevRound) DEBUG id: " + id);
            State current = stateList.get(id);
//            checkDoubleClick = true;
            System.out.println("checkDoubleClick prevRound: " + checkDoubleClick);
            System.out.println("roundCounter (prev round): " + roundCounter);
            fieldRoundAfterL.setText(null);
            fieldRoundAfterR.setText(null);
            fieldTransform.setText(null);
            fieldShiftLeft.setText(null);
            fieldAddRoundKey.setText(null);
            fieldViz11.setText(null);
            fieldViz12.setText(null);
            fieldViz13.setText(null);
            fieldViz14.setText(null);
            fieldViz15.setText(null);
            fieldViz16.setText(null);
            fieldViz17.setText(null);
            fieldViz21.setText(null);
            fieldViz22.setText(null);
            fieldViz23.setText(null);
            fieldViz24.setText(null);
            fieldViz25.setText(null);
            fieldViz26.setText(null);
            fieldViz27.setText(null);
            fieldXOR.setText(null);
            if (roundCounter <= 3) {
                checkDoubleClick = true;
            }
            if (current.getName().equals("Round " + (roundCounter - 2))) {
                fieldViz11.setText(null);
                fieldViz12.setText(null);
                fieldViz13.setText(null);
                fieldViz14.setText(null);
                fieldViz15.setText(null);
                fieldViz16.setText(null);
                fieldViz17.setText(null);
                fieldViz21.setText(null);
                fieldViz22.setText(null);
                fieldViz23.setText(null);
                fieldViz24.setText(null);
                fieldViz25.setText(null);
                fieldViz26.setText(null);
                fieldViz27.setText(null);

                fieldRoundAfterL.setText(null);
                fieldRoundAfterR.setText(null);
                stage.setText("Раунд №" + (roundCounter - 1));
                roundCounter--;
                roundArrIdx--;
                nextRound.setEnabled(true);
                previousRound.setEnabled(true);
                nextStep.setEnabled(true);

                roundKey1 = String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11);
                roundKey2 = String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23);

                valueRoundBeforeL = String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11);
                valueRoundBeforeR = String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23);
                valueRoundAfterL = String.valueOf(Conversions.hex(current.getAfter())).substring(0, 11);
                valueRoundAfterR = String.valueOf(Conversions.hex(current.getAfter())).substring(12, 23);

                fieldRoundKey.setText(null);
                Style styleRedRK = getStyleText(fieldRoundKey, Color.RED);
                insertColorText(fieldRoundKey, styleRedRK, "Ключ раунда:");
                insertColorText(fieldRoundKey, styleRedRK, " " + String.valueOf(Conversions.hex(current.getAdditional())));
                fieldRoundBeforeL.setText(null);

                Style styleRedBL = getStyleText(fieldRoundBeforeL, Color.RED);
                insertColorText(fieldRoundBeforeL, styleRedBL, "Субблок L:");
                insertColorText(fieldRoundBeforeL, styleRedBL, " " + String.valueOf(Conversions.hex(current.getBefore())).substring(0, 11));
                fieldRoundBeforeR.setText(null);

                Style styleRedBR = getStyleText(fieldRoundBeforeR, Color.RED);
                insertColorText(fieldRoundBeforeR, styleRedBR, "Субблок R:");
                insertColorText(fieldRoundBeforeR, styleRedBR, " " + String.valueOf(Conversions.hex(current.getBefore())).substring(12, 23));
//                fieldRoundAfterL.setText(null);
//
//                Style styleRedAL = getStyleText(fieldRoundAfterL, Color.RED);
//                insertColorText(fieldRoundAfterL, styleRedAL, "Субблок L':");
//                insertColorText(fieldRoundAfterL, styleRedAL, " " + String.valueOf(Conversions.hex(current.getAfter())).substring(0, 11));
//                fieldRoundAfterR.setText(null);
//                Style styleRedAR = getStyleText(fieldRoundAfterR, Color.RED);
//                insertColorText(fieldRoundAfterR, styleRedAR, "Субблок R':");
//                insertColorText(fieldRoundAfterR, styleRedAR, " " + String.valueOf(Conversions.hex(current.getAfter())).substring(12, 23));


            }
            if (current.getName().equals("Round 0") & id == 1) {
                previousRound.setEnabled(false);
                nextStep.setEnabled(true);
            }

//            tree.setSelectionPath(findTreePath(current));
            currentId = id;
        });
    }

    private List<State> createStateList(State state) {
        ArrayList<String> test = new ArrayList<String>(Arrays.asList(
                "Add round key",
                "Transform",
                "Shift left",
                "XOR with round key",
                "Round 0", "Round 1", "Round 2", "Round 3", "Round 4", "Round 5", "Round 6", "Round 7", "Round 8", "Round 9",
                "Round 10", "Round 11", "Round 12", "Round 13", "Round 14", "Round 15", "Round 16", "Round 17", "Round 18", "Round 19",
                "Round 20", "Round 21", "Round 22", "Round 23", "Round 24", "Round 25", "Round 26", "Round 27", "Round 28", "Round 29",
                "Round 30", "Round 31"
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
            setCurrentIdNextStep(stateList.indexOf((State) node.getUserObject()));
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
//    private TreePath findTreePath(State state) {
//        Enumeration<TreeNode> e = ((DefaultMutableTreeNode) tree.getModel().getRoot()).depthFirstEnumeration();
//        while (e.hasMoreElements()) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
//            if (node.getUserObject() == state) return new TreePath(node.getPath());
//        }
//        return null;
//    }

}
