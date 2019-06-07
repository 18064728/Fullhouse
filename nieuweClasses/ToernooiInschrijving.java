public class ToernooiInschrijving {

    int inschrijvingnr;
    int gastID;
    int toernooiID;
    String heeftBetaald;

    public ToernooiInschrijving(int inschrijvingnr, int gastID, int toernooiID, String heeftBetaald) {
        this.inschrijvingnr = inschrijvingnr;
        this.gastID = gastID;
        this.toernooiID = toernooiID;
        this.heeftBetaald = heeftBetaald;
    }

    public int getInschrijvingnr() {
        return inschrijvingnr;
    }

    public int getGastID() {
        return gastID;
    }

    public int getToernooiID() {
        return toernooiID;
    }

    public String getHeeftBetaald() {
        return heeftBetaald;
    }

    public String toString() {
        return  "nr: " + inschrijvingnr + " gast: " + gastID + " toernooi: " + toernooiID + " heeft betaald: " + heeftBetaald;
    }
}