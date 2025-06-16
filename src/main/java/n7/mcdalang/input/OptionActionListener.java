package n7.mcdalang.input;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.controllers.OptionsPopupController;
import n7.mcdalang.views.OptionsPopupView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionActionListener implements ActionListener {

    private MainController mainController;

    public OptionActionListener(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        OptionsPopupController optionsPopupController = new OptionsPopupController(new OptionsPopupView(), mainController);
        int popupResult = optionsPopupController.showAsPopup();

        if (popupResult == JOptionPane.OK_OPTION) {
            mainController.enableAutoRun(optionsPopupController.hasSelectedAutoRun());
            mainController.setFont(optionsPopupController.selectFont());
            mainController.setFontSize(optionsPopupController.selectFontSize());
        }
    }
}
