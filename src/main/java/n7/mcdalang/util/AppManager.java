package n7.mcdalang.util;

import n7.mcdalang.views.MainView;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class AppManager {

    private JFrame mainFrame;

    public void startApp() {
        createMainFrame();
        showGif();
        scheduleTask(this::showMainView, AppConfig.SPLASH_DURATION_MS);
    }

    public void createMainFrame() {
        mainFrame = new JFrame(AppConfig.APP_TITLE);
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void showGif() {
        displaySplashscreen(AppConfig.SPLASH_GIF_PATH);
        AudioPlayer.play(AppConfig.SPLASH_AUDIO_PATH);

        preventVisualGlitches();
    }

    private void showMainView() {
        AppSettings settings = GlobalInstances.getAppSettings();

        // TODO REMOVE THIS
        // Change settings.getCurrentLanguages() to a String array
        java.util.List<String> settingsLanguages = settings.getCurrentLanguages();
        String[] languagesArray = settingsLanguages.toArray(new String[0]);

        MainView m = new MainView(languagesArray, settings.getCurrentTheme());

        displayMainView(m);
        preventVisualGlitches();

        m.getOriginTextArea().focusCode();
    }

    private void displaySplashscreen(String gifPath) {
        mainFrame.setResizable(false);
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.WHITE);
        mainFrame.getContentPane().add(new JLabel(new ImageIcon(gifPath)), BorderLayout.CENTER);
    }

    private void displayMainView(MainView mainView) {
        mainFrame.setResizable(true);
        mainFrame.getContentPane().removeAll();
        mainFrame.add(mainView);
    }

    private void preventVisualGlitches() {
        mainFrame.setVisible(true);
        mainFrame.setSize(AppConfig.DEFAULT_SIZE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    public void scheduleTask(Runnable runnable, int delay) {
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
}
