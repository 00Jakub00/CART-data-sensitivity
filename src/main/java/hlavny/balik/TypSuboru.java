package hlavny.balik;

public enum TypSuboru {
    XLSX("xlsx"),
    CSV("csv"),
    TXT("txt"),
    DATA("data"),
    ARFF("arff");

    private final String pripona;

    TypSuboru(String pripona) {
        this.pripona = pripona;
    }

    public static TypSuboru dajMiSuborPodlaPripony(String hladanaPripona) {
        for (TypSuboru typSuboru : values()) {
            if (typSuboru.pripona.equals(hladanaPripona)) {
                return typSuboru;
            }
        }
        return null;
    }
}
