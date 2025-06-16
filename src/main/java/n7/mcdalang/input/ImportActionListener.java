package n7.mcdalang.input;

import n7.mcdalang.controllers.OptionsPopupController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ImportActionListener implements ActionListener {

    private final JFrame frame;
    private final JFileChooser fileChooser;
    private final FileNameExtensionFilter filter;

    private final OptionsPopupController optionsPopupController;

    public ImportActionListener(OptionsPopupController optionsPopupController) {
        this.optionsPopupController = optionsPopupController;

        // Create JFrame windows
        frame = new JFrame("File Import");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 300);

        // Create JFileChooser
        fileChooser = new JFileChooser();

        // Filter by txt
        filter = new FileNameExtensionFilter("Text file", "txt");
        fileChooser.setFileFilter(filter);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        int returnValue = fileChooser.showOpenDialog(frame);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(null, "File selected : " + selectedFile.getAbsolutePath());

            optionsPopupController.triggerChangeForOriginCode(readFile(selectedFile));

        } else {
            JOptionPane.showMessageDialog(null, "No file selected", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String readFile(File file) {
        StringBuilder content = new StringBuilder();

        // Read file
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error reading file : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return content.toString();
    }
}
