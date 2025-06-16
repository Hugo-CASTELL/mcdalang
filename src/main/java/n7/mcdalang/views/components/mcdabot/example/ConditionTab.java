package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class ConditionTab extends ExampleTab {
    public ConditionTab() {
        super();

        this.explication = "Voici les mots-clés de base pris en charge :\n" +
                "- si\n" + "- sinon\n" + "- snsi (sinon si)\n" +
                "Le switch case n'est pas pris en charge.";

        this.code = "si (x > 0) {\n" +
                "    afficher(\"x est positif\")\n" +
                "} sinon {\n" +
                "    afficher(\"x est négatif ou nul\")\n" +
                "}\n" +
                "\n" +
                "si (a > b) {\n" +
                "    afficher(\"A est supérieur à B\")\n" +
                "} snsi (a < b) {\n" +
                "    afficher(\"A est inférieur à B\")\n" +
                "} sinon {\n" +
                "    afficher(\"A et B sont égaux\")\n" +
                "}\n";



        this.setCode(this.code);
        this.setDialogue(this.explication);
    }
}
