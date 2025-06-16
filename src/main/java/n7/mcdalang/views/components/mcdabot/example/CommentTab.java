package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class CommentTab extends ExampleTab {
    public CommentTab() {
        super();

        this.explication = "Commentaire :\n" +
                "Il existe deux types de commentaires :\n" +
                " - Les commentaires sur une ligne\n" +
                " - Les commentaires multilignes" +
                "Les commentaires sont pour l'instant ignorés par le traducteur.";

        this.code = "|| Ceci est un commentaire sur une ligne\n\n" +
                "/** \n" +
                "Ceci est un \n" +
                "commentaire multiligne\n" +
                "*/\n";

        this.setCode(this.code);
        this.setDialogue(this.explication);
    }
}
