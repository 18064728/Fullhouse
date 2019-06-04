package fullhouse;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Medewerken vult de gegevens van het formulier in en drukt op 'voeg masterclass toe' om de masterclass toe te voegen aan de database
 * of drukt op 'terug' om terug te gaan naar 'masterclasses'
 */
public class Masterclass_Toevoegen extends JDialog {

    //ATTRIBUTES
    private String datum;
    private String locatie;
    private String begintijd;
    private String eindtijd;
    private int kosten;
    private int min_rating;
    private String bekende_pokerspeler;
    private ArrayList<String>pokerspelers;

    private ArrayList<Integer> jaren;
    private ArrayList<Integer> maanden;
    private ArrayList<Integer> dagen;

    //COMPONENTS
    private JComboBox<Object> dag;
    private JComboBox<Object> maand;
    private JComboBox<Object> jaar;
    private int datumDay;
    private int datumMonth;
    private int datumYear;

    private JSpinner begintijdMin;
    private JSpinner begintijdUur;
    private JSpinner eindtijdMin;
    private JSpinner eindtijdUur;

    private JSpinner min_ratingSp;

    private JComboBox<Object> pokerspelersCb;

    Masterclass_Toevoegen() {

        try { //tries to make a connection with the database
            pokerspelers = new ArrayList<>();
            Connection conn = ConnectionManager.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM bekende_pokerspeler");

            while (rs.next()) {
                pokerspelers.add(rs.getString("naam"));
            }

        } catch (SQLException e) { //error message if there's no connection with the database
            System.out.println("can't connect to the database, please contact tech-support");
        }

        //JPanel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        //datum
        JLabel datumLbl = new JLabel("datum:");
        datumLbl.setBounds(5,25,50,25);
        panel.add(datumLbl);

        //dag
        JLabel datumdagLbl = new JLabel("dag");
        datumdagLbl.setBounds(110,0,50,25);
        panel.add(datumdagLbl);

        //maand
        JLabel datumMaandLbl = new JLabel("maand");
        datumMaandLbl.setBounds(170,0,50,25);
        panel.add(datumMaandLbl);

        //jaar
        JLabel datumJaarLbl = new JLabel("jaar");
        datumJaarLbl.setBounds(225,0,50,25);
        panel.add(datumJaarLbl);

        jaren = new ArrayList<>(); //number of year
        maanden = new ArrayList<>(); // month of year
        dagen = new ArrayList<>(); // day of month
        addDate(); //fills the comboboxes

        dag = new JComboBox<>(dagen.toArray());
        dag.setBounds(110,25,50,25);
        panel.add(dag);

        maand = new JComboBox<>(maanden.toArray());
        maand.setBounds(170,25,50,25);
        panel.add(maand);

        jaar = new JComboBox<>(jaren.toArray());
        jaar.setBounds(230,25,75,25);
        panel.add(jaar);

        JLabel locatieLbl = new JLabel("locatie");
        locatieLbl.setBounds(5, 75,50,25);
        panel.add(locatieLbl);

        JComboBox<String>locaties = new JComboBox<>();
        locaties.addItem("Almere");
        locaties.addItem("Amsterdam");
        locaties.addItem("Den haag");
        locaties.addItem("Goes");
        locaties.addItem("Rotterdam");
        locaties.addItem("Utrecht");
        locaties.setSelectedIndex(2);
        locaties.setBounds(110,75,100,25);
        panel.add(locaties);

        JLabel begintijdLbl = new JLabel("begintijd:");
        begintijdLbl.setBounds(5,125,75,25);
        panel.add(begintijdLbl);

        SpinnerNumberModel beginUur = new SpinnerNumberModel(0, 0,24,1);
        begintijdUur = new JSpinner(beginUur);
        begintijdUur.setBounds(110,125,50,25);
        panel.add(begintijdUur);

        SpinnerNumberModel beginMin = new SpinnerNumberModel(0,0,59,1);
        begintijdMin = new JSpinner(beginMin);
        begintijdMin.setBounds(165,125,50,25);
        panel.add(begintijdMin);

        JLabel eindtijdLbl = new JLabel("eindtijd:");
        eindtijdLbl.setBounds(5,175,75,25);
        panel.add(eindtijdLbl);

        SpinnerNumberModel eindUur = new SpinnerNumberModel(0, 0,24,1);
        eindtijdUur = new JSpinner(eindUur);
        eindtijdUur.setBounds(110,175,50,25);
        panel.add(eindtijdUur);

        SpinnerNumberModel eindMin = new SpinnerNumberModel(0,0,59,1);
        eindtijdMin = new JSpinner(eindMin);
        eindtijdMin.setBounds(165,175,50,25);
        panel.add(eindtijdMin);

        JLabel kostenLbl = new JLabel("kosten");
        kostenLbl.setBounds(5,225,75,25);
        panel.add(kostenLbl);

        SpinnerNumberModel kostenModel = new SpinnerNumberModel(0,0,1000,10);
        JSpinner kostensp = new JSpinner(kostenModel);
        kostensp.setBounds(110,225,75,25);
        panel.add(kostensp);

        JLabel min_ratingLbl = new JLabel("<html>minimale<br>vereiste rating</html>");
        min_ratingLbl.setBounds(5,275,200,30);
        panel.add(min_ratingLbl);

        SpinnerNumberModel model98 = new SpinnerNumberModel(0,0,200,1);
        min_ratingSp = new JSpinner(model98);
        min_ratingSp.setBounds(110,275,50,25);
        panel.add(min_ratingSp);

        JLabel inleggeldLbl = new JLabel("<html>bekende<br>pokerspeler</html>");
        inleggeldLbl.setBounds(5,325, 75,30);
        panel.add(inleggeldLbl);

        pokerspelersCb = new JComboBox<>(pokerspelers.toArray());
        pokerspelersCb.setBounds(110,325,125,25);
        panel.add(pokerspelersCb);

        JButton voegToeBtn = new JButton("voeg masterclass toe");
        voegToeBtn.setBounds(100,475,160,30);
        panel.add(voegToeBtn);

        JButton terugBtn1 = new JButton("terug");
        terugBtn1.setBounds(275,475,100,30);
        panel.add(terugBtn1);

        add(panel);

        //voegt de toernooi toe aan de database
        class VoegToe implements ActionListener {
            public void actionPerformed(ActionEvent e) {

                datumDay = dag.getSelectedIndex() + 1;
                datumMonth = maand.getSelectedIndex() + 1;
                datumYear = jaar.getSelectedIndex() + 2019;
                datum = datumYear + "-" + datumMonth + "-" + datumDay;
                locatie = locaties.getItemAt(locaties.getSelectedIndex());
                begintijd = begintijdUur.getValue().toString() + ":" + begintijdMin.getValue().toString();
                eindtijd = eindtijdUur.getValue().toString() + ":" + eindtijdMin.getValue().toString();
                kosten = Integer.parseInt(kostensp.getValue().toString());
                min_rating = Integer.parseInt(min_ratingSp.getValue().toString());
                bekende_pokerspeler = pokerspelersCb.getItemAt(pokerspelersCb.getSelectedIndex()).toString();

                System.out.println(datum);

                if (isValidInput()) {
                    try { //adds toernooi to database
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("INSERT INTO masterclass (datum, locatie, begintijd, eindtijd, kosten, min_rating, bekende_pokerspeler) VALUES (?, ?, ?, ?, ?, ?, ?);");
                        ps.setString(1, datum);
                        ps.setString(2, locatie);
                        ps.setString(3, begintijd);
                        ps.setString(4, eindtijd);
                        ps.setInt(5,kosten);
                        ps.setInt(6,min_rating);
                        ps.setString(7, bekende_pokerspeler);
                        ps.executeUpdate();
                        System.out.println("Masterclass is added to the database");
                        dispose();
                        new Masterclasses();
                    } catch (Exception ex) {
                        System.out.println("\nSomething went wrong");
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"niet alle velden zijn correct ingevuld");
                }
            }
        }

        //gaat terug naar 'Toernooien'
        class Terug implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }
        ActionListener voegToe = new VoegToe();
        ActionListener terug = new Terug();
        voegToeBtn.addActionListener(voegToe);
        terugBtn1.addActionListener(terug);
    }

    //vult de comboboxes met getallen
    private void addDate() {
        jaren = new ArrayList<>();
        maanden = new ArrayList<>();
        dagen = new ArrayList<>();

        //year
        for (int i = 2019; i <= 2050 ; i++) {
            jaren.add(i);
        }

        //month
        for (int i = 1; i <= 12  ; i++) {
            maanden.add(i);
        }

        //day
        for (int i = 1; i <=31 ; i++) {
            dagen.add(i);
        }
    }

    //geeft true als alle velden juist zijn ingevuld
    private boolean isValidInput() {
        //todo code toevoegen zodat de method checkt of de alle input juist is
        return true;
    }
}
