package View;

import Controler.OptionActionListener;
import Controler.RunActionListener;
import Controler.SwitchActionListener;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class MainView extends JPanel {

    private JPanel topGUI;
    private JPanel bodyGUI;
    private JPanel bottomGUI;

    private JButton switchButton;
    private JButton runButton;
    private JButton optionsButton;

    private JLabel originLabel;
    private JLabel[] languagesLabel;

    private CodeTextArea originTextArea;
    private CodeTextArea[] codeTextArea;

    private int languageIndex;

    private boolean autoRun;

    public MainView(String[] languages) {
        languageIndex = languages.length;
        autoRun = false;

        originLabel = new JLabel("Origin");
        languagesLabel = new JLabel[languageIndex];
        for (int i = 0; i < languageIndex; i++) {
            languagesLabel[i] = new JLabel(languages[i]);
        }

        originTextArea = new CodeTextArea(this, true);

        codeTextArea = new CodeTextArea[languageIndex];
        for (int i = 0; i < languageIndex; i++) {
            codeTextArea[i] = new CodeTextArea(this, false);
        }


        runButton = new JButton("Run");
        switchButton = new JButton("Switch");
        optionsButton = new JButton("Options");

        runButton.addActionListener(
                new RunActionListener(this, originTextArea, codeTextArea));
        switchButton.addActionListener(new SwitchActionListener(autoRun));
        optionsButton.addActionListener(new OptionActionListener(autoRun, this));

        this.addComponents();
    }

    protected void addComponents() {
        // topGUI components
        topGUI = new JPanel(new MigLayout("fillx"));

        topGUI.add(originLabel, "cell 0 0, center");
        for (int i = 0; i < languageIndex; i++) {
            topGUI.add(languagesLabel[i], "cell " + (i + 2) + " 0, center");
        }


        // bodyGUI components
        bodyGUI = new JPanel(new MigLayout("fillx"));

        bodyGUI.add(originTextArea, "cell 0 0, grow, push");
        for (int i = 0; i < languageIndex; i++) {
            bodyGUI.add(codeTextArea[i], "cell " + (i + 1) + " 0, grow, push");
        }


        // bottomGUI components
        bottomGUI = new JPanel(new MigLayout("wrap, fillx"));

        bottomGUI.add(switchButton, "cell 1 0");
        bottomGUI.add(runButton, "cell 2 0");
        bottomGUI.add(optionsButton, "cell 3 0");


        // add all components
        setLayout(new MigLayout("fill, ins 10", "[grow]", "[][grow][]"));
        add(topGUI, "cell 0 0, growx");
        add(bodyGUI, "cell 0 1, grow, wrap");
        add(bottomGUI, "cell 0 2, growx");
    }

    public void run() {
        for (CodeTextArea textArea : codeTextArea) {
            textArea.setCode(originTextArea.getCode());
        }
    }

    public JLabel getOriginLabel() {
        return originLabel;
    }

    public JLabel[] getLanguagesLabel() {
        return languagesLabel;
    }

    public CodeTextArea getOriginTextArea() {
        return originTextArea;
    }

    public CodeTextArea[] getCodeTextArea() {
        return codeTextArea;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public boolean getAutoRun() {
        return autoRun;
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    public void triggerAutoRun() {
        autoRun();
    }

    private void autoRun() {
        if (autoRun) {
            run();
        }
    }
}
