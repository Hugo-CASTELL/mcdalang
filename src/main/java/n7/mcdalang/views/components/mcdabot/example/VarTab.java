package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class VarTab extends ExampleTab {

    public VarTab() {
        super();

        this.explication = "Variable et types\n" +
                "Mcdalang prend en charge les types suivants :\n" +
                " - nombre entier\n" +
                "- nombre à virgule\"" +
                "- caractère unique\n" +
                "- chaîne de caractères\n" +
                "- booléen (qui sont pour l'instant ignoré)\n"  +
                "- vide (uniquement pour les fonctions ne retournant rien)\n\n" +
                "Il est possible de déclarer :\n" +
                "- des variables (valeur modifiable)\n" +
                "- des constantes (valeur fixe)\n\n" +
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
