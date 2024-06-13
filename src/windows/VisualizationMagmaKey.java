package windows;

import statement.State;
import utils.Conversions;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static utils.OSInfo.*;
import static utils.OSInfo.isUnix;

public class VisualizationMagmaKey extends JDialog {
    private int currentId = 1;
    private int keyCounter = 1;
    private int fontSize = 14;
    private final List<State> stateList;
    private final JButton next = new JButton(">>");
    private final JButton previous = new JButton("<<");

    private final JLabel stage = new JLabel("Раунд №_", SwingConstants.CENTER);

    private JTextPane fieldRoundKey = new JTextPane();

    private final JButton genKeyHint = new JButton("Схема раундовых ключей");

    public VisualizationMagmaKey(Component owner, State state, byte[] result) {
        super((Frame) SwingUtilities.getWindowAncestor(owner), "Визуализация развертывания раундовых ключей шифра 'Магма'", false);
        stateList = createStateList(state);
        Font font = new Font("Arial", Font.BOLD, fontSize);

        fieldRoundKey.setEditable(false);

        next.addActionListener(e -> setCurrentIdNext(currentId + 1));
        previous.addActionListener(e -> setCurrentIdPrev(currentId - 1));

        stage.setFont(font);
        stage.setForeground(Color.BLUE);
        genKeyHint.setFont(font);
        next.setFont(font);
        previous.setFont(font);
        next.setForeground(Color.BLUE);
        previous.setForeground(Color.BLUE);
        previous.setEnabled(false);

        fieldRoundKey.setBackground(Color.getHSBColor(0.016f, 0.11f, 1.f));
        genKeyHint.addActionListener(e -> {
            try {
                Hint1();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        getContentPane().setLayout(new GridBagLayout());


        GridBagConstraints fieldRoundKeyConstraints = createConstants(1, 1, 1, 0, 0, 0, new Insets(5, 5, 5, 0));
        getContentPane().add(fieldRoundKey, fieldRoundKeyConstraints);


        GridBagConstraints stageConstraints = createConstants(1, 1, 2, 1, 1, 0, new Insets(0, 5, 5, 0));
        getContentPane().add(stage, stageConstraints);

        GridBagConstraints previousConstraints = createConstants(1, 1, 1, 1, 1, 1, new Insets(0, 5, 5, 0));
        getContentPane().add(previous, previousConstraints);

        GridBagConstraints nextConstraints = createConstants(1, 1, 1, 1, 2, 1, new Insets(0, 5, 5, 0));
        getContentPane().add(next, nextConstraints);

        GridBagConstraints genKeyHintConstraints = createConstants(1, 1, 1, 1, 2, 0, new Insets(0, 5, 5, 0));
        getContentPane().add(genKeyHint, genKeyHintConstraints);

        setResizable(true);
        setPreferredSize(new Dimension(800, 600));
        pack();
    }

    private void Hint1() throws Exception {

        String fileName = "keysMagma.png";
        String textFileName = "keysMagma_hint.txt";
        String title = "Расписание раундовых ключей";
        Desktop dt = Desktop.getDesktop();
        String user = getUser();

        if (isWindows()) {
            File fWin = new File("C:\\Users\\" + user + "\\Documents\\CryptoETU\\" + fileName);
            String path = "C:\\Users\\" + user + "\\Documents\\CryptoETU\\" + textFileName;
            dt.open(fWin);
            try{
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationMagmaKey.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (isMac()) {
            File fMac = new File("/Users/" + user + "/Documents/CryptoETU/" + fileName);
            String path = "/Users/" + user + "/Documents/CryptoETU/" + textFileName;
            dt.open(fMac);
            try{
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationMagmaKey.this, content, title, JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (isUnix()) {
            File fUnix = new File("/home/" + user + "/Documents/CryptoETU/" + fileName);
            String path = "/home/" + user + "/Documents/CryptoETU/" + textFileName;
            dt.open(fUnix);
            try{
                String content = Files.readString(Paths.get(path));
                JOptionPane.showMessageDialog(VisualizationMagmaKey.this, content, title, JOptionPane.INFORMATION_MESSAGE);
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


    private void setCurrentIdPrev(int id) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("DEBUG id " + id);
            State current = stateList.get(id);
            next.setEnabled(!current.getName().equals("Round key part 31"));
            previous.setEnabled(id != 1);

            Font font = new Font("Arial", Font.BOLD, fontSize);
            if (current.getName().equals("Round key part " + (keyCounter - 2))) {
                fieldRoundKey.setText(null);
                fieldRoundKey.setFont(font);
                Style styleRed = getStyleText(fieldRoundKey, Color.RED);
                Style styleBlack = getStyleText(fieldRoundKey, Color.BLACK);
                Style styleBlue = getStyleText(fieldRoundKey, Color.BLUE);
                insertColorText(fieldRoundKey, styleRed, "Секретный ключ:");
                insertColorText(fieldRoundKey, styleBlack, " " + String.valueOf(Conversions.hex(current.getBefore())));
                insertColorText(fieldRoundKey, styleBlue, "\n\nРаундовый ключ:");
                insertColorText(fieldRoundKey, styleBlack, " " + String.valueOf(Conversions.hex(current.getAfter())));
                keyCounter--;
                stage.setText("Раунд №" + String.valueOf(keyCounter));
            }
            currentId = id;
        });
    }

    private void setCurrentIdNext(int id) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("DEBUG id " + id);
            State current = stateList.get(id);
            next.setEnabled(!current.getName().equals("Round key part 31"));
            previous.setEnabled(id != 1);
            if (current.getName().equals("Round key part " + keyCounter)) {
                fieldRoundKey.setText(null);
                Style styleRed = getStyleText(fieldRoundKey, Color.RED);
                Style styleBlack = getStyleText(fieldRoundKey, Color.BLACK);
                Style styleBlue = getStyleText(fieldRoundKey, Color.BLUE);
                insertColorText(fieldRoundKey, styleRed, "Секретный ключ:");
                insertColorText(fieldRoundKey, styleBlack, " " + String.valueOf(Conversions.hex(current.getBefore())));
                insertColorText(fieldRoundKey, styleBlue, "\n\nРаундовый ключ:");
                insertColorText(fieldRoundKey, styleBlack, " " + String.valueOf(Conversions.hex(current.getAfter())));
                keyCounter++;
                stage.setText("Раунд №" + String.valueOf(keyCounter));
            }

            currentId = id;
        });
    }

    private List<State> createStateList(State state) {
        ArrayList<String> test = new ArrayList<String>(Arrays.asList(
                "Round key part 0", "Round key part 1", "Round key part 2", "Round key part 3", "Round key part 4", "Round key part 5",
                "Round key part 6", "Round key part 7", "Round key part 8", "Round key part 9", "Round key part 10", "Round key part 11",
                "Round key part 12", "Round key part 13", "Round key part 14", "Round key part 15", "Round key part 16", "Round key part 17",
                "Round key part 18", "Round key part 19", "Round key part 20", "Round key part 21", "Round key part 22", "Round key part 23",
                "Round key part 24", "Round key part 25", "Round key part 26", "Round key part 27", "Round key part 28", "Round key part 29",
                "Round key part 30", "Round key part 31"
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

}
