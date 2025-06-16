package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class BaseSyntaxTab extends ExampleTab {
    public BaseSyntaxTab() {
        super();

        this.explication = "Les blocs sont définis entre accolades :\n" +
                "L'accolade fermante doit être seule sur sa ligne." +
                "Le contenu des blocs est indenté avec 4 espaces.";

        this.code = "{\n" +
                "    var entier x = 0\n" +
                "    afficher(x)\n" +
                "}\n";


        this.setCode(this.code);
        this.setDialogue(this.explication);
    }
}
