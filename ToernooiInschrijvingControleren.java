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
    //private int tafel;
    private String[] toernooiParts;
    private String filter;

    //COMPONENTS
    private DefaultListModel<ToernooiInschrijving> model;
    private JList<ToernooiInschrijving> inschrijvingen;

    private JLabel filterLbl;
    private JLabel gekozenFilterLbl;
    private JLabel gekozenFilterLbl2;
    private JButton nietBetaaldBtn;
    private JButton welBetaaldBtn;
    private JButton resetBtn;

    private JButton kiesToernooi;
    private JButton wijzigen;
    private JButton verwijderen;
    private JButton terug;

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
                    if (model.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "kies eerst een toernooi");
                    } else {
                    if ( list.isSelectionEmpty()) {
                        JOptionPane.showMessageDialog(null, "Kies een gast om zijn betaling te wijzigen");
                    } else {
                        inschrijvingnr = list.getSelectedValue().inschrijvingnr;
                        heeftBetaald = list.getSelectedValue().heeftBetaald;

                        if (heeftBetaald.equals("nee")) { //wijzigt heeftBetaald naar ja
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE toernooi_inschrijving SET heeft_betaald = 'ja' where inschrijvingsnr = ?;");
                            ps.setInt(1, inschrijvingnr);
                            ps.executeUpdate();
                            addList();
                            JOptionPane.showMessageDialog(null, "Betaling is gewijzigd");

                        } else if (heeftBetaald.equals("ja")) { //wijzigt heeftBetaald naar nee
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE toernooi_inschrijving set heeft_betaald = 'nee' where inschrijvingsnr = ?;");
                            ps.setInt(1, inschrijvingnr);
                            ps.executeUpdate();
                            addList();
                            JOptionPane.showMessageDialog(null, "Betaling is gewijzigd");
                        }
                    }
                }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        class Verwijder implements ActionListener {
            private JList<ToernooiInschrijving> list;

            private Verwijder(JList<ToernooiInschrijving> list) {
                this.list = list;
            }
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (list.isSelectionEmpty()) {
                        JOptionPane.showMessageDialog(null, "Kies een gast om te verwijderen");
                    } else {
                        inschrijvingnr = list.getSelectedValue().getInschrijvingnr();
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("DELETE FROM toernooi_inschrijving WHERE inschrijvingsnr = ?");
                        ps.setInt(1, inschrijvingnr);
                        ps.executeUpdate();
                        addList();
                        JOptionPane.showMessageDialog(null,"Inschrijving is verwijderd");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        this.setSize(730,475);
        this.setVisible(true);
        this.setTitle("Toernooi inschrijving controleren");

        ActionListener wijzig = new Wijzig(inschrijvingen);
        ActionListener verwijder = new Verwijder(inschrijvingen);
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

            kiesToernooi = new JButton(new AbstractAction("<html>Kies toernooi</html>") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog d = new ToernooiKiezen();
                    d.setSize(600,400);
                    d.setVisible(true);
                }
            });
            kiesToernooi.setBounds(0,5,105,30);
            panel.add(kiesToernooi);

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

            wijzigen = new JButton("<html>Betaling wijzigen</html>");
            wijzigen.setToolTipText("Klik op een gast en deze knop om zijn betaling te wijzigen");
            wijzigen.setBounds(0, 55, 105, 30);
            panel.add(wijzigen);

            verwijderen = new JButton("Verwijderen");
            verwijderen.setBounds(0, 110, 105, 30);
            panel.add(verwijderen);

            terug = new JButton(new AbstractAction("Terug") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            terug.setBounds(0, 160, 105, 30);
            panel.add(terug);

        } catch (Exception e) {
            e.printStackTrace();
        }
        add(panel);
    }

    private void addList() {
        model.removeAllElements();
        try {
            ResultSet rs1 = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM toernooi_inschrijving where toernooi = " + toernooiID + ";");
            ResultSet rs2 = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * from toernooi_inschrijving where toernooi = " + toernooiID + " and heeft_betaald = 'nee';");
            ResultSet rs3 = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * from toernooi_inschrijving where toernooi = " + toernooiID + " and heeft_betaald = 'ja';");
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
                toernooiID = rs.getInt(3);
                heeftBetaald = rs.getString(4);
                ToernooiInschrijving ti = new ToernooiInschrijving(inschrijvingnr, gastID, toernooiID, heeftBetaald);
                model.addElement(ti);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ToernooiKiezen extends JDialog {

        ToernooiKiezen() {
            this.setTitle("Toernooi kiezen");
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
                ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM toernooi");
                while (rs.next()) {
                    int ID = rs.getInt("ID");
                    String datum = rs.getString("datum");
                    String locatie = rs.getString("locatie");
                    String begintijd = rs.getString("begintijd");
                    String eindtijd = rs.getString("eindtijd");
                    String beschrijving = rs.getString("beschrijving");
                    String conditie = rs.getString("conditie");
                    int maxInschrijvingen = rs.getInt("max_inschrijvingen");
                    int inleggeld = rs.getInt("inleggeld");
                    String inschrijfdatum = rs.getString("inschrijfdatum");

                    Toernooi toernooi = new Toernooi(ID, datum, locatie, begintijd, eindtijd, beschrijving, conditie, maxInschrijvingen, inleggeld, inschrijfdatum);
                    model.addElement(toernooi);
                }

                JButton kies = new JButton(new AbstractAction("Kies") {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (toernooien.isSelectionEmpty()) {
                            JOptionPane.showMessageDialog(null,"Kies een toernooi");
                        } else {

                            toernooiParts = toernooien.getSelectedValue().toString().split(" ");
                            toernooiID = Integer.parseInt(toernooiParts[1]);

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