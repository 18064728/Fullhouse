import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.awt.event.ActionListener;

/**
 * Medewerker krijgt een overzicht van alle masterclasses en heeft de mogelijkheid om ze aan te passen of te verwijderen
 */

public class MasterclassControleren extends JDialog {

    //JDialog
    private static final int width = 750;
    private static final int height = 500;
    private static final String title = "Masterclass controleren";

    //ATTRIBUTES
    private int ID;
    private String datum;
    private String locatie;
    private String begintijd;
    private String eindtijd;
    private int kosten;
    private int minRating;
    private String bekendePokerspeler;

    private DefaultListModel<Masterclass> model;
    private JList<Masterclass> masterclasses;

    private JButton bewerkBtn;
    private JButton terugBtn;
    private JButton verwijderBtn;

    MasterclassControleren() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        try { //Tries to make connection with the database
            System.out.println("connected to database");

            model = new DefaultListModel<>();
            masterclasses = new JList<>(model);
            panel.add(masterclasses);
            JScrollPane sp = new JScrollPane(masterclasses);
            sp.setBounds(125, 25, 500, 400);
            panel.add(sp);

            addList();

            if (model.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nog geen masterclasses toegevoegd, voeg masterclasses toe via 'Masterclass toevoegen'");
            }
        } catch (Exception e) { //error message if there's no connection with the database
            System.out.println("Can't connect to the database, please contact tech-support");
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
        ActionListener verwijder = new Verwijder(masterclasses);
        ActionListener bewerk = new Bewerk(masterclasses);

        bewerkBtn.addActionListener(bewerk);
        terugBtn.addActionListener(terug);
        verwijderBtn.addActionListener(verwijder);

        this.add(panel);
    }

    class Terug implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    class Bewerk implements ActionListener {
        private JList<Masterclass> list;

        private Bewerk(JList<Masterclass> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kies een masterclass om te bewerken");
                } else {

                    datum = list.getSelectedValue().getDatum();
                    begintijd = list.getSelectedValue().getBegintijd();
                    eindtijd = list.getSelectedValue().getEindtijd();

                    class MasterclassBewerken extends MasterclassToevoegen {

                        private MasterclassBewerken() {

                            String[] datumParts = datum.split("-");

                            jaar.setSelectedIndex(Integer.parseInt(datumParts[0]) - 2019);
                            maand.setSelectedIndex(Integer.parseInt(datumParts[1]) - 1);
                            dag.setSelectedIndex(Integer.parseInt(datumParts[2]) - 1);

                            locaties.setSelectedItem(list.getSelectedValue().getLocatie());

                            String[] begintijdParts = begintijd.split(":");
                            begintijdUur.setValue(Integer.parseInt(begintijdParts[0]));
                            begintijdMin.setValue(Integer.parseInt(begintijdParts[1]));

                            String[] eindtijdParts = eindtijd.split(":");
                            eindtijdUur.setValue(Integer.parseInt(eindtijdParts[0]));
                            eindtijdMin.setValue(Integer.parseInt(eindtijdParts[1]));

                            kostensp.setValue(list.getSelectedValue().getKosten());
                            min_ratingSp.setValue(list.getSelectedValue().getMinRating());
                            pokerspelersCb.setSelectedItem(list.getSelectedValue().getBekendePokerspeler());

                            voegToeBtn.setText("Wijzigen");
                            ConponentManager.deleteActionListeners(terugBtn);
                            ConponentManager.deleteActionListeners(voegToeBtn);
                            ActionListener terug = new Terug();
                            terugBtn.addActionListener(terug);
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
                                begintijd = begintijdUur.getValue() + ":" + begintijdMin.getValue();
                                eindtijd = eindtijdUur.getValue() + ":" + eindtijdMin.getValue();

                                try {
                                    PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("UPDATE masterclass SET datum = ?, locatie = ?, begintijd = ?, eindtijd = ?, kosten = ?, min_rating = ?, bekende_pokerspeler = ? WHERE ID = ?;");
                                    ps.setString(1, datum);
                                    ps.setString(2, locatie);
                                    ps.setString(3, begintijd);
                                    ps.setString(4, eindtijd);
                                    ps.setInt(5, kosten);
                                    ps.setInt(6, minRating);
                                    ps.setString(7, bekendePokerspeler);
                                    ps.setInt(8, ID);
                                    ps.executeUpdate();
                                    model.removeAllElements();
                                    addList();
                                } catch (com.mysql.jdbc.exceptions.jdbc4.CommunicationsException e2) {
                                    System.out.println("eror: geen verbinding meer met databse, start het programma opnieuw op");
                                } catch (SQLException e1) {
                                    System.out.println("er ging iets mis");
                                    e1.printStackTrace();
                                }
                                JOptionPane.showMessageDialog(null, "Masterclass is gewijzigd");
                                dispose();
                            }
                        }
                    }
                    JDialog d = new MasterclassBewerken();
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
        private JList<Masterclass> list;

        private Verwijder(JList<Masterclass> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kies een masterclass om te verwijderen");
                } else {
                    int ID = list.getSelectedValue().getID();
                    PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("DELETE FROM masterclass WHERE ID = ?");
                    ps.setInt(1, ID);
                    ps.executeUpdate();
                    model.removeElement(list.getSelectedValue());
                    JOptionPane.showMessageDialog(null,"masterclass is verwijderd");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    private void addList() {
        try {
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM masterclass");
            while (rs.next()) {
                ID = rs.getInt("ID");
                datum = rs.getString("datum");
                locatie = rs.getString("locatie");
                begintijd = rs.getString("begintijd");
                eindtijd = rs.getString("eindtijd");
                kosten = rs.getInt("kosten");
                minRating = rs.getInt("min_rating");
                bekendePokerspeler = rs.getString("bekende_pokerspeler");

                Masterclass masterclass= new Masterclass(ID, datum, locatie, begintijd, eindtijd, kosten, minRating, bekendePokerspeler);
                model.addElement(masterclass);
            }
        } catch (Exception e) {
            System.out.println("er ging iets mis");
            e.printStackTrace();
        }
    }
}
