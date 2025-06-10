package n7.mcdalang.models.antlr;

public enum Languages {
    MACDALANG,
    JAVA,
    ADA,
    ASSEMBLY,
    C,
    CPlusPlus,
    GO,
    PYTHON,
    RUST,
    POWERSHELL;

    @Override
    public String toString() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
