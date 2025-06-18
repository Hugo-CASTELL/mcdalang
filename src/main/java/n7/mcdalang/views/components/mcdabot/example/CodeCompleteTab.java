package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class CodeCompleteTab extends ExampleTab {
    public CodeCompleteTab() {
        super();

        this.explication = "Voici un exemple qui regroupe les principaux concept du mcdalang !";

        this.code = "|| Fonction pour calculer la moyenne de deux notes\n" +
                "methode flottant calculerMoyenne(entier note1, entier note2) {\n" +
                "    return (note1 + note2) / 2.0\n" +
                "}\n" +
                "\n" +
                "|| Fonction pour déterminer si l’élève a réussi\n" +
                "methode chaine resultat(flottant moyenne) {\n" +
                "    si (moyenne >= 10) {\n" +
                "        return \"Admis\"\n" +
                "    } snsi (moyenne >= 8) {\n" +
                "        return \"Rattrapage\"\n" +
                "    } sinon {\n" +
                "        return \"Échec\"\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "|| Fonction principale\n" +
                "methode vide principal() {\n" +
                "    const entier NB_ELEVES = 3\n" +
                "    var entier i = 0\n" +
                "\n" +
                "    tantque (i < NB_ELEVES) {\n" +
                "        afficher(\"Élève numéro \" & i + 1)\n" +
                "\n" +
                "        var entier note1 = i * 3 + 8\n" +
                "        var entier note2 = i * 2 + 9\n" +
                "\n" +
                "        afficher(\"Note 1 = \" & note1)\n" +
                "        afficher(\"Note 2 = \" & note2)\n" +
                "\n" +
                "        var flottant moyenne = calculerMoyenne(note1, note2)\n" +
                "        afficher(\"Moyenne = \" & moyenne)\n" +
                "\n" +
                "        var chaine decision = resultat(moyenne)\n" +
                "        afficher(\"Résultat : \" & decision)\n" +
                "\n" +
                "        i = i + 1\n" +
                "        afficher(\"\")\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "|| Appel de la fonction principale\n" +
                "principal()\n";


        this.setCode(this.code);
        this.setDialogue(this.explication);
    }
}
