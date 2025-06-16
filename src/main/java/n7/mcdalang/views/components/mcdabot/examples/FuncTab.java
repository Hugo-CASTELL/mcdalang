package n7.mcdalang.views.components.mcdabot.examples;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class FuncTab extends ExampleTab {
    public FuncTab() {
        super();

        this.setDialogue("""
                         Déclaration des méthodes :
                         - Les paramètres sont typés et séparés par des virgules
                         - return : permet de renvoyer une valeur
                         - Utilisez “vide” comme type de retour si la méthode ne retourne rien
                         La méthode afficher() est la seule méthode de base prise en charge. Elle prend une chaîne en argument.
                         """);

        this.setCode("""
                     methode entier addition(entier a, entier b) {
                         return a + b
                     }

                     methode vide afficherColler(chaine a, chaine b) {
                         afficher(a & b)
                     }
                     """);
    }
}
