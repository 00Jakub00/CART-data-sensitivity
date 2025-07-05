package hlavny.balik;

public class Predmet {
    private final ZmiesanyDatovyTyp[] vstupneAtributy;
    private ZmiesanyDatovyTyp klasifikovanaTrieda;
    private int pocetAtributov;

    public Predmet(ZmiesanyDatovyTyp[] atributy, ZmiesanyDatovyTyp triedaKlasifikacie) {
        this.klasifikovanaTrieda = triedaKlasifikacie;
        this.vstupneAtributy = atributy;
        this.pocetAtributov = this.vstupneAtributy.length;
    }

    public ZmiesanyDatovyTyp dajMiAtribut(int indexAtributu) {
        if (indexAtributu >= this.vstupneAtributy.length) {
            return this.klasifikovanaTrieda;
        }
        return this.vstupneAtributy[indexAtributu];
    }

    public ZmiesanyDatovyTyp dajMiKlasifikovanuTriedu() {
        return this.klasifikovanaTrieda;
    }

    /**
     * Metoda vypise dany predmet s jeho vsetkymi Atributmi
     * @return textova reprezentacia
     */

    public String dajMiReprezentaciuPredmetu() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.pocetAtributov; i++) {
            sb.append(this.vstupneAtributy[i].getDatovyTyp() + " ");
        }
        sb.append(this.klasifikovanaTrieda.getDatovyTyp() + "");

        return sb.toString();
    }
}
