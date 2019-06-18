import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

/**
 * Medewerker krijgt een scherm te zien waarin een Gast als winnaar ingeschreven kan worden
 */

class WinnaarControleren extends JDialog {

    //JDialog
    private static final int width = 750;
    private static final int height = 500;
    private static final String title = "Winnaar Controleren";

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
    private double bedragEerste;
    private double bedragTweede;

    int gast;
    String naam;
    private int tafel;
    private int ronde;
    int plaats;

    private JButton EerstePlaatsKijkenBtn;
    private JButton TweedePlaatsKijkenBtn;
    private JButton terugBtn;

    private JPanel panel;

    private DefaultListModel<Toernooi> model;
    private JList<Toernooi> toernooien;

    WinnaarControleren() {
        panel = new JPanel();
        panel.setLayout(null);

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.setSize(width, height);
        this.setTitle(title);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.add(panel);

        terugBtn = new JButton("Terug");
        terugBtn.setBounds(0, 0, 100, 30);
        ActionListener terug = new Terug();
        panel.add(terugBtn);

        EerstePlaatsKijkenBtn = new JButton("Eerste plaats");
        EerstePlaatsKijkenBtn.setBounds(0, 55, 100, 30);
        ActionListener EerstePlaatsKijken = new EerstePlaatsKijken(toernooien);
        panel.add(EerstePlaatsKijkenBtn);

        TweedePlaatsKijkenBtn = new JButton("Tweede plaats");
        TweedePlaatsKijkenBtn.setBounds(0, 80, 100, 30);
        ActionListener TweedePlaatsKijken = new TweedePlaatsKijken(toernooien);
        panel.add(TweedePlaatsKijkenBtn);

        terugBtn.addActionListener(terug);
        EerstePlaatsKijkenBtn.addActionListener(EerstePlaatsKijken);
        TweedePlaatsKijkenBtn.addActionListener(TweedePlaatsKijken);
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

    class Terug implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dispose();
        }
    }

    class EerstePlaatsKijken implements ActionListener {
        private JList<Toernooi> list;

        private EerstePlaatsKijken(JList<Toernooi> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int ID = list.getSelectedValue().getID();

            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kies een toernooi om de tweede plaats van te zien");
                } else {
                    ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select *, (inleggeld*(select count(*) from toernooi_inschrijving where heeft_betaald = 'ja' and toernooi = " + ID + ")*0.45) as aantal from winnaar w join gast g on gast = ID join toernooi t on toernooi = t.ID where plaats = 1 and w.toernooi = " + ID + ";");
                    while (rs.next()) {
                        gast = rs.getInt("gast");
                        naam = rs.getString("naam");
                        ronde = rs.getInt("ronde");
                        tafel = rs.getInt("tafel");
                        plaats = rs.getInt("plaats");
                        bedragTweede = rs.getDouble("aantal");
                    }

                    if (gast == 0) {
                        JOptionPane.showMessageDialog(null, "dit toernooi heeft nog geen winnaar");
                    } else if (ronde == 3) {
                        JOptionPane.showMessageDialog(null, naam + " (ID: " + gast + ")" + " is op 1e plaats beïndigd van ronde " + ronde + " aan tafel " + tafel + " en heeft €" + bedragTweede + " gewonnen");
                        gast = 0;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    class TweedePlaatsKijken implements ActionListener {
        private JList<Toernooi> list;

        private TweedePlaatsKijken(JList<Toernooi> list) {
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int ID = list.getSelectedValue().getID();

            try {
                if (list.isSelectionEmpty()) {
                    JOptionPane.showMessageDialog(null, "Kies een toernooi om de tweede plaats van te zien");
                } else {
                    ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("select *, (inleggeld*(select count(*) from toernooi_inschrijving where heeft_betaald = 'ja' and toernooi = " + ID + ")*0.25) as aantal from winnaar w join gast g on gast = ID join toernooi t on toernooi = t.ID where plaats = 2 and w.toernooi = " + ID + ";");
                    while (rs.next()) {
                        gast = rs.getInt("gast");
                        naam = rs.getString("naam");
                        ronde = rs.getInt("ronde");
                        tafel = rs.getInt("tafel");
                        plaats = rs.getInt("plaats");
                        bedragTweede = rs.getDouble("aantal");
                    }

                    if (gast == 0) {
                        JOptionPane.showMessageDialog(null, "dit toernooi heeft nog geen winnaar");
                    } else if (ronde == 3) {
                        JOptionPane.showMessageDialog(null, naam + " (ID: " + gast + ")" + " is op 2e plaats beïndigd van ronde " + ronde + " aan tafel " + tafel + " en heeft €" + bedragTweede + " gewonnen");
                        gast = 0;
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }
}