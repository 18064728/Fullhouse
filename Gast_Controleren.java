package fullhouse;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Gast_Controleren extends JDialog {

    //JDIALOG
    private static final int width = 750;
    private static final int height = 500;
    private static final String title = "Gast Controleren";

    //DATABASE
    private Connection connection;
    private PreparedStatement ps;

    //ATTRIBUTES
    private int ID;
    private String naam;
    private String geslacht;
    private Date geboortedatum;
    private String adres;
    private String postcode;
    private String woonplaats;
    private String telefoonnummer;
    private String email;

    //COMPONENTS
    private JTextField textField;
    private JList<String>gasten;
    private JTable table;

    public static void main(String[] args) {

        JDialog d = new Gast_Controleren();
        d.setSize(width, height);
        d.setTitle(title);
        d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        d.setVisible(true);
    }

    public Gast_Controleren() {

        JPanel panel = new JPanel();
        panel.setLayout(null);

        try { //tries to make a connection with the database
            connection = DriverManager.getConnection(
                    "jdbc:mysql://meru.hhs.nl/18064728?useLegacyDatetimeCode=false&serverTimezone=Europe/Amsterdam",
                    "leerlingnr", "password");

            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * from gast");
            System.out.println("connected to database");

            String[] columnNames = { "ID", "naam", "geslacht", "geboortedatum", "adres", "postcode", "woonplaats", "telefoonnummer", "email"};
            String[][] data = {
                    { "001", "Piet", "man", "2000-01-01", "straat", "1234ab", "den haag", "0612345678", "piet@email.com" },
                    { "002", "Kees", "man", "1999-12-31", "straat2", "5678cd", "den haag", "0687654321", "kees@email.com" }
            };

            table = new JTable(data, columnNames);
            panel.add(table);

            JScrollPane j = new JScrollPane(table);
            j.setBounds(0,50,750,500);
            panel.add(j);

            while (resultSet.next()) {
                ID = resultSet.getInt("ID");
                naam = resultSet.getString("naam");
                geslacht = resultSet.getString("geslacht");
                geboortedatum = resultSet.getDate("geboortedatum");
                adres = resultSet.getString("adres");
                postcode = resultSet.getString("postcode");
                woonplaats = resultSet.getString("woonplaats");
                telefoonnummer = resultSet.getString("telefoonnummer");
                email = resultSet.getString("email");

                System.out.println(resultSet.getInt("ID"));
                System.out.println(resultSet.getString("naam"));
                System.out.println(resultSet.getString("geslacht"));
                System.out.println(resultSet.getString("geboortedatum"));
                System.out.println(resultSet.getString("adres"));
                System.out.println(resultSet.getString("postcode"));
                System.out.println(resultSet.getString("woonplaats"));
                System.out.println(resultSet.getString("telefoonnummer"));
                System.out.println(resultSet.getString("email"));
                System.out.println();

                /*
                System.out.format("%s, %s, %s, %s, %s, %s, %s, %s, %s\n", "ID", "Naam", "Geslacht", "geboortedatum", "adres", "postcode", "woonplaats", "telefoonnummer", "email");
                System.out.println(resultSet.getString("naam"));
                System.out.println(resultSet.getString("author"));
                System.out.println(resultSet.getString("title"));
                System.out.println();*/

            }

        } catch (SQLException e) { //error message if there's no connection with the database
            System.out.println("can't connect to the database, please contact tech-support");
        }
        add(panel);
    }
}
