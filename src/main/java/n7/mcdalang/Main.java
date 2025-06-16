package n7.mcdalang;

import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.views.McdaBotMainView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> GlobalInstances.getAppManager().startApp());

        /*JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Traduction");
        frame.setVisible(true);

        McdaBotMainView tr = new McdaBotMainView();
        frame.add(tr, BorderLayout.CENTER);*/


    }
}