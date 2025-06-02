package Controler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwitchActionListener implements ActionListener {

    private boolean autoRun;

    public SwitchActionListener(boolean autoRun) {
        this.autoRun = autoRun;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(autoRun);
    }
}
