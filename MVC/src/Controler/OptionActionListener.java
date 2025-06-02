package Controler;

import View.MainView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionActionListener implements ActionListener {

    MainView mainView;

    private JFrame frame;
    private JPanel panel;

    private JCheckBox[] checkBoxes;

    private boolean autoRun;

    public OptionActionListener(boolean autoRun, MainView mainView) {

        this.mainView = mainView;

        frame = new JFrame();

        panel = new JPanel();
        panel.setLayout(new GridLayout(1,3));

        checkBoxes = new JCheckBox[3];
        checkBoxes[0] = new JCheckBox("AutoRun");
        checkBoxes[1] = new JCheckBox("Debug");
        checkBoxes[2] = new JCheckBox("Help");

        for (int i = 0; i < checkBoxes.length; i++) {
            panel.add(checkBoxes[i]);
        }

        this.autoRun = autoRun;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        int result = JOptionPane.showConfirmDialog(frame, panel, "Options", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            StringBuilder selectedOptions = new StringBuilder("You have selected : ");

            for (int i = 0; i < checkBoxes.length; i++) {
                if (checkBoxes[i].isSelected()) {
                    if (selectedOptions.length() > 20) {
                        selectedOptions.append(", ");
                    }
                    selectedOptions.append(checkBoxes[i].getText());
                }
            }

            if (checkBoxes[0].isSelected()) {
                if (autoRun) {
                    autoRun = false;
                    mainView.setAutoRun(true);
                } else {
                    autoRun = true;
                    mainView.setAutoRun(true);
                }
            }

            // Afficher les options sélectionnées
            JOptionPane.showMessageDialog(frame, selectedOptions.toString());
        } else {
            JOptionPane.showMessageDialog(frame, "You have cancelled.");
        }
    }
}
