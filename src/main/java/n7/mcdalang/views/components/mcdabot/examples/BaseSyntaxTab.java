package n7.mcdalang.views.components.mcdabot.examples;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class BaseSyntaxTab extends ExampleTab {
    public BaseSyntaxTab() {
        super();

        this.setDialogue("""
                         Les blocs sont définis entre accolades :
                         L'accolade fermante doit être seule sur sa ligne." +
                         Le contenu des blocs est indenté avec 4 espaces.
                         """);

        this.setCode("""
                     {
                         var entier x = 0
                         afficher(x)
                     }
                     """);
    }
}
