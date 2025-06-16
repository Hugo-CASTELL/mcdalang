package n7.mcdalang.input;

import n7.mcdalang.controllers.OptionsPopupController;
import n7.mcdalang.util.file.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ImportActionListener implements ActionListener {

    private final OptionsPopupController optionsPopupController;

    public ImportActionListener(OptionsPopupController optionsPopupController) {
        this.optionsPopupController = optionsPopupController;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(new JFrame());

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            try {
                optionsPopupController.triggerChangeForOriginCode(FileUtils.readFile(fileChooser.getSelectedFile()));
            } catch (IOException e) {
                JOptionPane.showMessageDialog(new Frame(), "Error while importing", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
