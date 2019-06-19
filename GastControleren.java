import javax.swing.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Medewerker krijgt een overzicht van alle gasten en heeft de mogelijkheid om ze te bewerken of te verwijderen
 */

class GastControleren extends JDialog {

    //JDIALOG
    private static final int width = 750;
    private static final int height = 500;
    private static final String title = "Gast Controleren";

    //ATTRIBUTES
    private int ID;
    private String naam;
    private String geslacht;
    private String geboortedatum;
    private String adres;
    private String postcode;
    private String woonplaats;
    private String telefoonnummer;
    private String email;
    private int rating;

    private DefaultListModel<Gast> model;
    private JList<Gast> gasten;

    private JButton bewerkBtn;
    private JButton terugBtn;
    private JButton verwijderBtn;

    GastControleren()  {

        JPanel panel = new JPanel();
        panel.setLayout(null);

        try { //tries to make a connection with the database

            model = new DefaultListModel<>();
            gasten = new JList<>(model);
            panel.add(gasten);
            JScrollPane sp = new JScrollPane(gasten);
            sp.setBounds(125,25,500,400);
            panel.add(sp);

            addList();

            if (model.isEmpty()) {
                JOptionPane.showMessageDialog(null, "er zijn nog geen gasten toegevoegd, voeg gasten toe vie 'gast toevoegen'");
            }
        } catch (Exception e) { //error message if there's no connection with the database
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
        ActionListener verwijder = new Verwijder(gasten);
        ActionListener bewerk = new Bewerk(gasten);

        bewerkBtn.addActionListener(bewerk);
        terugBtn.addActionListener(terug);
        verwijderBtn.addActionListener(verwijder);

        this.add(panel);
    }

    private void addList() {
        try {
        ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM gast");
        while (rs.next()) {
            ID = rs.getInt("ID");
            naam = rs.getString("naam");
            geslacht = rs.getString("geslacht");
            geboortedatum = rs.getString("geboortedatum");
            adres = rs.getString("adres");
            postcode = rs.getString("postcode");
            woonplaats = rs.getString("woonplaats");
            telefoonnummer = rs.getString("telefoonnummer");
            email = rs.getString("email");
            rating = rs.getInt("rating");

            Gast gast = new Gast(ID, naam, geslacht, geboortedatum, adres, postcode, woonplaats, telefoonnummer, email, rating);
            model.addElement(gast);
            }
        } catch (SQLException e) {
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
        private JList<Gast> list;

        private Bewerk(JList<Gast> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "kies een gast om te bewerken");
                }
                else {
                    class GastBewerken1 extends GastToevoegen {

                        private GastBewerken1() {
                            naamTxt.setText(list.getSelectedValue().getNaam());
                            if (list.getSelectedValue().getGeslacht().equals("man")) {
                                man.setSelected(true);
                            } else if (list.getSelectedValue().getGeslacht().equals("vrouw")) {
                                vrouw.setSelected(true);
                            }
                            adresTxt.setText(list.getSelectedValue().getAdres());

                            geboortedatum = list.getSelectedValue().getGeboortedatum();
                            Date date = Date.valueOf(geboortedatum);
                            String[] parts = date.toString().split("-");
                            int jaar = Integer.parseInt(parts[0]);
                            int maand = Integer.parseInt(parts[1]);
                            int dag = Integer.parseInt(parts[2]);

                            dayList.setSelectedIndex(dag-1);
                            monthList.setSelectedIndex(maand-1);
                            yearList.setSelectedIndex(jaar-1900);
                            postcodeTxt.setText(list.getSelectedValue().getPostcode());
                            woonplaatsTxt.setText(list.getSelectedValue().getWoonplaats());
                            telefoonnummerTxt.setText(list.getSelectedValue().getTelefoonnummer());
                            emailTxt.setText(list.getSelectedValue().getEmail());

                            voegToeBtn.setText("Wijzigen");
                            ListenerManager.deleteActionListeners(terugBtn1);
                            ListenerManager.deleteActionListeners(voegToeBtn);
                            ActionListener terug = new Terug();
                            terugBtn1.addActionListener(terug);
                            ActionListener wijzig = new Wijzig();
                            voegToeBtn.addActionListener(wijzig);
                        }

                        class Terug implements ActionListener {
                            public void actionPerformed(ActionEvent e) {
                                dispose();
                            }
                        }

                        class Wijzig implements ActionListener {
                            public void actionPerformed(ActionEvent e) {
                                ID = list.getSelectedValue().getID();
                                naam =  naamTxt.getText();
                                if (man.isSelected()) {
                                    geslacht = "man";
                                } else {
                                    geslacht = "vrouw";
                                }
                                day = dayList.getSelectedIndex() + 1;
                                month = monthList.getSelectedIndex() + 1;
                                year = yearList.getSelectedIndex() + 1900;
                                geboortedatum = year + "-" + month + "-" + day;
                                adres = adresTxt.getText();
                                postcode = postcodeTxt.getText();
                                woonplaats = woonplaatsTxt.getText();
                                telefoonnummer = telefoonnummerTxt.getText();
                                email = emailTxt.getText();

                                try {
                                    PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE gast SET naam = ?, geslacht = ?, geboortedatum = ?, adres = ?, postcode = ?, woonplaats = ?, telefoonnummer = ?, email = ?  WHERE ID = ?;");
                                    ps.setString(1, naam);
                                    ps.setString(2, geslacht);
                                    ps.setString(3, geboortedatum);
                                    ps.setString(4, adres);
                                    ps.setString(5, postcode);
                                    ps.setString(6, woonplaats);
                                    ps.setString(7, telefoonnummer);
                                    ps.setString(8, email);
                                    ps.setInt(9,ID);
                                    ps.executeUpdate();
                                    model.removeAllElements();
                                    addList();

                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(null,"Gast is gewijzigd");
                                dispose();
                            }
                        }
                    }
                    JDialog d = new GastBewerken1();
                    d.setSize(500, 500);
                    d.setTitle("Gast bewerken");
                    d.setResizable(false);
                    d.setVisible(true);
                    d.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    class Verwijder implements ActionListener {
        private JList<Gast> list;
        private Verwijder(JList<Gast> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null,"Kies een gast om te verwijderen");
                }
                else {
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