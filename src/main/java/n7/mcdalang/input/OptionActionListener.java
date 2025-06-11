package n7.mcdalang.input;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.views.MainView;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OptionActionListener implements ActionListener {

    private MainController mainController;

    private JFrame frame;
    private JPanel panel;

    private JCheckBox[] checkBoxes;

    private JButton exportButton;
    private JButton importButton;

    private boolean autoRun;

    public OptionActionListener(MainController mainController, boolean autoRun) {

        this.mainController = mainController;

        frame = new JFrame();

        panel = new JPanel();
        panel.setLayout(new MigLayout("fill"));

        checkBoxes = new JCheckBox[3];
        checkBoxes[0] = new JCheckBox("AutoRun");
        checkBoxes[1] = new JCheckBox("Debug");
        checkBoxes[2] = new JCheckBox("Help");

        for (int i = 0; i < checkBoxes.length; i++) {
            panel.add(checkBoxes[i], "cell " + (i) + " 0");
        }

        exportButton = new JButton("Export");
        importButton = new JButton("Import");

        panel.add(exportButton, "cell 0 1");
        panel.add(importButton, "cell 2 1");

        // exportButton.addActionListener(new ExportActionListener(mainView.getOriginTextArea(), mainView.getCodeTextArea()));
        // importButton.addActionListener(new ImportActionListener(this));

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
            if (checkBoxes[0].isSelected()) {
                autoRun = true;
                mainController.setAutoRun(true);

            } else if (!(checkBoxes[0].isSelected())) {
                autoRun = false;
                mainController.setAutoRun(false);
            }
        } else {
            //JOptionPane.showMessageDialog(frame, "You have cancelled.");
        }
    }

    public void setOriginTextArea(String code) {
        mainController.setOriginTextAreaCode(code);
    }

}
