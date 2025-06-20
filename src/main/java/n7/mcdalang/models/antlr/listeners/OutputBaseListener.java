package n7.mcdalang.models.antlr.listeners;

import n7.mcdalang.models.antlr.generated.McdalangBaseListener;

public abstract class OutputBaseListener extends McdalangBaseListener {
    protected StringBuilder output;

    // Retourne le code généré
    public String getCode(){
        return output.toString();
    }
}
