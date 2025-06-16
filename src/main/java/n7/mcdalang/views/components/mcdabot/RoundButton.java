package n7.mcdalang.views.components.mcdabot;

import javax.swing.*;
import java.awt.*;

public class RoundButton extends JButton {

    public RoundButton(String text, Color bgColor, int radius) {
        super(text);
        this.setOpaque(false);
        this.setBackground(bgColor);
        this.setBorder(new RoundedBorder(radius));
        this.setFont(new Font("Arial", Font.BOLD, 14));
        this.setForeground(Color.DARK_GRAY);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBorder() instanceof RoundedBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        }
        super.paintComponent(g);
    }
}
