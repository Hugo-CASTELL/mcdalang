package n7.mcdalang.views;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import n7.mcdalang.input.OptionActionListener;
import n7.mcdalang.input.RunActionListener;
import n7.mcdalang.input.SwitchActionListener;
import n7.mcdalang.util.GlobalInstances;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class MainView extends JPanel implements View {

    //private JPanel topGUI;
    private JPanel bodyGUI;
    private JPanel bottomGUI;

    private JButton switchButton;
    private JButton runButton;
    private JButton optionsButton;

    private CodeTextArea originTextArea;
    private CodeTextArea[] codeTextArea;

    private TabPanel tabPanel;

    private int languageIndex;

    private boolean autoRun;

    public MainView() {
        String[] languages = GlobalInstances.getAppSettings().getCurrentLanguages().toArray(new String[0]);
        languageIndex = languages.length;
        autoRun = false;

        this.setTheme(GlobalInstances.getAppSettings().getCurrentTheme());

        originTextArea = new CodeTextArea(this, "MacdaLang",true);

        codeTextArea = new CodeTextArea[languageIndex];
        for (int i = 0; i < languageIndex; i++) {
            codeTextArea[i] = new CodeTextArea(this, languages[i], false);
        }
        tabPanel = new TabPanel(codeTextArea);

        runButton = new JButton("Run");
        switchButton = new JButton("Switch");
        optionsButton = new JButton("Options");

        runButton.addActionListener(
                new RunActionListener(this, originTextArea, codeTextArea));
        switchButton.addActionListener(new SwitchActionListener(autoRun));
        optionsButton.addActionListener(new OptionActionListener(this, autoRun));

        this.addComponents();
    }

    protected void addComponents() {
        // topGUI components
        // topGUI = new JPanel(new MigLayout("fillx"));

        // bodyGUI components
        bodyGUI = new JPanel(new MigLayout("fill, ins 0", "[50%][50%]"));

        bodyGUI.add(originTextArea, "cell 0 0, grow, push");
        bodyGUI.add(tabPanel, "cell 1 0, grow, push");

        // bottomGUI components
        bottomGUI = new JPanel(new MigLayout("wrap, fillx"));

        bottomGUI.add(switchButton, "cell 1 0");
        bottomGUI.add(runButton, "cell 2 0");
        bottomGUI.add(optionsButton, "cell 3 0");


        // add all components
        this.setLayout(new MigLayout("fill, ins 0, gap 0", "[grow]", "[grow][shrink]"));
        // add(topGUI, "cell 0 0, growx");
        this.add(bodyGUI, "cell 0 0, grow, push");
        this.add(bottomGUI, "cell 0 1, grow, push");
    }

    public void run() {
        try {
            for (CodeTextArea textArea : codeTextArea) {
                textArea.setCode(originTextArea.getCode());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.fillInStackTrace(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    public CodeTextArea getOriginTextArea() {
        return originTextArea;
    }

    public CodeTextArea[] getCodeTextArea() {
        return codeTextArea;
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    public void triggerAutoRun() {
        autoRun();
    }

    public void setTheme(String theme) {
        try {
            switch (theme.toLowerCase()) {
                case "light":
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
                case "dark":
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                case "intellij":
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    break;
                case "darcula":
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    break;
                default:
                    break;
            }
        } catch (Exception ex) {
            new JOptionPane("Failed to initialize Look and Feel");
            ex.printStackTrace();
        }
    }

    private void autoRun() {
        if (autoRun) {
            run();
        }
    }
}
