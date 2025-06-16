package n7.mcdalang.views;

import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.util.font.Fonts;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OptionsPopupView extends View {

    private JCheckBox autoRunCheckBox;

    private JLabel fontSizeLabel;
    private JSpinner fontSizeSpinner;
    private SpinnerModel fontSizeSpinnerModel;

    private ButtonGroup fontGroup;
    private List<JRadioButton> fontRadioButtons;

    public OptionsPopupView() {
        setName("Options");
        setPopupOptions(JOptionPane.OK_CANCEL_OPTION);
        setLayout(new MigLayout("fill"));

        autoRunCheckBox = new JCheckBox("AutoRun");
        add(autoRunCheckBox, "cell " + (2) + " 0");

        fontSizeLabel = new JLabel("Font Size :");

        add(fontSizeLabel, "cell 0 0, alignx right");
        fontSizeSpinnerModel = new SpinnerNumberModel(
                12, // init value
                8,  // minimum value
                72, // maximum value
                1); // step value
        fontSizeSpinner = new JSpinner(fontSizeSpinnerModel);
        add(fontSizeSpinner, "cell 1 0, growx");

        fontGroup = new ButtonGroup();
        fontRadioButtons = new ArrayList<>();

        for (Map.Entry<Fonts, URL> entry : AppConfig.FONT_ADAPTERS.entrySet()) {
            JRadioButton radioButton = new JRadioButton(entry.getKey().toString());
            fontRadioButtons.add(radioButton);
            fontGroup.add(radioButton);
            add(radioButton, "cell " + fontRadioButtons.size() % 3 + " " + (3 + fontRadioButtons.size() / 3));
        }
    }

    public JCheckBox getAutoRunCheckBox() {
        return autoRunCheckBox;
    }

    public JSpinner getFontSizeSpinner() {
        return fontSizeSpinner;
    }

    public ButtonGroup getFontGroup() {
        return fontGroup;
    }

    public List<JRadioButton> getFontRadioButtons() {
        return fontRadioButtons;
    }

}
