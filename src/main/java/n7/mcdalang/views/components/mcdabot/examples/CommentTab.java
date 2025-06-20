package n7.mcdalang.views.components.mcdabot.examples;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class CommentTab extends ExampleTab {
    public CommentTab() {
        super();

        this.setDialogue("""
                         Commentaire :
                         Il existe deux types de commentaires :
                          - Les commentaires sur une ligne
                          - Les commentaires multilignes
                         """);

        this.setCode("""
                     # Ceci est un commentaire sur une ligne

                     /**
                     Ceci est un
                     commentaire multiligne
                     */
                     """);
    }
}
