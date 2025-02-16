package fullhouse;

import java.sql.*;

/**
 * ConnectionManager voor een connectie met de database,
 * zorgt ervoor dat er maar één keer een connectie gemaakt hoeft te worden
 */
class ConnectionManager {
    public static Connection getConnection() throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection("jdbc:mysql://meru.hhs.nl/18064728?useLegacyDatetimeCode=false&serverTimezone=Europe/Amsterdam",
                    "18064728", "aeph3vo3aV");
        return connection;
    }
    private static Connection connection;
}

/**
 * Met de ListenerManager kan je de alle ActionListeners van een JButton removen
 */
class ListenerManager {
    public static void deleteActionListeners(JButton button) {
        ActionListener[] ac = button.getActionListeners();
        for (ActionListener actionListener : ac) {
            button.removeActionListener(actionListener);
        }
    }
}

/**
 * Object Gast zoals die in de database hoort te staan
 */
public class Gast {

    private int ID;
    private String naam;
    private String geslacht;
    private Date geboortedatum;
    private String adres;
    private String postcode;
    private String woonplaats;
    private String telefoonnummer;
    private String email;

    public Gast(int ID, String naam, String geslacht, Date geboortedatum, String adres, String postcode, String woonplaats, String telefoonnummer, String email) {
        this.ID = ID;
        this.naam = naam;
        this.geslacht = geslacht;
        this.geboortedatum = geboortedatum;
        this.adres = adres;
        this.postcode = postcode;
        this.woonplaats = woonplaats;
        this.telefoonnummer = telefoonnummer;
        this.email = email;
    }

    public int getID() {
        return ID;
    }

    public String getNaam() {
        return naam;
    }

    public String getGeslacht() {
        return geslacht;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public String getAdres() {
        return adres;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public String getTelefoonnummer() {
        return telefoonnummer;
    }

    public String getEmail() {
        return email;
    }

    public String toString() {
        return ID + " " + naam + " " + geslacht + " " + geboortedatum + " " + adres + " " + postcode + " " + woonplaats + " " + telefoonnummer + " " + email;
    }
}
