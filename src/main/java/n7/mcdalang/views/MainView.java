package n7.mcdalang.views;

import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.components.CodeTextArea;
import n7.mcdalang.views.components.TabPanel;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class MainView extends JPanel implements View {

    //#region Fields

    //private JPanel topGUI;
    private final JPanel bodyGUI;
    private final JPanel bottomGUI;

    private final JButton switchButton;
    private final JButton runButton;
    private final JButton optionsButton;

    private final CodeTextArea originTextArea;
    private final CodeTextArea[] codeTextArea;

    private final TabPanel tabPanel;

    private final int languageIndex;

    //#endregion Fields

    //#region Constructor

    public MainView() {
        Languages[] languages = GlobalInstances.getAppSettings().getLanguages().toArray(new Languages[0]);
        languageIndex = languages.length;

        originTextArea = new CodeTextArea(Languages.MACDALANG,true);

        codeTextArea = new CodeTextArea[languageIndex];
        for (int i = 0; i < languageIndex; i++) {
            codeTextArea[i] = new CodeTextArea(languages[i], false);
        }
        tabPanel = new TabPanel(codeTextArea);

        runButton = new JButton("Run");
        switchButton = new JButton("Switch");
        optionsButton = new JButton("Options");

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

    //#endregion Constructor

    //#region Getters and Setters

    public CodeTextArea getOriginTextArea() {
        return originTextArea;
    }

    public CodeTextArea[] getCodeTextArea() {
        return codeTextArea;
    }

    public JButton getSwitchButton() {
        return switchButton;
    }

    public JButton getRunButton() {
        return runButton;
    }

    public JButton getOptionsButton() {
        return optionsButton;
    }

    //#endregion Getters and Setters
}
