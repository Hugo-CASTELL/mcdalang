package n7.mcdalang.views.components.mcdabot;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundButton extends JButton {
    private int arcWidth;
    private int arcHeight;
    private Color backgroundColor;

    public RoundButton(String text, Color bgColor, int radius) {
        super(text);
        this.arcWidth = radius;
        this.arcHeight = radius;
        this.backgroundColor = bgColor;

        setOpaque(false); // Important pour que le fond personnalisé soit visible
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setForeground(Color.BLACK); // Couleur du texte

        // Ajuster la taille préférée pour inclure le padding
        setPreferredSize(new Dimension(
                super.getPreferredSize().width + 20,
                super.getPreferredSize().height + 10
        ));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Dessiner le fond arrondi
        g2.setColor(backgroundColor);
        g2.fill(new RoundRectangle2D.Double(
                0, 0, getWidth(), getHeight(), arcWidth, arcHeight
        ));

        // Dessiner le texte
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optionnel: Dessiner une bordure si nécessaire
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getForeground());
        g2.draw(new RoundRectangle2D.Double(
                0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight
        ));
        g2.dispose();
    }
}
