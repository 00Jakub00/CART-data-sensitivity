package priponoveSubory;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import hlavny.balik.Predmet;
import hlavny.balik.ZmiesanyDatovyTyp;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class CSV implements SuborSPriponou {

    @Override
    public int dajMiPocetPredmetov(String nazovSuboru) {
        try (CSVReader reader = new CSVReader(new FileReader( nazovSuboru))) {
            int pocetPredmetov = reader.readAll().size();

            reader.close();
            return pocetPredmetov;
        } catch (IOException | CsvException e) {
            System.out.println(e);
            return -1;
        }
    }

    @Override
    public Predmet[] nacitajPredmetyZoSubora(String nazovSuboru, int velkostPola) {
        try {
            CSVReader reader = new CSVReaderBuilder(new FileReader(nazovSuboru))
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();

            List<String[]> vzorky = reader.readAll();
            int pocetAtributov = this.dajMiPocetAtributov(nazovSuboru);
            Predmet[] predmety = new Predmet[velkostPola];

            int cisloRecordu = 0;
            for (String[] record : vzorky) {
                ZmiesanyDatovyTyp[] atributy = new ZmiesanyDatovyTyp[pocetAtributov];

                for (int i = 0; i < record.length - 1; i++) {
                    atributy[i] = this.jeRetazecCislo(record[i])
                            ? new ZmiesanyDatovyTyp(Double.parseDouble(record[i]))
                            : new ZmiesanyDatovyTyp(record[i]);
                }
                ZmiesanyDatovyTyp triedaKlasifikacie = new ZmiesanyDatovyTyp(record[record.length - 1]);

                predmety[cisloRecordu] = new Predmet(atributy, triedaKlasifikacie);
                cisloRecordu++;
            }

            return predmety;
        } catch (IOException | CsvException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public int dajMiPocetAtributov(String nazovSuboru) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(nazovSuboru))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {

            String[] vzorky = reader.readNext();
            if (vzorky == null) {
                return 0;
            }

            int pocetAtributov = vzorky.length - 1;
            return pocetAtributov;
        } catch (IOException | CsvException e) {
            System.out.println(e);
            return -1;
        }
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
