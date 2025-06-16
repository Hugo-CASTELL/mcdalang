package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class VarTab extends ExampleTab {

    public VarTab() {
        super();

        this.explication = "Variable et types\n" +
                "Mcdalang prend en charge les types suivants :\n" +
                "nombre entier, nombre à virgule, caractère unique, " +
                "chaîne de caractères, booléen, et vide " +
                "(uniquement pour les fonctions ne retournant rien)\n" +
                "Il est possible de déclarer des variables (valeur modifiable)" +
                " ou des constantes (valeur fixe)\n" +
                "L’affectation (=) est optionnelle lors de la déclaration.";

        this.code = "var entier a\n" +
            "var flottant PI = 3.1415926\n" +
            "var char c = 'a'\n" +
            "var chaine d = \"Bonjour\"\n" +
            "var bool e = false\n";


        this.setCode(this.code);
        this.setDialogue(this.explication);
    }
}
