package n7.mcdalang.views.components.main;

import n7.mcdalang.views.components.util.CodeTextArea;

import javax.swing.*;
import java.awt.*;

public class TabPanel extends JPanel {

    //#region Constructor

    public TabPanel(CodeTextArea[] codeTextArea) {
        // Create Tabs
        JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.RIGHT);

        // Add CodeTextArea in Tabs
        for (CodeTextArea area : codeTextArea) {
            tabbedPane.addTab(area.getName(), area);
        }

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }

    //#endregion Constructor

}
