package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.View;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuTab extends View {

    public MenuTab() {
        this.setMainTutoFrame();
    }

    private void setMainTutoFrame() {
        this.setLayout(new MigLayout("fill, insets 20", "[grow]", "[][grow]"));

        // Création de l'en-tête
        PanelDialog headerPanel = new PanelDialog(
                "Bonjour ! Je suis Mc-Dala, votre guide pour vous apprendre le mcdalang." +
                        "Sélectionnez une option ci-dessous pour en apprendre plus sur la syntaxe.",
                AppConfig.MCDABOT_HEAD_PATH
        );

        JButton btnLeave = new RoundButton("Retour", new Color(100, 200, 100), 50);
        btnLeave.addActionListener(new LeaveListener(this));

        headerPanel.add(btnLeave, "pos 0 0, h 30!");

        // Création du corps avec les options cliquables
        JPanel bodyPanel = new JPanel(new MigLayout("wrap, fillx", "[center]", ""));

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Ajout des cases cliquables
        this.createOPtion("Variable et types", bodyPanel,
                "explication : Variable et types",
                "||code"
        );
        this.createOPtion("Boucles", bodyPanel,
                "explication : Boucles",
                "||code"
        );
        this.createOPtion("Conditions", bodyPanel,
                "explication : Conditions",
                "||code"
        );
        this.createOPtion("Fonctions / methodes", bodyPanel,
                "Explication : Fonctions / methodes",
                "||code"
        );

        // Ajout au panel principal
        this.add(headerPanel, "grow, wrap");
        this.add(scrollPane, "grow");
    }

    private void createOPtion(String titre, JPanel container, String explication, String code) {
        RoundButton option = new RoundButton(titre, new Color(180, 220, 255), 20);
        option.addActionListener(new ExampleTabListener(explication, code, this));
        container.add(option, "grow, h 100!, w 300!");
    }
}
