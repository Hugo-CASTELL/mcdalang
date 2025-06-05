package n7.mcdalang.views;

import n7.mcdalang.controllers.MainController;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class OptionsPopupView extends Popup {

    private MainController mainController;

    private JFrame frame;
    private JPanel panel;

    private JCheckBox[] checkBoxes;

    private JButton exportButton;
    private JButton importButton;

    private boolean autoRun;

    public OptionsPopupView() {

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill"));

        checkBoxes = new JCheckBox[3];

        checkBoxes[0] = new JCheckBox("Auto Run");
        checkBoxes[1] = new JCheckBox("Auto Import");
        checkBoxes[2] = new JCheckBox("Auto Export");
    }

}
