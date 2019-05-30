package fullhouse;

import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Medewerker krijgt een overzicht van alle gasten en heeft de mogelijkheid om ze aan te passen of te verwijderen
 */

public class Gast_Controleren extends JDialog {

    //JDIALOG
    private static final int width = 750;
    private static final int height = 500;
    private static final String title = "Gast Controleren";

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

    private DefaultListModel<Gast> model;
    private JScrollPane sp;
    protected JList<Gast> gasten;
    private Gast gast;

    //COMPONENTS
    private JButton bewerkBtn;
    private JButton verwijderBtn;
    private JButton terugBtn;

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
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM gast");
            System.out.println("connected to database");

            model = new DefaultListModel<>();
            gasten = new JList<>(model);
            panel.add(gasten);
            sp = new JScrollPane(gasten);
            sp.setBounds(125,25,500,400);
            panel.add(sp);

            while (rs.next()) {
                ID = rs.getInt("ID");
                naam = rs.getString("naam");
                geslacht = rs.getString("geslacht");
                geboortedatum = rs.getDate("geboortedatum");
                adres = rs.getString("adres");
                postcode = rs.getString("postcode");
                woonplaats = rs.getString("woonplaats");
                telefoonnummer = rs.getString("telefoonnummer");
                email = rs.getString("email");

                gast = new Gast(ID, naam, geslacht, geboortedatum, adres, postcode, woonplaats, telefoonnummer, email);
                model.addElement(gast);
            }
        } catch (SQLException e) { //error message if there's no connection with the database
            System.out.println("can't connect to the database, please contact tech-support");
            e.printStackTrace();
        }

        bewerkBtn = new JButton("bewerken");
        terugBtn = new JButton("terug");
        verwijderBtn = new JButton("verwijder");

        bewerkBtn.setBounds(0,0,100,30);
        terugBtn.setBounds(0,55,100,30);
        verwijderBtn.setBounds(0,110,100,30);

        panel.add(bewerkBtn);
        panel.add(terugBtn);
        panel.add(verwijderBtn);

        ActionListener terug = new Terug();
        ActionListener verwijder = new Verwijder(gasten);
        ActionListener bewerk = new Bewerk(gasten);

        bewerkBtn.addActionListener(bewerk);
        terugBtn.addActionListener(terug);
        verwijderBtn.addActionListener(verwijder);

        add(panel);
    } 
    
    /**
     * gaat terug naar class 'Gasten'
     */
    class Terug implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
            new Gasten();
        }
    }

    /**
     * opent formulier met gegevens van gekozen gast zodat die bewerkt kan worden
     */
    class Bewerk implements ActionListener {
        private JList<Gast> list;

        public Bewerk(JList<Gast> list) {
            this.list = list;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "kies een gast om te bewerken");
                } else {
                    ID = list.getSelectedValue().getID();
                    naam = list.getSelectedValue().getNaam();
                    geslacht = list.getSelectedValue().getGeslacht();
                    geboortedatum = list.getSelectedValue().getGeboortedatum();
                    adres = list.getSelectedValue().getAdres();
                    postcode = list.getSelectedValue().getPostcode();
                    woonplaats = list.getSelectedValue().getWoonplaats();
                    telefoonnummer = list.getSelectedValue().getTelefoonnummer();
                    email = list.getSelectedValue().getEmail();

                    /**
                     * new form with values from selected Gast
                     */
                    class Gast_Bewerken1 extends Gast_Toevoegen {

                        public Gast_Bewerken1() {
                            naamTxt.setText(list.getSelectedValue().getNaam());
                            if (list.getSelectedValue().getGeslacht().equals("man")) {
                                man.setSelected(true);
                            } else if (list.getSelectedValue().getGeslacht().equals("vrouw")) {
                                vrouw.setSelected(true);
                            }
                            adresTxt.setText(list.getSelectedValue().getAdres());
                            //geboortedatum moet nog pre-selected worden
                            postcodeTxt.setText(list.getSelectedValue().getPostcode());
                            woonplaatsTxt.setText(list.getSelectedValue().getWoonplaats());
                            telefoonnummerTxt.setText(list.getSelectedValue().getTelefoonnummer());
                            emailTxt.setText(list.getSelectedValue().getEmail());

                            ActionListener terug = new Terug();
                            terugBtn.addActionListener(terug);
                        }

                        class Terug implements ActionListener { //listener werk niet goed omdat het class 'Gast_Toevoegen' extend
                            public void actionPerformed(ActionEvent e) {
                                dispose();
                            }
                        }
                    }
                    JDialog d = new Gast_Bewerken1();
                    d.setSize(500, 500);
                    d.setTitle("gast bewerken");
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
        private JList<Gast> list;
        public Verwijder(JList<Gast> list) {
            this.list = list;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null,"kies een gast om te bewerken");
                } else {
                    int ID = list.getSelectedValue().getID();
                    ConnectionManager.getConnection().createStatement().execute("DELETE FROM gast WHERE id = " + ID);
                    model.removeElement(list.getSelectedValue());
                }
            } catch (SQLException e1){
                    e1.printStackTrace();
            }
        }
    }
}

