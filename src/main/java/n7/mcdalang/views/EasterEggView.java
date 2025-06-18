package n7.mcdalang.views;

import n7.mcdalang.util.app.AppConfig;

import javax.swing.*;
import java.awt.*;

public class EasterEggView extends View{

    private final JLayeredPane layeredPane;
    private JLabel easterEggGif;

    public EasterEggView() {
        super();
        this.setLayout(new BorderLayout());

        this.layeredPane = new JLayeredPane();
        this.layeredPane.setLayout(new OverlayLayout(layeredPane));
        this.add(layeredPane, BorderLayout.CENTER);

        ImageIcon gifIcon = new ImageIcon(AppConfig.TOTEM_GIF);
        this.easterEggGif = new JLabel(gifIcon);
        this.easterEggGif.setAlignmentX(0.5f);
        this.easterEggGif.setAlignmentY(0.5f);
        layeredPane.add(this.easterEggGif, JLayeredPane.PALETTE_LAYER);
    }

    public void setBackground(View view) {
        for (Component comp : this.layeredPane.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER)) {
            this.layeredPane.remove(comp);
        }
        view.setAlignmentX(0.5f);
        view.setAlignmentY(0.5f);
        this.layeredPane.add(view, JLayeredPane.DEFAULT_LAYER);
        System.out.println("Nb composants dans layeredPane : " + layeredPane.getComponentCount());
    }

    public void refreshGif() {
        if (this.easterEggGif != null) {
            this.layeredPane.remove(this.easterEggGif);
        }

        this.easterEggGif = new JLabel(new ImageIcon(AppConfig.TOTEM_GIF));
        this.easterEggGif.setAlignmentX(0.5f);
        this.easterEggGif.setAlignmentY(0.5f);
        this.layeredPane.add(this.easterEggGif, JLayeredPane.PALETTE_LAYER);
        this.layeredPane.revalidate();
        this.layeredPane.repaint();
    }
}
