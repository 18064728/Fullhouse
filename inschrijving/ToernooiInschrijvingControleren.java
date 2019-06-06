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

    public static void main(String[] args) {
        JDialog d = new ToernooiInschrijvingControleren();
        d.setSize(750, 500);
        d.setVisible(true);
    }

    public ToernooiInschrijvingControleren() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        try {
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM toernooi_inschrijving");

            model = new DefaultListModel<>();
            inschrijvingen = new JList<>(model);
            panel.add(inschrijvingen);
            JScrollPane sp = new JScrollPane(inschrijvingen);
            sp.setBounds(125, 25, 500, 400);
            panel.add(sp);

            while (rs.next()) {
                inschrijvingnr = rs.getInt(1);
                gastID = rs.getInt(2);
                toernooiID = rs.getInt(3);
                heeftBetaald = rs.getString(4);

                ToernooiInschrijving ti = new ToernooiInschrijving(inschrijvingnr, gastID, toernooiID, heeftBetaald);
                model.addElement(ti);
            }

            wijzigen = new JButton("<html>betaling wijzigen</html>");
            wijzigen.setToolTipText("klik op een gast en deze knop om zijn betaling te wijzigen");
            wijzigen.setBounds(5, 5, 105, 50);
            panel.add(wijzigen);

            verwijderen = new JButton("verwijderen");
            verwijderen.setBounds(5, 80, 105, 30);
            panel.add(verwijderen);

            terug = new JButton(new AbstractAction("terug") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            terug.setBounds(5, 130, 105, 30);
            panel.add(terug);

        } catch (Exception e) {
            System.out.println("er ging iets mis");
            e.printStackTrace();
        }
        add(panel);

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
                            //inschrijvingen.repaint();
                        } else if (heeftBetaald.equals("ja")) { //wijzigt heeftBetaald naar nee
                            PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE toernooi_inschrijving set heeft_betaald = 'nee' where inschrijvingsnr = ?;");
                            ps.setInt(1,inschrijvingnr);
                            ps.executeUpdate();
                            //inschrijvingen.repaint();
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
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("DELETE FROM toernooi_inschrijving WHERE id = ?");
                        ps.setInt(1, inschrijvingnr);
                        ps.executeUpdate();
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
}