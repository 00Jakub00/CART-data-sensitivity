package priponoveSubory;

import hlavny.balik.Predmet;
import hlavny.balik.ZmiesanyDatovyTyp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TXT implements SuborSPriponou {
    @Override
    public int dajMiPocetPredmetov(String nazovSuboru) {
        File file = new File(nazovSuboru);
        try {
            Scanner sc = new Scanner(file);
            int pocet = 0;
            while (sc.hasNextLine()) {
                sc.nextLine();
                pocet++;
            }
            sc.close();
            return pocet;
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        return -1;
    }

    @Override
    public int dajMiPocetAtributov(String nazovSuboru) {
        File file = new File(nazovSuboru);

        try {
            Scanner sc = new Scanner(file);
            String riadok = sc.nextLine();
            sc.close();

            return riadok.split(",").length - 1;            //Vrati mi pocet atributov pre predikciu
        } catch (FileNotFoundException e) {                      //bez finalnej classy
            System.out.println(e);
        }
        return -1;
    }

    public Predmet[] nacitajPredmetyZoSubora(String nazovSuboru, int velkostPola) {
        Predmet[] predmety = new Predmet[velkostPola];
        File file = new File(nazovSuboru);
        int pocetAtributov = this.dajMiPocetAtributov(nazovSuboru);

        try {
            Scanner sc = new Scanner(file);

            String predmet = "";
            int cisloPredmetu = 0;

            while (sc.hasNextLine()) {
                predmet = sc.nextLine();
                ZmiesanyDatovyTyp[] atributy = new ZmiesanyDatovyTyp[pocetAtributov];
                int cisloA = 0;

                String p = "";

                for (int i = 0; i < predmet.length(); i++)  {
                    if (predmet.charAt(i) == ',') {
                        atributy[cisloA] = this.jeRetazecCislo(p) ? new ZmiesanyDatovyTyp(Double.parseDouble(p)) : new ZmiesanyDatovyTyp(p);
                        cisloA++;

                        p = "";
                    } else {
                        p += predmet.charAt(i);
                    }
                }

                ZmiesanyDatovyTyp triedaKlasifikacie = new ZmiesanyDatovyTyp(p);
                predmety[cisloPredmetu] = new Predmet(atributy, triedaKlasifikacie);
                cisloPredmetu++;
            }
            sc.close();
            return predmety;

        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
        return null;
    }

    private boolean jeRetazecCislo(String retazec) {
        try {
            Double.parseDouble(retazec);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
