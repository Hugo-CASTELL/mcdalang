import View.MainView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        String[] st = new String[2];
        st[0] = "Python";
        st[1] = "Java";
        MainView m = new MainView(st);

        m.setVisible(true);

        JFrame f = new JFrame("McdaLang");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(m);

        f.pack();
        f.setVisible(true);
        f.setSize(800, 600);

    }
}