package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.listeners.mcdabot.ExampleTabListener;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.util.RoundButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MenuTab extends View {

    //#region Fields

    private final JButton btnLeave;
    private final JPanel bodyPanel;

    //#endregion Fields

    //#region Constructor

    public MenuTab() {
        this.setLayout(new BorderLayout());


        JPanel headerPanel = new JPanel(new BorderLayout());
        // Création de l'en-tête
        PanelDialog panelDialog = new PanelDialog(
                "Bonjour ! Je suis Mc-Dala, votre guide pour vous apprendre le McDalang. " +
                        "Veuillez sélectionner une option ci-dessous pour en apprendre davantage sur la syntaxe.",
                AppConfig.MCDABOT_HEAD_PATH
        );

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topButtonPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Ajout d'une bordure vide pour l'espacement
        btnLeave = new RoundButton("Retour", new Color(100, 200, 100), 50);
        topButtonPanel.add(btnLeave);

        headerPanel.add(topButtonPanel, BorderLayout.NORTH);
        headerPanel.add(panelDialog, BorderLayout.CENTER);

        // Création du corps code avec les options cliquables
        bodyPanel = new JPanel(new MigLayout("wrap 2, align center, insets 10 0 0 0", "[]20[]", "[]"));

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Ajout au panel principal
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    //#endregion Constructor

    //#region Getters

    public JButton getBtnLeave() {
        return btnLeave;
    }

    //#endregion Getters

    //#region Public Methods

    public void createOption(String titre, ExampleTabListener listener) {
        if(bodyPanel != null) {
            RoundButton option = new RoundButton(titre, new Color(180, 220, 255), 20);
            option.addActionListener(listener);
            bodyPanel.add(option, "grow, h 100!, w 300!");
        }
    }

    //#region Public Methods
}
