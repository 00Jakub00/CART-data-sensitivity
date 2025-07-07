package cart;

import hlavny.balik.Predmet;
import hlavny.balik.VzorkyAtributov;
import hlavny.balik.ZmiesanyDatovyTyp;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

public class CartStrom {
    private final VzorkyAtributov vzorky;
    private List korenovyList;
    private final int minimalnyPocetVzoriekVoVetve;
    private final TypVypoctuJednoznacnosti typVypoctuJednoznacnosti;

    public CartStrom(VzorkyAtributov vzorky, int pocetPredmetovPreUcenie, TypVypoctuJednoznacnosti typVypoctuJednoznacnosti) {
        this.vzorky = vzorky;
        this.korenovyList = null;
        this.minimalnyPocetVzoriekVoVetve = (int) (Math.ceil(pocetPredmetovPreUcenie * 0.03));
        this.typVypoctuJednoznacnosti = typVypoctuJednoznacnosti;
    }

    public void vytvorCartStrom(Predmet[] aktualnePredmety, List aktualnyList) {
        String hodnota = this.trebaPokracovatAkAnoReturnNull(aktualnePredmety);
        if (hodnota != null) {
            aktualnyList.nastavListAkoPosledny(hodnota, aktualnePredmety.length);
            return;
        }

        int pocetAtributov = this.vzorky.getPocetAtributov();

        double najlepsiaJednoznacnost = 1;
        int atributRozdelenia = -1;
        ZmiesanyDatovyTyp hodnotaAtributu = null;
        ArrayList<Integer> nechceneAtributy = new ArrayList<>();
        int indexZvolenehoAtributuNaPotencionalnePouzitie = 0;

        do {
            for (int i = 0; i < pocetAtributov; i++) {
                if (this.vzorky.jeAtributNepouzivany(i) && !nechceneAtributy.contains(i)) {
                    nechceneAtributy.add(i);
                    continue;
                }
                if (nechceneAtributy.contains(i)) {
                    continue;
                }
                if (this.vzorky.jeAtributCislo(i)) {
                    this.vzorky.zoradPredmetyPodlaAtributu(i, aktualnePredmety);

                    int cisloPredmetu = -1;

                    double zaciatok = (Double)aktualnePredmety[0].dajMiAtribut(i).getDatovyTyp();
                    double koniec = this.vzorky.getSizeOfCurrentList(i);
                    int indexAtributu = 0;

                    for (double j = zaciatok; cisloPredmetu < koniec; j += (Double) this.vzorky.getVzorka(cisloPredmetu, i)) {
                        int[][] rozdelenePredmety = this.najdiPocetPredmetov(aktualnePredmety, i, j, null);
                        double jendoznacnost = this.typVypoctuJednoznacnosti == TypVypoctuJednoznacnosti.GINI_INDEX ? this.vypocitajGiniIndex(rozdelenePredmety) : this.vypocitajEntropiu(rozdelenePredmety);
                        cisloPredmetu++;
                        if (jendoznacnost < najlepsiaJednoznacnost) {
                            najlepsiaJednoznacnost = jendoznacnost;
                            atributRozdelenia = i;
                            hodnotaAtributu = new ZmiesanyDatovyTyp(j);
                            indexZvolenehoAtributuNaPotencionalnePouzitie = indexAtributu;
                        }
                        indexAtributu++;
                        if (cisloPredmetu >= koniec) {
                            break;
                        }
                    }
                } else {
                    for (int j = 0; j < this.vzorky.getSizeOfCurrentList(i); j++) {
                        int[][] rozdelenePredmety = this.najdiPocetPredmetov(aktualnePredmety, i, -1, (String) this.vzorky.getVzorka(j, i));
                        double jendoznacnost = this.typVypoctuJednoznacnosti == TypVypoctuJednoznacnosti.GINI_INDEX ? this.vypocitajGiniIndex(rozdelenePredmety) : this.vypocitajEntropiu(rozdelenePredmety);

                        if (jendoznacnost < najlepsiaJednoznacnost) {
                            najlepsiaJednoznacnost = jendoznacnost;
                            atributRozdelenia = i;
                            hodnotaAtributu = new ZmiesanyDatovyTyp((String) this.vzorky.getVzorka(j, i));
                        }
                    }
                }
            }


            if (this.majuVsetkyPredmetyRovnakuHodnotuAtributu(atributRozdelenia, aktualnePredmety)) {
                nechceneAtributy.add(atributRozdelenia);
                najlepsiaJednoznacnost = 1;
                atributRozdelenia = -1;
                hodnotaAtributu = null;
                if (this.vzorky.getPocetAtributov() == nechceneAtributy.size()) {
                    String klasifikovanaTrieda = this.vygenerujNahodnuTrieduZPonukanychPredmetov(aktualnePredmety);
                    aktualnyList.nastavListAkoPosledny(klasifikovanaTrieda, aktualnePredmety.length);
                    return;
                }
            } else {
                break;
            }
        } while (true);

        if (aktualnePredmety.length >= this.minimalnyPocetVzoriekVoVetve && this.vzorky.jeAtributCislo(atributRozdelenia) && indexZvolenehoAtributuNaPotencionalnePouzitie == 0) {
            this.vzorky.zoradPredmetyPodlaAtributu(atributRozdelenia, aktualnePredmety);
            hodnotaAtributu = new ZmiesanyDatovyTyp((Double) aktualnePredmety[(aktualnePredmety.length - 1)].dajMiAtribut(atributRozdelenia).getDatovyTyp());
        }

        Predmet[][] noveZoznamyPredmetov = this.rozdelPredmetyPodlaAtributuDoNovychPoli(atributRozdelenia, hodnotaAtributu, aktualnePredmety);

        if (this.korenovyList == null) {
            this.korenovyList = new List("Korenovy list");
            aktualnyList = this.korenovyList;
        }
        aktualnyList.nastavHodnotuAtributu(atributRozdelenia, hodnotaAtributu);

        this.vytvorCartStrom(noveZoznamyPredmetov[0], aktualnyList.vytvorADajList(true));
        this.vytvorCartStrom(noveZoznamyPredmetov[1], aktualnyList.vytvorADajList(false));
    }

