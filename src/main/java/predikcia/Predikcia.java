package predikcia;

import cart.List;
import cart.Matica;
import hlavny.balik.Predmet;
import hlavny.balik.VzorkyAtributov;

public class Predikcia {

    private final List korenovyList;
    private final VzorkyAtributov vzorky;
    private final Matica confusionMatrix;

    public Predikcia(List korenovyList, VzorkyAtributov vzorky) {
        this.korenovyList = korenovyList;
        this.vzorky = vzorky;
        this.confusionMatrix = new Matica(this.vzorky.getKlasifikacneTriedy());
    }

    public void predikuj(Predmet[] predmety) {
        for (Predmet predmet : predmety) {
            this.predikuj(predmet);
        }
    }

    private void predikuj(Predmet predmet) {
        List aktualnyList = this.korenovyList;
        while (!aktualnyList.jePosledny()) {
           // this.vzorky.jeAtributCislo(aktualnyList.getHodnotaAtributu().getDatovyTyp())
            if (this.vzorky.jeAtributCislo(aktualnyList.getPoradieAtributu())) {
                if ((Double)predmet.dajMiAtribut(aktualnyList.getPoradieAtributu()).getDatovyTyp() < (Double)aktualnyList.getHodnotaAtributu().getDatovyTyp()) {
                    aktualnyList = aktualnyList.getListVlavo();
                } else {
                    aktualnyList = aktualnyList.getListVpravo();
                }
            } else {
                String a = ((String) "" + predmet.dajMiAtribut(aktualnyList.getPoradieAtributu()).getDatovyTyp());
                String b = ((String) "" + aktualnyList.getHodnotaAtributu().getDatovyTyp());
                if (!(((String) "" + predmet.dajMiAtribut(aktualnyList.getPoradieAtributu()).getDatovyTyp()).equals((String)aktualnyList.getHodnotaAtributu().getDatovyTyp()))) {
                    aktualnyList = aktualnyList.getListVlavo();
                } else {
                    aktualnyList = aktualnyList.getListVpravo();
                }
            }
        }
       // System.out.println("Predikovaná trieda predmetu je: " + aktualnyList.getTriedaPredmetu()  + " a reálna je: " + predmet.dajMiKlasifikovanuTriedu().getDatovyTyp());
        this.confusionMatrix.inkrementujDanuPredikovanuTriedu(aktualnyList.getTriedaPredmetu(), (String)predmet.dajMiKlasifikovanuTriedu().getDatovyTyp());
    }

    public void vypisVyslednuMaticu() {
        this.confusionMatrix.vypisConfusionMatrix();
    }

    public void vytvorStatistiky() {
        this.confusionMatrix.vytvorStatistiky();
    }

    public void vypisStatistiky() {
        this.confusionMatrix.vypisStatistiky();
    }


    public Matica getConfusionMatrix() {
        return this.confusionMatrix;
    }
}
