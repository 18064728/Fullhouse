import java.sql.*;

/**
 * Object BekendePokerspeler zoals die in de database hoort te staan
 */
public class BekendePokerspeler {

    private String naam;

    BekendePokerspeler(String naam) {
        this.naam = naam;
        }

    String getNaam() {
        return naam;
    }


    public String toString() {
        return naam;
    }
}