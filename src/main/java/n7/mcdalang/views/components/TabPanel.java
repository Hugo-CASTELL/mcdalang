package n7.mcdalang.views.components;

import javax.swing.*;
import java.awt.*;

public class TabPanel extends JPanel {
    private JTabbedPane tabbedPane;

    public TabPanel(CodeTextArea[] codeTextArea) {
        // Create Tabs
        tabbedPane = new JTabbedPane(JTabbedPane.RIGHT);

        // Add CodeTextArea in Tabs
        for (CodeTextArea area : codeTextArea) {
            tabbedPane.addTab(area.getName(), area);
        }

        setLayout(new BorderLayout());
        add(tabbedPane, BorderLayout.CENTER);
    }
}
