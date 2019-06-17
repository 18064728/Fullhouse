import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * Medewerken vult de gegevens van het formulier in en drukt op 'voeg toernooi toe' om het toernooi toe te voegen aan de database
 * of drukt op 'terug' om terug te gaan naar 'Toernooien'
 */
class ToernooiToevoegen extends JDialog {

    //JDIALOG
    private static final int width = 500;
    private static final int height = 600;
    private static final String title = "Toernooi Toevoegen";

    //ATTRIBUTES
    private String datum;
    private int datumDay;
    private int datumMonth;
    private int datumYear;
    private String locatie;
    private String begintijd;
    private String eindtijd;
    private String beschrijving;
    private String conditie;
    private int max_inschrijvingen;
    private int inleggeld;
    private String inschrijfdatum;
    private int inschrijfdatumDay;
    private int inschrijfdatumMonth;
    private int inschrijfdatumYear;
    private int rondes;

    private ArrayList<Integer> years;
    private ArrayList<Integer> months;
    private ArrayList<Integer> days;

    //COMPONENTS
    JComboBox<Object> datumDag;
    JComboBox<Object> datumMaand;
    JComboBox<Object> datumJaar;

    JComboBox<String>locaties;
    JLabel verkeerdeLocatie;

    JSpinner begintijdMin;
    JSpinner begintijdUur;
    JSpinner eindtijdMin;
    JSpinner eindtijdUur;
    private JLabel verkeerdeTijd;

    JTextField beschrijvingTxt;
    JTextField conditieTxt;

    JSpinner maxInschrijvingenSp;
    JLabel verkeerdeMaxInschrijvingen;

    JSpinner inleggeldSp;
    JLabel verkeerdeInleggeldLbl;

    JComboBox<Object> inschrijfdatumJaar;
    JComboBox<Object> inschrijfdatumMaand;
    JComboBox<Object> inschrijfdatumDag;

    JSpinner rondesSp;
    JLabel verkeerdeRondes;

    JButton voegToeBtn;
    JButton terugBtn1;

    ToernooiToevoegen() {

        try { //tries to make a connection with the database
            ConnectionManager.getConnection();
        } catch (SQLException e) { //error message if there's no connection with the database
            System.out.println("can't connect to the database, please contact tech-support");
        }

        this.setSize(width, height);
        this.setTitle(title);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);

        //JPanel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel datumLbl = new JLabel("Datum:");
        datumLbl.setBounds(5,25,50,25);
        panel.add(datumLbl);

        JLabel datumdagLbl = new JLabel("Dag");
        datumdagLbl.setBounds(110,0,50,25);
        panel.add(datumdagLbl);

        JLabel datumMaandLbl = new JLabel("Maand");
        datumMaandLbl.setBounds(170,0,50,25);
        panel.add(datumMaandLbl);

        JLabel datumJaarLbl = new JLabel("Jaar");
        datumJaarLbl.setBounds(225,0,50,25);
        panel.add(datumJaarLbl);

        years = new ArrayList<>();
        months = new ArrayList<>();
        days = new ArrayList<>();
        addDate(); //vult de comboboxes

        datumDag = new JComboBox<>(days.toArray());
        datumDag.setBounds(110,25,50,25);
        panel.add(datumDag);

        datumMaand = new JComboBox<>(months.toArray());
        datumMaand.setBounds(170,25,50,25);
        panel.add(datumMaand);

        datumJaar = new JComboBox<>(years.toArray());
        datumJaar.setBounds(230,25,75,25);
        panel.add(datumJaar);

        JLabel locatieLbl = new JLabel("Locatie");
        locatieLbl.setBounds(5, 75,50,25);
        panel.add(locatieLbl);

        locaties = new JComboBox<>();
        locaties.addItem("");
        locaties.addItem("Almere");
        locaties.addItem("Amsterdam");
        locaties.addItem("Den haag");
        locaties.addItem("Goes");
        locaties.addItem("Rotterdam");
        locaties.addItem("Utrecht");
        locaties.setBounds(110,75,100,25);
        panel.add(locaties);

