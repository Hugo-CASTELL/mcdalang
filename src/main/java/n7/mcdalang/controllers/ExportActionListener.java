package n7.mcdalang.controllers;

import n7.mcdalang.views.CodeTextArea;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

public class ExportActionListener implements ActionListener {
    private CodeTextArea originTextArea;
    private CodeTextArea[] codeTextArea;

    public ExportActionListener(CodeTextArea originTextArea, CodeTextArea[] codeTextArea) {
        this.originTextArea = originTextArea;
        this.codeTextArea = codeTextArea;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        createFile(originTextArea);

        for (CodeTextArea codeTextArea : codeTextArea) {
            createFile(codeTextArea);
        }
    }

    private void createFile(CodeTextArea codeTextArea) {
        try {
            // create a FileWriter object with the file name
            FileWriter writer = new FileWriter(codeTextArea.getName() + ".txt");

            // write the string to the file
            writer.write(codeTextArea.getCode());

            // close the writer
            writer.close();

            JOptionPane.showMessageDialog(null, "Export successfully for " + codeTextArea.getName() + " code.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Export error for " + codeTextArea.getName() + " code.");
            e.printStackTrace();
        }
    }
}
