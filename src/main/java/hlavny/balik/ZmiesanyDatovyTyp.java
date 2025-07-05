package hlavny.balik;

import java.util.Objects;

public class ZmiesanyDatovyTyp {
    private Double dAtribut;
    private String sAtribut;

    public ZmiesanyDatovyTyp(double dAtribut) {
        this.dAtribut = dAtribut;
        this.sAtribut = null;

    }

    public ZmiesanyDatovyTyp(String sAtribut) {
        this.sAtribut = sAtribut;
        this.dAtribut = 0.0;
    }

    public <T> T getDatovyTyp() {

        return this.sAtribut == null ? (T)this.dAtribut : (T)this.sAtribut;
    }
}
