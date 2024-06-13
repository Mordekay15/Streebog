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
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import static utils.OSInfo.*;

public class VisualizationKuznyechikRound extends JDialog {
    private int currentId = 4;
    private int xorIdx = 1;
    private int sIdx = 1;
    private int lIdx = 1;
    private int roundIdx = 1;
    private int roundArrIdx = 1;
    private int idxBlockL = 1;
    private int fontSize = 14;

    private String roundKeyForHint = "";
    private String roundBlockData = "";
    private final List<State> stateList;
    private final JTree tree;

    private JTextPane textPaneXOR = new JTextPane();
    private JTextPane textPaneS = new JTextPane();
    private JTextPane textPaneL = new JTextPane();


    private JTextPane textPaneKeyBlock = new JTextPane();

    private final JButton nextStep = new JButton(">");
    private final JButton previousStep = new JButton("<");
    private final JButton nextRound = new JButton(">>");
    private final JButton previousRound = new JButton("<<");
    private final JButton xHint = new JButton("Преобразование: 'сложение XOR'");
    private final JButton sHint = new JButton("Преобразование: 'подстановка S'");
    private final JButton lHint = new JButton("Преобразование: 'регистр сдвига L'");

    private final int[] indexRounds = new int[]{1, 5, 9, 13, 17, 21, 25, 29, 33, 37};

    private final int[] indexResultL = new int[]{4, 8, 12, 16, 20, 24, 28, 32, 36};
    private final JLabel stage = new JLabel("Раунд №_", SwingConstants.CENTER);

