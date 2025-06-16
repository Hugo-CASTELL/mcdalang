package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.input.CodeKeyListener;
import n7.mcdalang.input.mcdabot.LeaveListener;
import n7.mcdalang.input.mcdabot.TryCodeListener;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.main.CodeTextArea;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.views.components.util.RoundButton;

import javax.swing.*;
import java.awt.*;

public class ExampleTab extends View {
    public ExampleTab(String explication, String code) {
        this.setLayout(new BorderLayout());

        // bouton retour
        JButton btnLeave = new RoundButton("Retour", new Color(100, 200, 100), 50);
        btnLeave.addActionListener(new LeaveListener(this));

        // dialogue
        JPanel headerPanel = new PanelDialog(explication,
                AppConfig.MCDABOT_HEAD_PATH
        );

        JPanel mainContent = new JPanel(new BorderLayout());

        // Code
        CodeTextArea zoneCode = new CodeTextArea(Languages.MACDALANG, true);
        zoneCode.registerListener(new CodeKeyListener(new MainController(new MainView()), zoneCode));
        zoneCode.setCode(code);

        // liste de boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JButton btnRun = new RoundButton("Essayer la traduction", new Color(100, 200, 100), 10);
        btnRun.addActionListener(new TryCodeListener(this, zoneCode));
        buttonPanel.add(btnRun);


        headerPanel.add(btnLeave, "pos 0 0, h 30!");
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(mainContent, BorderLayout.CENTER);
        mainContent.add(buttonPanel, BorderLayout.NORTH);
        mainContent.add(zoneCode, BorderLayout.CENTER);

    }
}
