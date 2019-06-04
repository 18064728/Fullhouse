package fullhouse;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

/**
 * Medewerken vult de gegevens van het formulier in en drukt op 'voeg gast toe' om de gast toe te voegen aan de database
 * of drukt op 'terug' om terug te gaan naar 'Gasten'
 */
public class Gast_Toevoegen extends JDialog {

    //JDIALOG
    private static final int width = 500;
    private static final int height = 500;
    private static final String title = "Gast Toevoegen";

    //ATTRIBUTES
    private String naam;
    private String geslacht = "null";
    private LocalDate geboortedatum;
    private String gbdString;

    private ArrayList<Integer> years;
    private ArrayList<Integer> months;
    private ArrayList<Integer> days;
    private int day;
    private int month;
    private int year;

    private String adres;
    private String postcode;
    private String woonplaats;
    private String telefoonnummer;
    private String email;

    //COMPONENTS
    protected JFormattedTextField naamTxt;
    protected JRadioButton man;
    protected JRadioButton vrouw;
    protected JComboBox<Object> dayList;
    protected JComboBox<Object> monthList;
    protected JComboBox<Object> yearList;
    protected JFormattedTextField adresTxt;
    protected JFormattedTextField postcodeTxt;
    protected JFormattedTextField woonplaatsTxt;
    protected JFormattedTextField telefoonnummerTxt;
    protected JFormattedTextField emailTxt;
    protected JButton terugBtn1;
    protected JButton voegToeBtn;

    public static void main(String[] args) {

        JDialog d = new Gast_Toevoegen();
        d.setSize(width, height);
        d.setTitle(title);
        d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        d.setVisible(true);
    }

