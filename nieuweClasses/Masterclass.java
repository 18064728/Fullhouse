public class Masterclass {

    //ATTRIBUTES
    private int ID;
    private String datum;
    private String locatie;
    private String begintijd;
    private String eindtijd;

    private int kosten;
    private int minRating;
    private String bekendePokerspeler;

    Masterclass(int ID, String datum, String locatie, String begintijd, String eindtijd, int kosten, int minRating, String bekendePokerspeler){
        this.ID = ID;
        this.datum = datum;
        this.locatie = locatie;
        this.begintijd = begintijd;
        this.eindtijd = eindtijd;
        this.kosten = kosten;
        this.minRating = minRating;
        this.bekendePokerspeler = bekendePokerspeler;
    }

    public int getID() {
        return ID;
    }

    public String getDatum() {
        return datum;
    }

    public String getLocatie() {
        return locatie;
    }

    public String getBegintijd() {
        return begintijd;
    }

    public String getEindtijd() {
        return eindtijd;
    }

    public int getKosten() {
        return kosten;
    }

    public int getMinRating() {
        return minRating;
    }

    public String getBekendePokerspeler() {
        return bekendePokerspeler;
    }




    public String toString(){
        return ID + " " + datum + " " + locatie + " " + begintijd + " " + eindtijd + " " + kosten + " " + minRating + " " + bekendePokerspeler;
    }
}