    private Predmet[][] rozdelPredmetyPodlaAtributuDoNovychPoli(int atributRozdelenia, ZmiesanyDatovyTyp hodnotaAtributu, Predmet[] aktualnePredmety) {
        int velkostPolaPovodneho = aktualnePredmety.length;
        int velkostPolaA = 0;
        int velkostPolaB = 0;

        boolean jeCislo = false;
        if (this.vzorky.jeAtributCislo(atributRozdelenia)) {
            this.vzorky.zoradPredmetyPodlaAtributu(atributRozdelenia, aktualnePredmety);
            jeCislo = true;


            for (int i = 0; i < velkostPolaPovodneho; i++) {
                if ((Double) aktualnePredmety[i].dajMiAtribut(atributRozdelenia).getDatovyTyp() < (Double) hodnotaAtributu.getDatovyTyp()) {
                    velkostPolaA++;
                } else {
                    velkostPolaB++;
                }
            }
        } else {
            for (int i = 0; i < velkostPolaPovodneho; i++) {
                if (!((String) "" + aktualnePredmety[i].dajMiAtribut(atributRozdelenia).getDatovyTyp()).equals(((String) hodnotaAtributu.getDatovyTyp()))) {
                    velkostPolaA++;
                } else {
                    velkostPolaB++;
                }
            }
        }

        Predmet[][] ab = new Predmet[2][];
        ab[0] = new Predmet[velkostPolaA];
        ab[1] = new Predmet[velkostPolaB];

        int indexA = 0;
        int indexB = 0;

        for (int i = 0; i < velkostPolaPovodneho; i++) {
            if (jeCislo) {
                if ((Double) aktualnePredmety[i].dajMiAtribut(atributRozdelenia).getDatovyTyp() < (Double) hodnotaAtributu.getDatovyTyp()) {
                    ab[0][indexA] = aktualnePredmety[i];
                    indexA++;
                } else {
                    ab[1][indexB] = aktualnePredmety[i];
                    indexB++;
                }
            } else {
                if (!((String) "" + aktualnePredmety[i].dajMiAtribut(atributRozdelenia).getDatovyTyp()).equals(((String) hodnotaAtributu.getDatovyTyp()))) {
                    ab[0][indexA] = aktualnePredmety[i];
                    indexA++;
                } else {
                    ab[1][indexB] = aktualnePredmety[i];
                    indexB++;
                }
            }
        }
        return ab;
    }

