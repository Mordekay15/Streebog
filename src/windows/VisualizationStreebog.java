package windows;

import statement.State;
import utils.Conversions;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static java.lang.Thread.sleep;

public class VisualizationStreebog extends JDialog {

    private final int fontSize = 14;
    private final Font font;
    private final List<State> stateList;
    private JPanel initPanel;
    private JPanel stage2Panel;
    private JPanel finalPanel;

    private CardLayout layouts;
    //ImageIcon xor = new ImageIcon("C:\\Users\\stepk\\IdeaProjects\\Crypto_ETU_1\\src\\main\\resources\\files\\images\\xor.png");
    private JLabel h = new JLabel();
    private JLabel v_512 = new JLabel();
    private JLabel N = new JLabel();
    private JLabel sigma = new JLabel();
    private JLabel m = new JLabel();
    private JLabel afterXOR = new JLabel();
    private JLabel afterS = new JLabel();
    private JLabel afterP = new JLabel();
    private JLabel afterL = new JLabel();
    private JLabel afterE = new JLabel();
    private JLabel afterXOR1 = new JLabel();
    private JLabel afterSquare1 = new JLabel();
    private JLabel afterSquare2 = new JLabel();
    private JLabel stage_number = new JLabel();
    private final JButton XOR = new JButton("XOR");
    private final JButton S = new JButton("S");
    private final JButton P = new JButton("P");
    private final JButton L = new JButton("L");
    private final JButton E = new JButton("E");
    private final JButton XOR1 = new JButton("XOR");
    private final JButton nextStep = new JButton(">");
    private final JButton backStep = new JButton("<");
    private final JButton back = new JButton("Назад");
    ImageIcon icon = new ImageIcon("D:\\IdeaProjects\\Crypto_ETU_1\\src\\main\\resources\\files\\images\\square.png");
    private final JButton square1 = new JButton(icon);
    private final JButton square2 = new JButton(icon);
    private final JButton endOfTheBlock = new JButton("Следующий блок");
    private final JButton help = new JButton("Справка");
    private final String html_text = "<html><div style='width: %dpx; padding: 6px;font-size:%dpx;'>%s</div></html>";
    private int countG = 2;
    JPanel GPanel = new JPanel();
    //private String str_h, str_N, str_key, str_m;
    //byte[] null_arr = new byte[64];
    //private String str_afterXOR, str_afterS, str_afterP, str_afterL, str_afterE, str_afterXOR1;
    Color customBlue = new Color(0xEBFAFF);
    Color customDarkBlue = new Color(0x84C2D6);
    Color highlightColor = new Color(0x95E5FF);

    public VisualizationStreebog(JDesktopPane parent, State state, byte[] result) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Визуализация хеш-функции Стрибог", false);

        stateList = createStateList(state);

        font = new Font("Arial", Font.BOLD, fontSize);
        layouts = new CardLayout();
        getContentPane().setLayout(layouts);

        start_screen();
        layouts.show(getContentPane(), "GPanel");
        init_screen_1stage();
        screen_2stage();
        screen_G();
        final_screen();

