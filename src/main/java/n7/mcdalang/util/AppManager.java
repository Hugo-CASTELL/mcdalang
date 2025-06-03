package n7.mcdalang.util;

import n7.mcdalang.Views.MainView;

import javax.swing.*;
import java.awt.*;

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
        Image gif = loadAndRescaleGif(0.9);

        displaySplashscreen(gif);

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

    private void displaySplashscreen(Image gif) {
        mainFrame.setResizable(false);
        mainFrame.getContentPane().setLayout(new BorderLayout());
        mainFrame.getContentPane().setBackground(Color.WHITE);
        mainFrame.getContentPane().add(new JLabel(new ImageIcon(gif)), BorderLayout.CENTER);
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

    private Image loadAndRescaleGif(double scaleFactor) {
        ImageIcon originalGif = new ImageIcon(AppConfig.SPLASH_GIF_PATH);
        return originalGif.getImage().getScaledInstance((int) (AppConfig.DEFAULT_SIZE.width * scaleFactor), (int) (AppConfig.DEFAULT_SIZE.height * scaleFactor), Image.SCALE_DEFAULT);
    }

    public void scheduleTask(Runnable runnable, int delay) {
        new Timer(delay, e -> runnable.run()).start();
    }
}
