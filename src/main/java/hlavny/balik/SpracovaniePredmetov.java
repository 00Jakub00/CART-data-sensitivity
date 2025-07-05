package hlavny.balik;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SpracovaniePredmetov {

    /**
     * Nahodne zamiesa vsetky predmety v poli
     * @param predmety Zoznam vsetkych predmetov
     */

    public static void zamiesajPredmety(Predmet[] predmety) {
        Random random = new Random();
        for (int i = predmety.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            Predmet p = predmety[index];
            predmety[index] = predmety[i];
            predmety[i] = p;
        }
    }

    /**
     * Metoda nahodne vybere predmety pre ucenie a predmety pre testovanie
     * Predmetou je pre ucenie je 70% z celkoveho poctu
     * Predmetov pre testovanie je 30% z celkoveho poctu
     * @param predmety Zoznam vsetky predmetov
     * @return Vracia uz rozdelene predmety do jednotlivych kategorii
     */

    public static Predmet[][] vygenerujPredmetyNaUcenieANaTestovanie(Predmet[] predmety) {
        Predmet[][] p = new Predmet[2][];

        Set<Integer> pouziteIndexyPreUcenie = new HashSet<>();
        int pocetPredmetov = predmety.length;
        int pocetPredmetovNaUcenie = (int)(pocetPredmetov * 0.7);
        Random random = new Random();

        p[0] = new Predmet[pocetPredmetovNaUcenie];
        p[1] = new Predmet[pocetPredmetov - pocetPredmetovNaUcenie];

        while (pouziteIndexyPreUcenie.size() < pocetPredmetovNaUcenie) {
            int index = random.nextInt(pocetPredmetov);
            pouziteIndexyPreUcenie.add(index);
        }

        int indexUcenie = 0;
        int indexPredikcia = 0;

        for (int i = 0; i < pocetPredmetov; i++) {
            if (pouziteIndexyPreUcenie.contains(i)) {
                p[0][indexUcenie++] = predmety[i];
            } else {
                p[1][indexPredikcia++] = predmety[i];
            }
        }
        return p;
    }
}
