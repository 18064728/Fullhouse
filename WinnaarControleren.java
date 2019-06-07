import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class WinnaarControleren extends JDialog {

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

    private JButton winnaarKijkenBtn;
    private JButton terugBtn;

    private JPanel panel;

    private DefaultListModel<Toernooi> model;
    private JList<Toernooi> toernooien;

    private DefaultListModel<Winnaar> model1;
    private JList<Winnaar> winnaars;

    WinnaarControleren(){
        panel = new JPanel();
        panel.setLayout(null);

        try{
            System.out.println("connected to database");

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
        }
        catch (Exception e){
            System.out.println("er ging iets mis1");
            e.printStackTrace();
        }

        this.setSize(width, height);
        this.setTitle(title);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.add(panel);

        terugBtn = new JButton("Terug");
        terugBtn.setBounds(0,0,100,30);
        ActionListener terug = new Terug();
        panel.add(terugBtn);

        winnaarKijkenBtn = new JButton("Winnaars");
        winnaarKijkenBtn.setBounds(0, 55, 100, 30);
        ActionListener winnaarsKijken = new WinnaarsKijken(toernooien);
        panel.add(winnaarKijkenBtn);

        terugBtn.addActionListener(terug);
        winnaarKijkenBtn.addActionListener(winnaarsKijken);
    }

    void addList(){
        try{
            ResultSet rs = ConnectionManager.getConnection().createStatement().executeQuery("SELECT * FROM toernooi");
            while(rs.next()){
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
        }
        catch (Exception e){
            System.out.println("er ging iets mis2");
            e.printStackTrace();
        }
    }

    class Terug implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e){
            dispose();
        }
    }

    class WinnaarsKijken implements ActionListener{
        private JList<Toernooi> list;

        private WinnaarsKijken(JList<Toernooi> list){
            this.list = list;
        }

        @Override
        public void actionPerformed(ActionEvent e){
            try{
                if(list.isSelectionEmpty()){
                    JOptionPane.showMessageDialog(null, "Kies een toernooi om de winnaars te zien");
                }
                else{

                }
            }
            catch(Exception e1){
                e1.printStackTrace();
            }
        }
    }
}
