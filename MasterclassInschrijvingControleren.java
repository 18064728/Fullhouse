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
    int rating;
    int ratingInv = 0;

    //COMPONENTS
    private DefaultListModel<MasterclassInschrijving> model;
    private JList<MasterclassInschrijving> inschrijvingen;

    private JButton wijzigen;
    private JButton verwijderen;
    private JButton terug;
    private TextField ratingTF;
    private JButton ratingBtn;

    MasterclassInschrijvingControleren() {

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
                            JOptionPane.showMessageDialog(null,"Betaling is gewijzigd");

                        } else if (heeftBetaald.equals("ja")) { //wijzigt heeftBetaald naar nee
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE masterclass_inschrijving set heeft_betaald = 'nee' where inschrijvingnr = ?;");
                            ps.setInt(1,inschrijvingnr);
                            ps.executeUpdate();
                            model.removeAllElements();
                            addList();
                            JOptionPane.showMessageDialog(null,"Betaling is gewijzigd");
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
                        JOptionPane.showMessageDialog(null,"Inschrijving is verwijderd");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        class Rating implements ActionListener{
            private JList<MasterclassInschrijving> list;

            private Rating(JList<MasterclassInschrijving> list) {
                this.list = list;
            }
            @Override
            public void actionPerformed(ActionEvent e){
                JDialog d = new JDialog();
                d.setSize(750,500);
                d.setTitle("Ratings controleren");
                d.setVisible(true);

                JPanel panel = new JPanel();
                //int rating = list.getSelectedValue().getRating();
                try {
                    if (ratingTF.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Voer een rating in");
                    } else {
                        ratingInv = Integer.parseInt(ratingTF.getText());
                        ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select * from masterclass_inschrijving join gast on gast = ID where rating >= " + ratingInv);
                        while(rs.next()){
                            rating = rs.getInt("rating");
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        this.setSize(750,500);
        this.setVisible(true);
        this.setTitle("Masterclass inschrijving controleren");

        ActionListener wijzig = new Wijzig(inschrijvingen);
        ActionListener verwijder = new Verwijder(inschrijvingen);
        ActionListener rating = new Rating(inschrijvingen);
        ratingBtn.addActionListener(rating);
        wijzigen.addActionListener(wijzig);
        verwijderen.addActionListener(verwijder);
    }

    private void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        try {
            model = new DefaultListModel<>();
            inschrijvingen = new JList<>(model);
            panel.add(inschrijvingen);
            JScrollPane sp = new JScrollPane(inschrijvingen);
            sp.setBounds(105, 25, 500, 400);
            panel.add(sp);

            wijzigen = new JButton("<html>Betaling wijzigen</html>");
            wijzigen.setToolTipText("Klik op een gast en deze knop om zijn betaling te wijzigen");
            wijzigen.setBounds(0, 5, 105, 50);
            panel.add(wijzigen);

            verwijderen = new JButton("Verwijderen");
            verwijderen.setBounds(0, 80, 105, 30);
            panel.add(verwijderen);

            terug = new JButton(new AbstractAction("Terug") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            terug.setBounds(0, 130, 105, 30);
            panel.add(terug);

            ratingBtn = new JButton("Rating knop");
            ratingBtn.setBounds(0, 180, 105, 30);
            panel.add(ratingBtn);

            ratingTF = new TextField() {};
            ratingTF.setBounds(0, 230, 105, 30);
            panel.add(ratingTF);


        } catch (Exception e) {
            e.printStackTrace();
        }
        add(panel);
    }

    private void addList() {
        try {
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM masterclass_inschrijving");
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
}