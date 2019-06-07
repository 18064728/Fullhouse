import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Medewerker vult de naam van de Meesterspeler in en drukt op 'voeg Meesterspeler toe' om de Meesterspeler toe te voegen aan de database
 * of drukt op 'terug' om terug te gaan naar 'masterclasses'
 */
public class BekendePokerSpelerToevoegen extends JDialog {

    //JDIALOG
    private static final int width = 300;
    private static final int height = 200;
    private static final String title = "Meesterspeler toevoegen";

    //ATTRIBUTES
    private String naam;
    JButton voegToeBtnBP;
    JButton terugBtnBP;

    //COMPONENTS
    JFormattedTextField naamBekendTxt;

    BekendePokerSpelerToevoegen() {

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

        //name
        JLabel naamLbl = new JLabel("naam: ");
        naamLbl.setBounds(5,25,50,25);
        panel.add(naamLbl);

        naamBekendTxt = new JFormattedTextField();
        naamBekendTxt.setBounds(50,25,140,25);
        panel.add(naamBekendTxt);

        JButton voegToeBtnBP = new JButton("voeg meesterspeler toe");
        voegToeBtnBP.setBounds(5,75,160,25);
        panel.add(voegToeBtnBP);

        JButton terugBtnBP = new JButton("terug");
        terugBtnBP.setBounds(165,75,100,25);
        panel.add(terugBtnBP);

        add(panel);

        //voegt de meesterpeler toe aan de database
        class VoegToe implements ActionListener {
            public void actionPerformed(ActionEvent e) {

                naam = naamBekendTxt.getText();
                System.out.println(naam);

                if (isValidInput()) {
                    try { //adds toernooi to database
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("INSERT INTO bekende_pokerspeler (naam) VALUES (?);");
                        ps.setString(1, naam);
                        ps.executeUpdate();
                        System.out.println("Meesterspeler is added to the database");
                        naamBekendTxt.setText("");
                        //new Meesterspelers();
                    } catch (Exception ex) {
                        System.out.println("\nSomething went wrong");
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null,"niet alle velden zijn correct ingevuld");
                }
            }
        }

        //gaat terug naar 'meesterspeler'
        class Terug implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }
        ActionListener voegToe = new VoegToe();
        ActionListener terug = new Terug();
        voegToeBtnBP.addActionListener(voegToe);
        terugBtnBP.addActionListener(terug);
    }


    //geeft true als alle velden juist zijn ingevuld
    private boolean isValidInput() {
        if (naam.matches(".*\\d.*")) {
            System.out.println("naam mag geen nummers bevatten");
            naamBekendTxt.setText("naam mag geen nummers bevatten");
            return false;
        } else {
            return true;
        }
    }
}
