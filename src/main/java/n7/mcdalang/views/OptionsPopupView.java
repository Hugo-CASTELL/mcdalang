package n7.mcdalang.views;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class OptionsPopupView extends View {

    private JCheckBox autoRunCheckBox;

    private JLabel fontSizeLabel;
    private JSpinner fontSizeSpinner;
    private SpinnerModel fontSizeSpinnerModel;

    ButtonGroup FontGroup;
    JRadioButton FontRadioButton;

    private JButton exportButton;
    private JButton importButton;

    public OptionsPopupView() {
        setName("Options");
        setPopupOptions(JOptionPane.OK_CANCEL_OPTION);
        setLayout(new MigLayout("fill"));

        autoRunCheckBox = new JCheckBox("AutoRun");
        add(autoRunCheckBox, "cell " + (2) + " 0");

        exportButton = new JButton("Export");
        add(exportButton, "cell 0 1");

        importButton = new JButton("Import");
        add(importButton, "cell 2 1");
        fontSizeLabel = new JLabel("Font Size :");

        add(fontSizeLabel, "cell 0 0, alignx right");
        fontSizeSpinnerModel = new SpinnerNumberModel(
                12, // init value
                8,  // minimum value
                72, // maximum value
                1); // step value
        fontSizeSpinner = new JSpinner(fontSizeSpinnerModel);
        add(fontSizeSpinner, "cell 1 0, growx");
    }

    public JCheckBox getAutoRunCheckBox() {
        return autoRunCheckBox;
    }

    public JButton getExportButton() {
        return exportButton;
    }

    public JButton getImportButton() {
        return importButton;
    }

    public JSpinner getFontSizeSpinner() {
        return fontSizeSpinner;
    }

}
