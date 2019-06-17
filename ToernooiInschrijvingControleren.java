import jdk.nashorn.internal.scripts.JD;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Medewerker krijgt een aparte Panel van alle toernooi inschrijvingen en heeft de mogelijkheid om ze te bewerken of te verwijderen
 */

class ToernooiInschrijvingControleren extends JDialog {

    //ATTRIBUTES
    private int inschrijvingnr;
    private int gastID;
    private int toernooiID;
    private String heeftBetaald;
    private int ID;
    private String datum;
    private String locatie;
    private String begintijd;
    private String eindtijd;
    private String beschrijving;
    private String conditie;
    private int maxInschrijvingen;
    private int inleggeld;
    private String inschrijfdatum;
    private String toernooilijst;
    private String toernooinr;
    private String naamgast;

    //COMPONENTS
    private DefaultListModel<Toernooi> model;
    private JList<Toernooi> toernooi;

    private JButton wijzigen;
    //    private JButton verwijderen;
    private JButton terug;
    private JButton overzichtbspelersBtn;


    ToernooiInschrijvingControleren() {

        addComponents(); //zorgt ervoor dat alle components op het scherm komen
        addList(); //zorgt ervoor dat de list gevuld word

        class Wijzig implements ActionListener {
            private JList<ToernooiInschrijving> list;

            private Wijzig(JList<ToernooiInschrijving> list) {
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
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE toernooi_inschrijving SET heeft_betaald = 'ja' where inschrijvingsnr = ?;");
                            ps.setInt(1, inschrijvingnr);
                            ps.executeUpdate();
                            model.removeAllElements();
                            addList();
                            JOptionPane.showMessageDialog(null, "Betaling is gewijzigd");

                        } else if (heeftBetaald.equals("ja")) { //wijzigt heeftBetaald naar nee
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE toernooi_inschrijving set heeft_betaald = 'nee' where inschrijvingsnr = ?;");
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

//        class Verwijder implements ActionListener {
//            private JList<ToernooiInschrijving> list;
//
//            private Verwijder(JList<ToernooiInschrijving> list) {
//                this.list = list;
//            }
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    if (list.isSelectionEmpty()) {
//                        JOptionPane.showMessageDialog(null, "Kies een gast om te verwijderen");
//                    } else {
//                        inschrijvingnr = list.getSelectedValue().getInschrijvingnr();
//                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("DELETE FROM toernooi_inschrijving WHERE inschrijvingsnr = ?");
//                        ps.setInt(1, inschrijvingnr);
//                        ps.executeUpdate();
//                        model.removeAllElements();
//                        addList();
//                        JOptionPane.showMessageDialog(null,"Inschrijving is verwijderd");
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }

        this.setSize(750, 500);
        this.setVisible(true);
        this.setTitle("Toernooi inschrijving controleren");

//        ActionListener wijzig = new Wijzig(inschrijvingen);
//        ActionListener verwijder = new Verwijder(inschrijvingen);
//        wijzigen.addActionListener(wijzig);
//        verwijderen.addActionListener(verwijder);
    }

    class overzichtbetaaldespelers extends JDialog implements ActionListener {
        private static final int width = 750;
        private static final int height = 500;
        private static final String title = "Overzicht van de spelers";

        private JList<Toernooi> toernooilijst;

        JPanel panel;
        JButton overzicht;
        JButton terugvorigescherm;

        @Override
        public void actionPerformed(ActionEvent e) {
        try
        {
            if (toernooilijst.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(null, "Kies een toernooi om te wijzigen");
            } else {
                ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select ID, naam from toernooi_inschrijving join gast on gast = ID where heeft_betaald = 'nee'");
                while(rs.next()){
                    toernooinr = rs.getString("toernooi");
                    naamgast = rs.getString("naam");
                }
            }
        }
        catch(
        Exception ex)

        {
            ex.printStackTrace();
        }


            this.setSize(width, height);
            this.setTitle(title);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.setVisible(true);

            panel = new JPanel();
            panel.setLayout(null);
            this.add(panel);

            overzicht = new JButton(new AbstractAction("overzicht") {
                public void actionPerformed(ActionEvent e){
                }
            });
            overzicht.setBounds(0,80,180,30);
            panel.add(overzicht);

                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog d = new overzichtbetaaldespelers();
                    d.setSize(575, 400);
                    d.setVisible(true);

                }
            });

            terugvorigescherm = new JButton(new AbstractAction("Terug") {
                @Override
                public void actionPerformed(ActionEvent e) {
                   dispose();
                }
            });
            terugvorigescherm.setBounds(0,140, 180, 30 );
            panel.add(terugvorigescherm);

        }


    private void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        try {
            model = new DefaultListModel<>();
            toernooi = new JList<>(model);
            panel.add(toernooi);
            JScrollPane sp = new JScrollPane(toernooi);
            sp.setBounds(185, 25, 500, 400);
            panel.add(sp);

            overzichtbspelersBtn = new JButton("<html>Overzicht betaalde spelers</html>");
            overzichtbspelersBtn.setBounds(0, 80, 180, 50);
            panel.add(overzichtbspelersBtn);
            ActionListener overzichtbetaaldespelers = new overzichtbetaaldespelers();
            overzichtbspelersBtn.addActionListener(overzichtbetaaldespelers);


            wijzigen = new JButton("<html>Betaling wijzigen</html>");
            wijzigen.setToolTipText("Klik op een gast en deze knop om zijn betaling te wijzigen");
            wijzigen.setBounds(0, 5, 180, 50);
            panel.add(wijzigen);

//            verwijderen = new JButton("Verwijderen");
//            verwijderen.setBounds(0, 80, 105, 30);
//            panel.add(verwijderen);

            terug = new JButton(new AbstractAction("Terug") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            terug.setBounds(0, 140, 180, 30);
            panel.add(terug);


        } catch (Exception e) {
            e.printStackTrace();
        }
        add(panel);
    }

    private void addList() {
        try {
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM toernooi");
            while (rs.next()) {
                ID = rs.getInt("ID");
                datum = rs.getString("datum");
                locatie = rs.getString("locatie");
                begintijd = rs.getString("begintijd");
                eindtijd = rs.getString("eindtijd");
                beschrijving = rs.getString("beschrijving");
                conditie = rs.getString("conditie");
                maxInschrijvingen = rs.getInt("max_inschrijvingen");
                inleggeld = rs.getInt("inleggeld");
                inschrijfdatum = rs.getString("inschrijfdatum");

                Toernooi toernooi = new Toernooi(ID, datum, locatie, begintijd, eindtijd, beschrijving, conditie, maxInschrijvingen, inleggeld, inschrijfdatum);
                model.addElement(toernooi);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}