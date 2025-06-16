package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.McdaBotMainView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MenuTab extends JPanel {

    public MenuTab() {
        super();
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

// Création du corps avec les options cliquables
        JPanel bodyPanel = new JPanel(new MigLayout("wrap, fillx", "[center]", ""));
        JButton btnLeave = new RoundButton("Retour", Color.LIGHT_GRAY, 50);
        btnLeave.addActionListener(e -> {
            System.out.println("Leave button clicked");
        });

// Ajout du bouton en haut à droite avec une taille réduite
        headerPanel.add(btnLeave, "pos 0 0, h 30!");

        // Création des cases cliquables
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

        // Ajout des panels au panel principal
        this.add(headerPanel, "grow, wrap");
        this.add(bodyPanel, "grow");
    }

    private void createOPtion(String titre, JPanel container, String explication, String code) {
        RoundButton option = new RoundButton(titre, new Color(180, 220, 255), 20);
        option.addActionListener(e -> {
            ExampleTab newPanel = new ExampleTab(explication, code);
            McdaBotMainView mainView = (McdaBotMainView) this.getParent();
            mainView.show(newPanel);
        });
        container.add(option, "grow, h 100!, w 300!");
    }
}
