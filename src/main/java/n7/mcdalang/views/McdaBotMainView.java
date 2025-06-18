package n7.mcdalang.views;

import n7.mcdalang.controllers.MainController;
import n7.mcdalang.util.app.AppConfig;
import n7.mcdalang.views.components.mcdabot.MenuTab;

import javax.swing.*;
import java.awt.*;

public class McdaBotMainView extends View {
    private JPanel menuTab;
    private MainController mainViewController;

    public McdaBotMainView() {
        this.setLayout(new BorderLayout());
        menuTab = new MenuTab();
        int random = (int)(Math.random() * 2);
        if (random == 0){
            this.showWithGif(menuTab);
        }
        else{
            this.show(menuTab);
        }

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


    public void showWithGif(JPanel tab) {
        // Créer le layeredPane avec layout pour auto-sizing
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane)); // superposition propre
        this.add(layeredPane, BorderLayout.CENTER);

        // Ajouter le panneau principal (arrière-plan)
        tab.setAlignmentX(0.5f);
        tab.setAlignmentY(0.5f);
        layeredPane.add(tab, JLayeredPane.DEFAULT_LAYER);

        // GIF
        ImageIcon gifIcon = new ImageIcon(AppConfig.TOTEM_GIF);
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setAlignmentX(0.5f); // centré horizontalement
        gifLabel.setAlignmentY(0.5f); // centré verticalement
        layeredPane.add(gifLabel, JLayeredPane.PALETTE_LAYER);

        // Timer pour retirer le layeredPane et afficher seulement tab
        new javax.swing.Timer(AppConfig.TOTEM_DURATION, e -> {
            this.remove(layeredPane);
            this.add(tab, BorderLayout.CENTER);
            this.revalidate(); // important pour forcer le layout
            this.repaint();    // pour rafraîchir l'affichage
        }).start();
    }

    public void showMainView() {
        this.mainViewController.show();
    }

    public void runTraduction() {
        this.mainViewController.run();
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