    private int[][] najdiPocetPredmetov(Predmet[] aktualnePredmety, int atribut, double porovnavaciaHodnota, String hodnotaAtributu) {
        int[][] predmety = new int[2][this.vzorky.getPocetKlasifikacnychTried()];
        int pocetPredmetov = aktualnePredmety.length;

        for (int i = 0; i < pocetPredmetov; i++) {
            int index = this.vzorky.dajMiIndexKlasifikovanejTriedyVZozname(aktualnePredmety[i].dajMiKlasifikovanuTriedu());
            if (hodnotaAtributu == null) {
                if ((Double) aktualnePredmety[i].dajMiAtribut(atribut).getDatovyTyp() < porovnavaciaHodnota) {
                    predmety[0][index]++;
                } else {
                    predmety[1][index]++;
                }
            } else {
                if (!((String) "" + aktualnePredmety[i].dajMiAtribut(atribut).getDatovyTyp()).equals(hodnotaAtributu)) {
                    predmety[0][index]++;
                } else {
                    predmety[1][index]++;
                }
            }
        }
        return predmety;
    }

    private double vypocitajGiniIndex(int[][] predmety) {
        int celkovyPocetA = 0;
        int celkovyPocetB = 0;

        for (int i = 0; i < this.vzorky.getPocetKlasifikacnychTried(); i++) {
            celkovyPocetA += predmety[0][i];
            celkovyPocetB += predmety[1][i];
        }

        double celkovaJednoznacnostMensie = 1;
        double celkovaJednoznacnostVacsie = 1;

        for (int i = 0; i < this.vzorky.getPocetKlasifikacnychTried(); i++) {
            if (celkovyPocetA > 0) {
                celkovaJednoznacnostMensie -= Math.pow((double) predmety[0][i] / celkovyPocetA, 2);
            }
            if (celkovyPocetB > 0) {
                celkovaJednoznacnostVacsie -= Math.pow((double) predmety[1][i] / celkovyPocetB, 2);
            }
        }

        if (celkovyPocetA == 0) {
            return celkovaJednoznacnostVacsie;
        } else if (celkovyPocetB == 0) {
            return celkovaJednoznacnostMensie;
        } else {
            return (((double) celkovyPocetA / (celkovyPocetA + celkovyPocetB)) * celkovaJednoznacnostMensie) +
                    (((double) celkovyPocetB / (celkovyPocetA + celkovyPocetB)) * celkovaJednoznacnostVacsie);
        }
    }

    private double vypocitajEntropiu(int[][] predmety) {
        int[] predmetyA = predmety[0];
        int[] predmetyB = predmety[1];

        int celkovyPocetA = 0;
        int celkovyPocetB = 0;

        for (int i = 0; i < this.vzorky.getPocetKlasifikacnychTried(); i++) {
            celkovyPocetA += predmety[0][i];
            celkovyPocetB += predmety[1][i];
        }

        int celkovyPocetAB = celkovyPocetA + celkovyPocetB;

        double entropiaA = 0;
        double entropiaB = 0;

        for (int i = 0; i < predmety[0].length; i++) {
            if (predmetyA[i] > 0) {
                double a = (double) predmetyA[i] / celkovyPocetA;
                entropiaA += a * (Math.log(a) / Math.log(2));
            }

            if (predmetyB[i] > 0) {
                double b = (double) predmetyB[i] / celkovyPocetB;
                entropiaB += b * (Math.log(b) / Math.log(2));
            }
        }
        entropiaA = (entropiaA * -1) / (Math.log(this.vzorky.getPocetKlasifikacnychTried()) / Math.log(2));
        entropiaB = (entropiaB * -1) / (Math.log(this.vzorky.getPocetKlasifikacnychTried()) / Math.log(2));

        return (((double) celkovyPocetA / celkovyPocetAB) * entropiaA) + (((double) celkovyPocetB / celkovyPocetAB) * entropiaB);
    }

    private String trebaPokracovatAkAnoReturnNull(Predmet[] predmety) {
        if (this.jeToJednoznacne(predmety)) {
            return (String) predmety[0].dajMiKlasifikovanuTriedu().getDatovyTyp();
        }
        if (predmety.length < this.minimalnyPocetVzoriekVoVetve) {
            return this.vygenerujNahodnuTrieduZPonukanychPredmetov(predmety);
        }
        return null;
    }

