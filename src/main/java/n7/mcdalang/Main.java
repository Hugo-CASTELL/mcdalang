package n7.mcdalang;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import n7.mcdalang.Views.MainView;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        String[] st = new String[4];
        st[0] = "Python";
        st[1] = "Java";
        st[2] = "C";
        st[3] = "C++";
//        st[4] = "JavaScript";
//        st[5] = "Go";
//        st[6] = "Rust";
//        st[7] = "Swift";
//        st[8] = "Kotlin";
//        st[9] = "PHP";

        MainView m = new MainView(st, "darcula");

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