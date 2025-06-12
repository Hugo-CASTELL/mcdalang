package n7.mcdalang.util.app;

import n7.mcdalang.controllers.Controller;
import n7.mcdalang.controllers.MainController;
import n7.mcdalang.controllers.SplashController;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.SplashView;
import n7.mcdalang.views.View;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class AppManager {

    private JFrame mainFrame;

    public void startApp() {
        // Prepare the main frame
        createMainFrame();

        // Display the splash screen
        new SplashController(new SplashView()).show();

        // Display the main view after a delay
        schedule(() -> new MainController(new MainView()).show(), AppConfig.SPLASH_DURATION_MS);

        // Initialize default settings
        GlobalInstances.getAppSettings().initializeDefaultSettings();
    }

    private void createMainFrame() {
        mainFrame = new JFrame(AppConfig.APP_TITLE);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setSize(AppConfig.DEFAULT_SIZE);
        mainFrame.setLocationRelativeTo(null);
    }

    private void schedule(Runnable runnable, int delay) {
        new Timer().schedule(
            new TimerTask() {
                @Override
                public void run() {
                    runnable.run();
                }
            },
            delay
        );
    }

    public void display(View view) {
        mainFrame.getContentPane().removeAll();
        mainFrame.add(view);
        mainFrame.setVisible(true);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public int displayPopup(View view) {
        return JOptionPane.showConfirmDialog(new Frame(), view, view.getName(), view.getPopupOptions(), JOptionPane.PLAIN_MESSAGE);
    }

    public View getMainFrameCurrentView() {
        return (View) mainFrame.getContentPane().getComponent(0);
    }
}
