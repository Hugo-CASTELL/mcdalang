package n7.mcdalang.models.antlr;

public abstract class OutputBaseListener extends McdalangBaseListener {
    protected StringBuilder output;

    // Retourne le code généré
    public String getCode(){
        return output.toString();
    };
}
