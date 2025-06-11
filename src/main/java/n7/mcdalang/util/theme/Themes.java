package n7.mcdalang.util.theme;

public enum Themes {
    LIGHT,
    DARK,
    INTELLIJ,
    DARCULA;


    @Override
    public String toString() {
        return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
    }
}
