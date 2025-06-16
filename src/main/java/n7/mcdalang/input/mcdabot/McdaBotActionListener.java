package n7.mcdalang.input.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.controllers.McdaBotController;
import n7.mcdalang.views.McdaBotMainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class McdaBotActionListener implements ActionListener {
    private MainController mainController;

    public McdaBotActionListener(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> new McdaBotController(new McdaBotMainView(), mainController).show());
    }
}
