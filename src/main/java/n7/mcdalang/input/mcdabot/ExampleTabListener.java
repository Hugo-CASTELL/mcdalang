package n7.mcdalang.input.mcdabot;

import n7.mcdalang.controllers.McdaBotController;
import n7.mcdalang.views.McdaBotMainView;
import n7.mcdalang.views.View;
import n7.mcdalang.views.components.mcdabot.ExampleTab;
import n7.mcdalang.views.components.mcdabot.MenuTab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ExampleTabListener implements ActionListener {
    private final ExampleTab frame;
    private final McdaBotController controller;


    public ExampleTabListener(ExampleTab exampleTab, McdaBotController controller) {
        this.frame = exampleTab;
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SwingUtilities.invokeLater(() -> controller.triggerShow(this.frame));
    }
}
