package cart;

public class Matica {
    private final int[][] confusionMatrix;
    private final String[] triedy;

    private final double[] recall;
    private final double[] precision;
    private final double[] f1Skore;

    private double celkovyRecall;
    private double celkovyPrecision;
    private double celkoveF1skore;

    private int atribut;

    public int getAtribut() {
        return this.atribut;
    }

    public Matica(String[] triedy) {
        this.confusionMatrix = new int[triedy.length][triedy.length];
        this.triedy = triedy;
        this.recall = new double[triedy.length];
        this.precision = new double[triedy.length];
        this.f1Skore = new double[triedy.length];

        this.celkovyRecall = 0;
        this.celkovyPrecision = 0;
        this.celkoveF1skore = 0;
        this.atribut = Integer.MIN_VALUE;
    }

    public Matica(double[] recall, double[] precision, double[] f1Skore, double celkovyRecall, double celkovyPrecision, double celkoveF1skore, String[] triedy, int atribut) {
        this.f1Skore = f1Skore;
        this.precision = precision;
        this.recall = recall;
        this.celkovyPrecision = celkovyPrecision;
        this.celkovyRecall = celkovyRecall;
        this.celkoveF1skore = celkoveF1skore;

        this.confusionMatrix = null;
        this.triedy = triedy;
        this.atribut = atribut;

    }


    public void vytvorStatistiky() {
        int celkovyPocetTestovanychTried = 0;

        int[] poctySpravneIdentifikovanychTried =  new int[this.triedy.length];
        int[] celkovyPocetVzoriekVTriedach = new int[this.triedy.length];
        int[] celkovyPocetPredikovanychVzoriekPreDaneTriedy = new int[this.triedy.length];

        for (int i = 0; i < this.confusionMatrix.length; i++) {
            for (int j = 0; j < this.confusionMatrix[i].length; j++) {
                celkovyPocetPredikovanychVzoriekPreDaneTriedy[i] += this.confusionMatrix[i][j];
                celkovyPocetVzoriekVTriedach[j] += this.confusionMatrix[i][j];

                celkovyPocetTestovanychTried += this.confusionMatrix[i][j];
                if (i == j) {
                    poctySpravneIdentifikovanychTried[i] += this.confusionMatrix[i][j];
                }
            }
        }

        int celkovyPocet = 0;
        int celkovyPocetTestovanychVzoriek = 0;

        for (int i = 0; i < this.triedy.length; i++) {
            this.recall[i] = celkovyPocetVzoriekVTriedach[i] == 0 ? 0 : ((double)poctySpravneIdentifikovanychTried[i] / celkovyPocetVzoriekVTriedach[i]) * 100;
            this.precision[i] = celkovyPocetPredikovanychVzoriekPreDaneTriedy[i] == 0 ? 0 : ((double)poctySpravneIdentifikovanychTried[i] / celkovyPocetPredikovanychVzoriekPreDaneTriedy[i]) * 100;
            this.f1Skore[i] = (this.recall[i] + this.precision[i]) == 0 ? 0 : (2 * this.recall[i] * this.precision[i]) / (this.recall[i] + this.precision[i]);
            celkovyPocet += poctySpravneIdentifikovanychTried[i];
            celkovyPocetTestovanychVzoriek += celkovyPocetVzoriekVTriedach[i];
        }

        for (int i = 0; i < this.triedy.length; i++) {
            this.celkovyRecall += ((double)celkovyPocetVzoriekVTriedach[i] / celkovyPocetTestovanychVzoriek) * this.recall[i];
            this.celkovyPrecision += ((double)celkovyPocetVzoriekVTriedach[i] / celkovyPocetTestovanychVzoriek) * this.precision[i];
            this.celkoveF1skore += ((double)celkovyPocetVzoriekVTriedach[i] / celkovyPocetTestovanychVzoriek) * this.f1Skore[i];
           // System.out.println(celkovyPocetVzoriekVTriedach[i]);
           // System.out.println(celkovyPocetTestovanychVzoriek);
        }
    }

    public void vypisStatistiky() {
        for (int i = 0; i < this.triedy.length; i++) {
            System.out.println("Trieda: " + this.triedy[i]);
            System.out.println("-------------------------------------");
            System.out.printf("Precision: %.2f%s\n", this.precision[i], "%");
            System.out.printf("Recall: %.2f%s\n", this.recall[i], "%");
            System.out.printf("F1 skore: %.2f%s\n", this.f1Skore[i], "%");
            System.out.println();
        }
        System.out.println("-------------------------------------");

        System.out.printf("Celková precision: %.2f%s\n", this.celkovyPrecision, "%");
        System.out.printf("Celkový recall: %.2f%s\n", this.celkovyRecall, "%");
        System.out.printf("Celkové F1 skore: %.2f%s\n", this.celkoveF1skore, "%");
        System.out.println();

    }

    public void inkrementujDanuPredikovanuTriedu(String predikovanaTrieda, String realnaTrieda) {
        this.confusionMatrix[this.getIndexTriedy(predikovanaTrieda)][this.getIndexTriedy(realnaTrieda)]++;
    }