        verkeerdeLocatie = new JLabel();
        verkeerdeLocatie.setBounds(225,75,175,25);
        panel.add(verkeerdeLocatie);

        JLabel begintijdLbl = new JLabel("Begintijd:");
        begintijdLbl.setBounds(5,125,75,25);
        panel.add(begintijdLbl);

        SpinnerNumberModel beginUurModel = new SpinnerNumberModel(0, 0,24,1);
        begintijdUur = new JSpinner(beginUurModel);
        begintijdUur.setBounds(110,125,50,25);
        panel.add(begintijdUur);

        SpinnerNumberModel beginMinModel = new SpinnerNumberModel(0,0,59,1);
        begintijdMin = new JSpinner(beginMinModel);
        begintijdMin.setBounds(165,125,50,25);
        panel.add(begintijdMin);

        JLabel eindtijdLbl = new JLabel("Eindtijd:");
        eindtijdLbl.setBounds(5,160,75,25);
        panel.add(eindtijdLbl);

        SpinnerNumberModel eindUurModel = new SpinnerNumberModel(0, 0,24,1);
        eindtijdUur = new JSpinner(eindUurModel);
        eindtijdUur.setBounds(110,160,50,25);
        panel.add(eindtijdUur);

        SpinnerNumberModel eindMinModel = new SpinnerNumberModel(0,0,59,1);
        eindtijdMin = new JSpinner(eindMinModel);
        eindtijdMin.setBounds(165,160,50,25);
        panel.add(eindtijdMin);

        verkeerdeTijd = new JLabel();
        verkeerdeTijd.setBounds(225,125,175,50);
        panel.add(verkeerdeTijd);

        JLabel beschrijvingLbl = new JLabel("Beschrijving");
        beschrijvingLbl.setBounds(5,225,75,25);
        panel.add(beschrijvingLbl);

        beschrijvingTxt = new JTextField();
        beschrijvingTxt.setBounds(110,225,200,25);
        panel.add(beschrijvingTxt);

        JLabel conditieLbl = new JLabel("Conditie");
        conditieLbl.setBounds(5,275,75,25);
        panel.add(conditieLbl);

        conditieTxt = new JTextField();
        conditieTxt.setBounds(110,275,200,25);
        panel.add(conditieTxt);

        JLabel maxInschrijvingenLbl1 = new JLabel("<html>Maximaal aantal<br>inschrijvingen</html>");
        maxInschrijvingenLbl1.setBounds(5,325,200,30);
        panel.add(maxInschrijvingenLbl1);

        SpinnerNumberModel maxInschrijvingenModel = new SpinnerNumberModel(0,0,200,1);
        maxInschrijvingenSp = new JSpinner(maxInschrijvingenModel);
        maxInschrijvingenSp.setBounds(110,325,50,25);
        panel.add(maxInschrijvingenSp);

        verkeerdeMaxInschrijvingen = new JLabel();
        verkeerdeMaxInschrijvingen.setBounds(175,325,175,25);
        panel.add(verkeerdeMaxInschrijvingen);

        JLabel inleggeldLbl = new JLabel("Inleggeld");
        inleggeldLbl.setBounds(5,375, 75,25);
        panel.add(inleggeldLbl);

        SpinnerNumberModel inleggeldModel = new SpinnerNumberModel(0,0,10000,100);
        inleggeldSp = new JSpinner(inleggeldModel);
        inleggeldSp.setBounds(110,375,75,25);
        panel.add(inleggeldSp);

        verkeerdeInleggeldLbl = new JLabel();
        verkeerdeInleggeldLbl.setBounds(200,375,175,25);
        panel.add(verkeerdeInleggeldLbl);

        JLabel inschrijfdatumLbl = new JLabel("Inschrijfdatum");
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

