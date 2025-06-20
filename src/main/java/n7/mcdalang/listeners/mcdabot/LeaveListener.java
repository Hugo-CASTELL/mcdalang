package n7.mcdalang.listeners.mcdabot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeaveListener implements ActionListener {

    //#region Fields

    private final Runnable runnable;

    //#endregion Fields

    //#region Constructor

    public LeaveListener(Runnable runnable) {
        this.runnable = runnable;
    }

    //#endregion Constructor

    //#region Listener Methods

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(runnable);
    }

    //#endregion Listener Methods

}
