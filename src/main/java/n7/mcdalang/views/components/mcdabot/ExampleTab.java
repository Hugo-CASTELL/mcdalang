package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.util.CodeTextArea;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.views.components.util.RoundButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ExampleTab extends View {

    //#region Fields

    private final JButton leaveButton;
    private final PanelDialog dialogue;

    private final JButton tryCodeButton;
    private final CodeTextArea codeArea;

    //#region Fields

    //#region Constructor

    public ExampleTab() {
        this.setLayout(new BorderLayout()); // on change ici pour BorderLayout

        // bouton au top
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Ajout d'une bordure vide pour l'espacement
        leaveButton = new RoundButton("Retour", new Color(100, 200, 100), 50);
        topButtonPanel.add(leaveButton);
        this.add(topButtonPanel, BorderLayout.NORTH);

        // headerPanel
        PanelDialog headerPanel = new PanelDialog("", AppConfig.MCDABOT_HEAD_PATH);
        this.dialogue = headerPanel;

        // mainContent
        JPanel mainContent = new JPanel(new BorderLayout());

        // zone de code
        codeArea = new CodeTextArea(Languages.MCDALANG, false);
        mainContent.add(codeArea, BorderLayout.CENTER);

        //zone bouton code
        JPanel codeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tryCodeButton = new RoundButton("Essayer la traduction", new Color(100, 200, 100), 10);
        codeButtonPanel.add(tryCodeButton);
        mainContent.add(codeButtonPanel, BorderLayout.NORTH);


        // split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, headerPanel, mainContent);
        splitPane.setResizeWeight(0.3); // 30% pour le haut, 70% pour le bas
        splitPane.setOneTouchExpandable(true); // petit bouton pour replier/dÃ©plier
        this.add(splitPane, BorderLayout.CENTER);    }

    //#endregion Constructor

    //#region Getters

    public JButton getLeaveButton() {
        return leaveButton;
    }

    public JButton getTryCodeButton() {
        return tryCodeButton;
    }

    public String getCode() {
        return this.codeArea.getCode();
    }

    //#endregion Getters

    //#region Setters

    protected void setCode(String code){
        this.codeArea.setCode(code);
    }

    protected void setDialogue(String message){
        this.dialogue.setDialogue(message);
    }

    //#region Setters

}
