package n7.mcdalang.Controllers;

import n7.mcdalang.Views.CodeTextArea;
import n7.mcdalang.Views.MainView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RunActionListener implements ActionListener {

    private MainView mainView;

    private CodeTextArea codeZone;
    private CodeTextArea[] languagesTextArea;

    public RunActionListener(MainView mainView, CodeTextArea codeZone, CodeTextArea[] languagesTextArea) {
        this.mainView = mainView;
        this.codeZone = codeZone;
        this.languagesTextArea = languagesTextArea;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        mainView.run();
    }
}
