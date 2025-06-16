package n7.mcdalang.views.components.mcdabot.examples;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class VarTab extends ExampleTab {

    public VarTab() {
        super();

        this.setDialogue("""
                         Variable et types
                         Mcdalang prend en charge les types suivants :
                         nombre entier, nombre à virgule, caractère unique, 
                         chaîne de caractères, booléen, et vide 
                         (uniquement pour les fonctions ne retournant rien)
                         Il est possible de déclarer des variables (valeur modifiable) 
                         ou des constantes (valeur fixe)
                         L’affectation (=) est optionnelle lors de la déclaration.
                         """);

        this.setCode("""
                     var entier a
                     var flottant PI = 3.1415926
                     var char c = 'a'
                     var chaine d = "Bonjour"
                     var bool e = false
                     """);
    }
}
