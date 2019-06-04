package fullhouse;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Medewerken vult de gegevens van het formulier in en drukt op 'voeg toernooi toe' om het toernooi toe te voegen aan de database
 * of drukt op 'terug' om terug te gaan naar 'Toernooien'
 */
public class Toernooi_Toevoegen extends JDialog {

    //ATTRIBUTES
    private String datum;
    private String locatie;
    private String begintijd;
    private String eindtijd;
    private String beschrijving;
    private String conditie;
    private int max_inschrijvingen;
    private int inleggeld;
    private String inschrijfdatum;

    private ArrayList<Integer> years;
    private ArrayList<Integer> months;
    private ArrayList<Integer> days;

    //COMPONENTS
    private JComboBox<Object> datumDag;
    private JComboBox<Object> datumMaand;
    private JComboBox<Object> datumJaar;
    private int datumDay;
    private int datumMonth;
    private int datumYear;

    private JSpinner begintijdMin;
    private JSpinner begintijdUur;
    private JSpinner eindtijdMin;
    private JSpinner eindtijdUur;

    private JComboBox<Object> inschrijfdatumJaar;
    private JComboBox<Object> inschrijfdatumMaand;
    private JComboBox<Object> inschrijfdatumDag;
    private int inschrijfdatumDay;
    private int inschrijfdatumMonth;
    private int inschrijfdatumYear;

    private JTextField beschrijvingTxt;
    private JTextField conditieTxt;

