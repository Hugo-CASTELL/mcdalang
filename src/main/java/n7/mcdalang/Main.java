package n7.mcdalang;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import n7.mcdalang.Views.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String[] st = new String[2];
        st[0] = "Python";
        st[1] = "Java";
        MainView m = new MainView(st, "light");

        m.setVisible(true);

        JFrame f = new JFrame("McdaLang");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(m);

        f.pack();
        f.setVisible(true);
        f.setSize(800, 600);

        m.getOriginTextArea().focusCode();
    }
}