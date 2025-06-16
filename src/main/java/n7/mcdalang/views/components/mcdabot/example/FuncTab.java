package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class FuncTab extends ExampleTab {
    public FuncTab() {
        super();

        this.explication = "Déclaration des méthodes :\n" +
                "- Les paramètres sont typés et séparés par des virgules\n" +
                "- return : permet de renvoyer une valeur\n" +
                "- Utilisez “vide” comme type de retour si la méthode ne retourne rien\n" +
                "La méthode afficher() est la seule méthode de base prise en charge. " +
                "Elle prend une chaîne en argument.";

        this.code = "methode entier addition(entier a, entier b) {\n" +
                "\treturn a + b\n" +
                "}\n\n" +
                "methode vide afficherColler(chaine a, chaine b) {\n" +
                "\tafficher(a & b)\n" +
                "}\n";


        this.setCode(this.code);
        this.setDialogue(this.explication);
    }
}
