package n7.mcdalang.util.app;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.controllers.SplashController;
import n7.mcdalang.listeners.MainFrameWindowListener;
import n7.mcdalang.listeners.EasterEggListener;
import n7.mcdalang.listeners.MainFrameWindowListener;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.SplashView;
import n7.mcdalang.views.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AppManager {

    //#region Attribute

    private JFrame mainFrame;

    //#endregion Attribute

    //#region Public Methods

    public void onStartUp() {
        // Prepare the main frame
        createMainFrame();

        // Display the splash screen
        new SplashController(new SplashView()).show();

        // Display the main view after a delay
        schedule(() -> new MainController(new MainView()).show(), AppConfig.SPLASH_DURATION_MS);

        // Initialize default settings
        if(new File(AppConfig.APP_SETTINGS_FILE).exists()) {
            GlobalInstances.getAppSettings().loadFromFile();
        } else {
            GlobalInstances.getAppSettings().initializeDefaultSettings();
        }
    }

    public void onShutDown() {
        // Save settings to file before exiting
        GlobalInstances.getAppSettings().saveToFile();

        // Close the main frame
        mainFrame.dispose();

        // Log the shutdown event
        System.exit(0);
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

    //#endregion Public Methods

    //#region Private Methods

    private void createMainFrame() {
        mainFrame = new JFrame(AppConfig.APP_TITLE);
        mainFrame.setVisible(true);
        mainFrame.setSize(AppConfig.DEFAULT_SIZE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        mainFrame.addWindowListener(new MainFrameWindowListener());
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new EasterEggListener(this));
        // Icon application
        try {
            mainFrame.setIconImage(ImageIO.read(AppConfig.MCDA_ICON));
        } catch (IOException ignored) {
            // If the icon cannot be loaded, we just ignore it
        }
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

    //#endregion Private Methods
}
