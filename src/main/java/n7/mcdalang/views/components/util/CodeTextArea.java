package n7.mcdalang.views.components.util;

import n7.mcdalang.listeners.CodeKeyListener;
import n7.mcdalang.models.antlr.Languages;
import n7.mcdalang.util.GlobalInstances;
import n7.mcdalang.util.app.AppConfig;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

public class CodeTextArea extends JPanel {

    //#region Fields

    private final Languages name;
    private final JTextArea codeArea;
    private final JTextArea lineNumbers;
    private final JScrollPane scrollPane;
    private Font fontCode;

    //#endregion Fields

    //#region Constructor

    public CodeTextArea(Languages name, boolean editable) {
        this.name = name;

        // Add content pane
        JPanel contentPane = new JPanel();
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

        // Remove tab key functionality because it conflicts with the custom tab and new line handling
        InputMap inputMap = codeArea.getInputMap(JComponent.WHEN_FOCUSED);
        inputMap.put(KeyStroke.getKeyStroke("TAB"), "none");
        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "none");

        // Create font
        fontCode = this.createFont(AppConfig.FONT_ADAPTERS.get(GlobalInstances.getAppSettings().getFont()));
        setFont(fontCode);

        // Add label
        JLabel nameLabel = new JLabel(name.toString(), SwingConstants.CENTER);

        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(nameLabel, BorderLayout.CENTER);

        contentPane.add(codeArea, BorderLayout.CENTER);

        scrollPane = new JScrollPane(contentPane);

        this.setLayout(new MigLayout("", "[100%]", "[5%][95%]"));
        this.add(labelPanel, "cell 0 0,grow");
        this.add(scrollPane, "cell 0 1,grow");
    }

    //#region Constructor

    //#region Getters

    public String getCode() {
        return codeArea.getText();
    }

    @Override
    public String getName() {
        if (name == Languages.CPLUSPLUS) {
            return "C++";
        } else {
            return name.toString();
        }
    }

    public Languages getLanguage(){
        return name;
    }

    //#endregion Getters

    //#region Setters

    public void focusCode() {
        codeArea.requestFocusInWindow();
    }

    public void setCode(String code) {
        this.codeArea.setText(code);
        this.updateLineNumbers();
    }

    public void setCode(String code, Color color) {
        this.codeArea.setForeground(color);
        this.setCode(code);
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

    public void setSizeFont(int fontSize) {
        Font newFont = fontCode.deriveFont(fontCode.getStyle(), fontSize);
        codeArea.setFont(newFont);
        lineNumbers.setFont(newFont);
    }

    //#endregion Setters

    //#region Public Methods

    public void registerListener(CodeKeyListener listener) {
        codeArea.addKeyListener(listener);
    }

    public void updateLineNumbers() {
        int scrollPosition = scrollPane.getVerticalScrollBar().getValue();

        int lines = codeArea.getLineCount();
        StringBuilder lineNumbersText = new StringBuilder();
        for (int i = 1; i <= lines; i++) {
            lineNumbersText.append(i).append("\n");
        }
        lineNumbers.setText(lineNumbersText.toString());

        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPosition));
    }

    public Font createFont(URL fontUrl) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File(fontUrl.toURI())).deriveFont(Font.PLAIN, GlobalInstances.getAppSettings().getFontSize());
        } catch (Exception e) {
            Logger.getLogger(CodeTextArea.class.getName()).warning("Could not load font from URL: " + fontUrl + ". Using default font instead.");
            return new Font("Arial", Font.PLAIN, 12);
        }
    }

    public void completePreviousCharWith(char character) {
        int caretPosition = codeArea.getCaretPosition();
        String currentText = codeArea.getText();
        String newText = currentText.substring(0, caretPosition) + character + currentText.substring(caretPosition);
        codeArea.setText(newText);
        codeArea.setCaretPosition(caretPosition);
    }

    public void addTab() {
        String currentText = codeArea.getText();
        int caretPosition = codeArea.getCaretPosition();
        String newText = currentText.substring(0, caretPosition) + "    " + currentText.substring(caretPosition);
        codeArea.setText(newText);
        codeArea.setCaretPosition(caretPosition + 4);
    }

    public void addNewLine() {
        String text = codeArea.getText();
        int pos = codeArea.getCaretPosition();
        int start = text.lastIndexOf('\n', pos - 1) + 1;
        int spaces = 0; while (start + spaces < text.length() && text.charAt(start + spaces) == ' ') spaces++;
        String indent = " ".repeat(spaces);
        String extra = (pos > 0 && "{[(".indexOf(text.charAt(pos - 1)) != -1) ? "    " : "";
        String insert = "\n" + indent + extra + ((extra.isEmpty()) ? "" : "\n" + indent);
        codeArea.setText(text.substring(0, pos) + insert + text.substring(pos));
        codeArea.setCaretPosition(pos + indent.length() + 1 + (extra.isEmpty() ? 0 : 4));
    }

    //#endregion Public Methods
}
