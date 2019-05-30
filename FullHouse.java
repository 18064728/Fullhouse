package fullhouse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * hoofdscherm waar de medewerker kan kiezen uit 'gasten, 'toernooi' en 'masterclass'
 */
public class FullHouse extends JFrame {

    private static final int width = 500;
    private static final int height = 500;
    private static final String title = "FullHouse";

    public static void main(String[] args) {
        JFrame frame = new FullHouse();
    }

    public FullHouse() {
        addComponents();
        setSize(width, height);
        setTitle(title);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void addComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        JButton gastenBtn = new JButton("gasten");
        JButton toernooiBtn = new JButton("toernooi");
        JButton masterclassBtn = new JButton("masterclass");

        Dimension minSize = new Dimension(150, 100);
        Dimension prefSize = new Dimension(150, 100);
        Dimension maxSize = new Dimension(Short.MAX_VALUE, 100);

        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(gastenBtn);
        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(toernooiBtn);
        panel.add(new Box.Filler(minSize, prefSize, maxSize));
        panel.add(masterclassBtn);

        /**
         * opent nieuw JDialog met opties voor 'gasten'
         */
        class Gasten1 implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JDialog d = new Gasten();
                dispose();
            }
        }

        /**
         * opent nieuw JDialog met opties voor 'toernooi'
         */
        class Toernooi1 implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JDialog d = new Toernooi();
                dispose();
            }
        }

        /**
         * opent nieuw JDialog met opties voor 'masterclass'
         */
        class Masterclass1 implements ActionListener {
            public void actionPerformed(ActionEvent e) {
                JDialog d = new Masterclass();
                dispose();
            }
        }

        ActionListener gasten = new Gasten1();
        ActionListener toernooi = new Toernooi1();
        ActionListener masterclass = new Masterclass1();

        gastenBtn.addActionListener(gasten);
        toernooiBtn.addActionListener(toernooi);
        masterclassBtn.addActionListener(masterclass);

        add(panel);
    }
}