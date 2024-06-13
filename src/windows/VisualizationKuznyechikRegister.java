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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class VisualizationKuznyechikRegister extends JDialog {
    private int currentId = 3;
    private int lcIdx = 0;
    private int roundIdx = 1;

    private final List<State> stateList;
    private final JTree tree;

    private JTextPane textPaneLinearConvert = new JTextPane();
    private final JButton nextStep = new JButton(">");
    private final JButton nextRound = new JButton(">>");
    private final JButton prevRound = new JButton("<<");
    private final JButton prevStep = new JButton("<");

    private int roundArrIdx = 0;
    private int fontSize = 14;

    private final int[] indexRounds = new int[]{3, 21, 39, 57, 75, 93, 111, 129, 147, 162};

    private final JLabel step = new JLabel("Шаг №_", SwingConstants.CENTER);


    public VisualizationKuznyechikRegister(Component owner, State state, byte[] result) {
        super((Frame) SwingUtilities.getWindowAncestor(owner), "Визуализация работы регистра сдвига шифра 'Кузнечик'", false);
        Font font = new Font("Arial", Font.BOLD, fontSize);
        stateList = createStateList(state);

        nextRound.setForeground(Color.BLUE);
        prevRound.setForeground(Color.BLUE);

        nextStep.setForeground(Color.RED);
        prevStep.setForeground(Color.RED);

        step.setForeground(Color.RED);
//        textPaneLinearConvert


//        float[] hsbValues = Color.RGBtoHSB(255, 245, 238, null);
//        float hue = hsbValues[0]; // оттенок
//        float saturation = hsbValues[1]; // насыщенность
//        float brightness = hsbValues[2]; // яркость
//        System.out.println("hue: "+hue);
//        System.out.println("saturation: "+saturation);
//        System.out.println("brightness: "+brightness);

        textPaneLinearConvert.setBackground(Color.getHSBColor(0.33f, 0.058f, 1.f));


        nextStep.setFont(font);
        prevStep.setFont(font);
        nextRound.setFont(font);
        prevRound.setFont(font);

        step.setFont(font);

        if (currentId == 3) {
            scipStartSteps();
        }

        tree = createStateTree(state);
        tree.setScrollsOnExpand(true);
        tree.setExpandsSelectedPaths(true);


        nextStep.addActionListener(e -> setCurrentNextStep(currentId + 1));
        prevStep.addActionListener(e -> prevStep(currentId - 1));
        nextRound.addActionListener(e -> setCurrentNextRound(indexRounds[roundArrIdx + 1]));
        prevRound.addActionListener(e -> setCurrentPrevRound(indexRounds[roundArrIdx - 1]));

        getContentPane().setLayout(new GridBagLayout());


        GridBagConstraints textPaneLinearConvertConstraints = createConstants(1, 1, 1, 1, 0, 0, new Insets(5, 5, 5, 0));
        getContentPane().add(textPaneLinearConvert, textPaneLinearConvertConstraints);


        GridBagConstraints stepConstraints = createConstants(1, 1, 1, 1, 2, 0, new Insets(0, 5, 5, 0));
        GridBagConstraints nextStepConstraints = createConstants(1, 1, 1, 1, 4, 1, new Insets(0, 5, 5, 0));
        GridBagConstraints nextRoundConstraints = createConstants(1, 1, 1, 1, 4, 2, new Insets(0, 5, 5, 0));
        GridBagConstraints prevRoundConstraints = createConstants(1, 1, 1, 1, 3, 2, new Insets(0, 5, 5, 0));
        GridBagConstraints prevStepConstraints = createConstants(1, 1, 1, 1, 3, 1, new Insets(0, 5, 5, 0));
        GridBagConstraints hintConstraints = createConstants(1, 1, 1, 1, 2, 1, new Insets(0, 5, 5, 0));


        getContentPane().add(step, stepConstraints);
        getContentPane().add(prevStep, prevStepConstraints);
        getContentPane().add(nextStep, nextStepConstraints);
        getContentPane().add(nextRound, nextRoundConstraints);
        getContentPane().add(prevRound, prevRoundConstraints);

        JScrollPane treePane = new JScrollPane(tree);
        treePane.setPreferredSize(new Dimension(200, treePane.getPreferredSize().height));
        GridBagConstraints treeConstraints = createConstants(1, 2, 1, 8, 7, 0, new Insets(5, 5, 5, 5));
        getContentPane().add(treePane, treeConstraints);


        setResizable(true);
        setPreferredSize(new Dimension(800, 600));

        pack();
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

    private void scipStartSteps() {
        State curTemp = stateList.get(3);

        textPaneLinearConvert.setText(null);
        Style styleRed = getStyleText(textPaneLinearConvert, Color.RED);
        Style styleBlue = getStyleText(textPaneLinearConvert, Color.BLUE);

        insertColorText(textPaneLinearConvert, styleRed, "Регистр:");
        insertColorText(textPaneLinearConvert, styleRed, " " + Conversions.hex(curTemp.getBefore()));
        insertColorText(textPaneLinearConvert, styleBlue, "\n\nСдвиг №");
        insertColorText(textPaneLinearConvert, styleBlue, "1");
        insertColorText(textPaneLinearConvert, styleRed, "\n\nРегистр:");
        insertColorText(textPaneLinearConvert, styleRed, " " + String.valueOf(Conversions.hex(curTemp.getAfter())));
        lcIdx = 0;
        step.setText("Шаг №" + Integer.toString(lcIdx));
        prevRound.setEnabled(false);
        prevStep.setEnabled(false);


    }


    private void prevStep(int id) {
        SwingUtilities.invokeLater(() -> {
//            System.out.println("DEBUG prev id: " + id);
            State current_prev = stateList.get(id);
//            System.out.println("DEBUG prev id--: " + id);
            nextStep.setEnabled(true);


            if (current_prev.getName()
                    .equals("Linear convert " + (lcIdx - 1))) {

                textPaneLinearConvert.setText(null);
                Style styleRed = getStyleText(textPaneLinearConvert, Color.RED);
                Style styleBlue = getStyleText(textPaneLinearConvert, Color.BLUE);

                insertColorText(textPaneLinearConvert, styleRed, "Регистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + Conversions.hex(current_prev.getBefore()));
                insertColorText(textPaneLinearConvert, styleBlue, "\n\nСдвиг №");
                insertColorText(textPaneLinearConvert, styleBlue, Integer.toString(roundIdx));
                insertColorText(textPaneLinearConvert, styleRed, "\n\nРегистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + String.valueOf(Conversions.hex(current_prev.getAfter())));
                lcIdx--;
                step.setText("Шаг №" + Integer.toString(lcIdx));
            }
            if (current_prev.getName()
                    .equals("Round " + (roundIdx - 1))) {

                textPaneLinearConvert.setText(null);
                Style styleRed = getStyleText(textPaneLinearConvert, Color.RED);
                Style styleBlue = getStyleText(textPaneLinearConvert, Color.BLUE);

                insertColorText(textPaneLinearConvert, styleRed, "Регистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + Conversions.hex(current_prev.getBefore()));
                insertColorText(textPaneLinearConvert, styleBlue, "\n\nСдвиг №");
                insertColorText(textPaneLinearConvert, styleBlue, Integer.toString(roundIdx - 1));
                insertColorText(textPaneLinearConvert, styleRed, "\n\nРегистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + String.valueOf(Conversions.hex(current_prev.getAfter())));
                lcIdx--;
                roundArrIdx--;
                roundIdx--;
                step.setText("Шаг №" + Integer.toString(lcIdx));


            }
            nextStep.setEnabled(!current_prev.getName().equals("Linear convert 15"));
            if (current_prev.getName().equals("Linear convert 0")) {
                lcIdx = 0;
                prevStep.setEnabled(false);
                nextRound.setEnabled(true);
                step.setText("Шаг №" + Integer.toString(lcIdx));
            } else {
                prevStep.setEnabled(true);
            }

//            prevRound.setEnabled(id > 3);
            prevRound.setEnabled(roundIdx != 1);
            tree.setSelectionPath(findTreePath(current_prev));
            currentId = id;

        });
    }


    private void setCurrentNextRound(int id) {
        SwingUtilities.invokeLater(() -> {
            lcIdx = 0;

            step.setText("Шаг №" + Integer.toString(lcIdx));
            State current = stateList.get(id);
//            System.out.println("DEBUG lcIdx before: " + lcIdx);
//            System.out.println("DEBUG roundIdx before: " + roundIdx);
            if (current.getName()
                    .equals("Linear convert " + (lcIdx))) {
                nextRound.setEnabled(true);
                prevRound.setEnabled(true);

                textPaneLinearConvert.setText(null);
                Style styleRed = getStyleText(textPaneLinearConvert, Color.RED);
                Style styleBlue = getStyleText(textPaneLinearConvert, Color.BLUE);

                insertColorText(textPaneLinearConvert, styleRed, "Регистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + Conversions.hex(current.getBefore()));
                insertColorText(textPaneLinearConvert, styleBlue, "\n\nСдвиг №");
                insertColorText(textPaneLinearConvert, styleBlue, Integer.toString(roundIdx + 1));
                insertColorText(textPaneLinearConvert, styleRed, "\n\nРегистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + String.valueOf(Conversions.hex(current.getAfter())));
                lcIdx++;
                roundIdx++;
                roundArrIdx++;
                step.setText("Шаг №" + Integer.toString(lcIdx));

            }
            if (current.getName().equals("Linear convert 15")) {
                nextStep.setEnabled(false);
                lcIdx = 15;
                step.setText("Шаг №" + Integer.toString(lcIdx));
            }
            if (current.getName().equals("Linear convert 0")) {
                prevStep.setEnabled(false);
                lcIdx = 0;
                step.setText("Шаг №" + Integer.toString(lcIdx));
            }
            if (current.getName().equals("Round 9")) {
                textPaneLinearConvert.setText(null);
                Style styleRed = getStyleText(textPaneLinearConvert, Color.RED);

                insertColorText(textPaneLinearConvert, styleRed, "Регистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + Conversions.hex(current.getBefore()));
                prevStep.setEnabled(false);
                nextStep.setEnabled(false);
                nextRound.setEnabled(false);
            }

            if (id >= 147) {
                nextRound.setEnabled(false);
            }
            if (roundIdx == 1) {
                prevRound.setEnabled(false);
            }
//            prevRound.setEnabled(id > 3);
            prevRound.setEnabled(roundIdx != 1);
            tree.setSelectionPath(findTreePath(current));
            currentId = id;
//            System.out.println("DEBUG lcIdx after: " + lcIdx);

        });
    }

    private void setCurrentPrevRound(int id) {
        SwingUtilities.invokeLater(() -> {
            lcIdx = 0;
            nextStep.setEnabled(true);
            step.setText("Шаг №" + Integer.toString(lcIdx));
            State current = stateList.get(id);
//            System.out.println("DEBUG lcIdx before: " + lcIdx);
            if (current.getName().equals("Linear convert " + (lcIdx))) {
                nextRound.setEnabled(true);

                textPaneLinearConvert.setText(null);
                Style styleRed = getStyleText(textPaneLinearConvert, Color.RED);
                Style styleBlue = getStyleText(textPaneLinearConvert, Color.BLUE);

                insertColorText(textPaneLinearConvert, styleRed, "Регистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + Conversions.hex(current.getBefore()));
                insertColorText(textPaneLinearConvert, styleBlue, "\n\nСдвиг №");
                insertColorText(textPaneLinearConvert, styleBlue, Integer.toString(roundIdx - 1));
                insertColorText(textPaneLinearConvert, styleRed, "\n\nРегистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + String.valueOf(Conversions.hex(current.getAfter())));
                roundIdx--;
                roundArrIdx--;

            }
            if (current.getName().equals("Linear convert 15")) {
                nextStep.setEnabled(false);
                lcIdx = 15;
                step.setText("Шаг №" + Integer.toString(lcIdx));
            }
            if (current.getName().equals("Linear convert 0")) {
                prevStep.setEnabled(false);
            }
//            prevRound.setEnabled(id > 3);
            prevRound.setEnabled(roundIdx != 1);
            tree.setSelectionPath(findTreePath(current));
            currentId = id;
//            System.out.println("DEBUG lcIdx after: " + lcIdx);

        });
    }

    private void setCurrentNextStep(int id) {
        SwingUtilities.invokeLater(() -> {
//            System.out.println("DEBUG next id: " + id);
//            System.out.println("DEBUG lcIdx before: " + lcIdx);
            State current = stateList.get(id);
            nextStep.setEnabled(currentId != stateList.size() - 2);
            prevStep.setEnabled(true);
//            previousStep.setEnabled(id > 3);
            if (current.getName()
                    .equals("Linear convert " + (lcIdx + 1))) {
                textPaneLinearConvert.setText(null);
                Style styleRed = getStyleText(textPaneLinearConvert, Color.RED);
                Style styleBlue = getStyleText(textPaneLinearConvert, Color.BLUE);

                insertColorText(textPaneLinearConvert, styleRed, "Регистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + Conversions.hex(current.getBefore()));
                insertColorText(textPaneLinearConvert, styleBlue, "\n\nСдвиг №");
                insertColorText(textPaneLinearConvert, styleBlue, Integer.toString(roundIdx));
                insertColorText(textPaneLinearConvert, styleRed, "\n\nРегистр:");
                insertColorText(textPaneLinearConvert, styleRed, " " + String.valueOf(Conversions.hex(current.getAfter())));
//                System.out.println("lcIdx = "+lcIdx);
                lcIdx++;
                step.setText("Шаг №" + Integer.toString(lcIdx));
            } else if (current.getName()
                    .equals("Round " + roundIdx)) {
                roundIdx++;

            }
            if (current.getName().equals("Linear convert 15")) {
                nextStep.setEnabled(false);
            }
            if (current.getName().equals("Linear convert 0")) {
                prevStep.setEnabled(false);
            }
            if (id >= 162) {
                nextRound.setEnabled(false);
            }

            prevRound.setEnabled(roundIdx != 1);
//            prevRound.setEnabled(id > 3);
            tree.setSelectionPath(findTreePath(current));
            currentId = id;
//            System.out.println("DEBUG lcIdx after: " + lcIdx);
        });

    }

    private List<State> createStateList(State state) {
        ArrayList<String> test = new ArrayList<String>(Arrays.asList(
                "Rotate bytes 0",
                "Rotate bytes 1",
                "Rotate bytes 2",
                "Rotate bytes 3",
                "Rotate bytes 4",
                "Rotate bytes 5",
                "Rotate bytes 6",
                "Rotate bytes 7",
                "Rotate bytes 8",
                "Linear convert 0",
                "Linear convert 1",
                "Linear convert 2",
                "Linear convert 3",
                "Linear convert 4",
                "Linear convert 5",
                "Linear convert 6",
                "Linear convert 7",
                "Linear convert 8",
                "Linear convert 9",
                "Linear convert 10",
                "Linear convert 11",
                "Linear convert 12",
                "Linear convert 13",
                "Linear convert 14",
                "Linear convert 15",
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

    private GridBagConstraints createConstants(int width, int height, int weightX, int weightY, int x,
                                               int y, Insets insets) {
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

    private JTree createStateTree(State state) {
        JTree tree = new JTree(createStateTreeNodes(state));
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath()
                    .getLastPathComponent();
            setCurrentNextStep(stateList.indexOf((State) node.getUserObject()));
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
