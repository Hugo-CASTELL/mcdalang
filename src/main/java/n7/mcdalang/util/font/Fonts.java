package n7.mcdalang.util.font;

public enum Fonts {
    JETBRAINS_LIGHT,
    JETBRAINS_MEDIUM,
    JETBRAINS_BOLD,

    FIRACODE_LIGHT,
    FIRACODE_MEDIUM,
    FIRACODE_BOLD,

    CONSOLAS_LIGHT,
    CONSOLAS_BOLD;

    @Override
    public String toString() {
        String[] split = name().split("_");
        return  split[0].substring(0, 1).toUpperCase() + split[0].substring(1).toLowerCase() + " " +
                split[1].substring(0, 1).toUpperCase() + split[1].substring(1).toLowerCase();
    }
}