/**
 * Object Toernooi_Inschrijving zoals die ook in de database staat
 */

public class Toernooi_Inschrijving {

    private int inschrijvingnr;
    private int gast;
    private int toernooi;
    private String heeftBetaald;

    Toernooi_Inschrijving(int inschrijvingnr, int gast, int toernooi, String heeftBetaald) {
        this.inschrijvingnr = inschrijvingnr;
        this.gast = gast;
        this.toernooi = toernooi;
        this.heeftBetaald = heeftBetaald;
    }

    public int getInschrijvingnr() {
        return inschrijvingnr;
    }

    public int getGast() {
        return gast;
    }

    public int getToernooi() {
        return toernooi;
    }

    public String getHeeftBetaald() {
        return heeftBetaald;
    }

    public String toString() {
        return  "inschrijfingnr: " + inschrijvingnr + " gast: " + gast + " toernooi: " + toernooi + " heeft betaald: " + heeftBetaald;
    }

}