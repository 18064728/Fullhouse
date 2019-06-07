import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MasterclassInschrijven extends JDialog {

    private static final int width = 300;
    private static final int height = 350;
    private static final String title = "Masterclass inschrijving";

    //ATTRIBUTES
    private String[] gastParts;
    private String[] masterclassParts;
    private String heeftBetaald;
    private String heeftRating;

    //COMPONENTS
    private JLabel gastLbl;
    private JLabel gekozenGast;
    private JLabel masterclassLbl;
    private JLabel gekozenMasterclass;
    private JLabel heeftBetaaldLbl;
    private JLabel heeftRatingLbl;
    private JCheckBox betaaldJa;
    private JCheckBox betaaldNee;
    private JCheckBox ratingJa;
    private JCheckBox ratingNee;
    private JLabel geenKeuze;

    private JButton inschrijven;
    private JButton terug;

    MasterclassInschrijven(){

        this.setSize(width, height);
        this.setTitle(title);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);

        //JPanel
        JPanel panel = new JPanel();
        panel.setLayout(null);

        panel.add(Box.createRigidArea(new Dimension(5, 10)));
        gastLbl = new JLabel("gast:");
        gastLbl.setBounds(5,0,50,15);
        panel.add(gastLbl);

        gekozenGast = new JLabel();
        gekozenGast.setBounds(60,0,250,15);
        panel.add(gekozenGast);
        JButton kiesGast = new JButton(new AbstractAction("kies gast") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog d = new MasterclassInschrijven.GastKiezen();
                d.setSize(575,400);
                d.setVisible(true);
            }
        });
        kiesGast.setBounds(5,20, 100,25);
        panel.add(kiesGast);

        masterclassLbl = new JLabel("masterclass:");
        masterclassLbl.setBounds(5,50,150,25);
        panel.add(masterclassLbl);


        gekozenMasterclass = new JLabel();
        gekozenMasterclass.setBounds(60, 50, 200,25);
        panel.add(gekozenMasterclass);
        JButton kiesMasterclass = new JButton(new AbstractAction("kies Masterclass") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog d = new MasterclassInschrijven.MasterclassKiezen();
                d.setSize(575,400);
                d.setVisible(true);
            }
        });
        kiesMasterclass.setBounds(5,80,125,25);
        panel.add(kiesMasterclass);

        heeftBetaaldLbl = new JLabel("heeft betaald:");
        heeftBetaaldLbl.setBounds(5,125,100,15);
        panel.add(heeftBetaaldLbl);

        betaaldJa = new JCheckBox("ja");
        betaaldJa.setBounds(110,125,50,15);
        panel.add(betaaldJa);

        betaaldNee = new JCheckBox("nee");
        betaaldNee.setBounds(160,125,50,15);
        panel.add(betaaldNee);

        ButtonGroup bg = new ButtonGroup();
        bg.add(betaaldJa);
        bg.add(betaaldNee);

        heeftRatingLbl = new JLabel("heeft rating:");
        heeftRatingLbl.setBounds(5,175,100,15);
        panel.add(heeftRatingLbl);

        ratingJa = new JCheckBox("ja");
        ratingJa.setBounds(110,175,50,15);
        panel.add(ratingJa);

        ratingNee = new JCheckBox("nee");
        ratingNee.setBounds(160,175,50,15);
        panel.add(ratingNee);

        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(ratingJa);
        bg1.add(ratingNee);

        geenKeuze = new JLabel();
        geenKeuze.setBounds(210,125,100,15);
        panel.add(geenKeuze);

        inschrijven = new JButton(new AbstractAction("schrijf gast in") {
            @Override
            public void actionPerformed(ActionEvent e) {
                int gastID = 0;
                int masterclassID = 0;

                try {
                    gastID = Integer.parseInt(gastParts[0]);
                } catch (Exception ex1) {
                    gekozenGast.setForeground(Color.RED);
                    gekozenGast.setText("nog geen gast gekozen");
                }

                try {
                    masterclassID = Integer.parseInt(masterclassParts[0]);
                } catch (Exception e2) {
                    gekozenMasterclass.setForeground(Color.RED);
                    gekozenMasterclass.setText("nog geen masterclass gekozen");
                }

                if (betaaldJa.isSelected()) {
                    heeftBetaald = "ja";
                } else if (betaaldNee.isSelected()) {
                    heeftBetaald = "nee";
                } else {
                    heeftBetaald = "null";
                    geenKeuze.setForeground(Color.RED);
                    geenKeuze.setText("kies ja / nee");
                }

                if (ratingJa.isSelected()) {
                    heeftRating = "ja";
                } else if (ratingNee.isSelected()) {
                    heeftRating = "nee";
                } else {
                    heeftRating = "null";
                    geenKeuze.setForeground(Color.RED);
                    geenKeuze.setText("kies ja / nee");
                }

                if (gastID == 0 || masterclassID == 0 || heeftBetaald.equals("null") || heeftRating.equals("null")) {
                    JOptionPane.showMessageDialog(null, "vul alle velden in");
                } else {
                    try {
                        //voegt inschrijving toe aan database
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("INSERT INTO masterclass_inschrijving (gast, masterclass, heeft_betaald) VALUES (?, ?, ?);");
                        ps.setInt(1, gastID);
                        ps.setInt(2, masterclassID);
                        ps.setString(3, heeftBetaald);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"gast is ingeschreven");

                        //maakt alle velden leeg zodat je nog een gast kan inschrijven
                        gekozenGast.setText("");
                        gekozenMasterclass.setText("");
                        geenKeuze.setText("");
                        bg.clearSelection();
                        bg1.clearSelection();
                    } catch (SQLException ex) {
                        System.out.println("er ging iets mis");
                        ex.printStackTrace();
                    }
                }
            }
        });
        inschrijven.setBounds(5,275,125,25);
        panel.add(inschrijven);

        terug = new JButton(new AbstractAction("terug") {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        terug.setBounds(150,275,75,25);
        panel.add(terug);

        add(panel);
    }

    class GastKiezen extends JDialog {
        private int ID;
        private String naam;
        private String geslacht;
        private String geboortedatum;
        private String adres;
        private String postcode;
        private String woonplaats;
        private String telefoonnummer;
        private String email;

        private JButton kies;
        private JButton terug;

        public GastKiezen() {
            this.setTitle("Gast kiezen");
            JPanel gastenPanel = new JPanel();
            gastenPanel.setLayout(null);
            DefaultListModel<Object> model = new DefaultListModel<>();
            JList<Object> gasten = new JList<>(model);
            gastenPanel.add(gasten);
            JScrollPane sp = new JScrollPane(gasten);
            sp.setBounds(0,0,550,300);
            gastenPanel.add(sp);
            this.add(gastenPanel);

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

                    Gast gast = new Gast(ID, naam, geslacht, geboortedatum, adres, postcode, woonplaats, telefoonnummer, email);
                    model.addElement(gast);
                }
                kies = new JButton(new AbstractAction("kies") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (gasten.isSelectionEmpty()) {
                            JOptionPane.showMessageDialog(null, "kies een gast");
                        } else {
                            gastParts = gasten.getSelectedValue().toString().split(" ");
                            StringBuilder naam = new StringBuilder();
                            for (int i = 1; i < gastParts.length; i++) {
                                if (gastParts[i].equals("man") || gastParts[i].equals("vrouw")) {
                                    break;
                                }
                                naam.append(gastParts[i]).append(" ");
                            }
                            gekozenGast.setForeground(Color.BLACK);
                            gekozenGast.setText("ID: " + gastParts[0] + "   naam: " + naam);
                            dispose();
                        }
                    }
                });
                kies.setBounds(0,325,75,25);
                gastenPanel.add(kies);


                terug = new JButton(new AbstractAction("terug") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                terug.setBounds(100,325,75,25);
                gastenPanel.add(terug);

            } catch (SQLException e) {
                System.out.println("something went wrong");
                e.printStackTrace();
            }

        }
    }

    class MasterclassKiezen extends JDialog {
        private int ID;
        private String datum;
        private String locatie;
        private String begintijd;
        private String eindtijd;
        private int kosten;
        private int minRating;
        private String bekendePokerspeler;

        public MasterclassKiezen() {
            this.setTitle("Masterclass kiezen");
            JPanel masterclassPanel = new JPanel();
            masterclassPanel.setLayout(null);
            DefaultListModel<Object> model = new DefaultListModel<>();
            JList<Object> masterclasses = new JList<>(model);
            masterclassPanel.add(masterclasses);
            JScrollPane sp = new JScrollPane(masterclasses);
            sp.setBounds(0, 0, 550, 300);
            masterclassPanel.add(sp);
            this.add(masterclassPanel);

            try {
                ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM masterclass");
                while (rs.next()) {
                    ID = rs.getInt("ID");
                    datum = rs.getString("datum");
                    locatie = rs.getString("locatie");
                    begintijd = rs.getString("begintijd");
                    eindtijd = rs.getString("eindtijd");
                    kosten = rs.getInt("kosten");
                    minRating = rs.getInt("min_Rating");
                    bekendePokerspeler = rs.getString("bekende_pokerspeler");

                    Masterclass masterclass = new Masterclass(ID, datum, locatie, begintijd, eindtijd, kosten, minRating, bekendePokerspeler);
                    model.addElement(masterclass);
                }

                JButton kies = new JButton(new AbstractAction("kies") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (masterclasses.isSelectionEmpty()) {
                            JOptionPane.showMessageDialog(null, "kies een masterclass");
                        } else {
                            masterclassParts = masterclasses.getSelectedValue().toString().split(" ");
                            StringBuilder masterclass = new StringBuilder();

                            for (int i = 2; i < masterclassParts.length; i++) {
                                if (masterclassParts[i].matches(".*\\d.*")) {
                                    break;
                                }
                                masterclass.append(masterclassParts[i]).append(" ");
                            }
                            gekozenMasterclass.setForeground(Color.BLACK);
                            gekozenMasterclass.setText("ID: " + masterclassParts[0] + " datum: " + masterclassParts[1] + ", " + masterclass);
                            dispose();
                        }
                    }
                });
                kies.setBounds(0, 325, 75, 25);
                masterclassPanel.add(kies);


                terug = new JButton(new AbstractAction("terug") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dispose();
                    }
                });
                terug.setBounds(100, 325, 75, 25);
                masterclassPanel.add(terug);

            } catch (SQLException e) {
                System.out.println("something went wrong");
                e.printStackTrace();
            }
        }
    }
    }