        JLabel rondesLbl = new JLabel("rondes:");
        rondesLbl.setBounds(5,475,75,25);
        panel.add(rondesLbl);

        SpinnerNumberModel rondesModel = new SpinnerNumberModel(0,0,5,1);
        rondesSp = new JSpinner(rondesModel);
        rondesSp.setBounds(110,475,75,25);
        panel.add(rondesSp);

        verkeerdeRondes = new JLabel();
        verkeerdeRondes.setBounds(200,475,175,25);
        panel.add(verkeerdeRondes);

        voegToeBtn = new JButton("Voeg toernooi toe");
        voegToeBtn.setBounds(100,525,120,30);
        panel.add(voegToeBtn);

        terugBtn1 = new JButton("Terug");
        terugBtn1.setBounds(250,525,100,30);
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

                if (begintijdMin.getValue().toString().equals("0")) {
                    begintijd = begintijdUur.getValue().toString() + ":" + begintijdMin.getValue().toString() + "0:00" ;
                } else {
                    begintijd = begintijdUur.getValue().toString() + ":" + begintijdMin.getValue().toString() + ":00";
                }
                if (eindtijdMin.getValue().toString().equals("0")) {
                    eindtijd = eindtijdUur.getValue().toString() + ":" + eindtijdMin.getValue().toString() + "0:00";
                } else {
                    eindtijd = eindtijdUur.getValue().toString() + ":" + eindtijdMin.getValue().toString() + ":00";
                }

                beschrijving = beschrijvingTxt.getText();
                conditie = conditieTxt.getText();
                max_inschrijvingen = Integer.parseInt(maxInschrijvingenSp.getValue().toString());
                inleggeld = Integer.parseInt(inleggeldSp.getValue().toString());
                inschrijfdatumDay = inschrijfdatumDag.getSelectedIndex() + 1;
                inschrijfdatumMonth = inschrijfdatumMaand.getSelectedIndex() + 1;
                inschrijfdatumYear = inschrijfdatumJaar.getSelectedIndex() + 2019;
                inschrijfdatum = inschrijfdatumYear + "-" + inschrijfdatumMonth + "-" + inschrijfdatumDay;
                rondes = Integer.parseInt(rondesSp.getValue().toString());

                if (isValidInput()) {
                    try { //adds toernooi to database
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("INSERT INTO toernooi (datum, locatie, begintijd, eindtijd, beschrijving, conditie, max_inschrijvingen, inleggeld, inschrijfdatum, aantal_rondes) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?);");
                        ps.setString(1, datum);
                        ps.setString(2, locatie);
                        ps.setString(3, begintijd);
                        ps.setString(4, eindtijd);
                        ps.setString(5, beschrijving);
                        ps.setString(6, conditie);
                        ps.setInt(7, max_inschrijvingen);
                        ps.setInt(8, inleggeld);
                        ps.setString(9, inschrijfdatum);
                        ps.setInt(10,rondes);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"toernooi is toegevoegd aan de database");

                        datumDag.setSelectedIndex(0);
                        datumMaand.setSelectedIndex(0);
                        datumJaar.setSelectedIndex(0);
                        locaties.setSelectedItem(0);
                        begintijdUur.setValue(0);
                        begintijdMin.setValue(0);
                        eindtijdUur.setValue(0);
                        eindtijdMin.setValue(0);
                        beschrijvingTxt.setText("");
                        conditieTxt.setText("");
                        maxInschrijvingenSp.setValue(0);
                        inleggeldSp.setValue(0);
                        inschrijfdatumDag.setSelectedIndex(0);
                        inschrijfdatumMaand.setSelectedIndex(0);
                        inschrijfdatumJaar.setSelectedIndex(0);
                        rondesModel.setValue(0);
                        verkeerdeLocatie.setVisible(false);
                        verkeerdeTijd.setVisible(false);
                        verkeerdeMaxInschrijvingen.setVisible(false);
                        verkeerdeInleggeldLbl.setVisible(false);
                        verkeerdeRondes.setVisible(false);

                    } catch (Exception ex) {
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
        years = new ArrayList<>();
        months = new ArrayList<>();
        days = new ArrayList<>();

        //jaar
        for (int i = 2019; i <= 2050 ; i++) {
            years.add(i);
        }

        //maand
        for (int i = 1; i <= 12  ; i++) {
            months.add(i);
        }

        //dag
        for (int i = 1; i <=31 ; i++) {
            days.add(i);
        }
    }

