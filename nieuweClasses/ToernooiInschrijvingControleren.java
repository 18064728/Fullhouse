import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ToernooiInschrijvingControleren extends JDialog {

    //ATTRIBUTES
    int inschrijvingnr;
    int gastID;
    int toernooiID;
    String heeftBetaald;

    //COMPONENTS
    private DefaultListModel<ToernooiInschrijving> model;
    private JList<ToernooiInschrijving> inschrijvingen;

    private JButton wijzigen;
    private JButton verwijderen;
    private JButton terug;
    private JButton welBetaald;
    private JButton nietBetaald;
    private JButton geenFilter;
    private JLabel filter;

    public ToernooiInschrijvingControleren() {

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
                            JOptionPane.showMessageDialog(null,"betaling is gewijzigd");

                        } else if (heeftBetaald.equals("ja")) { //wijzigt heeftBetaald naar nee
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE toernooi_inschrijving set heeft_betaald = 'nee' where inschrijvingsnr = ?;");
                            ps.setInt(1,inschrijvingnr);
                            ps.executeUpdate();
                            model.removeAllElements();
                            addList();
                            JOptionPane.showMessageDialog(null,"betaling is gewijzigd");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("er ging iets mis");
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
                        model.removeAllElements();
                        addList();
                        JOptionPane.showMessageDialog(null,"inschrijving is verwijderd");
                    }
                } catch (Exception ex) {
                    System.out.println("er ging iets mis");
                    ex.printStackTrace();
                }
            }

        }


        ActionListener wijzig = new Wijzig(inschrijvingen);
        ActionListener verwijder = new Verwijder(inschrijvingen);
        wijzigen.addActionListener(wijzig);
        verwijderen.addActionListener(verwijder);
    }

    public void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        try {
            model = new DefaultListModel<>();
            inschrijvingen = new JList<>(model);
            panel.add(inschrijvingen);
            JScrollPane sp = new JScrollPane(inschrijvingen);
            sp.setBounds(105, 25, 500, 400);
            panel.add(sp);

            wijzigen = new JButton("<html>betaling wijzigen</html>");
            wijzigen.setToolTipText("klik op een gast en deze knop om zijn betaling te wijzigen");
            wijzigen.setBounds(0, 5, 105, 50);
            panel.add(wijzigen);

            verwijderen = new JButton("verwijderen");
            verwijderen.setBounds(0, 80, 105, 30);
            panel.add(verwijderen);

            terug = new JButton(new AbstractAction("terug") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            terug.setBounds(0, 130, 105, 30);
            panel.add(terug);

            filter = new JLabel("filter op");
            filter.setBounds(650,25,50,25);
            panel.add(filter);

            welBetaald = new JButton(new AbstractAction("wel betaald") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //todo code toevoegen zodat alleen gasten die wel betaald hebben te zien zijn
                }
            });
            welBetaald.setBounds(605,75,145,25);
            panel.add(welBetaald);

            nietBetaald = new JButton(new AbstractAction("niet betaald") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //todo code toevoegen zodat alleen gasten die niet betaald hebben te zien zijn
                }
            });
            nietBetaald.setBounds(605,125,145,25);
            panel.add(nietBetaald);

            geenFilter = new JButton(new AbstractAction("geen filter") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //todo code toevoegen zodat alle gasten weer te zien zijn
                }
            });
            geenFilter.setBounds(605,175,145,25);
            panel.add(geenFilter);



        } catch (Exception e) {
            System.out.println("er ging iets mis");
            e.printStackTrace();
        }
        add(panel);
    }

    public void addList() {
        try {
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM toernooi_inschrijving");
            while (rs.next()) {
                inschrijvingnr = rs.getInt(1);
                gastID = rs.getInt(2);
                toernooiID = rs.getInt(3);
                heeftBetaald = rs.getString(4);

                ToernooiInschrijving ti = new ToernooiInschrijving(inschrijvingnr, gastID, toernooiID, heeftBetaald);
                model.addElement(ti);
            }
        } catch (Exception e) {
            System.out.println("er ging iets mis");
            e.printStackTrace();
        }
    }
}