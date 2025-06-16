package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class OperationTab extends ExampleTab {
    public OperationTab() {
        super();

        this.explication = "Opérations arithmétiques : \n" +
                "addition (+), soustraction (-), multiplication (*), " +
                "division flottante (/), division entière (//), modulo (%), puissance (^)\n\n" +
                "Comparaisons :\n" +
                "égal à (==), différent de (!=), " +
                "inférieur strict (<), inférieur ou égal (<=), " +
                "supérieur strict (>), supérieur ou égal (>=)\n" +
                "Le et, ou et non ne sont pas supportés pour l'instant\n\n" +
                "Chaînes de caractères :\n" +
                "concaténation de chaînes (&)";

        this.code = "var entier x = ((1+2*(3-1))^4/5)%6\n\n" +
                "const flottant pi = 3.141592\n" +
                "const flottant n = 1.23456789\n" +
                "var entier y = x//pi\n\n" +
                "bool test = y < x" +
                "var chaine msg = \"hello \" & \"world \" & \"!\"";



        this.setCode(this.code);
        this.setDialogue(this.explication);
    }
}
