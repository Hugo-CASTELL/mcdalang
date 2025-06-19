package n7.mcdalang.listeners;

import n7.mcdalang.controllers.EasterEggController;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.app.AppManager;
import n7.mcdalang.util.timer.Scheduler;
import n7.mcdalang.views.EasterEggView;
import n7.mcdalang.views.View;

import java.awt.*;
import java.awt.event.KeyEvent;

public class EasterEggListener implements KeyEventDispatcher {

    //#region Fields

    private static final String KEYWORD = "MC-DALA";

    private int index;
    private boolean animationIsRunning;

    //#endregion Fields

    //#region Constructor

    public EasterEggListener() {
        this.index = 0;
        this.animationIsRunning = false;
    }

    //#endregion Constructor

    //#region Listener Methods

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_TYPED) {
            char typedChar = Character.toUpperCase(e.getKeyChar());

            if (typedChar == KEYWORD.charAt(index)) {
                index++;
                if (index == KEYWORD.length()) {
                    this.runEasterEgg();
                    index = 0;
                }
            }
        }

        return false; // ne bloque pas la propagation
    }

    //#endregion Listener Methods

    //#region Private Methods

    private void runEasterEgg() {
        if (!this.animationIsRunning) {
            AppManager appManager = GlobalInstances.getAppManager();
            View actualView = appManager.getMainFrameCurrentView();

            new EasterEggController(new EasterEggView(actualView)).show();
            this.animationIsRunning = true;

            Scheduler.runAfter(() -> {
                appManager.display(actualView);
                this.animationIsRunning = false;
            }, AppConfig.TOTEM_DURATION_MS);
        }
    }

    //#endregion Private Methods

}
