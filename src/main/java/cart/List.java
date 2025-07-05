package cart;

import hlavny.balik.ZmiesanyDatovyTyp;

public class List {
    private int poradieAtributu;
    private ZmiesanyDatovyTyp hodnotaAtributu;

    private List listVlavo;
    private List listVpravo;

    private String triedaPredmetu;
    private int pocetPredmetov;
    private String umiestenieLista;

    public List(String umiestenieLista) {
        this.listVlavo = null;
        this.listVpravo = null;
        this.triedaPredmetu = null;
        this.pocetPredmetov = -1;
        this.poradieAtributu = -1;
        this.hodnotaAtributu = null;
        this.umiestenieLista = umiestenieLista;
    }

    public boolean jePosledny() {
        return this.triedaPredmetu != null;
    }

    public ZmiesanyDatovyTyp getHodnotaAtributu() {
        return this.hodnotaAtributu;
    }

    public int getPoradieAtributu() {
        return this.poradieAtributu;
    }

    public void nastavListAkoPosledny(String triedaPredmetu, int pocetPredmetov) {
        this.triedaPredmetu = triedaPredmetu;
        this.pocetPredmetov = pocetPredmetov;
    }

    public void nastavHodnotuAtributu(int poradieAtributu, ZmiesanyDatovyTyp hodnotaAtributu) {
        this.hodnotaAtributu = hodnotaAtributu;
        this.poradieAtributu = poradieAtributu;
    }

    public String getTriedaPredmetu() {
        return this.triedaPredmetu;
    }

    public List getListVpravo() {
        return this.listVpravo;
    }

    public List getListVlavo() {
        return this.listVlavo;
    }

    public List vytvorADajList(boolean vlavo) {
        if (vlavo) {
            this.listVlavo = new List("Lavy list");
            return this.listVlavo;
        } else {
            this.listVpravo = new List("Pravy list");
            return this.listVpravo;
        }
    }

    public String getUmiestenieLista() {
        return this.umiestenieLista;
    }
}
