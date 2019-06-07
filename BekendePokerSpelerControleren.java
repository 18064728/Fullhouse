import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Medewerker krijgt een overzicht van alle bekende pokerspelers en heeft de mogelijkheid om ze te bewerken of te verwijderen
 */

public class BekendePokerSpelerControleren extends JDialog{

    //JDIALOG
    private static final int width = 300;
    private static final int height = 200;
    private static final String title = "Bekende Pokerspelers Controleren";

    //ATTRIBUTES
    private String naam;

    private DefaultListModel<BekendePokerspeler> model;
    private JList<BekendePokerspeler> bekendePokerspelers;

    private JButton bewerkBtn;
    private JButton terugBtn;
    private JButton verwijderBtn;

    BekendePokerSpelerControleren(){

        JPanel panel = new JPanel();
        panel.setLayout(null);
        addList();

        try { //tries to make a connection with the database
            model = new DefaultListModel<>();
            bekendePokerspelers = new JList<BekendePokerspeler>(model);
            panel.add(bekendePokerspelers);
            JScrollPane sp = new JScrollPane(bekendePokerspelers);
            sp.setBounds(125,25,500,400);
            panel.add(sp);

            if (model.isEmpty()) {
                JOptionPane.showMessageDialog(null, "er zijn nog geen bekende pokerspelers toegevoegd, voeg bekende pokerspelers toe vie 'Bekende Pokerspeler toevoegen'");
            }

        } catch (Exception e) { //error message if there's no connection with the database
            System.out.println("can't connect to the database, please contact tech-support");
            e.printStackTrace();
        }

        this.setSize(width, height);
        this.setTitle(title);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);

        bewerkBtn = new JButton("Bewerken");
        terugBtn = new JButton("Terug");
        verwijderBtn = new JButton("Verwijder");

        bewerkBtn.setBounds(0,0,100,30);
        terugBtn.setBounds(0,55,100,30);
        verwijderBtn.setBounds(0,110,100,30);

        panel.add(bewerkBtn);
        panel.add(terugBtn);
        panel.add(verwijderBtn);

        ActionListener terug = new Terug();
        ActionListener verwijder = new Verwijder(bekendePokerspelers);
        ActionListener bewerk = new Bewerk(bekendePokerspelers);

        bewerkBtn.addActionListener(bewerk);
        terugBtn.addActionListener(terug);
        verwijderBtn.addActionListener(verwijder);

        this.add(panel);

    }

    public void addList() {
        try {
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM bekende_pokerspeler");
            while (rs.next()) {
                naam = rs.getString("naam");

                BekendePokerspeler bekendepokerspeler = new BekendePokerspeler(naam);
                model.addElement(bekendepokerspeler);
            }
        } catch (Exception e) {
            System.out.println("er ging iets mis");
            e.printStackTrace();
        }
    }

    /**
     * gaat terug naar class 'Gasten'
     */
    class Terug implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    /**
     * opent formulier met gegevens van gekozen gast zodat die bewerkt kan worden
     */
    class Bewerk implements ActionListener {
        private JList<BekendePokerspeler> list;

        private Bewerk(JList<BekendePokerspeler> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "kies een bekende pokerspeler om te bewerken");
                }
                else {
                    //hetzelfde formulier als van BekendePokerSpelerToevoegen
                    class BekendePokerspelerBewerken extends BekendePokerSpelerToevoegen {

                        private BekendePokerspelerBewerken() {
                            naamBekendTxt.setText(list.getSelectedValue().getNaam());

                            voegToeBtnBP.setText("Wijzigen");
                            ListenerManager.deleteActionListeners(terugBtnBP);
                            ListenerManager.deleteActionListeners(voegToeBtnBP);
                            ActionListener terug = new Terug();
                            terugBtnBP.addActionListener(terug);
                            ActionListener wijzig = new Wijzig();
                            voegToeBtnBP.addActionListener(wijzig);

                            model.removeAllElements();
                            addList();
                        }

                        class Terug implements ActionListener {
                            public void actionPerformed(ActionEvent e) {
                                dispose();
                            }
                        }

                        class Wijzig implements ActionListener {
                            public void actionPerformed(ActionEvent e) {
                                naam =  naamBekendTxt.getText();

                                try {
                                    PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE bekende_pokerspeler SET naam = ?");
                                    ps.setString(1, naam);
                                    ps.executeUpdate();
                                    System.out.println("Bekende Pokerspeler is bijgewerkt");
                                } catch (Exception e1) {
                                    System.out.println("Something went wrong");
                                    e1.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(null,"Bekende Pokerspeler is gewijzigd");
                                dispose();
                            }
                        }
                    }

                    JDialog d = new BekendePokerspelerBewerken();
                    d.setSize(500, 500);
                    d.setTitle("Bekende Pokerspeler bewerken");
                    d.setResizable(false);
                    d.setVisible(true);
                    d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    /**
     * verwijderd gast van database
     */
    class Verwijder implements ActionListener {
        private JList<BekendePokerspeler> list;
        private Verwijder(JList<BekendePokerspeler> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null,"Kies een Bekende Pokerspeler om te verwijderen");
                }
                else {
                    String Naam = list.getSelectedValue().getNaam();
                    ConnectionManager.getConnection().createStatement().execute("DELETE FROM bekende_pokerspeler WHERE naam = " + Naam);
                    model.removeElement(list.getSelectedValue());
                }
            } catch (SQLException e1){
                e1.printStackTrace();
            }
        }
    }


}
