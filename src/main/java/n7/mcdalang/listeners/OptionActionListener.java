package n7.mcdalang.listeners;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.controllers.OptionsPopupController;
import n7.mcdalang.views.OptionsPopupView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionActionListener implements ActionListener {

    //#region Fields

    private final MainController mainController;

    //#endregion Fields

    //#region Constructor

    public OptionActionListener(MainController mainController) {
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
        OptionsPopupController optionsPopupController = new OptionsPopupController(new OptionsPopupView());
        int popupResult = optionsPopupController.showAsPopup();

        if (popupResult == JOptionPane.OK_OPTION) {
            mainController.enableAutoRun(optionsPopupController.hasSelectedAutoRun());
            mainController.setFont(optionsPopupController.getSelectedFont());
            mainController.setFontSize(optionsPopupController.getSelectedFontSize());
            mainController.setTheme(optionsPopupController.getSelectedTheme());
        }
    }

    //#endregion Listener Methods
}
