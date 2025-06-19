package n7.mcdalang.listeners.mcdabot;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.controllers.McdaBotController;
import n7.mcdalang.views.McdaBotMainView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class McdaBotActionListener implements ActionListener {

    //#region Fields

    private final MainController mainController;

    //#endregion Fields

    //#region Constructor

    public McdaBotActionListener(MainController mainController) {
        this.mainController = mainController;
    }

    //#endregion Constructor

    //#region Listener Methods

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> new McdaBotController(new McdaBotMainView(), mainController).show());
    }

    //#endregion Listener Methods

}
