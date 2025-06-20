package n7.mcdalang.views.components.mcdabot.examples;

import n7.mcdalang.views.components.mcdabot.ExampleTab;

public class CodeCompleteTab extends ExampleTab {
    public CodeCompleteTab() {
        super();

        this.setDialogue("Voici un exemple qui regroupe les principaux concept du mcdalang !");

        this.setCode("""
                     # Fonction pour calculer la moyenne de deux notes
                     methode flottant calculerMoyenne(entier note1, entier note2) {
                         return (note1 + note2) / 2.0
                     }
                     
                     # Fonction pour déterminer si l’élève a réussi
                     methode chaine resultat(flottant moyenne) {
                         si (moyenne >= 10) {
                             return "Admis"
                         } snsi (moyenne >= 8) {
                             return "Rattrapage"
                         } sinon {
                             return "Échec"
                         }
                     }
                     
                     # Fonction principale
                     methode vide principal() {
                         const entier NB_ELEVES = 3
                         var entier i = 0
                     
                         tantque (i < NB_ELEVES) {
                             afficher("Élève numéro " & str(i + 1))
                     
                             var entier note1 = i * 3 + 8
                             var entier note2 = i * 2 + 9
                     
                             afficher("Note 1 = " & str(note1))
                             afficher("Note 2 = " & str(note2))
                     
                             var flottant moyenne = calculerMoyenne(note1, note2)
                             afficher("Moyenne = " & str(moyenne))
                     
                             var chaine decision = resultat(moyenne)
                             afficher("Résultat : " & decision)
                     
                             i = i + 1
                             afficher("")
                         }
                     }
                     
                     # Appel de la fonction principale
                     principal()
                     """);
    }
}
