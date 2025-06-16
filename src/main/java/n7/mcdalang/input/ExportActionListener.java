package n7.mcdalang.input;


//import n7.mcdalang.views.components.main.CodeTextArea;

import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.MainView;
import n7.mcdalang.views.components.main.CodeTextArea;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExportActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (GlobalInstances.getAppManager().getMainFrameCurrentView() instanceof MainView mainView) {
            createFile(mainView.getOriginTextArea());

            for (CodeTextArea codeTextArea : mainView.getCodeTextAreas()) {
                createFile(codeTextArea);
            }
        }
    }

    private void createFile(CodeTextArea codeTextArea) {
        try(FileWriter writer = new FileWriter(codeTextArea.getName() + ".txt")) {
            // write the string to the file
            writer.write(codeTextArea.getCode());

            JOptionPane.showMessageDialog(null, "Export successfully for " + codeTextArea.getName() + " code.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Export error for " + codeTextArea.getName() + " code.");
            Logger.getLogger(ExportActionListener.class.getName()).log(Level.SEVERE, e.getMessage());
        }
    }
}
