import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Medewerker krijgt een aparte Panel van alle masterclass inschrijvingen en heeft de mogelijkheid om ze te bewerken of te verwijderen
 */

class MasterclassInschrijvingControleren extends JDialog {

    //ATTRIBUTES
    int inschrijvingnr;
    int gastID;
    int masterclassID;
    String heeftBetaald;
    int ID;
    String naam;
    String geslacht;
    String geboortedatum;
    String adres;
    String postcode;
    String woonplaats;
    String telefoonnummer;
    String email;
    int rating;
    int ratingInv = 0;
    private String[] masterclassParts;
    private String filter;

    //COMPONENTS
    private DefaultListModel<MasterclassInschrijving> model;
    private DefaultListModel<Gast> model1;
    private JList<MasterclassInschrijving> inschrijvingen;
    private JList<Gast> ratings;

    private JLabel filterLbl;
    private JLabel gekozenFilterLbl;
    private JLabel gekozenFilterLbl2;
    private JButton nietBetaaldBtn;
    private JButton welBetaaldBtn;
    private JButton resetBtn;

    private JButton kiesMasterclass;
    private JButton wijzigen;
    private JButton verwijderen;
    private JButton terug;
    private TextField ratingTF;
    private JButton ratingBtn;

    MasterclassInschrijvingControleren() {

        this.setSize(750, 500);
        this.setVisible(true);
        this.setTitle("Masterclass inschrijving controleren");

        addComponents(); //zorgt ervoor dat alle components op het scherm komen
        addList(); //zorgt ervoor dat de list gevuld word

        class Wijzig implements ActionListener {
            private JList<MasterclassInschrijving> list;

            private Wijzig(JList<MasterclassInschrijving> list) {
                this.list = list;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (list.isSelectionEmpty()) {
                        JOptionPane.showMessageDialog(null, "Kies een gast om zijn betaling te wijzigen");
                    } else {
                        inschrijvingnr = list.getSelectedValue().inschrijvingnr;
                        heeftBetaald = list.getSelectedValue().heeftBetaald;

                        if (heeftBetaald.equals("nee")) { //wijzigt heeftBetaald naar ja
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE masterclass_inschrijving SET heeft_betaald = 'ja' where inschrijvingnr = ?;");
                            ps.setInt(1, inschrijvingnr);
                            ps.executeUpdate();
                            model.removeAllElements();
                            addList();
                            JOptionPane.showMessageDialog(null, "Betaling is gewijzigd");

                        } else if (heeftBetaald.equals("ja")) { //wijzigt heeftBetaald naar nee
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE masterclass_inschrijving set heeft_betaald = 'nee' where inschrijvingnr = ?;");
                            ps.setInt(1, inschrijvingnr);
                            ps.executeUpdate();
                            model.removeAllElements();
                            addList();
                            JOptionPane.showMessageDialog(null, "Betaling is gewijzigd");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        class Verwijder implements ActionListener {
            private JList<MasterclassInschrijving> list;

            private Verwijder(JList<MasterclassInschrijving> list) {
                this.list = list;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (list.isSelectionEmpty()) {
                        JOptionPane.showMessageDialog(null, "Kies een gast om te verwijderen");
                    } else {
                        inschrijvingnr = list.getSelectedValue().getInschrijvingnr();
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("DELETE FROM masterclass_inschrijving WHERE inschrijvingnr = ?");
                        ps.setInt(1, inschrijvingnr);
                        ps.executeUpdate();
                        model.removeAllElements();
                        addList();
                        JOptionPane.showMessageDialog(null, "Inschrijving is verwijderd");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        class Rating implements ActionListener {
            private JList<Gast> list;
            private Rating(JList<Gast> list) {
                this.list = list;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog d = new JDialog();
                d.setSize(750, 500);
                d.setTitle("Ratings controleren");
                d.setVisible(true);

                JPanel panel = new JPanel();
                panel.setLayout(null);
                d.add(panel);

                model1 = new DefaultListModel<>();
                ratings = new JList<>(model1);
                panel.add(ratings);
                JScrollPane sg = new JScrollPane(ratings);
                sg.setBounds(105, 25, 500, 400);
                panel.add(sg);

                //int rating = list.getSelectedValue().getRating();
                try {
                    if (ratingTF.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Voer een rating in");
                    } else {
                        ratingInv = Integer.parseInt(ratingTF.getText());
                        ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select * from masterclass_inschrijving join gast on gast = ID where rating >= " + ratingInv);
                        while (rs.next()) {
                            rating = rs.getInt("rating");
                            model1.removeAllElements();
                        }
                        addRating();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        ActionListener wijzig = new Wijzig(inschrijvingen);
        ActionListener verwijder = new Verwijder(inschrijvingen);
        ActionListener rating = new Rating(ratings);
        ratingBtn.addActionListener(rating);
        wijzigen.addActionListener(wijzig);
        verwijderen.addActionListener(verwijder);
    }

    private void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);
        filter = "geen";

        try {
            model = new DefaultListModel<>();
            inschrijvingen = new JList<>(model);
            panel.add(inschrijvingen);
            JScrollPane sp = new JScrollPane(inschrijvingen);
            sp.setBounds(105, 25, 500, 400);
            panel.add(sp);

            filterLbl = new JLabel("filter op:");
            filterLbl.setBounds(625,25,75,15);
            panel.add(filterLbl);

            gekozenFilterLbl = new JLabel("<html><center>gekozen filter is:</center></html>");
            gekozenFilterLbl.setBounds(625,200,125,15);
            panel.add(gekozenFilterLbl);

            gekozenFilterLbl2 = new JLabel();
            gekozenFilterLbl2.setBounds(625,215,125,15);
            panel.add(gekozenFilterLbl2);

            nietBetaaldBtn = new JButton(new AbstractAction("Niet betaald") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filter = "niet";
                    addList();
                    gekozenFilterLbl2.setText("<html><b>niet betaald</b></html>");
                }
            });
            nietBetaaldBtn.setBounds(605,60,105,30);
            panel.add(nietBetaaldBtn);

            welBetaaldBtn = new JButton(new AbstractAction("wel betaald") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filter = "wel";
                    addList();
                    gekozenFilterLbl2.setText("<html><b>wel betaald</b></html>");
                }
            });
            welBetaaldBtn.setBounds(605,100,105,30);
            panel.add(welBetaaldBtn);

            resetBtn = new JButton(new AbstractAction("geen filter") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    filter = "geen";
                    addList();
                    gekozenFilterLbl2.setText("<html><b>geen filter</b></html>");
                }
            });
            resetBtn.setBounds(605,140,105,30);
            panel.add(resetBtn);

            kiesMasterclass = new JButton(new AbstractAction("<html>kies masterclass</html>") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog d = new MasterclassKiezen();
                    d.setSize(625,400);
                    d.setVisible(true);
                }
            });
            kiesMasterclass.setBounds(0,5,105,50);
            panel.add(kiesMasterclass);

            wijzigen = new JButton("<html>Betaling wijzigen</html>");
            wijzigen.setToolTipText("Klik op een gast en deze knop om zijn betaling te wijzigen");
            wijzigen.setBounds(0, 75, 105, 50);
            panel.add(wijzigen);

            verwijderen = new JButton("Verwijderen");
            verwijderen.setBounds(0, 145, 105, 30);
            panel.add(verwijderen);

            terug = new JButton(new AbstractAction("Terug") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            terug.setBounds(0, 195, 105, 30);
            panel.add(terug);

            ratingBtn = new JButton("Rating knop");
            ratingBtn.setBounds(0, 245, 105, 30);
            panel.add(ratingBtn);

            ratingTF = new TextField() {
            };
            ratingTF.setBounds(0, 295, 105, 30);
            panel.add(ratingTF);


        } catch (Exception e) {
            e.printStackTrace();
        }
        add(panel);
    }

    private void addList() {
        model.removeAllElements();
        try {

            ResultSet rs1 = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM masterclass_inschrijving where masterclass = " + masterclassID + ";");
            ResultSet rs2 = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * from masterclass_inschrijving where masterclass = " + masterclassID + " and heeft_betaald = 'nee';");
            ResultSet rs3 = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * from masterclass_inschrijving where masterclass = " + masterclassID + " and heeft_betaald = 'ja';");
            ResultSet rs;

            if (filter.equals("wel")) {
                rs = rs3;
            } else if (filter.equals("niet")) {
                rs = rs2;
            } else {
                rs = rs1;
            }

            while (rs.next()) {
                inschrijvingnr = rs.getInt(1);
                gastID = rs.getInt(2);
                masterclassID = rs.getInt(3);
                heeftBetaald = rs.getString(4);

                MasterclassInschrijving ti = new MasterclassInschrijving(inschrijvingnr, gastID, masterclassID, heeftBetaald);
                model.addElement(ti);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRating() {
        try {
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select * from masterclass_inschrijving join gast on gast = ID where rating >= " + ratingInv);
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

                Gast rd = new Gast(ID, naam, geslacht, geboortedatum, adres, postcode, woonplaats, telefoonnummer, email, rating);
                model1.addElement(rd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class MasterclassKiezen extends JDialog {

        MasterclassKiezen() {
            this.setTitle("Masterclass kiezen");
            JPanel toernooiPanel = new JPanel();
            toernooiPanel.setLayout(null);
            DefaultListModel<Object> model = new DefaultListModel<>();
            JList<Object> toernooien = new JList<>(model);
            toernooiPanel.add(toernooien);
            JScrollPane sp = new JScrollPane(toernooien);
            sp.setBounds(0,0,550,300);

            toernooiPanel.add(sp);

            this.add(toernooiPanel);

            try {
                ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM masterclass");
                while (rs.next()) {
                    int ID = rs.getInt("ID");
                    String datum = rs.getString("datum");
                    String locatie = rs.getString("locatie");
                    String begintijd = rs.getString("begintijd");
                    String eindtijd = rs.getString("eindtijd");
                    int kosten = rs.getInt("kosten");
                    int minRating = rs.getInt("min_rating");
                    String bekendePokerspeler = rs.getString("bekende_pokerspeler");

                    Masterclass masterclass = new Masterclass(ID, datum, locatie, begintijd, eindtijd, kosten, minRating, bekendePokerspeler);
                    model.addElement(masterclass);
                }

                JButton kies = new JButton(new AbstractAction("Kies") {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (toernooien.isSelectionEmpty()) {
                            JOptionPane.showMessageDialog(null,"Kies een masterclass");
                        } else {

                            masterclassParts = toernooien.getSelectedValue().toString().split(" ");
                            masterclassID = Integer.parseInt(masterclassParts[1]);

                            addList();
                            dispose();
                        }
                    }
                });
                kies.setBounds(0,325,75,25);
                toernooiPanel.add(kies);

                JButton terug = new JButton(new AbstractAction("Terug") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                terug.setBounds(100,325,75,25);
                toernooiPanel.add(terug);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}