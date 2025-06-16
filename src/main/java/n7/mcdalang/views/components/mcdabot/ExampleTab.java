package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.input.CodeKeyListener;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.util.CodeTextArea;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.views.components.util.RoundButton;

import javax.swing.*;
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
        this.setLayout(new BorderLayout());

        dialogue = new PanelDialog("", AppConfig.MCDABOT_HEAD_PATH);

        leaveButton = new RoundButton("Retour", new Color(100, 200, 100), 50);
        dialogue.add(leaveButton, "pos 0 0, h 30!");

        codeArea = new CodeTextArea(Languages.MCDALANG, true);
        codeArea.registerListener(new CodeKeyListener(null, codeArea));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tryCodeButton = new RoundButton("Essayer la traduction", new Color(100, 200, 100), 10);
        buttonPanel.add(tryCodeButton);

        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(buttonPanel, BorderLayout.NORTH);
        mainContent.add(codeArea, BorderLayout.CENTER);

        this.add(dialogue, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
    }

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
