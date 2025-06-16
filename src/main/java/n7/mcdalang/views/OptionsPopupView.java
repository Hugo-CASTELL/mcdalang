package n7.mcdalang.views;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class OptionsPopupView extends View {

    private JCheckBox autoRunCheckBox;

    private JButton exportButton;
    private JButton importButton;

    public OptionsPopupView() {
        setName("Options");
        setPopupOptions(JOptionPane.OK_CANCEL_OPTION);
        setLayout(new MigLayout("fill"));

        autoRunCheckBox = new JCheckBox("AutoRun");
        add(autoRunCheckBox, "cell " + (1) + " 0");

        exportButton = new JButton("Export");
        add(exportButton, "cell 0 1");

        importButton = new JButton("Import");
        add(importButton, "cell 2 1");
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

}
