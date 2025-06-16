package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.input.mcdabot.ExampleTabListener;
import n7.mcdalang.input.mcdabot.LeaveListener;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.mcdabot.example.*;
import n7.mcdalang.views.components.util.RoundButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class MenuTab extends View {

    public MenuTab() {
        this.setMainTutoFrame();
    }

    private void setMainTutoFrame() {
        this.setLayout(new MigLayout("fill, insets 20", "[grow]", "[][grow]"));

        // Création de l'en-tête
        PanelDialog headerPanel = new PanelDialog(
                "Bonjour ! Je suis Mc-Dala, votre guide pour vous apprendre le McDalang. " +
                        "Veuillez sélectionner une option ci-dessous pour en apprendre davantage sur la syntaxe.",
                AppConfig.MCDABOT_HEAD_PATH
        );

        JButton btnLeave = new RoundButton("Retour", new Color(100, 200, 100), 50);
        btnLeave.addActionListener(new LeaveListener(this));

        headerPanel.add(btnLeave, "pos 0 0, h 30!");

        // Création du corps avec les options cliquables
        JPanel bodyPanel = new JPanel(new MigLayout("wrap 2, align center, insets 0", "[]20[]", "[]"));

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        // Ajout des cases cliquables
        this.createOPtion("Variable et types\n", bodyPanel, new VarTab());
        this.createOPtion("Boucles", bodyPanel,new LoopTab());
        this.createOPtion("Conditions", bodyPanel,new ConditionTab());
        this.createOPtion("Fonctions", bodyPanel,new FuncTab());
        this.createOPtion("Blocs", bodyPanel,new BaseSyntaxTab());
        this.createOPtion("Commentaires", bodyPanel,new CommentTab());
        this.createOPtion("Operations", bodyPanel,new OperationTab());


        // Ajout au panel principal
        this.add(headerPanel, "grow, wrap");
        this.add(scrollPane, "grow");
    }

    private void createOPtion(String titre, JPanel container, ExampleTab frame) {
        RoundButton option = new RoundButton(titre, new Color(180, 220, 255), 20);
        option.addActionListener(new ExampleTabListener(frame, this));
        container.add(option, "grow, h 100!, w 300!");
    }
}