        setResizable(false);
        setPreferredSize(new Dimension(1000, 800));
        pack();

    }

    private void start_screen() {
        JPanel startPanel = new JPanel();
        JLabel theory = new JLabel("<html><body style='width: 600px; padding: 10px;font-size:14px;'>" +
                "<h1>Немного теории:</h1>" +
                "<h2>Основу хеш-функции «Стрибог» составляет - функция сжатия (G-функция)</h2>" +
                "<p>В целом хеширование производится в три этапа:</p>" +
                "<ol>" +
                "<li>Первый этап — инициализация всех нужных параметров.</li>" +
                "<li>Второй этап состоит из функции сжатия и двух операций исключающего ИЛИ.</li>" +
                "<li>Третий этап — завершающее преобразование: функция сжатия применяется к сумме всех блоков сообщения и дополнительно хешируется длина сообщения.</li>" +
                "</ol>" +
                "<p>Далее будет подробно разобран каждый этап!</p>" +
                "</body></html>");
        theory.setFont(font);
        theory.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Добавление отступов (верх, лево, низ, право)
        startPanel.add(theory, BorderLayout.NORTH);

        JButton nextButton = new JButton("Перейти к первому этапу");
        nextButton.setBackground(customBlue);
        nextButton.addActionListener(e -> showPanel("initPanel"));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(nextButton);
        buttonPanel.setBackground(Color.WHITE);
        startPanel.add(buttonPanel, BorderLayout.SOUTH);
        startPanel.setBackground(Color.WHITE);

        getContentPane().add(startPanel, "startPanel");
    }


    private void init_screen_1stage() {
        initPanel = new JPanel(new BorderLayout());
        initPanel.setBackground(Color.WHITE);
        initPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel resultLabel = new JLabel();
        resultLabel.setText(String.format("<html><h1 style='font-family:Arial;'>Этап 1. Инициализация</h1>Так как задана длина хеша равная <span style" +
                "='font-weight:bold;font-size:14px;font-family:Arial;'> %d </span>,то при инициализации хеш-код имеет " +
                "следующий вид: <br/><br/><span style='color:#2E88A6; font-size:14px;font-family:Arial;'>%s</span></html>", 256, Conversions.hex(stateList.get(1).getAfter())));//Arrays.toString(stateList.get(1).getAfter()).replaceAll("[\\[\\]]", "")));
        initPanel.add(resultLabel, BorderLayout.NORTH);

        JPanel buttonPanel = add_buttons("stage2Panel", "startPanel");
        buttonPanel.setBackground(Color.WHITE);
        initPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(initPanel, "initPanel");
    }

    private void showPanel(String layout_name) {
        layouts.show(getContentPane(), layout_name);
    }

    private JPanel add_buttons(String next, String prev) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton nextButton = new JButton("Продолжить");
        nextButton.setBackground(customBlue);
        nextButton.addActionListener(e -> showPanel(next));

        JButton prevButton = new JButton("Назад");
        prevButton.addActionListener(e -> showPanel(prev));
        prevButton.setBackground(customBlue);

        buttonPanel.add(prevButton, BorderLayout.WEST);
        buttonPanel.add(nextButton, BorderLayout.EAST);
        return buttonPanel;
    }

    private void screen_2stage() {
        stage2Panel = new JPanel(new BorderLayout());
        stage2Panel.setBackground(Color.WHITE);
        stage2Panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel stage2 = new JLabel("<html><body style='font-size:14px;'><h1>Этап 2</h1>" +
                "<p>Второй этап состоит из:</p>"+
                "<ul><li>Функции сжатия G. Последовательность вычислений:<ol>" +
                "<li>XOR - Сложение двух двоичных векторов по модулю 2</li>" +
                "<li>S   - Нелинейное биективное преобразование</li>" +
                "<li>P   - Перестановка байтов</li>" +
                "<li>L   - Линейное преобразование</li>" +
                "<li>E   - Функцию преобразования, в которой высчитываются раундовые ключи</li>" +
                "<li>XOR - Сложение двух двоичных векторов по модулю 2</li></ol></li>" +
                "<li>Побитовое исключающее ИЛИ над 512-битными блоками</li></ul>" +
                "</body></html>");
        stage2Panel.add(stage2, BorderLayout.NORTH);

        JPanel buttonPanel = add_buttons("GPanel", "initPanel");
        buttonPanel.setBackground(Color.WHITE);
        stage2Panel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(stage2Panel, "stage2Panel");
    }

    void screen_G(){
        GPanel.setLayout(null);
        GPanel.setBackground(Color.WHITE);

        this.h.setText(String.format(html_text, 200, 10, "Hash: <br/>"+Conversions.hex(stateList.get(3).getBefore())));
        this.N.setText(String.format(html_text, 180, 9, "N: "+ Conversions.hex(stateList.get(3).getAdditional())));
        this.v_512.setText(String.format(html_text, 180, 9, "512: "+Conversions.hex(stateList.get(10).getAdditional())));
        this.sigma.setText(String.format(html_text, 190, 9, "Sigma: "+ Conversions.hex(stateList.get(11).getBefore())));
        this.m.setText(String.format(html_text, 190, 9, "Data: "+Conversions.hex(stateList.get(2).getBefore())));

        this.stage_number.setFont(new Font("Arial", Font.BOLD, 23));
        this.stage_number.setText("Этап 2");
        this.afterXOR.setText(null);
        this.afterS.setText(null);
        this.afterP.setText(null);
        this.afterL.setText(null);
        this.afterE.setText(null);
        this.afterXOR1.setText(null);

        Border border = BorderFactory.createLineBorder(customDarkBlue, 2);

        this.h.setBounds(655, 15, 260, 125);
        this.h.setBorder(border);
        this.h.setOpaque(true);
        this.h.setBackground(customBlue);

        this.v_512.setBounds(370,15,240,100);
        this.v_512.setOpaque(true);
        this.v_512.setBackground(customBlue);

        this.N.setBounds(80, 15, 240, 100);
        this.N.setOpaque(true);
        this.N.setBackground(customBlue);

        this.sigma.setBounds(655, 185, 260, 100);
        this.sigma.setOpaque(true);
        this.sigma.setBackground(customBlue);

        this.m.setBounds(655, 325, 260, 100);
        this.m.setOpaque(true);
        this.m.setBackground(customBlue);

        this.square1.setBounds(330,50,30, 30);
        this.square1.setBackground(customDarkBlue);

        this.square2.setBounds(760, 290, 30,30);
        this.square2.setBackground(customDarkBlue);

        this.XOR.setBounds(80, 185, 530, 30);
        this.XOR.setBackground(customDarkBlue);
        this.S.setBounds(80, 265, 530, 30);
        this.S.setBackground(customDarkBlue);
        this.P.setBounds(80, 345, 530, 30);
        this.P.setBackground(customDarkBlue);
        this.L.setBounds(80, 425, 530, 30);
        this.L.setBackground(customDarkBlue);
        this.E.setBounds(80, 505, 530, 30);
        this.E.setBackground(customDarkBlue);
        this.XOR1.setBounds(80, 585, 530, 30);
        this.XOR1.setBackground(customDarkBlue);


        this.back.setBounds(80, 680, 100, 20);
        this.back.setBackground(customDarkBlue);
        this.backStep.setBounds(230, 680, 100, 20);
        this.backStep.setBackground(customDarkBlue);
        this.nextStep.setBounds(350, 680, 100, 20);
        this.nextStep.setBackground(customDarkBlue);
        this.endOfTheBlock.setBounds(500, 680, 150, 20);
        this.endOfTheBlock.setBackground(customDarkBlue);
        this.help.setBounds(700,680, 100,20);
        this.help.setBackground(customDarkBlue);

        this.afterSquare1.setBounds(80, 120, 530, 50);
        //this.afterSquare1.setBorder(border);
        this.afterSquare1.setOpaque(true);
        this.afterSquare1.setBackground(customBlue);

        this.afterSquare2.setBounds(655, 430, 260, 100);
        //this.afterSquare2.setBorder(border);
        this.afterSquare2.setOpaque(true);
        this.afterSquare2.setBackground(customBlue);

        this.afterXOR.setBounds(80, 215, 530, 50);
        //this.afterXOR.setBorder(border);
        this.afterXOR.setOpaque(true);
        this.afterXOR.setBackground(customBlue);
        this.afterS.setBounds(80, 295, 530, 50);
        //this.afterS.setBorder(border);
        this.afterS.setOpaque(true);
        this.afterS.setBackground(customBlue);
        this.afterP.setBounds(80, 375, 530, 50);
        //this.afterP.setBorder(border);
        this.afterP.setOpaque(true);
        this.afterP.setBackground(customBlue);
        this.afterL.setBounds(80, 455, 530, 50);
        //this.afterL.setBorder(border);
        this.afterL.setOpaque(true);
        this.afterL.setBackground(customBlue);
        this.afterE.setBounds(80, 535, 530, 50);
        //this.afterE.setBorder(border);
        this.afterE.setOpaque(true);
        this.afterE.setBackground(customBlue);
        this.afterXOR1.setBounds(80, 615, 530, 50);
        //this.afterXOR1.setBorder(border);
        this.afterXOR1.setOpaque(true);
        this.afterXOR1.setBackground(customBlue);

        this.stage_number.setBounds(655, 550, 100, 50);

        nextStep.addActionListener(e -> setCurrentIdNextStep());
        back.addActionListener(e -> showPanel("stage2Panel"));
        backStep.addActionListener(e -> setCurrentIdBackStep());
        endOfTheBlock.addActionListener(e -> setNextBlock());

        GPanel.add(this.h);
        GPanel.add(this.N);
        GPanel.add(this.v_512);
        GPanel.add(this.sigma);
        GPanel.add(this.m);
        GPanel.add(this.XOR);
        GPanel.add(this.S);
        GPanel.add(this.P);
        GPanel.add(this.L);
        GPanel.add(this.E);
        GPanel.add(this.XOR1);
        GPanel.add(this.back);
        GPanel.add(this.backStep);
        GPanel.add(this.nextStep);
        GPanel.add(this.endOfTheBlock);
        GPanel.add(this.help);

        GPanel.add(this.afterXOR);
        GPanel.add(this.afterS);
        GPanel.add(this.afterP);
        GPanel.add(this.afterL);
        GPanel.add(this.afterE);
        GPanel.add(this.afterXOR1);
        GPanel.add(this.square1);
        GPanel.add(this.square2);
        GPanel.add(this.afterSquare1);
        GPanel.add(this.afterSquare2);

        GPanel.add(this.stage_number);

        getContentPane().add(GPanel, "GPanel");
    }

    private void final_screen(){
        finalPanel = new JPanel(new BorderLayout());
        finalPanel.setBackground(Color.WHITE);
        finalPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel resultLabel = new JLabel();
        resultLabel.setText(String.format("<html><h1>Результат хеширования</h1>Хеш-код имеет следующий вид: <br/><br/>" +
                "<span style='color:#2E88A6; font-size:14px;'>%s</span></html>", Conversions.hex(stateList.get(stateList.size()-1).getAfter())));
        finalPanel.add(resultLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        JButton prevButton = new JButton("В начало");
        prevButton.setBackground(customBlue);
        prevButton.addActionListener(e -> showPanel("startPanel"));

        buttonPanel.add(prevButton, BorderLayout.WEST);
        finalPanel.add(buttonPanel, BorderLayout.SOUTH);

        getContentPane().add(finalPanel, "finalPanel");
    }

    private List<State> createStateList(State state) {
        ArrayList<String> test = new ArrayList<String>(Arrays.asList(
        /*init*/           "Init",
        /*stage 2 and 3*/  "G",
        /*G*/              "XOR", "S", "P", "L", "E", "XOR_h","XOR_m", "512_1", "512_2"
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

    private void setCurrentIdNextStep()  {
        countG++;
        if(countG >= stateList.size()) showPanel("finalPanel");
        State current = stateList.get(countG);

        switch (current.getName()) {
            case "XOR":
                if(countG == 3) {
                    highlight(this.N, this.h);
                } else {
                    highlight(this.afterSquare1, this.h);
                }
                this.afterXOR.setText(String.format(html_text,415, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterXOR);
                break;
            case "S":
                highlight(this.afterXOR);
                this.afterS.setText(String.format(html_text,415, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterS);
                break;
            case "P":
                highlight(this.afterS);
                this.afterP.setText(String.format(html_text,415, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterP);
                break;
            case "L":
                highlight(this.afterP);
                //str_key = Conversions.hex(current.getAfter());
                this.afterL.setText(String.format(html_text,415, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterL);
                break;
            case "E":
                //str_afterE = Conversions.hex(current.getAfter());
                highlight(this.afterL);
                this.afterE.setText(String.format(html_text,415, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterE);
                break;
            case "XOR_h":
                highlight(this.h, this.afterE);
                this.afterXOR1.setText(String.format(html_text,415, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterXOR1);
                break;
            case "XOR_m":
                highlight(this.afterXOR1, this.m);
                this.afterXOR1.setText(String.format(html_text,415, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterXOR1);
                break;
            case "512_1":
                highlight(this.N,this.v_512);
                this.afterSquare1.setText(String.format(html_text, 415, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterSquare1);
                break;
            case "512_2":
                highlight(this.m, this.sigma);
                this.afterSquare2.setText(String.format(html_text, 190, 9, Conversions.hex(current.getAfter())));
                highlight(this.afterSquare2);
                break;
            case "G":
                this.afterXOR.setText(null);
                this.afterS.setText(null);
                this.afterP.setText(null);
                this.afterL.setText(null);
                this.afterE.setText(null);
                this.afterXOR1.setText(null);

                this.h.setText(String.format(html_text, 190, 9, "Hash:<br/>" + Conversions.hex(stateList.get(countG+1).getBefore())));
                this.N.setText(String.format(html_text, 180, 9, "N: " + Conversions.hex(stateList.get(countG+1).getAdditional())));

                this.m.setText(String.format(html_text, 190, 9, "Data: "+Conversions.hex(current.getBefore())));
                this.sigma.setText(String.format(html_text, 190, 9, "Sigma: " + Conversions.hex(stateList.get(countG+9).getBefore())));

                if(Objects.equals(current.getID(), "streebog_3")) {
                    this.stage_number.setText("Этап 3");
                }
                else {
                    this.stage_number.setText("Этап 2");
                }
                break;
            }
    }

    private void setCurrentIdBackStep() {
        if(countG < 3) return;
        State current = stateList.get(countG);

        switch (current.getName()) {
            case "XOR":
                this.afterXOR.setText(null);
                break;
            case "S":
                this.afterS.setText(null);
                break;
            case "P":
                this.afterP.setText(null);
                break;
            case "L":
                //str_key = Conversions.hex(current.getAfter());
                this.afterL.setText(null);
                //this.key.setText(null);
                break;
            case "E":
                //str_afterE = Conversions.hex(current.getAfter());
                this.afterE.setText(null);
                break;
            case "XOR_h":
                this.afterXOR1.setText(null);
                break;
            case "XOR_m":
                this.afterXOR1.setText(String.format(html_text,415, 9, Conversions.hex(stateList.get(countG-1).getAfter())));
                break;
            case "G":
                this.afterXOR.setText(String.format(html_text,415, 9, Conversions.hex(stateList.get(countG-6).getAfter())));
                this.afterS.setText(String.format(html_text,415, 9, Conversions.hex(stateList.get(countG-5).getAfter())));
                this.afterP.setText(String.format(html_text,415, 9, Conversions.hex(stateList.get(countG-4).getAfter())));
                this.afterL.setText(String.format(html_text,415, 9, Conversions.hex(stateList.get(countG-3).getAfter())));
                //this.key.setText(String.format("<html><div style='width: 200px; padding: 6px;font-size:9px;'>Key:<br/>%s</div></html>"
                //        , Conversions.hex(stateList.get(countG-3).getAfter())));
                this.afterE.setText(String.format(html_text,415, 9, Conversions.hex(stateList.get(countG-2).getAfter())));
                this.afterXOR1.setText(String.format(html_text,415, 9, Conversions.hex(stateList.get(countG).getAfter())));

                //str_h = Conversions.hex(stateList.get(countG-7).getBefore());
                this.h.setText(String.format(html_text,200, 9, "Hash:<br/>" + Conversions.hex(stateList.get(countG-7).getBefore())));

                //str_N = Conversions.hex(stateList.get(countG-7).getAdditional());
                this.N.setText(String.format(html_text,200, 9, "N:<br/>" + Conversions.hex(stateList.get(countG-7).getAdditional())));

                //str_m = Conversions.hex(stateList.get(countG-8).getBefore());
                this.m.setText(String.format("<html><div style='width: 200px; padding: 6px;font-size:9px;'>Data:<br/>%s</div></html>"
                        , Conversions.hex(stateList.get(countG-8).getBefore())));
                if(Objects.equals(current.getID(), "streebog_3")) {
                    this.stage_number.setText("Этап 3");
                }
                else {
                    this.stage_number.setText("Этап 2");
                }
                break;
        }
            countG--;
    }

    private void setNextBlock(){
        countG += 8;
        if(countG >= stateList.size()) showPanel("finalPanel");
        State current = stateList.get(countG);

        this.afterXOR.setText(null);

        this.afterS.setText(null);

        this.afterP.setText(null);

        this.afterL.setText(null);

        this.afterE.setText(null);

        this.afterXOR1.setText(null);

        //str_h = Conversions.hex(stateList.get(countG+1).getBefore());
        this.h.setText(String.format("<html><div style='width: 200px; padding: 6px;font-size:9px;'>Hash:<br/>%s</div></html>"
                , Conversions.hex(stateList.get(countG+1).getBefore())));

        //str_N = Conversions.hex(stateList.get(countG+1).getAdditional());
        this.N.setText(String.format("<html><div style='width: 200px; padding: 6px;font-size:9px;'>N:<br/>%s</div></html>"
                , Conversions.hex(stateList.get(countG+1).getAdditional())));

        //str_m = Conversions.hex(stateList.get(countG).getBefore());
        this.m.setText(String.format("<html><div style='width: 200px; padding: 6px;font-size:9px;'>Data:<br/>%s</div></html>"
                , Conversions.hex(stateList.get(countG).getBefore())));
        if(Objects.equals(current.getID(), "streebog_3")) {
            this.stage_number.setText("Этап 3");
        }
        else {
            this.stage_number.setText("Этап 2");
        }
    }

    private void highlight(JLabel label1, JLabel label2) {
        label1.setBackground(highlightColor);
        label2.setBackground(highlightColor);

        Timer timer = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label1.setBackground(customBlue);
                label2.setBackground(customBlue);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void highlight(JLabel label1) {
        label1.setBackground(highlightColor);
        Timer timer = new Timer(400, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label1.setBackground(customBlue);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