    Toernooi_Toevoegen() {

        try { //tries to make a connection with the database
            ConnectionManager.getConnection();
            System.out.println("connected to database");
        } catch (SQLException e) { //error message if there's no connection with the database
            System.out.println("can't connect to the database, please contact tech-support");
        }

        //JPanel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel datumLbl = new JLabel("datum:");
        datumLbl.setBounds(5,25,50,25);
        panel.add(datumLbl);

        JLabel datumdagLbl = new JLabel("dag");
        datumdagLbl.setBounds(110,0,50,25);
        panel.add(datumdagLbl);

        JLabel datumMaandLbl = new JLabel("maand");
        datumMaandLbl.setBounds(170,0,50,25);
        panel.add(datumMaandLbl);

        JLabel datumJaarLbl = new JLabel("jaar");
        datumJaarLbl.setBounds(225,0,50,25);
        panel.add(datumJaarLbl);

        years = new ArrayList<>(); //number of year
        months = new ArrayList<>(); // month of year
        days = new ArrayList<>(); // day of month
        addDate(); //fills the comboboxes

        datumDag = new JComboBox<>(days.toArray());
        datumDag.setBounds(110,25,50,25);
        panel.add(datumDag);

        datumMaand = new JComboBox<>(months.toArray());
        datumMaand.setBounds(170,25,50,25);
        panel.add(datumMaand);

        datumJaar = new JComboBox<>(years.toArray());
        datumJaar.setBounds(230,25,75,25);
        panel.add(datumJaar);

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

        SpinnerNumberModel model1 = new SpinnerNumberModel(0, 0,24,1);
        begintijdUur = new JSpinner(model1);
        begintijdUur.setBounds(110,125,50,25);
        panel.add(begintijdUur);

        SpinnerNumberModel model2 = new SpinnerNumberModel(0,0,59,1);
        begintijdMin = new JSpinner(model2);
        begintijdMin.setBounds(165,125,50,25);
        panel.add(begintijdMin);

        JLabel eindtijdLbl = new JLabel("eindtijd:");
        eindtijdLbl.setBounds(5,175,75,25);
        panel.add(eindtijdLbl);

        SpinnerNumberModel model3 = new SpinnerNumberModel(0, 0,24,1);
        eindtijdUur = new JSpinner(model3);
        eindtijdUur.setBounds(110,175,50,25);
        panel.add(eindtijdUur);

        SpinnerNumberModel model4 = new SpinnerNumberModel(0,0,59,1);
        eindtijdMin = new JSpinner(model4);
        eindtijdMin.setBounds(165,175,50,25);
        panel.add(eindtijdMin);

        JLabel beschrijvingLbl = new JLabel("beschrijving");
        beschrijvingLbl.setBounds(5,225,75,25);
        panel.add(beschrijvingLbl);

        beschrijvingTxt = new JTextField();
        beschrijvingTxt.setBounds(110,225,200,25);
        panel.add(beschrijvingTxt);

        JLabel conditieLbl = new JLabel("conditie");
        conditieLbl.setBounds(5,275,75,25);
        panel.add(conditieLbl);

        conditieTxt = new JTextField();
        conditieTxt.setBounds(110,275,200,25);
        panel.add(conditieTxt);

        JLabel max_inschrijvingenLbl = new JLabel("<html>maximaal aantal<br>inschrijvingen</html>");
        max_inschrijvingenLbl.setBounds(5,325,200,30);
        panel.add(max_inschrijvingenLbl);

        SpinnerNumberModel model98 = new SpinnerNumberModel(0,0,200,1);
        JSpinner max_inschrijvingenSp = new JSpinner(model98);
        max_inschrijvingenSp.setBounds(110,325,50,25);
        panel.add(max_inschrijvingenSp);

        JLabel inleggeldLbl = new JLabel("inleggeld");
        inleggeldLbl.setBounds(5,375, 75,25);
        panel.add(inleggeldLbl);

        SpinnerNumberModel model99 = new SpinnerNumberModel(0,0,10000,100);
        JSpinner inleggeldSp = new JSpinner(model99);
        inleggeldSp.setBounds(110,375,75,25);
        panel.add(inleggeldSp);

        JLabel inschrijfdatumLbl = new JLabel("inschrijfdatum");
        inschrijfdatumLbl.setBounds(5,425,100,25);
        panel.add(inschrijfdatumLbl);

        inschrijfdatumDag = new JComboBox<>(days.toArray());
        inschrijfdatumDag.setBounds(110,425,50,25);
        panel.add(inschrijfdatumDag);

        inschrijfdatumMaand = new JComboBox<>(months.toArray());
        inschrijfdatumMaand.setBounds(170,425,50,25);
        panel.add(inschrijfdatumMaand);

        inschrijfdatumJaar = new JComboBox<>(years.toArray());
        inschrijfdatumJaar.setBounds(230,425,75,25);
        panel.add(inschrijfdatumJaar);

        JButton voegToeBtn = new JButton("voeg gast toe");
        voegToeBtn.setBounds(100,475,120,30);
        panel.add(voegToeBtn);

        JButton terugBtn1 = new JButton("terug");
        terugBtn1.setBounds(250,475,100,30);
        panel.add(terugBtn1);

        panel.add(voegToeBtn);
        panel.add(terugBtn1);
        add(panel);

        //voegt de toernooi toe aan de database
        class VoegToe implements ActionListener {
            public void actionPerformed(ActionEvent e) {

                datumDay = datumDag.getSelectedIndex() + 1;
                datumMonth = datumMaand.getSelectedIndex() + 1;
                datumYear = datumJaar.getSelectedIndex() + 2019;
                datum = datumYear + "-" + datumMonth + "-" + datumDay;

                locatie = locaties.getItemAt(locaties.getSelectedIndex());
                 begintijd = begintijdUur.getValue().toString() + ":" + begintijdMin.getValue().toString();
                 eindtijd = eindtijdUur.getValue().toString() + ":" + eindtijdMin.getValue().toString();
                beschrijving = beschrijvingTxt.getText();
                conditie = conditieTxt.getText();
                max_inschrijvingen = Integer.parseInt(max_inschrijvingenSp.getValue().toString());
                inleggeld = Integer.parseInt(inleggeldSp.getValue().toString());
                inschrijfdatumDay = inschrijfdatumDag.getSelectedIndex() + 1;
                inschrijfdatumMonth = inschrijfdatumMaand.getSelectedIndex() + 1;
                inschrijfdatumYear = inschrijfdatumJaar.getSelectedIndex() + 2019;
                inschrijfdatum = inschrijfdatumYear + "-" + inschrijfdatumMonth + "-" + inschrijfdatumDay;

                if (isValidInput()) {
                    try { //adds toernooi to database
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("INSERT INTO toernooi (datum, locatie, begintijd, eindtijd, beschrijving, conditie, max_inschrijvingen, inleggeld, inschrijfdatum) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);");
                        ps.setString(1, datum);
                        ps.setString(2, locatie);
                        ps.setString(3, begintijd);
                        ps.setString(4, eindtijd);
                        ps.setString(5, beschrijving);
                        ps.setString(6, conditie);
                        ps.setInt(7, max_inschrijvingen);
                        ps.setInt(8, inleggeld);
                        ps.setString(9, inschrijfdatum);
                        ps.executeUpdate();
                        System.out.println("Toernooi is added to the database");
                        dispose();
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
                JDialog d = new Toernooien();
                d.setVisible(true);
                d.setSize(300,400);
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
        years = new ArrayList<>();
        months = new ArrayList<>();
        days = new ArrayList<>();

        //year
        for (int i = 2019; i <= 2050 ; i++) {
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

    //geeft true als alle velden juist zijn ingevuld
    private boolean isValidInput() {
        //todo code toevoegen zodat de method checkt of de alle input juist is
        return true;
    }
}