     Gast_Toevoegen() {

            try { //tries to make a connection with the database
                ConnectionManager.getConnection();
                System.out.println("connected to database");
            } catch (SQLException e) { //error message if there's no connection with the database
                System.out.println("can't connect to the database, please contact tech-support");
            }

                //JPanel
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

                //name
                JLabel naamLbl = new JLabel("naam: ");
                naamTxt = new JFormattedTextField();

                //gender
                ButtonGroup bg = new ButtonGroup();
                man = new JRadioButton("man");
                vrouw = new JRadioButton("vrouw");
                bg.add(man);
                bg.add(vrouw);

                // date of birth
                JLabel yearLbl = new JLabel("year");
                years = new ArrayList<>(); //number of year

                JLabel monthLbl = new JLabel("month");
                months = new ArrayList<>(); // month of year

                JLabel dayLbl = new JLabel("day");
                days = new ArrayList<>(); // day of month

                addDate(); // sets comboboxes to current date so user will be forced to change it
                dayList = new JComboBox<>(days.toArray());
                monthList = new JComboBox<>(months.toArray());
                yearList = new JComboBox<>(years.toArray());
                yearList.setSelectedIndex(yearList.getItemCount()-1);

                //adres
                JLabel adresLbl = new JLabel("adres: ");
                adresTxt = new JFormattedTextField();

                //postcode
                JLabel postcodeLbl = new JLabel("postcode: ");
                postcodeTxt = new JFormattedTextField();
                postcodeTxt.setToolTipText("bv. 1234ab");

                //woonplaats
                JLabel woonplaatsLbl = new JLabel("woonplaats: ");
                woonplaatsTxt = new JFormattedTextField();

                //telefoonnr
                JLabel telefoonnummerLbl = new JLabel("telefoonnummer: ");
                telefoonnummerTxt = new JFormattedTextField();

                //email
                JLabel emailLbl = new JLabel("email: ");
                emailTxt = new JFormattedTextField();

                //add guest button
                voegToeBtn = new JButton("voeg gast toe");

                //terug
                terugBtn1 = new JButton("terug");

                //adds components
                panel.add(naamLbl);
                panel.add(naamTxt);

                panel.add(man);
                panel.add(vrouw);

                panel.add(dayLbl);
                panel.add(dayList);
                panel.add(monthLbl);
                panel.add(monthList);
                panel.add(yearLbl);
                panel.add(yearList);

                panel.add(adresLbl);
                panel.add(adresTxt);

                panel.add(postcodeLbl);
                panel.add(postcodeTxt);

                panel.add(woonplaatsLbl);
                panel.add(woonplaatsTxt);

                panel.add(telefoonnummerLbl);
                panel.add(telefoonnummerTxt);

                panel.add(emailLbl);
                panel.add(emailTxt);

                panel.add(Box.createRigidArea(new Dimension(0, 25)));
                panel.add(voegToeBtn);
                panel.add(terugBtn1);
                add(panel);

        //voegt de gast toe aan de database
        class VoegToe implements ActionListener {
            public void actionPerformed(ActionEvent e) {

                try { //adds guest to database
                    naam = naamTxt.getText();

                    if (man.isSelected()) {
                        geslacht = "man";
                    } else if (vrouw.isSelected()) {
                        geslacht = "vrouw";
                    }

                    day = dayList.getSelectedIndex() + 1;
                    month = monthList.getSelectedIndex() + 1;
                    year = yearList.getSelectedIndex() + 1900;
                    geboortedatum = LocalDate.of(year, month, day);
                    gbdString = year + "-" + month + "-" + day;

                    adres = adresTxt.getText();
                    postcode = postcodeTxt.getText();
                    woonplaats = woonplaatsTxt.getText();
                    telefoonnummer = telefoonnummerTxt.getText();
                    email = emailTxt.getText();

                    if (!is18()) { //checks if user is of legal age
                        throw new underageException();
                    }

                    if (isValidInput()) { //adds guest to the database if the input is valid
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("INSERT INTO gast (naam, geslacht, geboortedatum, adres, postcode, woonplaats, telefoonnummer, email) " +
                                                                                                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?);");
                        ps.setString(1, naam);
                        ps.setString(2, geslacht);
                        ps.setString(3, gbdString);
                        ps.setString(4, adres);
                        ps.setString(5, postcode);
                        ps.setString(6, woonplaats);
                        ps.setString(7, telefoonnummer);
                        ps.setString(8, email);
                        ps.executeUpdate();
                        System.out.println("Guest is added to the database");
                        dispose();
                    }

                } catch (underageException ex1) {
                    System.out.println("gast is nog geen 18");
                } catch (Exception ex) {
                    System.out.println("\nSomething went wrong");
                    ex.printStackTrace();
                }
            }
        }

        //gaat terug naar 'Gasten'
        class Terug implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Gasten();
            }
        }

        ActionListener voegToe = new VoegToe();
        ActionListener terug = new Terug();
        voegToeBtn.addActionListener(voegToe);
        terugBtn1.addActionListener(terug);
    }

    private void addDate() {
        years = new ArrayList<>();
        months = new ArrayList<>();
        days = new ArrayList<>();
        int currentYear = LocalDateTime.now().getYear();

        //year
        for (int i = 1900; i <= currentYear ; i++) {
           years.add(i);
        }

        //month
        for (int i = 1; i <= 12  ; i++) {
            months.add(i);
        }

        //day
        for (int i = 1; i <=31 ; i++) {
            days.add(i);
        }
    }

    /**
     * checkt of de gast 18 jaar is
     * @return true als de gast 18 is, false als dat niet zo is
     */
    private Boolean is18() {
        LocalDate now = LocalDate.now();
        int age = Period.between(geboortedatum, now).getYears();
        return  age >= 18;
    }

    /**
     * checkt of de gegevens juist ingevuld zijn
     * @return true als de gegevens juist zijn, false als dat niet zo is
     */
    private Boolean isValidInput() { //checks if input is valid
        boolean validInput = true;

            if (naam.equals("") || naam.equals("vul een naam in a.u.b.")) {
                System.out.println("vul een naam in a.u.b.");
                naamTxt.setText("vul een naam in a.u.b.");
                validInput = false;
            }
            if (naam.matches(".*\\d.*")) {
                System.out.println("naam mag geen nummers bevatten");
                naamTxt.setText("naam mag geen nummers bevatten");
                validInput = false;
            }
            if (geslacht.equals("null")) {
                System.out.println("geen geslacht gekozen");
                JOptionPane.showMessageDialog(null,"geen geslacht gekozen");
                validInput = false;
            }
            if (adres.equals("")) {
                System.out.println("vul een adres in a.u.b.");
                adresTxt.setText("vul een adres in a.u.b.");
                validInput = false;
            }
            if (postcode.equals("")) {
                System.out.println("vul een postcode in a.u.b.");
                postcodeTxt.setText("vul een postcode in a.u.b.");
                validInput = false;
            }
            if (woonplaats.equals("")) {
                System.out.println("vul een woonplaats in a.u.b.");
                woonplaatsTxt.setText("vul een woonplaats in a.u.b.");
                validInput = false;
            }
            if (telefoonnummer.equals("")) {
                System.out.println("vul een telefoonnummer in a.u.b.");
                telefoonnummerTxt.setText("vul een telefoonnummer in a.u.b.");
                validInput = false;
            }
            if (telefoonnummer.length() >= 12) {
                System.out.println("telefoonnummer te lang");
                validInput = false;
            }
            if (email.equals("")) {
                System.out.println("vul een email in a.u.b.");
                emailTxt.setText("vul een email in a.u.b.");
                validInput = false;
            }

            if (validInput) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "gegevens zijn nog onjuist ingevoerd");
                return false;
            }
    }

    /**
     * custom Exception voor als de gast nog geen 18 is
     */
     private class underageException extends Exception {
        private underageException() {
        }
    }
}