package priponoveSubory;

import hlavny.balik.Predmet;

public interface SuborSPriponou {

    /**
     * Zisti potrebnu velkost pola
     * @param nazovSuboru subor z akeho sa ma cerpat
     * @return vrati tuto velkost
     */

    int dajMiPocetPredmetov(String nazovSuboru);

    /**
     * Metoda nacita a vytvory pole predmetov, Zohladnuje sa tu typ pripony suboru
     * @param nazovSuboru subor z akeho sa ma cerpat
     * @param velkostPola velkost aka sa mi pridelit polu
     * @return vratenie tohoto pola rpedmetov
     */
    Predmet[] nacitajPredmetyZoSubora(String nazovSuboru, int velkostPola);

    /**
     * Zisti pocet atributov pre jeden predmet
     * @param nazovSuboru subor z akeho sa ma cerpat
     * @return vrati tento pocet
     */

    int dajMiPocetAtributov(String nazovSuboru);

}
