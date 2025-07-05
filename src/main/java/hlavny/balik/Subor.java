package hlavny.balik;

import priponoveSubory.*;

import java.io.File;

public class Subor {
    private File treningovySet;
    private File testovaciSet;
    private TypSuboru typSuboru;

    public String getNazovTreningoveHoSetu() {
        return this.treningovySet.getName();
    }

    public Subor() {
        this.typSuboru = this.dajMiTypSuboru();
        File[] sety = this.dajMiTrenovaciATestovaciSet();

        this.treningovySet = sety[0];
        this.testovaciSet = sety[1];
    }

    /**
     * Metoda zistí s akým typom súboru pracujeme a následne nám ho
     * poskytne
     * @return TypSuboru
     */

    private TypSuboru dajMiTypSuboru() {
        File folder = new File("src/main/java/sety");

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                String nazovSuboru = files[0].getName();
                StringBuilder sb = new StringBuilder();

                for (int j = nazovSuboru.length() - 1; j >= 0 && nazovSuboru.charAt(j) != '.'; j--) {
                    sb.append(nazovSuboru.charAt(j));
                }

                sb.reverse();
                return TypSuboru.dajMiSuborPodlaPripony(sb.toString());
            }
        }
        return null;
    }

    /**
     * Vyberie z priečinka src/sety 2 subory, s ktorymi bude
     * program nasledne pracovat. Treningovy set na vytvorenie stromu
     * a testovaci set na otestovanie predikcnej schopnosti stromu.
     * @return Files[]
     */

    private File[] dajMiTrenovaciATestovaciSet() {
        File folder = new File("src/main/java/sety");
        File[] sety = new File[2];

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    String nazovSuboru = files[i].getName();

                    if (i == 0 && nazovSuboru.contains("testovaci_set")) {
                        sety[i + 1] = files[i];
                    } else if (i == 1 && !nazovSuboru.contains("testovaci_set")) {
                        sety[i - 1] = files[i];
                    } else {
                        sety[i] = files[i];
                    }
                }
            }
            return sety;
        }
        return null;
    }

    public SuborSPriponou dajMiTrieduPripony() {
        switch (this.typSuboru) {
            case CSV -> {
                return new CSV();
            }
            case TXT, DATA, ARFF -> {
                return new TXT();
            }
            case XLSX -> {
                return new XLSX();
            }
        }
        return null;
    }
}





