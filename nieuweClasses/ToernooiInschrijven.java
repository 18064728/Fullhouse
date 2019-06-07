import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ToernooiInschrijven extends JDialog {

    private static final int width = 300;
    private static final int height = 350;
    private static final String title = "Toernooi inschrijving";

    //ATTRIBUTES
    private String[] gastParts;
    private String[] toernooiParts;
    private String heeftBetaald;

    //COMPONENTS
    private JLabel gastLbl;
    private JLabel gekozenGast;
    private JLabel toernooiLbl;
    private JLabel gekozenToernooi;
    private JLabel heeftBetaaldLbl;
    private JCheckBox ja;
    private JCheckBox nee;
    private JLabel geenKeuze;

    private JButton inschrijven;
    private JButton terug;

        ToernooiInschrijven() {

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
                    JDialog d = new GastKiezen();
                    d.setSize(575,400);
                    d.setVisible(true);
                }
            });
            kiesGast.setBounds(5,20, 100,25);
            panel.add(kiesGast);

            toernooiLbl = new JLabel("toernooi:");
            toernooiLbl.setBounds(5,50,50,25);
            panel.add(toernooiLbl);


            gekozenToernooi = new JLabel();
            gekozenToernooi.setBounds(60, 50, 200,25);
            panel.add(gekozenToernooi);
            JButton kiesToernooi = new JButton(new AbstractAction("kies toernooi") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog d = new ToernooiKiezen();
                    d.setSize(575,400);
                    d.setVisible(true);
                }
            });
            kiesToernooi.setBounds(5,80,125,25);
            panel.add(kiesToernooi);

            heeftBetaaldLbl = new JLabel("heeft betaald:");
            heeftBetaaldLbl.setBounds(5,125,100,15);
            panel.add(heeftBetaaldLbl);

            ja = new JCheckBox("ja");
            ja.setBounds(110,125,50,15);
            panel.add(ja);

            nee = new JCheckBox("nee");
            nee.setBounds(160,125,50,15);
            panel.add(nee);

            ButtonGroup bg = new ButtonGroup();
            bg.add(ja);
            bg.add(nee);

            geenKeuze = new JLabel();
            geenKeuze.setBounds(210,125,100,15);
            panel.add(geenKeuze);

            inschrijven = new JButton(new AbstractAction("schrijf gast in") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int gastID = 0;
                    int toernooiID = 0;

                    try {
                        gastID = Integer.parseInt(gastParts[0]);
                    } catch (Exception ex1) {
                        gekozenGast.setForeground(Color.RED);
                        gekozenGast.setText("nog geen gast gekozen");
                    }

                    try {
                        toernooiID = Integer.parseInt(toernooiParts[0]);
                    } catch (Exception e2) {
                        gekozenToernooi.setForeground(Color.RED);
                        gekozenToernooi.setText("nog geen toernooi gekozen");
                    }

                    if (ja.isSelected()) {
                        heeftBetaald = "ja";
                    } else if (nee.isSelected()) {
                        heeftBetaald = "nee";
                    } else {
                        heeftBetaald = "null";
                        geenKeuze.setForeground(Color.RED);
                        geenKeuze.setText("kies ja / nee");
                    }

                    if (gastID == 0 || toernooiID == 0 || heeftBetaald.equals("null")) {
                        JOptionPane.showMessageDialog(null, "vul alle velden in");
                    } else {
                    try {
                        //voegt inschrijving toe aan database
                        PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("INSERT INTO toernooi_inschrijving (gast, toernooi, heeft_betaald) VALUES (?, ?, ?)");
                        ps.setInt(1, gastID);
                        ps.setInt(2, toernooiID);
                        ps.setString(3, heeftBetaald);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(null,"gast is ingeschreven");

                        //maakt alle velden leeg zodat je nog een gast kan inschrijven
                        gekozenGast.setText("");
                        gekozenToernooi.setText("");
                        geenKeuze.setText("");
                        bg.clearSelection();
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

        class ToernooiKiezen extends JDialog {
            private int ID;
            private String datum;
            private String locatie;
            private String begintijd;
            private  String eindtijd;
            private String beschrijving;
            private String conditie;
            private int maxInschrijvingen;
            private int inleggeld;
            private String inschrijfdatum;

            public ToernooiKiezen() {
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

                    JButton kies = new JButton(new AbstractAction("kies") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (toernooien.isSelectionEmpty()) {
                                JOptionPane.showMessageDialog(null,"kies een toernooi");
                            } else {
                                toernooiParts = toernooien.getSelectedValue().toString().split(" ");
                            StringBuilder toernooi = new StringBuilder();

                            for (int i = 2; i < toernooiParts.length; i++) {
                                if (toernooiParts[i].matches(".*\\d.*")) {
                                    break;
                                }
                                toernooi.append(toernooiParts[i]).append(" ");
                            }
                            gekozenToernooi.setForeground(Color.BLACK);
                            gekozenToernooi.setText("ID: " + toernooiParts[0] + " datum: " + toernooiParts[1] + ", " + toernooi);
                            dispose();
                        }
                    }
                    });
                    kies.setBounds(0,325,75,25);
                    toernooiPanel.add(kies);


                    terug = new JButton(new AbstractAction("terug") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dispose();
                        }
                    });
                    terug.setBounds(100,325,75,25);
                    toernooiPanel.add(terug);

                } catch (SQLException e) {
                    System.out.println("something went wrong");
                    e.printStackTrace();
                }
            }
        }
    }