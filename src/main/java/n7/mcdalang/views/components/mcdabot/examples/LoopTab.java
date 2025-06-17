package n7.mcdalang.views.components.mcdabot.examples;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class LoopTab extends ExampleTab {
    public LoopTab() {
        super();

        this.setDialogue("""
                         3 types de boucles sont supportés :
                         - tant que
                         - faire tant que
                         - et pour
                         """);

        this.setCode("""
                     tantque (x < 5) {
                         afficher(x)
                         x = x + 1
                     }

                     faire {
                         afficher(x)
                         x = x + 1
                     } tantque (x < 5)

                     pour (i = 0; i < 3; i = i + 1) {
                         afficher(i)
                     }
                     """);
    }
}
