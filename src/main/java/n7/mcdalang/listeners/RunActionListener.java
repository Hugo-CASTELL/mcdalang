package n7.mcdalang.listeners;

import n7.mcdalang.controllers.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunActionListener implements ActionListener {

    //#region Fields

    private final MainController mainController;

    //#endregion Fields

    //#region Constructor

    public RunActionListener(MainController mainController) {
        this.mainController = mainController;
    }

    //#endregion Constructor

    //#region Listener Methods

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainController.run();
    }

    //#endregion Listener Methods

}
