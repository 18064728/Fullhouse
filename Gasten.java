package fullhouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * geeft de medewerker de mogelijkheid om een gast toe te voegen of overzicht te krijgen
 * of om terug te gaan naar class 'FullHouse'
 */
public class Gasten extends JDialog {

    private static final int width = 300;
    private static final int height = 400;
    private static final String title = "gasten";

    public static void main(String[] args) {
        Gasten g = new Gasten();
    }

    public Gasten() {
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
        JButton terubgBtn = new JButton("terug");

        Dimension minSize = new Dimension(100, 50);
        Dimension prefSize = new Dimension(100, 50);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 50);

        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(toevoegenBtn);
        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(controlerenBtn);
        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(terubgBtn);

        /**
         * opent nieuw JDialog met formulier om gast toe te voegen
         */
        class Toevoegen implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JDialog d = new Gast_Toevoegen();
                d.setVisible(true);
                d.setSize(500,500);
                dispose();
            }
        }

        /**
         * opent nieuw JDialog met een overzicht van alle gasten
         */
        class Controleren implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JDialog d = new Gast_Controleren2();
                d.setVisible(true);
                d.setSize(750,500);
                dispose();
            }
        }

        /**
         * gaat terug naar de vorige JDialog
         */
        class Terug implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                new FullHouse();
                dispose();
            }
        }

        ActionListener terug = new Terug();
        ActionListener toevoegen = new Toevoegen();
        ActionListener controleren = new Controleren();

        terubgBtn.addActionListener(terug);
        toevoegenBtn.addActionListener(toevoegen);
        controlerenBtn.addActionListener(controleren);

        add(panel);
    }
}