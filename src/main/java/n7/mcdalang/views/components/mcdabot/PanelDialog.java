package n7.mcdalang.views.components.mcdabot;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class PanelDialog extends JPanel {

    //#region Fields

    private final JTextArea dialogue;

    //#endregion Fields

    //#region Constructor

    public PanelDialog(String message, URL image) {
        super(new MigLayout("alignx center, aligny top", "[]20[]", "[grow]"));

        // Image tête fixe
        JLabel character = new JLabel(new ImageIcon(
                new ImageIcon(image).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)
        ));

        // Boîte de dialogue (style moderne avec bordure arrondie)
        dialogue = new JTextArea(message);
        dialogue.setEditable(false);
        dialogue.setLineWrap(true);
        dialogue.setWrapStyleWord(true);
        dialogue.setBackground(new Color(220, 240, 255));
        dialogue.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 220, 255), 2),
            new EmptyBorder(10, 15, 10, 15)
        ));
        dialogue.setFont(new Font("Arial", Font.PLAIN, 14));
        dialogue.setOpaque(true);

        // Scrollpane
        JScrollPane scrollPane = new JScrollPane(dialogue);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // Ajout des composants
        this.add(character, "w 120!, h 120!, top");
        this.add(scrollPane, "w 500!, growy");
    }

    //#endregion Constructor

    //#region Setters

    public void setDialogue(String message) {
        this.dialogue.setText(message);
    }

    //#region Setters

}
