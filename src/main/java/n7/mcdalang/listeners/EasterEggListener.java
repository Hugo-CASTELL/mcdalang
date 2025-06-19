package n7.mcdalang.input;

import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.app.AppManager;
import n7.mcdalang.util.audio.AudioPlayer;
import n7.mcdalang.views.EasterEggView;
import n7.mcdalang.views.View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class EasterEggListener implements KeyEventDispatcher {

    private int index = 0;
    private static boolean animationIsRunning = false;
    private final String keyWord = "MC-DALA";
    private final AppManager appManager;
    private final EasterEggView easterEggPanel;

    public EasterEggListener(AppManager manager) {
        this.appManager = manager;
        this.easterEggPanel = new EasterEggView();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() != KeyEvent.KEY_TYPED) {
            return false;
        }

        char typedChar = Character.toUpperCase(e.getKeyChar());

        if (typedChar == keyWord.charAt(index)) {
            index++;
            if (index == keyWord.length()) {
                this.runEasterEgg();
                index = 0;
            }
        }
        return false; // ne bloque pas la propagation
    }

    private void runEasterEgg() {
        if (!EasterEggListener.animationIsRunning) {
            View actualView = this.appManager.getMainFrameCurrentView();

            this.easterEggPanel.setBackground(actualView);
            this.appManager.display(this.easterEggPanel);

            Timer timer = new Timer(AppConfig.TOTEM_DURATION, null);
            timer.setRepeats(false);
            AudioPlayer.play(AppConfig.TOTEM_AUDIO);
            EasterEggListener.animationIsRunning = true;
            timer.addActionListener(e -> {
                View newActualView = this.appManager.getMainFrameCurrentView();
                if (newActualView == easterEggPanel) {
                    this.appManager.display(actualView);
                }
                EasterEggListener.animationIsRunning = false;
            });
            timer.start();
        }
    }
}
