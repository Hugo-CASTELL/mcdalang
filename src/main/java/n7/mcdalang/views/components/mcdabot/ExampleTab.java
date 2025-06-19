package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.input.CodeKeyListener;
import n7.mcdalang.input.mcdabot.LeaveListener;
import n7.mcdalang.input.mcdabot.TryCodeListener;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.main.CodeTextArea;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.views.components.util.RoundButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ExampleTab extends View {
    protected String explication = "";
    protected String code = "";
    private CodeTextArea codeArea;
    private PanelDialog dialogue;

    /*
    headerPanel
        btnLeave
    mainContent
        buttonPanel
            btnRun
        codeArea
    */

    public ExampleTab() {
        this.setLayout(new BorderLayout()); // on change ici pour BorderLayout

        // bouton au top
        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton btnLeave = new RoundButton("Retour", new Color(100, 200, 100), 50);
        btnLeave.addActionListener(new LeaveListener(this));
        topButtonPanel.add(btnLeave);
        this.add(topButtonPanel, BorderLayout.NORTH);

        // headerPanel
        PanelDialog headerPanel = new PanelDialog("", AppConfig.MCDABOT_HEAD_PATH);
        this.dialogue = headerPanel;

        // mainContent
        JPanel mainContent = new JPanel(new BorderLayout());

        // zone de code
        CodeTextArea codeArea = new CodeTextArea(Languages.MCDALANG, true);
        codeArea.registerListener(new CodeKeyListener(new MainController(new MainView()), codeArea));
        codeArea.setFont(codeArea.createFont(AppConfig.FONT_ADAPTERS.get(GlobalInstances.getAppSettings().getFont())));
        codeArea.setSizeFont(GlobalInstances.getAppSettings().getFontSize());
        this.codeArea = codeArea;
        mainContent.add(codeArea, BorderLayout.CENTER);

        //zone bouton code
        JPanel codeButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnRun = new RoundButton("Essayer la traduction", new Color(100, 200, 100), 10);
        btnRun.addActionListener(new TryCodeListener(this, codeArea));
        codeButtonPanel.add(btnRun);
        mainContent.add(codeButtonPanel, BorderLayout.NORTH);



        // split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, headerPanel, mainContent);
        splitPane.setResizeWeight(0.3); // 30% pour le haut, 70% pour le bas
        splitPane.setOneTouchExpandable(true); // petit bouton pour replier/déplier
        this.add(splitPane, BorderLayout.CENTER);
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

}
