package priponoveSubory;

import hlavny.balik.Predmet;
import hlavny.balik.ZmiesanyDatovyTyp;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;


public class XLSX implements SuborSPriponou {

    @Override
    public int dajMiPocetPredmetov(String nazovSuboru) {
        try (FileInputStream fis = new FileInputStream(nazovSuboru);
            Workbook subor = new XSSFWorkbook(fis)) {

            int pocetRiadkov = subor.getSheetAt(0).getLastRowNum() + 1;
            fis.close();
            return pocetRiadkov;
        } catch (IOException e) {
            return -1;
        }
    }

    @Override
    public Predmet[] nacitajPredmetyZoSubora(String nazovSuboru, int velkostPola) {
        try (FileInputStream fis = new FileInputStream(nazovSuboru);
             Workbook subor = new XSSFWorkbook(fis)) {

            int pocetAtributov = this.dajMiPocetAtributov(nazovSuboru);
            Predmet[] predmety = new Predmet[velkostPola];
            Sheet zosit = subor.getSheetAt(0);
            int cisloRiadka = 0;

            while (cisloRiadka <= zosit.getLastRowNum()) {
                Row row = zosit.getRow(cisloRiadka);
                ZmiesanyDatovyTyp[] atributy = new ZmiesanyDatovyTyp[pocetAtributov];

                for (int i = 0; i < row.getLastCellNum(); i++) {
                    atributy[i] = this.dajMiDruhBunky(row.getCell(i)) == CellType.STRING ? new ZmiesanyDatovyTyp(row.getCell(i).getStringCellValue()) :
                            new ZmiesanyDatovyTyp(row.getCell(i).getNumericCellValue());
                    System.out.println(i);
                }
                int last = row.getLastCellNum() - 1;

                ZmiesanyDatovyTyp triedaKlasifikacie = new ZmiesanyDatovyTyp(row.getCell(last).getStringCellValue());

                predmety[cisloRiadka] = new Predmet(atributy, triedaKlasifikacie);
                cisloRiadka++;
            }

            return predmety;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public int dajMiPocetAtributov(String nazovSuboru) {
        try (FileInputStream fis = new FileInputStream(nazovSuboru);
             Workbook subor = new XSSFWorkbook(fis)) {

            int pocetAtributov = subor.getSheetAt(0).getRow(0).getLastCellNum();
            System.out.println(pocetAtributov);
            fis.close();
            return pocetAtributov;
        } catch (IOException e) {
            System.out.println(e);
            return -1;
        }
    }

    private CellType dajMiDruhBunky(Cell cell) {
        switch (cell.getCellType()) {
            case NUMERIC -> {
                return CellType.NUMERIC;
            }
            case STRING, BOOLEAN -> {
                return CellType.STRING;
            }
        }
        return null;
    }

}


