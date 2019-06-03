package fullhouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * geeft de medewerker de mogelijkheid om een toernooi toe te voegen of een overzicht ervan te krijgen
 * of om terug te gaan naar 'FullHouse'
 */
public class Toernooi extends JDialog {

    private static final int width = 300;
    private static final int height = 400;
    private static final String title = "toernooi";

    public static void main(String[] args) {
        new Toernooi();
    }

    public Toernooi() {
        addComponents();
        setSize(width, height);
        setTitle(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JButton toevoegenBtn = new JButton("toevoegen");
        JButton controlerenBtn = new JButton("controleren");
        JButton terugBtn = new JButton("terug");

        Dimension minSize = new Dimension(100, 50);
        Dimension prefSize = new Dimension(100, 50);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 50);

        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(toevoegenBtn);
        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(controlerenBtn);
        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(terugBtn);

        /**
         * gaat terug naar 'FullHouse'
         */
        class Terug implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                new FullHouse();
                dispose();
            }
        }

        /**
         * opent nieuw JDialog met formulier om een toernooi toe te voegen
         */
        class Toevoegen implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                //todo add code
                //todo nieuwe Toernooi_Toevoegen class implementeren
            }
        }

        /**
         * opent nieuw JDialog met een overzicht van alle toernooien
         */
        class Controleren implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                //todo add code
                //todo nieuwe Toernooi_Controleren class implementeren
            }
        }

        ActionListener terug = new Terug();
        ActionListener toevoegen = new Toevoegen();
        ActionListener controleren = new Controleren();

        terugBtn.addActionListener(terug);
        toevoegenBtn.addActionListener(toevoegen);
        controlerenBtn.addActionListener(controleren);

        add(panel);
    }
}
