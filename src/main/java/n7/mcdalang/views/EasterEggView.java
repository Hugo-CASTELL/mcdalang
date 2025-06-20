package n7.mcdalang.views;

import n7.mcdalang.util.app.AppConfig;

import javax.swing.*;
import java.awt.*;

public class EasterEggView extends View{

    private final JLayeredPane layeredPane;

    public EasterEggView(View backgroundView) {
        this.setLayout(new BorderLayout());

        this.layeredPane = new JLayeredPane();
        this.layeredPane.setLayout(new OverlayLayout(layeredPane));
        this.add(layeredPane, BorderLayout.CENTER);

        ImageIcon gifIcon = new ImageIcon(AppConfig.TOTEM_GIF);
        JLabel easterEggGif = new JLabel(gifIcon);
        easterEggGif.setAlignmentX(0.5f);
        easterEggGif.setAlignmentY(0.5f);
        layeredPane.add(easterEggGif, JLayeredPane.PALETTE_LAYER);

        this.setBackgroundView(backgroundView);
    }

    public void setBackgroundView(View view) {
        for (Component comp : this.layeredPane.getComponentsInLayer(JLayeredPane.DEFAULT_LAYER)) {
            this.layeredPane.remove(comp);
        }
        view.setAlignmentX(0.5f);
        view.setAlignmentY(0.5f);
        this.layeredPane.add(view, JLayeredPane.DEFAULT_LAYER);
    }
}