    public VisualizationKuznyechikRound(Component owner, State state, byte[] result, String key, String block) {
        super((Frame) SwingUtilities.getWindowAncestor(owner), "Визуализация раундовых преобразований шифра 'Кузнечик'", false);
        Font font = new Font("Arial", Font.BOLD, fontSize);

        stateList = createStateList(state);

        nextRound.setForeground(Color.RED);
        previousRound.setForeground(Color.RED);
        stage.setForeground(Color.RED);

        nextStep.setForeground(Color.BLUE);
        xHint.setForeground(Color.BLUE);
        sHint.setForeground(Color.BLUE);
        lHint.setForeground(Color.BLUE);
        xHint.setFont(font);
        sHint.setFont(font);
        lHint.setFont(font);

        textPaneXOR.setEditable(false);
        textPaneS.setEditable(false);
        textPaneL.setEditable(false);
        textPaneKeyBlock.setEditable(false);

        nextRound.setFont(font);
        nextStep.setFont(font);
        previousRound.setFont(font);

        textPaneXOR.setBackground(Color.getHSBColor(0.5f, 0.12f, 1.f));
        textPaneS.setBackground(Color.getHSBColor(0.5f, 0.12f, 1.f));
        textPaneL.setBackground(Color.getHSBColor(0.5f, 0.12f, 1.f));
        textPaneKeyBlock.setBackground(Color.getHSBColor(0.016f, 0.11f, 1.f));

        tree = createStateTree(state, key, block);
        tree.setScrollsOnExpand(true);
        tree.setExpandsSelectedPaths(true);

        stage.setFont(font);

        textPaneKeyBlock.setText(null);
        Style styleRed = getStyleText(textPaneKeyBlock, Color.RED);

        insertColorText(textPaneKeyBlock, styleRed, "Блок данных:");
        insertColorText(textPaneKeyBlock, styleRed, " " + block);

        insertColorText(textPaneKeyBlock, styleRed, "\n\nРаундовый ключ:");
        insertColorText(textPaneKeyBlock, styleRed, " " + roundKeyForHint);

        System.out.println(stateList);

        if (currentId == 4) {
            scipStartSteps(key, block);
        }


        nextStep.addActionListener(e -> setCurrIdNextStep(currentId + 1, key, block));
//        previousStep.addActionListener(e -> setCurrIdPrevStep(currentId - 1,key,block));
        nextRound.addActionListener(e -> setCurrIdNextRound(indexRounds[roundArrIdx + 1], key, block));
        previousRound.addActionListener(e -> setCurrIdPrevRound(indexRounds[roundArrIdx - 1], key, block));
        xHint.addActionListener(e -> xHintButton());
        sHint.addActionListener(e -> {
            try {
                sHintButton();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        lHint.addActionListener(e -> {
            try {
                lHintButton();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        setCurrIdNextStep(4, key, block);
        getContentPane().setLayout(new GridBagLayout());

        GridBagConstraints textPaneKeyBlockConstraints = createConstants(1, 1, 1, 1, 0, 0, new Insets(5, 5, 5, 0));

        GridBagConstraints textPaneXORConstraints = createConstants(1, 1, 1, 1, 0, 2, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneSConstraints = createConstants(1, 1, 1, 1, 0, 4, new Insets(5, 5, 5, 0));
        GridBagConstraints textPaneLConstraints = createConstants(1, 1, 1, 1, 0, 6, new Insets(5, 5, 5, 0));


        getContentPane().add(textPaneKeyBlock, textPaneKeyBlockConstraints);
        getContentPane().add(textPaneXOR, textPaneXORConstraints);
        getContentPane().add(textPaneS, textPaneSConstraints);
        getContentPane().add(textPaneL, textPaneLConstraints);


        GridBagConstraints xHintConstraints = createConstants(1, 1, 1, 1, 0, 1, new Insets(0, 5, 5, 0));
        GridBagConstraints sHintConstraints = createConstants(1, 1, 1, 1, 0, 3, new Insets(0, 5, 5, 0));
        GridBagConstraints lHintConstraints = createConstants(1, 1, 1, 1, 0, 5, new Insets(0, 5, 5, 0));

        GridBagConstraints stageConstraints = createConstants(1, 1, 1, 1, 2, 5, new Insets(0, 5, 5, 0));

        GridBagConstraints nextStepConstraints = createConstants(1, 1, 1, 1, 2, 6, new Insets(0, 5, 5, 0));
        GridBagConstraints previousRoundConstraints = createConstants(1, 1, 1, 1, 3, 5, new Insets(0, 5, 5, 0));
        GridBagConstraints nextRoundConstraints = createConstants(1, 1, 1, 1, 3, 6, new Insets(0, 5, 5, 0));

        getContentPane().add(stage, stageConstraints);
        getContentPane().add(xHint, xHintConstraints);
        getContentPane().add(sHint, sHintConstraints);
        getContentPane().add(lHint, lHintConstraints);

        getContentPane().add(nextStep, nextStepConstraints);
        getContentPane().add(previousRound, previousRoundConstraints);
        getContentPane().add(nextRound, nextRoundConstraints);


//        JScrollPane treePane = new JScrollPane(tree);
//        treePane.setPreferredSize(new Dimension(200, treePane.getPreferredSize().height));
//        GridBagConstraints treeConstraints = createConstants(1, 2, 1, 8, 7, 0, new Insets(5, 5, 5, 5));
//        getContentPane().add(treePane, treeConstraints);

        setResizable(true);
        setPreferredSize(new Dimension(800, 600));
        pack();
    }


    private void xHintButton() {
        String textFileName = "X_hint.txt";
        String user = getUser();
        String title = "Принцип работы X-преобразования";

        if (isWindows()) {
            String path = "C:\\Users\\" + user + "\\Documents\\CryptoETU\\"+ textFileName;
            try {
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isMac()) {
            String path = "/Users/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isUnix()) {
            String path = "/home/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void sHintButton() throws Exception {
        String pngFileName = "sbox.png";
        String textFileName = "S_hint.txt";
        Desktop dt = Desktop.getDesktop();
        String user = getUser();
        String title = "Принцип работы S-преобразования";

        if (isWindows()) {
            String path = "C:\\Users\\" + user + "\\Documents\\CryptoETU\\"+ textFileName;
            File fWin = new File("C:\\Users\\" + user + "\\Documents\\CryptoETU\\" + pngFileName);
            dt.open(fWin);
            try {
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isMac()) {
            String path = "/Users/" + user + "/Documents/CryptoETU/" + textFileName;
            File fMac = new File("/Users/" + user + "/Documents/CryptoETU/" + pngFileName);
            dt.open(fMac);
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isUnix()) {
            String path = "/home/" + user + "/Documents/CryptoETU/" + textFileName;
            File fUnix = new File("/home/" + user + "/Documents/CryptoETU/" + pngFileName);
            dt.open(fUnix);
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void lHintButton() throws Exception {
        String textFileName = "L_hint.txt";
        String user = getUser();
        String title = "Принцип работы L-преобразования";

        if (isWindows()) {
            String path = "C:\\Users\\" + user + "\\Documents\\CryptoETU\\"+ textFileName;
            try {
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isMac()) {
            String path = "/Users/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isUnix()) {
            String path = "/home/" + user + "/Documents/CryptoETU/" + textFileName;
            try {
                String content = new String(Files.readAllBytes(Paths.get(path)));
                JOptionPane.showMessageDialog(VisualizationKuznyechikRound.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


    private void scipStartSteps(String key, String block) {
        State curTemp = stateList.get(2);

        roundKeyForHint = String.valueOf(Conversions.hex(curTemp.getAdditional()));

        textPaneKeyBlock.setText(null);
        Style styleRed = getStyleText(textPaneKeyBlock, Color.RED);

        insertColorText(textPaneKeyBlock, styleRed, "Блок данных:");
        insertColorText(textPaneKeyBlock, styleRed, " " + block);


        insertColorText(textPaneKeyBlock, styleRed, "\n\nРаундовый ключ:");
        insertColorText(textPaneKeyBlock, styleRed, " " + roundKeyForHint);

        textPaneXOR.setText(null);
        Style styleXOR = getStyleText(textPaneXOR, Color.BLUE);
        Style styleXorResult = getStyleText(textPaneXOR, Color.BLUE);
        insertColorText(textPaneXOR, styleXOR, "Результат X:");
        insertColorText(textPaneXOR, styleXorResult, " " + String.valueOf(Conversions.hex(curTemp.getAfter())));

        curTemp = stateList.get(3);
//        textAreaS.setText(null);

//        textAreaS.append(String.valueOf(
////                "Блок данных: " + Conversions.hex(curTemp.getBefore())) +
////                "\n\nприменение S-преобразования\n\n" +
//                "Результат S: " + String.valueOf(Conversions.hex(curTemp.getAfter()))));
        textPaneS.setText(null);
        Style styleS = getStyleText(textPaneS, Color.BLUE);
        Style styleSResult = getStyleText(textPaneS, Color.BLUE);
        insertColorText(textPaneS, styleS, "Результат S:");
        insertColorText(textPaneS, styleSResult, " " + String.valueOf(Conversions.hex(curTemp.getAfter())));

        curTemp = stateList.get(4);
//        textAreaL.setText(null);
//        textAreaL.append(String.valueOf(
////                "Блок данных: " + Conversions.hex(curTemp.getBefore())) +
////                "\n\nприменение L-преобразования\n\n" +
//                "Результат L: " + String.valueOf(Conversions.hex(curTemp.getAfter()))));
        textPaneL.setText(null);
        Style styleL = getStyleText(textPaneL, Color.RED);
        Style styleLResult = getStyleText(textPaneL, Color.RED);
        insertColorText(textPaneL, styleL, "Результат L:");
        insertColorText(textPaneL, styleLResult, " " + String.valueOf(Conversions.hex(curTemp.getAfter())));

        roundBlockData = String.valueOf(Conversions.hex(curTemp.getAfter()));
        tree.setSelectionPath(findTreePath(curTemp));
        stage.setText("Раунд №1");
        nextStep.setEnabled(false);
        previousRound.setEnabled(false);
        roundIdx = 2;
    }

    private void setCurrIdNextRound(int id, String key, String block) {
        SwingUtilities.invokeLater(() -> {
            State currentNextRound = stateList.get(id);
//            textAreaXOR.setText(null);
//            textAreaS.setText(null);
//            textAreaL.setText(null);
            textPaneXOR.setText(null);
            textPaneS.setText(null);
            textPaneL.setText(null);
            nextStep.setEnabled(true);
            System.out.println("debug nextRound id: " + id);
//            System.out.println("DEBUG roundArrIdx = "+ roundArrIdx);
//            System.out.println("DEBUG indexRounds[roundArrIdx] = "+ indexRounds[roundArrIdx]);
//            System.out.println(roundIdx + " " + xorIdx + " " + sIdx + " " + lIdx);

            if (currentNextRound.getName().equals("Round " + roundIdx)) {
                stage.setText("Раунд №" + (roundIdx + 1));
                nextRound.setEnabled(true);
                previousRound.setEnabled(true);
//                nextStep.setEnabled(true);
//                nextRound.setEnabled(false);

//                textAreaKeyBlock.setText(null);
//                textAreaKeyBlock.append(
//                        "БЛОК ДАННЫХ: " + block +
//                                "\n\nСЕКРЕТНЫЙ КЛЮЧ: " + key +
//                                "\n\nРАУНДОВЫЙ КЛЮЧ: " + roundKeyForHint
//                );

                textPaneKeyBlock.setText(null);
                Style styleRed = getStyleText(textPaneKeyBlock, Color.RED);

                insertColorText(textPaneKeyBlock, styleRed, "Блок данных:");
                insertColorText(textPaneKeyBlock, styleRed, " " + String.valueOf(Conversions.hex(currentNextRound.getBefore())));

                roundIdx++;
                roundArrIdx++;
                idxBlockL++;
                xorIdx = roundArrIdx;
                sIdx = roundArrIdx;
                lIdx = roundArrIdx;
            }


            if (currentNextRound.getName().equals("Round 9")) {
                roundKeyForHint = String.valueOf(Conversions.hex(currentNextRound.getAdditional()));
//                textAreaKeyBlock.setText(null);
//                textAreaKeyBlock.append(
//                        "БЛОК ДАННЫХ: " + block +
//                                "\n\nСЕКРЕТНЫЙ КЛЮЧ: " + key +
//                                "\n\nРАУНДОВЫЙ КЛЮЧ: " + roundKeyForHint
//                );
                textPaneKeyBlock.setText(null);
                Style styleRed = getStyleText(textPaneKeyBlock, Color.RED);

                insertColorText(textPaneKeyBlock, styleRed, "Блок данных:");
                insertColorText(textPaneKeyBlock, styleRed, " " + String.valueOf(Conversions.hex(currentNextRound.getBefore())));

                insertColorText(textPaneKeyBlock, styleRed, "\n\nРаундовый ключ:");
                insertColorText(textPaneKeyBlock, styleRed, " " + roundKeyForHint);

                textPaneXOR.setText(null);
                Style styleXOR = getStyleText(textPaneXOR, Color.BLUE);
                Style styleXORResult = getStyleText(textPaneXOR, Color.BLUE);
                insertColorText(textPaneXOR, styleXOR, "Результат:");
                insertColorText(textPaneXOR, styleXORResult, " " + String.valueOf(Conversions.hex(currentNextRound.getAfter())));
                nextStep.setEnabled(false);
                nextRound.setEnabled(false);
            }
            tree.setSelectionPath(findTreePath(currentNextRound));
            currentId = id;
        });
    } // nextRound

    private void setCurrIdPrevRound(int id, String key, String block) {
        SwingUtilities.invokeLater(() -> {
            State currentPrevRound = stateList.get(id);
            previousRound.setEnabled(roundArrIdx != 0);
            previousRound.setEnabled(id != 1);
            nextStep.setEnabled(true);
            System.out.println("debug prevRound id: " + id);
//            textAreaXOR.setText(null);
//            textAreaS.setText(null);
//            textAreaL.setText(null);
            textPaneXOR.setText(null);
            textPaneS.setText(null);
            textPaneL.setText(null);
//            System.out.println("DEBUG roundArrIdx = "+ roundArrIdx);
//            System.out.println("DEBUG indexRounds[roundArrIdx] = "+ indexRounds[roundArrIdx]);
            if (currentPrevRound.getName()
                    .equals("Round " + (roundIdx - 2))) {
                stage.setText("Раунд №" + (roundIdx - 1));
                nextRound.setEnabled(true);
//                previousRound.setEnabled(false);
//                nextStep.setEnabled(true);
//                nextRound.setEnabled(false);
//                textAreaKeyBlock.setText(null);
//                textAreaKeyBlock.append(
//                        "БЛОК ДАННЫХ: " + block +
//                                "\n\nСЕКРЕТНЫЙ КЛЮЧ: " + key
//                );
                textPaneKeyBlock.setText(null);
                Style styleRed = getStyleText(textPaneKeyBlock, Color.RED);

                insertColorText(textPaneKeyBlock, styleRed, "Блок данных:");
                insertColorText(textPaneKeyBlock, styleRed, " " + String.valueOf(Conversions.hex(currentPrevRound.getBefore())));

                roundIdx--;
                roundArrIdx--;
                idxBlockL--;
                xorIdx = roundArrIdx;
                sIdx = roundArrIdx;
                lIdx = roundArrIdx;
            }
            if (currentPrevRound.getName().equals("Round 1")) {
                roundBlockData = block;
            }
            tree.setSelectionPath(findTreePath(currentPrevRound));
            currentId = id;
        });
    } // prevRound

    private void setCurrIdNextStep(int id, String key, String block) {
        SwingUtilities.invokeLater(() -> {

//            previousStep.setEnabled(id > 0);
//            nextStep.setEnabled(id < 37);
            if (id == 6) {
                stage.setText("Раунд №2");
            }
//            System.out.println("DEBUG nex id: " + id);
            State current = stateList.get(id);
//            System.out.println(stateList);
//            System.out.println("\n");
//            System.out.println(current.getName());
//            System.out.println(current.getID());
//            previous.setEnabled(currentId != 0);
//            next.setEnabled(currentId != stateList.size() - 1);
//            for (int i = 0; i < stateList.size(); i++) {
//                for (String s : test) {
//                    if (current.getName().equals(s)) {
////                        System.out.println(id);
//                        diagram.setDiagram(current);
//                        stage.setText(current.getName());
//                        tree.setSelectionPath(findTreePath(current));
//                        System.out.println(current);
//                    }
//
//                }
//                tree.setSelectionPath(findTreePath(current));
//                currentId = id;
//            }

//            diagram.setDiagram(current);
//            nextStep.setEnabled(id < 37);
            if (current.getName()
                    .equals("XOR with round key " + xorIdx)) {
//                textAreaXOR.setText(null);
//                textAreaXOR.append(
////                                "Блок данных: " + String.valueOf(Conversions.hex(current.getBefore()) +
////                                "\n\nприменение операции XOR поэлементно между блоком данных и ключом раунда\n\n" +
//                                "Результат X: " + String.valueOf(Conversions.hex(current.getAfter())));
                textPaneXOR.setText(null);
                Style styleXOR = getStyleText(textPaneXOR, Color.BLUE);
                Style styleXorResult = getStyleText(textPaneXOR, Color.BLUE);
                insertColorText(textPaneXOR, styleXOR, "Результат X:");
                insertColorText(textPaneXOR, styleXorResult, " " + String.valueOf(Conversions.hex(current.getAfter())));
                roundKeyForHint = String.valueOf(Conversions.hex(current.getAdditional()));
//                textAreaKeyBlock.setText(null);
//                textAreaKeyBlock.append(
//                        "БЛОК ДАННЫХ: " + block +
//                                "\n\nСЕКРЕТНЫЙ КЛЮЧ: " + key +
//                                "\n\nРАУНДОВЫЙ КЛЮЧ: " + roundKeyForHint
//                );
                textPaneKeyBlock.setText(null);
                Style styleRed = getStyleText(textPaneKeyBlock, Color.RED);

                insertColorText(textPaneKeyBlock, styleRed, "Блок данных:");
                insertColorText(textPaneKeyBlock, styleRed, " " + String.valueOf(Conversions.hex(current.getBefore())));

                insertColorText(textPaneKeyBlock, styleRed, "\n\nРаундовый ключ:");
                insertColorText(textPaneKeyBlock, styleRed, " " + roundKeyForHint);

                xorIdx++;
                nextStep.setEnabled(true);
            } else if (current.getName()
                    .equals("Substitute bytes " + sIdx)) {
                textPaneS.setText(null);
                Style styleS = getStyleText(textPaneS, Color.BLUE);
                Style styleSResult = getStyleText(textPaneS, Color.BLUE);
                insertColorText(textPaneS, styleS, "Результат S:");
                insertColorText(textPaneS, styleSResult, " " + String.valueOf(Conversions.hex(current.getAfter())));
                sIdx++;

            } else if (current.getName()
                    .equals("Rotate bytes " + lIdx)) {
                textPaneL.setText(null);
                System.out.println("DEBUG L: " + id);
                Style styleL = getStyleText(textPaneS, Color.RED);
                Style styleLResult = getStyleText(textPaneS, Color.RED);
                insertColorText(textPaneL, styleL, "Результат L:");
                insertColorText(textPaneL, styleLResult, " " + String.valueOf(Conversions.hex(current.getAfter())));

                roundBlockData = String.valueOf(Conversions.hex(current.getAfter()));

                lIdx++;
                nextStep.setEnabled(false);
                nextRound.setEnabled(true);
            } else if (current.getName()
                    .equals("Round 9")) {
                textPaneXOR.setText(null);
                Style styleXOR = getStyleText(textPaneS, Color.BLUE);
                Style styleXORResult = getStyleText(textPaneS, Color.BLUE);
                insertColorText(textPaneL, styleXOR, "Результат X:");
                insertColorText(textPaneL, styleXORResult, " " + String.valueOf(Conversions.hex(current.getAfter())));

                nextRound.setEnabled(false);
                nextStep.setEnabled(false);
            }

            tree.setSelectionPath(findTreePath(current));
            currentId = id;
        });

    }

    private List<State> createStateList(State state) {
        ArrayList<String> test = new ArrayList<String>(Arrays.asList(
                "XOR with round key 0",
                "XOR with round key 1",
                "XOR with round key 2",
                "XOR with round key 3",
                "XOR with round key 4",
                "XOR with round key 5",
                "XOR with round key 6",
                "XOR with round key 7",
                "XOR with round key 8",
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
                "Swap keys",
                "Rotate bytes 0",
                "Rotate bytes 1",
                "Rotate bytes 2",
                "Rotate bytes 3",
                "Rotate bytes 4",
                "Rotate bytes 5",
                "Rotate bytes 6",
                "Rotate bytes 7",
                "Rotate bytes 8",
                "Round 0", "Round 1", "Round 2", "Round 3", "Round 4", "Round 5", "Round 6", "Round 7", "Round 8", "Round 9"
        ));
        List<State> list = new ArrayList<>();
        list.add(state);
        for (State sub : state.getInner()) {
            for (String s : test) {
                if (sub.getName()
                        .equals(s)) {
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

    private JTree createStateTree(State state, String key, String block) {
        JTree tree = new JTree(createStateTreeNodes(state));
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath()
                    .getLastPathComponent();
            setCurrIdNextStep(stateList.indexOf((State) node.getUserObject()), key, block);
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

    private TreePath findTreePath(State state) {
        Enumeration<TreeNode> e = ((DefaultMutableTreeNode) tree.getModel()
                .getRoot()).depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.getUserObject() == state) return new TreePath(node.getPath());
        }
        return null;
    }
}