    private String vygenerujNahodnuTrieduZPonukanychPredmetov(Predmet[] predmety) {
        int[] zastupenieJednotlivychTried = new int[this.vzorky.getPocetKlasifikacnychTried()];

        for (int i = 0; i < predmety.length; i++) {
            int indexTriedy = this.vzorky.dajMiIndexKlasifikovanejTriedyVZozname(predmety[i].dajMiKlasifikovanuTriedu());
            zastupenieJednotlivychTried[indexTriedy]++;
        }

        int najpocetnjesiaTrieda = Integer.MIN_VALUE;
        ArrayList<Integer> mozneTriedyPreRozhodnutie = new ArrayList<>();

        for (int i = 0; i < zastupenieJednotlivychTried.length; i++) {
            if (zastupenieJednotlivychTried[i] > najpocetnjesiaTrieda) {
                najpocetnjesiaTrieda = zastupenieJednotlivychTried[i];
                mozneTriedyPreRozhodnutie.clear();
                mozneTriedyPreRozhodnutie.add(i);
            } else if (zastupenieJednotlivychTried[i] == najpocetnjesiaTrieda) {
                mozneTriedyPreRozhodnutie.add(i);
            }
        }

        Random random = new Random();
        int vybranyIndex = mozneTriedyPreRozhodnutie.get(random.nextInt(mozneTriedyPreRozhodnutie.size()));

        return this.vzorky.dajNazovKlasifikovanejTriedyPodlaIndexu(vybranyIndex);
    }

    private boolean jeToJednoznacne(Predmet[] predmety) {
        String predmet = "";
        for (int i = 0; i < predmety.length; i++) {
            if (i == 0) {
                predmet = (String) predmety[0].dajMiKlasifikovanuTriedu().getDatovyTyp();
                continue;
            }
            if (!(predmet.equals((String) predmety[i].dajMiKlasifikovanuTriedu().getDatovyTyp()))) {
                return false;
            }
        }
        return true;
    }

    private boolean majuVsetkyPredmetyRovnakuHodnotuAtributu(int atribut, Predmet[] predmety) {
        ZmiesanyDatovyTyp a = null;
        for (int i = 0; i < predmety.length; i++) {
            if (i == 0) {
                a = predmety[0].dajMiAtribut(atribut);
                continue;
            }
            if (!a.getDatovyTyp().equals(predmety[i].dajMiAtribut(atribut).getDatovyTyp())) {
                return false;
            }
        }
        return true;
    }

    public void ukazAkoVyzeraStrom(int vynechanyAtribut) {
        try {
            String json = this.exportCelyStromAJedinyVynechany(vynechanyAtribut);

            File folder = new File("stromy");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            int index = 1;
            while (new File(folder, "strom" + index + ".json").exists()) {
                index++;
            }

            File vystupnySubor = new File(folder, "strom" + index + ".json");
            Files.write(vystupnySubor.toPath(), json.getBytes(StandardCharsets.UTF_8));

            System.out.println("Strom uložený ako: " + vystupnySubor.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String exportCelyStromAJedinyVynechany(int vynechanyAtribut) {
        String stromJson = this.exportDoJson(this.korenovyList, true);
        String celkovyJson = "{\n" +
                "  \"skippedAttributes\": [\"A č." + vynechanyAtribut + "\"],\n" +
                "  \"tree\": " + stromJson + "\n" +
                "}";
        return celkovyJson;
    }

    @SuppressWarnings("checkstyle:RequireThis")
    public String exportDoJson(List uzol, boolean jeLavySyn) {
        if (uzol.jePosledny()) {
            return "{ \"text\": \"\", \"trieda\": \"" + uzol.getTriedaPredmetu() + "\" }";
        }

        String typ = uzol.getHodnotaAtributu().getDatovyTyp().toString();
        boolean jeCislo = typ.equalsIgnoreCase("Double") || typ.equalsIgnoreCase("Integer") || typ.equalsIgnoreCase("Float");

        String znakLavo = jeCislo ? "<" : "=";
        String znakPravo = jeCislo ? ">=" : "!=";

        String hodnota = uzol.getHodnotaAtributu().getDatovyTyp().toString();  // Oprava: správna hodnota, nie datový typ
        try {
            double cislo = Double.parseDouble(hodnota);
            hodnota = String.format("%.2f", cislo);
        } catch (NumberFormatException e) {
            // nechaj text
        }

        String popis = "A č." + uzol.getPoradieAtributu() + ", hodnota: " + hodnota;

        return "{\n" +
                "  \"text\": \"" + popis + "\",\n" +
                "  \"znakLavy\": \"" + znakLavo + "\",\n" +
                "  \"znakPravy\": \"" + znakPravo + "\",\n" +
                "  \"children\": [\n" +
                this.exportDoJson(uzol.getListVlavo(), true) + ",\n" +
                this.exportDoJson(uzol.getListVpravo(), false) + "\n" +
                "  ]\n" +
                "}";
    }

    public List getKorenovyList() {
        return this.korenovyList;
    }
}
