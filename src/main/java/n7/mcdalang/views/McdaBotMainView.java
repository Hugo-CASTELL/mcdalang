package n7.mcdalang.views;

import n7.mcdalang.views.components.mcdabot.ExampleTab;
import n7.mcdalang.views.components.mcdabot.MenuTab;
import n7.mcdalang.views.components.mcdabot.PanelDialog;
import n7.mcdalang.views.components.mcdabot.RoundButton;
import net.miginfocom.swing.MigLayout;
import org.antlr.v4.runtime.misc.NotNull;

import javax.swing.*;
import java.awt.*;

public class McdaBotMainView extends JPanel implements View {
    private JPanel menuTab;
    private JPanel exampleTab;

    public McdaBotMainView() {
        super(new BorderLayout());
        menuTab = new MenuTab();
        exampleTab = null;

        show(menuTab);
    }

    public JPanel getMenuTab(){
        return menuTab;
    }

    public void show(JPanel tab) {
        assert tab != null;
        this.removeAll();
        this.add(tab, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
