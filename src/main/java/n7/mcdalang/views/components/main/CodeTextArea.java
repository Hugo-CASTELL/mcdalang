package n7.mcdalang.views.components.main;

import n7.mcdalang.input.CodeKeyListener;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.app.AppConfig;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;

public class CodeTextArea extends JPanel {

    private Languages name;
    private JTextArea codeArea;
    private JTextArea lineNumbers;
    private JLabel nameLabel;
    private JPanel contentPane;
    private JScrollPane scrollPane;
    private JPanel labelPanel;
    private Font fontCode;

    public CodeTextArea(Languages name, boolean editable) {
        this.name = name;

        // Add content pane
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        // Add line numbers
        lineNumbers = new JTextArea("1");
        lineNumbers.setBackground(Color.LIGHT_GRAY);
        lineNumbers.setEditable(false);
        lineNumbers.setFocusable(false);

        contentPane.add(lineNumbers, BorderLayout.WEST);

        // Add code zone
        codeArea = new JTextArea();
        codeArea.setEditable(editable);

        // Create font
        fontCode = new Font("Arial", Font.PLAIN, 12);
        setFont(this.createFont(AppConfig.FONT_ADAPTERS.get(AppConfig.DEFAULT_FONT)));

        // Add label
        nameLabel = new JLabel(name.toString(), SwingConstants.CENTER);

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

    public void setCode(String code) {
        this.codeArea.setText(code);
        this.updateLineNumbers();
    }

    public void setCode(String code, Color color) {
        this.codeArea.setForeground(color);
        this.setCode(code);
    }

    public void updateLineNumbers() {
        int scrollPosition = scrollPane.getVerticalScrollBar().getValue();

        int lines = codeArea.getLineCount();
        StringBuilder lineNumbersText = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            lineNumbersText.append(i).append("\n");
        }
        lineNumbers.setText(lineNumbersText.toString());

        SwingUtilities.invokeLater(() -> {
            scrollPane.getVerticalScrollBar().setValue(scrollPosition);
        });
    }

    @Override
    public String getName() {
        if (name == Languages.CPlusPlus) {
            return "C++";
        } else {
            return name.toString();
        }
    }

    public Languages getLanguage(){
        return name;
    }

    public void setSizeFont(int fontSize) {
        Font newFont = fontCode.deriveFont(fontCode.getStyle(), (float) fontSize);
        codeArea.setFont(newFont);
        lineNumbers.setFont(newFont);
    }

    @Override
    public void setFont(Font fontType) {
        super.setFont(fontType);
        fontCode = fontType;
        if (codeArea != null && lineNumbers != null) {
            codeArea.setFont(fontType);
            lineNumbers.setFont(fontType);
        }
    }

    public Font createFont(URL fontUrl) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(fontUrl.toURI())).deriveFont(Font.PLAIN, 12);
        } catch (Exception e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 12);
        }
    }

    public void registerListener(CodeKeyListener listener) {
        codeArea.addKeyListener(listener);
    }

}
