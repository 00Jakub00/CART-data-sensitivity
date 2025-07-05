package hlavny.balik;

import cart.Matica;
import cart.TypVypoctuJednoznacnosti;

import predikcia.Predikcia;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) throws IOException {

        try {
            premazJsonSubory();

            ProcessBuilder builder = new ProcessBuilder("node", "server.js");
            builder.inheritIO();
            builder.start();

            ArrayList<Integer> nepouzivaneAtributy = new ArrayList<>();

            SuborPredmetov suborPredmetov = new SuborPredmetov(nepouzivaneAtributy, TypVypoctuJednoznacnosti.GINI_INDEX);
            int pocetAtributov = suborPredmetov.getPocetAtributov();
            Matica[] spriemerovaneMatice = new Matica[pocetAtributov + 1];

        /*DatoveTypyAtributov[] dt = new DatoveTypyAtributov[pocetAtributov];
        for (int i = 0; i < dt.length; i++) {
            dt[i] = DatoveTypyAtributov.RETAZEC;
        }
        suborPredmetov.nastavDatoveTypy(dt);*/


            for (int i = -1; i < pocetAtributov; i++) {
                suborPredmetov.nastavNepouzivanyAtribut(i);
                Matica[] matice = new Matica[100];

                for (int j = 0; j < 100; j++) {
                    suborPredmetov.zacat();

                    if (j == 0) {
                        suborPredmetov.ukazAkoVyzeraStrom(i);
                    }

                    Predmet[] predmetyNaTesty = suborPredmetov.getPredmetyPreTestovanie();

                    Predikcia predikcia = new Predikcia(suborPredmetov.getKorenovyListZcartStromu(), suborPredmetov.getVzorkyAtributov());
                    predikcia.predikuj(predmetyNaTesty);
                    predikcia.vytvorStatistiky();

                    matice[j] = predikcia.getConfusionMatrix();
                }

                spriemerovaneMatice[i + 1] = Matica.vytvorSpriemerovanuMaticu(matice, suborPredmetov.getFinalneTriedy(), i);
                System.out.println();
                System.out.println();
                System.out.println("Spriemerované štatistiky pre atribút: " + i);
                System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*");
                spriemerovaneMatice[i + 1].vypisStatistiky();
            }

            Matica.zoradMaticePodlaCelkovehoVykonu(spriemerovaneMatice);
            int atribut = 1;
            for (Matica m : spriemerovaneMatice) {
                System.out.println(atribut + ". najvýznamnejší atribút: " + m.getAtribut() + " s celkovým priemerom F1 skore: " + m.getCelkoveF1skore());
                atribut++;
            }

            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI("http://localhost:3000"));
            } else {
                System.err.println("Prehliadač sa nedá otvoriť automaticky. Otvorte ho ručne na http://localhost:3000");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void premazJsonSubory() {
        Path stromyDir = Paths.get("stromy");
        try {
            if (Files.exists(stromyDir)) {
                Files.walk(stromyDir)
                        .filter(path -> !path.equals(stromyDir))
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                            } catch (IOException e) {
                            }
                        });
            } else {
                Files.createDirectories(stromyDir);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}