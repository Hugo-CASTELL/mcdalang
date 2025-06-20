package n7.mcdalang.models.antlr;

import java.io.Serializable;

public enum Languages implements Serializable {
    MCDALANG,
    JAVA,
    ADA,
    ASSEMBLY,
    C,
    CPLUSPLUS,
    PYTHON,
    RUST,
    POWERSHELL,
    JAVASCRIPT,
    RUBY;

    @Override
    public String toString() {
        String capitalizedName = name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
        if (capitalizedName.endsWith("plusplus")) {
            return capitalizedName.replace("plusplus", "++");
        }
        return capitalizedName;
    }
}
