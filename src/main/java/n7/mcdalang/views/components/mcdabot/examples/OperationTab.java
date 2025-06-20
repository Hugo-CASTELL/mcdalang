package n7.mcdalang.views.components.mcdabot.examples;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class OperationTab extends ExampleTab {
    public OperationTab() {
        super();

        this.setDialogue("""
                         Opérations arithmétiques :
                         addition (+), soustraction (-), multiplication (*),
                         division flottante (/), division entière (//), modulo (%), puissance (^)

                         Comparaisons :
                         égal à (==), différent de (!=),
                         inférieur strict (<), inférieur ou égal (<=),
                         supérieur strict (>), supérieur ou égal (>=),
                         le et (AND), le ou (OU), le non (NOT)

                         Chaînes de caractères :
                         concaténation de chaînes (&)
                         """);

        this.setCode("""
                     var entier x = ((1 + 2 * (3 - 1)) ^ 4 / 5) % 6

                     const flottant pi = 3.141592
                     const flottant n = 1.23456789
                     var entier y = x // pi

                     var bool test = NOT (y < x OR y == x)
                     var chaine msg = "hello " & "world " & "!"
                     """);
    }
}