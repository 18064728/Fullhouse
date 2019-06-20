import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Medewerken vult de gegevens van het formulier in en drukt op 'voeg winnar toe' om de winnaar toe te voegen aan de database
 * of drukt op 'terug' om terug te gaan naar 'Toernooi'
 */

class WinnaarToevoegen extends JDialog {

    //JDIALOG
    private static final int width = 750;
    private static final int height = 500;
    private static final String title = "Winnaar toevoegen";

    //ATTRIBUTES
    private int winaarNr;
    private int gastID;
    private int toernooiID;
    private int tafel;
    private int ronde;
    private int plaats;

    private String[] gastParts;
    private String[] toernooiParts;
    int aantalRondes;
    int aantalTafels;
    int eindPlaats;

    private JButton toevoegenBtn;
    private JButton verwijderBtn;
    private JButton terugBtn;

    private JSpinner rondeSp;
    private JSpinner tafelSp;
    private JSpinner plaatsSp;

    JLabel winnaar;
    JLabel gekozenWinnaar;
    JLabel toernooi;
    JLabel gekozenToernooi;
    JLabel rondeLbl;
    JLabel tafelLbl;
    JLabel plaatsNr;

    private DefaultListModel<Winnaar> model;
    private JList<Winnaar> winnaars;

    JPanel panel;

    WinnaarToevoegen(){
        panel = new JPanel();
        panel.setLayout(null);

        model = new DefaultListModel<>();
        winnaars = new JList<>(model);
        panel.add(winnaars);
        JScrollPane sp = new JScrollPane(winnaars);
        sp.setBounds(125,25,500,400);
        panel.add(sp);

        addList();


        this.setSize(width, height);
        this.setTitle(title);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.add(panel);

        toevoegenBtn = new JButton("Toevoegen");
        terugBtn = new JButton("Terug");
        verwijderBtn = new JButton("Verwijderen");

        toevoegenBtn.setBounds(0,0,100,30);
        terugBtn.setBounds(0,55,100,30);
        verwijderBtn.setBounds(0,110,100,30);

        panel.add(toevoegenBtn);
        panel.add(terugBtn);
        panel.add(verwijderBtn);

        ActionListener toevoegen = new Toevoegen();
        ActionListener terug = new Terug();
        ActionListener verwijder = new Verwijder(winnaars);

        toevoegenBtn.addActionListener(toevoegen);
        terugBtn.addActionListener(terug);
        verwijderBtn.addActionListener(verwijder);

        this.add(panel);
    }