    private int getIndexTriedy(String trieda) {
        for (int i = 0; i < this.triedy.length; i++) {
            if (this.triedy[i].equals(trieda)) {
                return i;
            }
        }
        return -1;
    }

    public void vypisConfusionMatrix() {
        // Zisti najdlhší názov triedy pre správne zarovnanie
        int najdlhsiaDlzka = this.dajMiNajdlhsiuDlzkuRetazciaTriedy();

        // Vypíš hlavičku
        System.out.print(" ".repeat(najdlhsiaDlzka + 2)); // Odsadenie pre hlavičku (vrátane dvojitej medzery)
        for (String trieda : this.triedy) {
            int odsadenie = Math.max(5, trieda.length() + 2); // Dvojité medzery zabezpečujú odsadenie
            System.out.print(String.format("%-" + odsadenie + "s", trieda));
        }
        System.out.println();

        for (int i = 0; i < this.confusionMatrix.length; i++) {
            System.out.print(String.format("%-" + (najdlhsiaDlzka + 2) + "s", this.triedy[i]));

            for (int j = 0; j < this.confusionMatrix[i].length; j++) {
                int hodnota = this.confusionMatrix[i][j];

                int[] odsadenia = this.dajMiOdsadenia(this.triedy[j], hodnota);
                String zarovnaneCislo = " ".repeat(odsadenia[0]) + hodnota + " ".repeat(odsadenia[1]);

                System.out.print(zarovnaneCislo + "  ");
            }
            System.out.println();
        }
    }

    private int[] dajMiOdsadenia(String trieda, int hodnota) {
        int dlzkaTextu = trieda.length();
        int dlzkaCisla = String.valueOf(hodnota).length();
        int rozdiel = dlzkaTextu - dlzkaCisla;

        int vlavoOdsadenie = Math.max(0, rozdiel / 2);
        int vpravoOdsadenie = Math.max(0, rozdiel - vlavoOdsadenie);

        return new int[] {vlavoOdsadenie, vpravoOdsadenie};
    }

    private int dajMiNajdlhsiuDlzkuRetazciaTriedy() {
        int najdlhsia = Integer.MIN_VALUE;

        for (String trieda : this.triedy) {
            najdlhsia = Math.max(najdlhsia, trieda.length());
        }
        return najdlhsia;
    }

    public double getCelkoveF1skore() {
        return this.celkoveF1skore;
    }

    public double getCelkovyPrecision() {
        return this.celkovyPrecision;
    }

    public double getCelkovyRecall() {
        return this.celkovyRecall;
    }

    public double getF1Skore(int index) {
        return this.f1Skore[index];
    }

    public double getPrecision(int index) {
        return this.precision[index];
    }

    public double getRecall(int index) {
        return this.recall[index];
    }

    public static Matica vytvorSpriemerovanuMaticu(Matica[] matice,  String[] triedy, int atribut) {
        int pocetMatic = matice.length;
        int pocetTried = triedy.length;

        double[] recall = new double[pocetTried];
        double[] precision = new double[pocetTried];
        double[] f1Skore = new double[pocetTried];

        double celkovyRecall = 0;
        double celkovyPrecision = 0;
        double celkoveF1skore = 0;

        for (int i = 0; i < pocetMatic; i++) {
            for (int j = 0; j < pocetTried; j++) {
                recall[j] += matice[i].getRecall(j);
                precision[j] += matice[i].getPrecision(j);
                f1Skore[j] += matice[i].getF1Skore(j);
            }
            celkovyRecall += matice[i].getCelkovyRecall();
            celkovyPrecision += matice[i].getCelkovyPrecision();
            celkoveF1skore += matice[i].getCelkoveF1skore();
        }

        celkovyRecall = celkovyRecall / pocetMatic;
        celkovyPrecision = celkovyPrecision / pocetMatic;
        celkoveF1skore = celkoveF1skore / pocetMatic;

        for (int i = 0; i < pocetTried; i++) {
            recall[i] = recall[i] / pocetMatic;
            precision[i] = precision[i] / pocetMatic;
            f1Skore[i] = f1Skore[i] / pocetMatic;
        }
        return new Matica(recall, precision, f1Skore, celkovyRecall, celkovyPrecision, celkoveF1skore, triedy, atribut);
    }

    public double vytvorPriemerZHodnot() {
        double celkovost = this.celkovyRecall + this.celkovyPrecision + this.celkoveF1skore;

        for (int i = 0; i < this.triedy.length; i++) {
            celkovost += this.recall[i] + this.precision[i] + this.f1Skore[i];
        }
        int a = 3 + (3 * this.triedy.length);

        return celkovost / a;
    }

    public static void zoradMaticePodlaCelkovehoVykonu(Matica[] matice) {
        int pocetMatic = matice.length;

        for (int i = 1; i < pocetMatic; i++) {
            for (int j = i; j > 0; j--) {
                if (matice[j].celkoveF1skore < matice[j - 1].celkoveF1skore) {
                    vymen(matice, j, j - 1);
                }
            }
        }
    }

    public static void vymen(Matica[] matice, int indexA, int indexB) {
        Matica m = matice[indexA];
        matice[indexA] = matice[indexB];
        matice[indexB] = m;
    }

}


