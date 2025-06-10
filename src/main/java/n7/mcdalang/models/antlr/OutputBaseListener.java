package n7.mcdalang.models.antlr;

public abstract class OutputBaseListener extends McdalangBaseListener {
    private StringBuilder output;

    // Retourne le code généré
    public String getCode(){
        return output.toString();
    };
}
