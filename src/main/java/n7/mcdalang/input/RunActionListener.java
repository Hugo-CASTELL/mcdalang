package n7.mcdalang.input;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.views.components.CodeTextArea;
import n7.mcdalang.views.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunActionListener implements ActionListener {

    private MainController mainController;

    public RunActionListener(MainController mainController) {
        this.mainController = mainController;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainController.run();
    }
}
