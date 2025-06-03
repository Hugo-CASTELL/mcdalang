package n7.mcdalang.controllers;

import n7.mcdalang.views.CodeTextArea;
import n7.mcdalang.views.MainView;

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
