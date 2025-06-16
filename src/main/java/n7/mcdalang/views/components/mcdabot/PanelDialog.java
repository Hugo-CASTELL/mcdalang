package n7.mcdalang.views.components.mcdabot;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.net.URL;

public class PanelDialog extends JPanel {
    public PanelDialog(String message, URL image) {
        super(new MigLayout("insets 0, align center center", "[center]", "[center]"));

        // Personnage (simulé par une icône)
        JLabel character = new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));

        // Boîte de dialogue (style moderne avec bordure arrondie)
        JTextArea dialogue = new JTextArea(message);
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

        // Ajout des composants à l'en-tête
        this.add(character, "w 120!, h 160!");
        this.add(dialogue, "w 300!, h 120!, gapright 20");
    }

}
