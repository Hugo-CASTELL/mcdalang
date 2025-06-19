package n7.mcdalang.views.components.mcdabot;

import n7.mcdalang.input.mcdabot.ExampleTabListener;
import n7.mcdalang.input.mcdabot.LeaveListener;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.font.Fonts;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.mcdabot.example.*;
import n7.mcdalang.views.components.util.RoundButton;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class MenuTab extends View {
    private Font font = new Font("Arial", Font.PLAIN, 12);



    public MenuTab() {
        this.setMainTutoFrame();
    }

    /*
    headerPanel
        btnLeave
    scrollPane
        bodyPanel
            options
    */

    private void setMainTutoFrame() {
        this.setLayout(new BorderLayout());


        JPanel headerPanel = new JPanel(new BorderLayout());
        // Création de l'en-tête
        PanelDialog panelDialog = new PanelDialog(
                "Bonjour ! Je suis Mc-Dala, votre guide pour vous apprendre le McDalang. " +
                        "Veuillez sélectionner une option ci-dessous pour en apprendre davantage sur la syntaxe.",
                AppConfig.MCDABOT_HEAD_PATH
        );

        JPanel topButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton btnLeave = new RoundButton("Retour", new Color(100, 200, 100), 50);
        btnLeave.addActionListener(new LeaveListener(this));
        topButtonPanel.add(btnLeave);

        headerPanel.add(topButtonPanel, BorderLayout.NORTH);
        headerPanel.add(panelDialog, BorderLayout.CENTER);

        // Création du corps code avec les options cliquables
        JPanel bodyPanel = new JPanel(new MigLayout("wrap 2, align center, insets 10 0 0 0", "[]20[]", "[]"));

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
        this.createOPtion("Exemple complet", bodyPanel,new CodeCompleteTab());


        // Ajout au panel principal
        this.add(headerPanel, BorderLayout.NORTH);
        this.add(scrollPane, BorderLayout.CENTER);


    }

    private void createOPtion(String titre, JPanel container, ExampleTab frame) {
        RoundButton option = new RoundButton(titre, new Color(180, 220, 255), 20);
        option.addActionListener(new ExampleTabListener(frame, this));
        container.add(option, "grow, h 100!, w 300!");
        Fonts font = GlobalInstances.getAppSettings().getFont();
        int size = GlobalInstances.getAppSettings().getFontSize();
        this.setFont(font, size, option);
    }

    public void setFont(Fonts font, int size, JComponent component)  {
        try{
            Font newFont = Font.createFont(Font.TRUETYPE_FONT, new File(AppConfig.FONT_ADAPTERS.get(font).toURI())).deriveFont(Font.PLAIN, size);
            component.setFont(newFont);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
