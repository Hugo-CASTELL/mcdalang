package n7.mcdalang.util.file;

import java.io.Serial;
import java.io.Serializable;

public class Setting<V extends Serializable> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //#region Fields

    private final String key;
    private final V value;

    //#endregion Fields

    //#region Constructor

    public Setting(String key, V value) {
        this.key = key;
        this.value = value;
    }

    //#endregion Constructor

    //#region Getters

    public String getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    //#region Getters

    //#region Static Factory Method

    public static <V extends Serializable> Setting<V> of(String key, V value) {
        return new Setting<>(key, value);
    }

    //#endregion Static Factory Method
}
