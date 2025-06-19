package n7.mcdalang.views.components.mcdabot.examples;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class ConditionTab extends ExampleTab {
    public ConditionTab() {
        super();

        this.setDialogue("""
                         Voici les mots-clés de base pris en charge :
                         - si
                         - sinon
                         - snsi (sinon si)
                         Le switch case n'est pas pris en charge.
                         """);

        this.setCode("""
                     si (x > 0) {
                         afficher("x est positif")
                     } sinon {
                         afficher("x est négatif ou nul")
                     }

                     si (a > b) {
                         afficher("A est supérieur à B")
                     } snsi (a < b) {
                         afficher("A est inférieur à B")
                     } sinon {
                         afficher("A et B sont égaux")
                     }
                     """);
    }
}
