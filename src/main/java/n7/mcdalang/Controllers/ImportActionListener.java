package n7.mcdalang.Controllers;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ImportActionListener implements ActionListener {
    private JFrame frame;
    private JFileChooser fileChooser;
    private FileNameExtensionFilter filter;

    OptionActionListener optionActionListener;

    ImportActionListener(OptionActionListener optionActionListener) {
        this.optionActionListener = optionActionListener;

        // Create JFrame windows
        frame = new JFrame("File Import");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

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
            java.io.File selectedFile = fileChooser.getSelectedFile();
            JOptionPane.showMessageDialog(null, "File selected : " + selectedFile.getAbsolutePath());

            // Read file
            try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
                String line;
                StringBuilder content = new StringBuilder();
                while ((line = br.readLine()) != null) {
                    content.append(line).append("\n");
                }

                optionActionListener.setOriginTextArea(content.toString());

            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error reading file : " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "No file selected");
        }
    }
}
