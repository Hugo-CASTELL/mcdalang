package n7.mcdalang.input;

import n7.mcdalang.controllers.MainController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mcdaBotActionListener implements ActionListener {
    private MainController mainController;

    public mcdaBotActionListener(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("hell owa!");
    }
}
