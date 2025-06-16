package n7.mcdalang.input.mcdabot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LeaveListener implements ActionListener {

    private final Runnable runnable;

    public LeaveListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(runnable);
    }
}
