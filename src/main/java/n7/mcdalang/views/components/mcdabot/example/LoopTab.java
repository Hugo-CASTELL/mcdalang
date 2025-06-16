package n7.mcdalang.views.components.mcdabot.example;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class LoopTab extends ExampleTab {
    public LoopTab() {
        super();

        this.explication = "3 types de boucles sont supportés :\n" +
                "- tant que\n" +
                "- faire tant que\n" +
                "- et pour\n";

        this.code = "tantque (x < 5) {\n" +
                "    afficher(x)\n" +
                "    x = x + 1\n" +
                "}\n" +
                "\n" +
                "faire {\n" +
                "    afficher(x)\n" +
                "    x = x + 1\n" +
                "} tantque (x < 5)\n" +
                "\n" +
                "pour (i = 0; i < 3; i = i + 1) {\n" +
                "    afficher(i)\n" +
                "}\n";


        this.setCode(this.code);
        this.setDialogue(this.explication);
    }
}
