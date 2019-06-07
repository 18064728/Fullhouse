public class Winnaar {

    protected int winaarNr;
    protected int gastID;
    protected int toernooiID;
    protected int tafel;
    protected int ronde;

    public Winnaar(int winaarNr, int gastID, int toernooiID, int tafel, int ronde) {
        this.winaarNr = winaarNr;
        this.gastID = gastID;
        this.toernooiID = toernooiID;
        this.tafel = tafel;
        this.ronde = ronde;
    }

    public int getWinaarNr() {
        return winaarNr;
    }

    public int getGastID() {
        return gastID;
    }

    public int getToernooiID() {
        return toernooiID;
    }

    public int getTafel() {
        return tafel;
    }

    public int getRonde() {
        return ronde;
    }

    public String toString() {
        return winaarNr + " " + gastID + " " + toernooiID + " " + tafel +  " " + ronde;
    }
}
