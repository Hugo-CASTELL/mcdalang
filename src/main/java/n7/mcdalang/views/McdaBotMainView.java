package n7.mcdalang.views;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.views.components.mcdabot.MenuTab;

import javax.swing.*;
import java.awt.*;

public class McdaBotMainView extends View {
    private JPanel menuTab;
    private MainController mainViewController;

    public McdaBotMainView() {
        this.setLayout(new BorderLayout());
        menuTab = new MenuTab();
        show(menuTab);
    }

    public JPanel getMenuTab(){
        return menuTab;
    }

    public void setMainViewController(MainController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public MainController getMainViewController() {
        return mainViewController;
    }

    public void showMainView() {
        this.mainViewController.show();
    }

    public void showMenuTab() {
        this.show(menuTab);
    }

    public void show(JPanel tab) {
        assert tab != null;
        this.removeAll();
        this.add(tab, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
}
