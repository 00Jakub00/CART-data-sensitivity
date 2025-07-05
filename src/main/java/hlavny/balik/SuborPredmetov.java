package hlavny.balik;
import cart.CartStrom;
import cart.DatoveTypyAtributov;
import cart.List;
import cart.TypVypoctuJednoznacnosti;
import priponoveSubory.*;

import java.util.ArrayList;

public class SuborPredmetov {
    private final Predmet[] predmety;

    private Predmet[] predmetyPreUcenie;
    private Predmet[] predmetyPreTestovanie;

    private final SuborSPriponou triedaPripony;
    private final Subor subor;
    private final int pocetPredmetov;
    private VzorkyAtributov vzorkyAtributov;
    private CartStrom cartStrom;
    private final ArrayList<Integer> nepouzivaneAtributy;
    private TypVypoctuJednoznacnosti typVypoctuJednoznacnosti;
    private DatoveTypyAtributov[] datoveTypyAtributov;

    public SuborPredmetov(ArrayList<Integer> nepouzivaneAtributy, TypVypoctuJednoznacnosti typVypoctuJednoznacnosti) {
        this.subor = new Subor();
        this.triedaPripony = this.subor.dajMiTrieduPripony();
        this.pocetPredmetov = this.triedaPripony.dajMiPocetPredmetov("src/main/java/sety/" + this.subor.getNazovTreningoveHoSetu());
        this.predmety = this.triedaPripony.nacitajPredmetyZoSubora("src/main/java/sety/" + this.subor.getNazovTreningoveHoSetu(),  this.pocetPredmetov);
        this.typVypoctuJednoznacnosti = typVypoctuJednoznacnosti;
        this.nepouzivaneAtributy = nepouzivaneAtributy;
        this.datoveTypyAtributov = null;

        SpracovaniePredmetov.zamiesajPredmety(this.predmety);

        //this.zacat();
        //this.vygenerujPredmetyNaUcenieANaTestovanie();
        //this.vzorkyAtributov = new VzorkyAtributov(this.triedaPripony.dajMiPocetAtributov("src/main/java/sety/" + this.subor.getNazovTreningoveHoSetu()), this.predmetyPreUcenie, nepouzivaneAtributy);
        // this.vypisVsetkyPredmety();
        //this.vzorkyAtributov.vytvorVzorky();

       // this.vypisVsetkyPredmety();
       // this.vzorkyAtributov.ukazMiKlasifikacneTriedy();
        // this.vzorkyAtributov.ukazMiVsetkyHodnotyAtributov();

        //this.cartStrom = new CartStrom(this.vzorkyAtributov, this.predmetyPreUcenie.length);
        //this.cartStrom.vytvorCartStrom(this.predmetyPreUcenie, null);
        //this.cartStrom.ukazAkoVyzeraStrom();
    }

    public void nastavDatoveTypy(DatoveTypyAtributov[] dt) {
        this.datoveTypyAtributov = dt;
    }

    public int getPocetAtributov() {
        return this.triedaPripony.dajMiPocetAtributov("src/main/java/sety/" + this.subor.getNazovTreningoveHoSetu());
    }

    public void nastavNepouzivanyAtribut(int atribut) {
        this.nepouzivaneAtributy.clear();
        if (atribut > -1) {
            this.nepouzivaneAtributy.add(atribut);
        }
    }

    public String[] getFinalneTriedy() {
        return this.vzorkyAtributov.getKlasifikacneTriedy();
    }

    public void zacat() {
        this.vygenerujPredmetyNaUcenieANaTestovanie();
        this.vzorkyAtributov = new VzorkyAtributov(this.triedaPripony.dajMiPocetAtributov("src/main/java/sety/" + this.subor.getNazovTreningoveHoSetu()), this.predmetyPreUcenie, this.nepouzivaneAtributy, this.datoveTypyAtributov);
        this.vzorkyAtributov.vytvorVzorky();
        this.cartStrom = new CartStrom(this.vzorkyAtributov, this.predmetyPreUcenie.length, this.typVypoctuJednoznacnosti);
        this.cartStrom.vytvorCartStrom(this.predmetyPreUcenie, null);
    }

    private void vygenerujPredmetyNaUcenieANaTestovanie() {
        Predmet[][] p = SpracovaniePredmetov.vygenerujPredmetyNaUcenieANaTestovanie(this.predmety);

        this.predmetyPreUcenie = p[0];
        this.predmetyPreTestovanie = p[1];
    }

    public void vypisVsetkyPredmety() {
        for (Predmet predmet : this.predmety) {
            System.out.println(predmet.dajMiReprezentaciuPredmetu());
        }
    }

    public void ukazAkoVyzeraStrom(int vynechanyAtribut) {
        this.cartStrom.ukazAkoVyzeraStrom(vynechanyAtribut);
    }

    public VzorkyAtributov getVzorkyAtributov() {
        return this.vzorkyAtributov;
    }

    public Predmet[] getPredmetyPreTestovanie() {
        return this.predmetyPreTestovanie;
    }

    public List getKorenovyListZcartStromu() {
        return this.cartStrom.getKorenovyList();
    }
}
