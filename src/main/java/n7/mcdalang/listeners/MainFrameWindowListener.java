package n7.mcdalang.listeners;

import n7.mcdalang.util.GlobalInstances;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MainFrameWindowListener implements WindowListener {

    //#region Listener Methods

    @Override
    public void windowOpened(WindowEvent e) {
        // No action needed here
    }

    @Override
    public void windowClosing(WindowEvent e) {
        GlobalInstances.getAppManager().onShutDown();
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // No action needed here
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // No action needed here
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // No action needed here
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // No action needed here
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // No action needed here
    }

    //#endregion Listener Methods

}
