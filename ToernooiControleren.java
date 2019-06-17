import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.event.ActionListener;

/**
 * Medewerker krijgt een scherm te zien waarin een Gast in een Toernooi ingeschreven kan worden
 */

class ToernooiControleren extends JDialog {

    //JDialog
    private static final int width = 750;
    private static final int height = 500;
    private static final String title = "Toernooi Controleren";

    //ATTRIBUTES
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
    private int totaalInleggeld;
    private int aantalSpelers;
    private int aantalBetaald;

    //COMPONENTS
    private DefaultListModel<Toernooi> model;
    private JList<Toernooi> toernooien;

    private JButton bewerkBtn;
    private JButton terugBtn;
    private JButton verwijderBtn;

    private JLabel aantalSpelersLbl;
    //private JButton aantalNietBetaald;
    private JButton totaalInleggeldBtn;


    ToernooiControleren() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        try { //Tries to make connection with the database
            model = new DefaultListModel<>();
            toernooien = new JList<>(model);
            panel.add(toernooien);
            JScrollPane sp = new JScrollPane(toernooien);
            sp.setBounds(125, 25, 500, 400);
            panel.add(sp);

            addList();

            if (model.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nog geen toernooien toegevoegd, voeg toernooien toe via 'Toernooi toevoegen'");
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

        bewerkBtn.setBounds(0, 0, 100, 30);
        terugBtn.setBounds(0, 55, 100, 30);
        verwijderBtn.setBounds(0, 110, 100, 30);

        panel.add(bewerkBtn);
        panel.add(terugBtn);
        panel.add(verwijderBtn);

        ActionListener terug = new Terug();
        ActionListener verwijder = new Verwijder(toernooien);
        ActionListener bewerk = new Bewerk(toernooien);

        bewerkBtn.addActionListener(bewerk);
        terugBtn.addActionListener(terug);
        verwijderBtn.addActionListener(verwijder);

        totaalInleggeldBtn = new JButton("<html>totaal inleggeld</html>");
        totaalInleggeldBtn.setBounds(625,0,100,30);
        ActionListener bekijkInleggeld = new BekijkInleggeld(toernooien);
        totaalInleggeldBtn.addActionListener(bekijkInleggeld);
        panel.add(totaalInleggeldBtn);

        aantalSpelersLbl = new JLabel("0 van de 0 spelers hebben betaald");
        aantalSpelersLbl.setBounds(525,425,200,25);
        panel.add(aantalSpelersLbl);

        ListSelectionListener bekijkAantalSpelers = new BekijkAantalSpelers(toernooien);
        toernooien.addListSelectionListener(bekijkAantalSpelers);

        this.add(panel);
    }

    class Terug implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    class Bewerk implements ActionListener {
        private JList<Toernooi> list;

        private Bewerk(JList<Toernooi> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kies een toernooi om te bewerken");
                }
                else {
                    datum = list.getSelectedValue().getDatum();
                    begintijd = list.getSelectedValue().getBegintijd();
                    eindtijd = list.getSelectedValue().getEindtijd();
                    inschrijfdatum = list.getSelectedValue().getInschrijfdatum();

                    class ToernooiBewerken extends ToernooiToevoegen {

                        private ToernooiBewerken() {
                            String[] datumParts = datum.split("-");
                            datumJaar.setSelectedIndex(Integer.parseInt(datumParts[0]) - 2019);
                            datumMaand.setSelectedIndex(Integer.parseInt(datumParts[1]) - 1);
                            datumDag.setSelectedIndex(Integer.parseInt(datumParts[2]) - 1);

                            locaties.setSelectedItem(list.getSelectedValue().getLocatie());

                            String[] begintijdParts = begintijd.split(":");
                            begintijdUur.setValue(Integer.parseInt(begintijdParts[0]));
                            begintijdMin.setValue(Integer.parseInt(begintijdParts[1]));

                            String[] eindtijdParts = eindtijd.split(":");
                            eindtijdUur.setValue(Integer.parseInt(eindtijdParts[0]));
                            eindtijdMin.setValue(Integer.parseInt(eindtijdParts[1]));

                            beschrijvingTxt.setText(list.getSelectedValue().getBeschrijving());
                            conditieTxt.setText(list.getSelectedValue().getConditie());

                            maxInschrijvingenSp.setValue(list.getSelectedValue().getMaxInschrijvingen());
                            inleggeldSp.setValue(list.getSelectedValue().getInleggeld());

                            String[] inschrijfdatumParts = inschrijfdatum.split("-");
                            inschrijfdatumJaar.setSelectedIndex(Integer.parseInt(inschrijfdatumParts[0]) - 2019);
                            inschrijfdatumMaand.setSelectedIndex(Integer.parseInt(inschrijfdatumParts[1]) - 1);
                            inschrijfdatumDag.setSelectedIndex(Integer.parseInt(inschrijfdatumParts[2]) - 1);

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
                                locatie = list.getSelectedValue().getLocatie();
                                beschrijving = beschrijvingTxt.getText();
                                conditie = conditieTxt.getText();

                                try {
                                    PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE toernooi SET datum = ?, locatie = ?, begintijd = ?, eindtijd = ?, beschrijving = ?, conditie = ?, max_inschrijvingen = ?, inleggeld = ?, inschrijfdatum = ? WHERE ID = ?;");
                                    ps.setString(1, datum);
                                    ps.setString(2, locatie);
                                    ps.setString(3, begintijd);
                                    ps.setString(4, eindtijd);
                                    ps.setString(5, beschrijving);
                                    ps.setString(6, conditie);
                                    ps.setInt(7, maxInschrijvingen);
                                    ps.setInt(8, inleggeld);
                                    ps.setString(9, inschrijfdatum);
                                    ps.setInt(10, ID);
                                    ps.executeUpdate();
                                    model.removeAllElements();
                                    addList();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(null, "Toernooi is gewijzigd");
                                dispose();
                            }
                        }
                    }
                    JDialog d = new ToernooiBewerken();
                    d.setSize(500, 600);
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
        private JList<Toernooi> list;

        private Verwijder(JList<Toernooi> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kies een toernooi om te verwijderen");
                } else {
                    int ID = list.getSelectedValue().getID();
                    PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("DELETE FROM toernooi WHERE ID = ?");
                    ps.setInt(1, ID);
                    ps.executeUpdate();
                    model.removeElement(list.getSelectedValue());
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class BekijkInleggeld implements ActionListener {
        private JList<Toernooi> list;

        private BekijkInleggeld(JList<Toernooi> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kies een toernooi om het totale inleggeld te bekijken");
                } else {
                    int ID = list.getSelectedValue().getID();
                    ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select (count(*)*inleggeld) from toernooi_inschrijving join toernooi on toernooi = ID where toernooi = " + ID + " and heeft_betaald = 'ja';");
                    while (rs.next()) {
                        totaalInleggeld = rs.getInt(1);
                        JOptionPane.showMessageDialog(null,"het totaal ingelegde geld van dit toernooi is €" + totaalInleggeld);
                        }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class BekijkAantalSpelers implements ListSelectionListener {
        private JList<Toernooi> list;

        private BekijkAantalSpelers(JList<Toernooi> list) {
            this.list = list;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            try {
                int ID = list.getSelectedValue().getID();

                ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select count(*) as q1, (select count(*) from toernooi_inschrijving where toernooi = " + ID + " and heeft_betaald = 'ja') as q2 from toernooi_inschrijving join toernooi on toernooi = ID  where toernooi = " + ID + ";");
                while (rs.next()) {
                    aantalSpelers = rs.getInt("q1");
                    aantalBetaald = rs.getInt("q2");
                }

                if (!e.getValueIsAdjusting()) {
                    aantalSpelersLbl.setText(aantalBetaald + " van de " + aantalSpelers + " heeft betaald" );
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
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

    public static void main(String[] args) {
        JDialog d = new ToernooiControleren();
        d.setSize(750, 500);
        d.setVisible(true);
    }
}
