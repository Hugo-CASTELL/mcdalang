package n7.mcdalang.views;

import n7.mcdalang.util.app.AppConfig;

import javax.swing.*;
import java.awt.*;

public class SplashView extends JPanel implements View {

    public SplashView() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(new JLabel(new ImageIcon(AppConfig.SPLASH_GIF)), BorderLayout.CENTER);
    }
}