    //geeft true als alle velden juist zijn ingevuld
    private boolean isValidInput() {
        boolean isValid = true;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date datumDate = sdf.parse(datum);
            Date inschrijfdatumDate = sdf.parse(inschrijfdatum);
            if (datumDate.before(inschrijfdatumDate)) {
                isValid = false;
                JOptionPane.showMessageDialog(null,"inschrijfdatum moet voor datum zijn" );
            } else if (datumDate.equals(inschrijfdatumDate)) {
                isValid = false;
                JOptionPane.showMessageDialog(null,"inschrijfdatum en datum mogen niet op dezelfde dag zijn");
            }

            if (locaties.getSelectedIndex() == 0) {
                isValid = false;
                verkeerdeLocatie.setForeground(Color.RED);
                verkeerdeLocatie.setText("kies een locatie a.u.b.");
                verkeerdeLocatie.setVisible(true);
            }

            LocalTime begintijdTime = Time.valueOf(begintijd).toLocalTime();
            LocalTime eindtijdTime = Time.valueOf(eindtijd).toLocalTime();
            long d = Duration.between(begintijdTime, eindtijdTime).toMinutes();

            if (d < 0) {
                isValid = false;
                verkeerdeTijd.setForeground(Color.RED);
                verkeerdeTijd.setText("eindtijd mag niet voor begintijd zijn");
                verkeerdeTijd.setVisible(true);
            } else if (d == 0) {
                isValid = false;
                verkeerdeTijd.setForeground(Color.RED);
                verkeerdeTijd.setText("<html>begin- en eindtijd mogen niet op hetzelfde tijdstip zijn</html>");
                verkeerdeTijd.setVisible(true);
            } else if (d < 30) {
                isValid = false;
                verkeerdeTijd.setForeground(Color.RED);
                verkeerdeTijd.setText("toernooi moet minimaal een half uur duren");
                verkeerdeTijd.setVisible(true);
            }

            if (beschrijving.equals("") || beschrijving.equals("veld invullen a.u.b.")) {
                isValid = false;
                beschrijvingTxt.setText("veld invullen a.u.b.");
            }

            if (conditie.equals("") || conditie.equals("veld invullen a.u.b.")) {
                isValid = false;
                conditieTxt.setText("veld invullen a.u.b.");
            }

            if (max_inschrijvingen < 1) {
                isValid = false;
                verkeerdeMaxInschrijvingen.setForeground(Color.RED);
                verkeerdeMaxInschrijvingen.setText("getal moet hoger dan 0 zijn");
                verkeerdeMaxInschrijvingen.setVisible(true);
            }

            if (inleggeld  < 1) {
                isValid = false;
                verkeerdeInleggeldLbl.setForeground(Color.RED);
                verkeerdeInleggeldLbl.setText("getal moet hoger dan 0 zijn");
                verkeerdeInleggeldLbl.setVisible(true);
            }

            if (rondes < 1 ) {
                isValid = false;
                verkeerdeRondes.setForeground(Color.RED);
                verkeerdeRondes.setText("getal moet hoger dan 0 zijn");
                verkeerdeRondes.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isValid;
    }

public static void main(String[] args) {
        JDialog d = new ToernooiToevoegen();
        d.setVisible(true);
        d.setSize(500,600);
    }
}