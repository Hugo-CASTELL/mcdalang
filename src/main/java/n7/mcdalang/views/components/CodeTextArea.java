package n7.mcdalang.views.components;

import n7.mcdalang.input.CodeKeyListener;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class CodeTextArea extends JPanel {

    private String name;
    private JTextArea codeArea;
    private JTextArea lineNumbers;
    private JLabel nameLabel;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JPanel labelPanel;

    public CodeTextArea(String name, boolean editable) {
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
        codeArea.setEditable(editable);

        // Add label
        nameLabel = new JLabel(name, SwingConstants.CENTER);

        labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(nameLabel, BorderLayout.CENTER);

        contentPane.add(codeArea, BorderLayout.CENTER);

        scrollPane = new JScrollPane(contentPane);
        this.setLayout(new MigLayout("", "[100%]", "[5%][95%]"));
        this.add(labelPanel, "cell 0 0,grow");
        this.add(scrollPane, "cell 0 1,grow");
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

    public void registerListener(CodeKeyListener listener) {
        codeArea.addKeyListener(listener);
    }

}
