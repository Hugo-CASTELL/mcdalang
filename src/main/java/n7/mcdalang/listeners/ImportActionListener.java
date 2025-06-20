package n7.mcdalang.listeners;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.util.file.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ImportActionListener implements ActionListener {

    //#region Fields

    private final MainController mainController;

    //#endregion Fields

    //#region Constructor

    public ImportActionListener(MainController mainController) {
        this.mainController = mainController;
    }

    //#endregion Constructor

    //#region Listener Methods

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(new JFrame());

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                mainController.triggerChangeForOriginCode(FileUtils.readFile(fileChooser.getSelectedFile()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new Frame(), "Error while importing", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //#endregion Listener Methods

}
