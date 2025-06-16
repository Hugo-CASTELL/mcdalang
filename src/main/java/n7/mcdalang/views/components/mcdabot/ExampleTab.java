package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.input.CodeKeyListener;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.components.main.CodeTextArea;
import n7.mcdalang.models.antlr.Languages;

import javax.swing.*;
import java.awt.*;

public class ExampleTab extends JPanel {
    public ExampleTab(String explication, String code) {
        super(new BorderLayout());
                CodeTextArea zoneCode = new CodeTextArea(Languages.MACDALANG, true);

        // met a jour les numeros de lignes
        zoneCode.registerListener(new CodeKeyListener(new MainController(new MainView()), zoneCode));
        zoneCode.setCode(code);

        JPanel headerPanel = new PanelDialog(explication,
                AppConfig.MCDABOT_HEAD_PATH
        );

        // Ajout du bouton retour
        JButton btnLeave = new RoundButton("Retour", Color.LIGHT_GRAY, 50);
        btnLeave.addActionListener(e -> {
            MenuTab newPanel = new MenuTab();
            McdaBotMainView mainView = (McdaBotMainView) this.getParent();
            mainView.show(newPanel);
        });
        headerPanel.add(btnLeave, "pos 0 0, h 30!");

        this.add(headerPanel, BorderLayout.NORTH);
        this.add(zoneCode, BorderLayout.CENTER);

    }
}
