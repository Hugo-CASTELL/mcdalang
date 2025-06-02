package n7.mcdalang.Views;

import n7.mcdalang.Controllers.CodeKeyListener;

import javax.swing.*;
import java.awt.*;

public class CodeTextArea extends JPanel {

    private MainView mainView;
    private String name;
    private JTextArea codeArea;
    private JTextArea lineNumbers;
    private JLabel nameLabel;
    private JPanel contentPane;
    private JScrollPane scrollPane;

    public CodeTextArea(MainView mainView, String name, boolean editable) {
        this.mainView = mainView;
        this.name = name;

        // Add content pane
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        // Add line numbers
        lineNumbers = new JTextArea("1");
        lineNumbers.setBackground(Color.LIGHT_GRAY);
        lineNumbers.setEditable(false);

        contentPane.add(lineNumbers, BorderLayout.WEST);

        // Add code zone
        codeArea = new JTextArea();
        codeArea.addKeyListener(new CodeKeyListener(this));
        codeArea.setEditable(editable);

        // Add label
        nameLabel = new JLabel(name, SwingConstants.CENTER);

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(nameLabel, BorderLayout.CENTER);

        contentPane.add(codeArea, BorderLayout.CENTER);

        scrollPane = new JScrollPane(contentPane);
        this.setLayout(new BorderLayout());
        this.add(labelPanel, BorderLayout.NORTH); // Ajouter le panneau du label au nord
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void focusCode() {
        codeArea.requestFocusInWindow();
    }

    public String getCode() {
        return codeArea.getText();
    }

    public void setCode(String codeZone) {
        this.codeArea.setText(codeZone);
        this.updateLineNumbers();
    }

    public void triggerAutoRun() {
        mainView.triggerAutoRun();
    }

    public void updateLineNumbers() {
        int lines = codeArea.getLineCount();
        StringBuilder lineNumbersText = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            lineNumbersText.append(i).append("\n");
        }
        lineNumbers.setText(lineNumbersText.toString());
    }

    @Override
    public String getName() {
        return name;
    }

}
