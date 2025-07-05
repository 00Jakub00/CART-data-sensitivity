package hlavny.balik;

import cart.DatoveTypyAtributov;
import datove.struktury.ManagedDuplicatesCollection;

import java.util.ArrayList;

public class VzorkyAtributov{
    private final ManagedDuplicatesCollection[] vzorkyAtributov;
    private final Predmet[] predmetyNaUcenie;
    private final int pocetAtributov;
    private final ArrayList<String> vzorkyFinalnychTried;
    private final ArrayList<Integer> nepouzivaneAtributy;
    private final DatoveTypyAtributov[] datoveTypyAtributov;

    public VzorkyAtributov(int pocetAtributov, Predmet[] predmetyNaUcenie, ArrayList<Integer> nepouzivaneAtributy, DatoveTypyAtributov[] datoveTypyAtributov) {
        this.datoveTypyAtributov = datoveTypyAtributov;
        this.nepouzivaneAtributy = nepouzivaneAtributy;
        this.vzorkyFinalnychTried = new ArrayList<>();
        this.pocetAtributov = pocetAtributov;
        this.vzorkyAtributov = new ManagedDuplicatesCollection[this.pocetAtributov];
        this.predmetyNaUcenie = predmetyNaUcenie;
        this.vytvorSetyPodlaPotreby();
    }

    public int dajMiIndexKlasifikovanejTriedyVZozname(ZmiesanyDatovyTyp trieda) {
        String t = (String)trieda.getDatovyTyp();
        return this.vzorkyFinalnychTried.indexOf(t);
    }

    public<T> T getVzorka(int index, int atribut) {
        try {
            return (T)this.vzorkyAtributov[atribut].get(index);
        } catch (Exception e) {
            return null;
        }
    }

    public int getSizeOfCurrentList(int index) {
        return this.vzorkyAtributov[index].size();
    }

    public void vytvorVzorky() {
        boolean jeAcislo = false;

        for (int i = 0; i <= this.pocetAtributov; i++) {
            if (this.jeAtributNepouzivany(i)) {
                continue;
            }
            if (i != this.pocetAtributov && this.vzorkyAtributov[i].getTyp().equals("Double")) {
                this.zoradPredmetyPodlaAtributu(i, this.predmetyNaUcenie);
                jeAcislo = true;
            }

            for (int j = 0; j < this.predmetyNaUcenie.length; j++) {
                if (i == this.pocetAtributov) {
                    if (!this.vzorkyFinalnychTried.contains(this.predmetyNaUcenie[j].dajMiAtribut(i).getDatovyTyp() + "")) {
                        this.vzorkyFinalnychTried.add(this.predmetyNaUcenie[j].dajMiAtribut(i).getDatovyTyp() + "");
                    }
                    continue;
                }
                if (jeAcislo) {
                    if (j == this.predmetyNaUcenie.length - 1) {
                        break;
                    }
                    double rozdiel = (Double)this.predmetyNaUcenie[j + 1].dajMiAtribut(i).getDatovyTyp() - (Double)this.predmetyNaUcenie[j].dajMiAtribut(i).getDatovyTyp();
                    if (rozdiel > 0) {
                        this.vzorkyAtributov[i].add(rozdiel);
                    }
                } else {
                   // System.out.println(this.predmetyNaUcenie[j].dajMiAtribut(i).getDatovyTyp() + "");
                  //  ZmiesanyDatovyTyp zdtString = new ZmiesanyDatovyTyp(this.predmetyNaUcenie[j].dajMiAtribut(i).getDatovyTyp());

                    String typ = "" + this.predmetyNaUcenie[j].dajMiAtribut(i).getDatovyTyp();
                    this.vzorkyAtributov[i].add(typ);
                }
            }
            jeAcislo = false;
        }
    }


    private <T> boolean jeAtributCislo (T atribut) {
        try {
            String a = atribut + "";
            Double.parseDouble(a);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean jeAtributCislo (int index) {
       // System.out.println("Index je: " + index);
        return this.vzorkyAtributov[index].getTyp().equals("Double");
    }


    public void zoradPredmetyPodlaAtributu(int atribut, Predmet[] predmety) {
        if (this.jeAtributNepouzivany(atribut)) {
            throw new RuntimeException();
        }
        for (int i = 1; i < predmety.length; i++) {
            for (int j = i; j > 0; j--) {
               // System.out.println("1." + predmety[j].dajMiAtribut(atribut).getDatovyTyp() + " 2." + predmety[j - 1].dajMiAtribut(atribut).getDatovyTyp());
              //  System.out.println(atribut);
                if ((Double)predmety[j].dajMiAtribut(atribut).getDatovyTyp() < (Double)predmety[j - 1].dajMiAtribut(atribut).getDatovyTyp()) {
                    this.vymen(predmety, j, j - 1);
                }
            }
        }
    }

    private void vymen(Predmet[] predmety, int j, int i) {
        Predmet predmet = predmety[i];
        predmety[i] = predmety[j];
        predmety[j] = predmet;
    }

    private void vytvorSetyPodlaPotreby() {
        if (this.datoveTypyAtributov != null) {
            for (int i = 0; i < this.datoveTypyAtributov.length; i++) {
                if (this.jeAtributNepouzivany(i)) {
                    this.vzorkyAtributov[i] = null;
                } else {
                    this.vzorkyAtributov[i] = this.datoveTypyAtributov[i] == DatoveTypyAtributov.CISLO ? new ManagedDuplicatesCollection<Double>(false, "Double") :
                            new ManagedDuplicatesCollection<String>(true, "String");
                }
            }
            return;
        }

        for (int i = 0; i < this.pocetAtributov; i++) {
            if (this.jeAtributCislo(this.predmetyNaUcenie[0].dajMiAtribut(i).getDatovyTyp())) {
                this.vzorkyAtributov[i] = this.jeAtributNepouzivany(i) ? null : new ManagedDuplicatesCollection<Double>(false, "Double");
            } else {
                this.vzorkyAtributov[i] = this.jeAtributNepouzivany(i) ? null : new ManagedDuplicatesCollection<String>(true, "String");
            }
        }
    }

    public void ukazMiVsetkyHodnotyAtributov() {
        for (int i = 0; i < this.pocetAtributov; i++) {
            if (this.jeAtributNepouzivany(i)) {
                continue;
            }
            for (Object o : this.vzorkyAtributov[i]) {
                System.out.print(o + ", ");
            }
            System.out.println();
        }
    }

    public void ukazMiKlasifikacneTriedy() {
        for (String trieda : this.vzorkyFinalnychTried) {
            System.out.print(trieda + ", ");
        }
        System.out.println();
    }

    public int getPocetAtributov() {
        return this.pocetAtributov;
    }

    public int getPocetKlasifikacnychTried() {
        return this.vzorkyFinalnychTried.size();
    }

    public boolean jeAtributNepouzivany(int atribut) {
        return this.nepouzivaneAtributy.contains(atribut);
    }

    public String dajNazovKlasifikovanejTriedyPodlaIndexu(int index) {
        return this.vzorkyFinalnychTried.get(index);
    }


    public String[] getKlasifikacneTriedy() {
        String[] triedy = new String[this.vzorkyFinalnychTried.size()];

        for (int i = 0; i < this.vzorkyFinalnychTried.size(); i++) {
            triedy[i] = this.vzorkyFinalnychTried.get(i);
        }

        return triedy;
    }
}
