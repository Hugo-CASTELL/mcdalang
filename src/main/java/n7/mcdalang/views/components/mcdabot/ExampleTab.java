package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.input.CodeKeyListener;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.main.CodeTextArea;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.views.components.util.RoundButton;

import javax.swing.*;
import java.awt.*;

public class ExampleTab extends View {
    protected String explication = "";
    protected String code = "";
    private final CodeTextArea codeArea;
    private final PanelDialog dialogue;
    private final JButton btnLeave;
    private final JButton btnTryCode;

    public ExampleTab() {
        this.setLayout(new BorderLayout());

        // dialogue
        PanelDialog headerPanel = new PanelDialog("",
                AppConfig.MCDABOT_HEAD_PATH
        );
        this.dialogue = headerPanel;

        JPanel mainContent = new JPanel(new BorderLayout());

        // Code
        CodeTextArea codeArea = new CodeTextArea(Languages.MCDALANG, true);
        codeArea.registerListener(new CodeKeyListener(null, codeArea));
        this.codeArea = codeArea;

        // liste de boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnTryCode = new RoundButton("Essayer la traduction", new Color(100, 200, 100), 10);
        buttonPanel.add(btnTryCode);

        btnLeave = new RoundButton("Retour", new Color(100, 200, 100), 50);
        headerPanel.add(btnLeave, "pos 0 0, h 30!");
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
        mainContent.add(buttonPanel, BorderLayout.NORTH);
        mainContent.add(codeArea, BorderLayout.CENTER);
    }

    public JButton getBtnTryCode() {
        return btnTryCode;
    }

    public String getCode() {
        return this.codeArea.getCode();
    }
    
    protected void setCode(String code){
        this.codeArea.setCode(code);
    }

    protected void setDialogue(String message){
        this.dialogue.setDialogue(message);
    }

    public void reset(){
        this.setCode(this.code);
        this.setDialogue(this.explication);
    }

    public JButton getBtnLeave() {
        return btnLeave;
    }

}
