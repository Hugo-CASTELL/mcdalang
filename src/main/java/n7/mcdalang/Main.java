package n7.mcdalang;

import n7.mcdalang.util.GlobalInstances;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> GlobalInstances.getAppManager().onStartUp());
    }
}