    class Terug implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            dispose();
        }
    }

    class Toevoegen extends JDialog implements ActionListener{
        //private JList<>
        private static final int width = 300;
        private static final int height = 350;
        private static final String title = "Winnaar toevoegen";

        JButton kiesWinnaar;
        JButton kiesToernooi;
        JButton toevoegenWinnaar;
        JButton terugMainScreen;

        JPanel panel;

        @Override
        public void actionPerformed(ActionEvent e) {
            this.setSize(width, height);
            this.setTitle(title);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            this.setVisible(true);

            panel = new JPanel();
            panel.setLayout(null);

            winnaar = new JLabel("Winnaar:");
            winnaar.setBounds(5,0,50,15);
            panel.add(winnaar);

            gekozenWinnaar = new JLabel();
            gekozenWinnaar.setBounds(60,0,250,15);
            panel.add(gekozenWinnaar);

            kiesWinnaar = new JButton(new AbstractAction("Kies winnaar") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog d = new WinnaarKiezen();
                    d.setSize(575, 400);
                    d.setVisible(true);
                }
            });
            kiesWinnaar.setBounds(5,20,125,25);
            panel.add(kiesWinnaar);

            toernooi = new JLabel("Toernooi:");
            toernooi.setBounds(5,50,50,25);
            panel.add(toernooi);

            gekozenToernooi = new JLabel();
            gekozenToernooi.setBounds(60,50,200,25);
            panel.add(gekozenToernooi);

            kiesToernooi = new JButton(new AbstractAction("Kies toernooi") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JDialog d = new ToernooiKiezen();
                    d.setSize(575,400);
                    d.setVisible(true);
                }
            });
            kiesToernooi.setBounds(5,80,125,25);
            panel.add(kiesToernooi);

            rondeLbl = new JLabel("Ronde nummer:");
            rondeLbl.setBounds(5,125,100,15);
            panel.add(rondeLbl);

            SpinnerNumberModel rondes = new SpinnerNumberModel(0,0,3,1);
            rondeSp = new JSpinner(rondes);
            rondeSp.setBounds(110,125, 50,15);
            panel.add(rondeSp);

            tafelLbl = new JLabel("Tafel nummer:");
            tafelLbl.setBounds(5, 150, 100, 15);
            panel.add(tafelLbl);

            SpinnerNumberModel tafels = new SpinnerNumberModel(0,0,10,1);
            tafelSp = new JSpinner(tafels);
            tafelSp.setBounds(110, 150, 50, 15);
            panel.add(tafelSp);

            plaatsNr = new JLabel("Plaats nummer:");
            plaatsNr.setBounds(5, 175, 100, 15);
            panel.add(plaatsNr);

            SpinnerNumberModel plaatsen = new SpinnerNumberModel(0,0,3,1);
            plaatsSp = new JSpinner(plaatsen);
            plaatsSp.setBounds(110, 175, 50, 15);
            panel.add(plaatsSp);

            toevoegenWinnaar = new JButton(new AbstractAction("Voeg winnaar toe") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int winnaarID = 0;
                    int toernooiID = 0;

                    try{
                        winnaarID = Integer.parseInt(gastParts[1]);
                    }
                    catch (Exception ex1){
                        gekozenWinnaar.setForeground(Color.red);
                        gekozenWinnaar.setText("Nog geen winnaar gekozen");
                    }

                    try{
                        toernooiID = Integer.parseInt(toernooiParts[1]);
                    }
                    catch (Exception e2){
                        gekozenToernooi.setForeground(Color.red);
                        gekozenToernooi.setText("Nog geen toernooi gekozen");
                    }

                    aantalRondes = Integer.parseInt(rondeSp.getValue().toString());
                    aantalTafels = Integer.parseInt(tafelSp.getValue().toString());
                    eindPlaats = Integer.parseInt(plaatsSp.getValue().toString());

                    try {
                        int i = 0;
                        ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select count(*) from winnaar where gast = " + gastID + " and toernooi = " + toernooiID + " ;");
                        while (rs.next()) {
                          i = rs.getInt(1);
                            System.out.println(i);
                        }

                        if (i == 0) {

                            PreparedStatement ps2 = ConnectionManager.getConnection().prepareStatement("INSERT INTO winnaar (gast, toernooi, tafel, ronde, plaats) VALUES (?,?,?,?,?) ;");
                            ps2.setInt(1, winnaarID);
                            ps2.setInt(2, toernooiID);
                            ps2.setInt(3, aantalTafels);
                            ps2.setInt(4, aantalRondes);
                            ps2.setInt(5, eindPlaats);
                            ps2.executeUpdate();
                            JOptionPane.showMessageDialog(null, "winnaar is toegevoegd");
                        } else {
                            JOptionPane.showMessageDialog(null,"deze gast doet al mee aan dit toernooi");
                        }
                        i = 0;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    try {
                        PreparedStatement ps2 = ConnectionManager.getConnection().prepareStatement("UPDATE gast SET rating = rating + 25 WHERE ID = " + winnaarID);
                        ps2.executeUpdate();
                    } catch (Exception ex2){
                        ex2.printStackTrace();
                    }
                    addList();
                }
            });
            toevoegenWinnaar.setBounds(5,275,125,25);
            panel.add(toevoegenWinnaar);

            terugMainScreen = new JButton(new AbstractAction("Terug") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            terugMainScreen.setBounds(150,275,75,25);
            panel.add(terugMainScreen);

            this.add(panel);
        }
    }

    class WinnaarKiezen extends JDialog{
        private int ID;
        private String naam;
        private String geslacht;
        private String geboortedatum;
        private String adres;
        private String postcode;
        private String woonplaats;
        private String telefoonnummer;
        private String email;
        private int rating;

        private JButton kies;
        private JButton terug;

        WinnaarKiezen() {
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
                ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM gast WHERE ID IN (SELECT gast FROM toernooi_inschrijving WHERE gast.ID = toernooi_inschrijving.gast)");
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

                    Gast gast = new Gast(ID, naam, geslacht, geboortedatum, adres, postcode, woonplaats, telefoonnummer, email, rating);
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
                            gekozenWinnaar.setForeground(Color.BLACK);
                            gekozenWinnaar.setText("ID: " + gastParts[1] + " naam: " + gastParts[3]);
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
                            gekozenToernooi.setText("ID: " + toernooiParts[1] + " datum: " + toernooiParts[3]);
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

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    class Verwijder implements ActionListener{
        private JList<Winnaar> list;

        private Verwijder(JList<Winnaar> list){
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            try{
                if(list.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(null, "Kies een winnaar om te verwijderen");
                }
                else{
                    int ID = list.getSelectedValue().getGastID();
                    PreparedStatement ps = ConnectionManager.getConnection().prepareStatement("DELETE FROM winnaar WHERE gast = ?");
                    ps.setInt(1, ID);
                    ps.executeUpdate();
                    model.removeElement(list.getSelectedValue());
                    JOptionPane.showMessageDialog(null, "Winnaar is verwijderd");
                }
            }
            catch (SQLException e1){
                e1.printStackTrace();
            }
        }
    }

    private void addList(){
        model.removeAllElements();
        try{
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM winnaar");
            while(rs.next()){
                winaarNr = rs.getInt("winnaarnr");
                gastID = rs.getInt("gast");
                toernooiID = rs.getInt("toernooi");
                tafel = rs.getInt("tafel");
                ronde = rs.getInt("ronde");
                plaats = rs.getInt("plaats");
                Winnaar winnaar = new Winnaar(winaarNr, gastID, toernooiID, tafel, ronde, plaats);
                model.addElement(winnaar);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}