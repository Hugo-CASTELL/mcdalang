package n7.mcdalang.listeners.mcdabot;

import n7.mcdalang.controllers.McdaBotController;
import n7.mcdalang.views.components.mcdabot.ExampleTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExampleTabListener implements ActionListener {

    //#region Fields

    private final ExampleTab frame;
    private final McdaBotController controller;

    //#endregion Fields

    //#region Constructor

    public ExampleTabListener(ExampleTab exampleTab, McdaBotController controller) {
        this.frame = exampleTab;
        this.controller = controller;
    }

    //#endregion Constructor

    //#region Listener Methods

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> controller.triggerShow(this.frame));
    }

    //#endregion Listener Methods

}
