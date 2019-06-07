import javax.swing.*;
import java.awt.event.ActionEvent;

public class Scherm extends JFrame {

    private static final String TITLE = "Full House";
    private static final int FRAME_WIDTH = 600;
    private static final int FRAME_HEIGHT = 400;

    private JLabel screenText;
    private JLabel toernooiScreenText;
    private JLabel gastenScreenText;
    private JLabel masterClassScreenText;
    private JLabel inschrijvingScreenText;

    private JButton toernooiButton;
    private JButton gastenButton;
    private JButton masterClassButton;
    private JButton inschrijvingButton;

    private JButton toernooiToevoegen;
    private JButton toernooicontroleren;
    private JButton toernooiTerug;

    private JButton gastenToevoegen;
    private JButton gastencontroleren;
    private JButton gastenTerug;

    private JButton masterClassToevoegen;
    private JButton masterClasscontroleren;
    private JButton masterClassTerug;

    private JButton voorToernooi;
    private JButton voorMasterclass;
    private JButton inschrijvingButtonTerug;
    private JButton toernooInschrijvingControleren;

    private JPanel panel;

    Scherm(){
        JFrame scherm = new JFrame();
        scherm.setTitle(TITLE);
        scherm.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        scherm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        scherm.setVisible(true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        panel = new JPanel();
        panel.setLayout(null);
        scherm.add(panel);

        //SCREEN 1
        screenText = new JLabel("Home screen voor Full House");
        screenText.setBounds(20, 20, 560, 180);
        panel.add(screenText);

        //Toernooi button
        toernooiButton = new JButton( new AbstractAction("Toernooi") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                toernooiButton.setVisible(false);
                gastenButton.setVisible(false);
                masterClassButton.setVisible(false);
                inschrijvingButton.setVisible(false);
                screenText.setVisible(false);

                toernooiScreenText.setVisible(true);

                toernooiTerug.setVisible(true);
                toernooiToevoegen.setVisible(true);
                toernooicontroleren.setVisible(true);
            }
        });
        toernooiButton.setBounds(15, 225, 150, 50);
        panel.add(toernooiButton);

        //Gasten button
        gastenButton = new JButton( new AbstractAction("Gasten") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                toernooiButton.setVisible(false);
                gastenButton.setVisible(false);
                masterClassButton.setVisible(false);
                inschrijvingButton.setVisible(false);
                screenText.setVisible(false);

                gastenScreenText.setVisible(true);

                gastenTerug.setVisible(true);
                gastenToevoegen.setVisible(true);
                gastencontroleren.setVisible(true);
            }
        });
        gastenButton.setBounds(15, 300, 150, 50);
        panel.add(gastenButton);

        //Master class button
        masterClassButton = new JButton( new AbstractAction("Master Class") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                toernooiButton.setVisible(false);
                gastenButton.setVisible(false);
                masterClassButton.setVisible(false);
                inschrijvingButton.setVisible(false);
                screenText.setVisible(false);

                masterClassScreenText.setVisible(true);

                masterClassTerug.setVisible(true);
                masterClassToevoegen.setVisible(true);
                masterClasscontroleren.setVisible(true);
            }
        });
        masterClassButton.setBounds(415, 225, 150, 50);
        panel.add(masterClassButton);

        //Inschrijving class button
        inschrijvingButton = new JButton(new AbstractAction("Inschrijven") {
            @Override
            public void actionPerformed(ActionEvent e) {
                toernooiButton.setVisible(false);
                gastenButton.setVisible(false);
                masterClassButton.setVisible(false);
                inschrijvingButton.setVisible(false);
                screenText.setVisible(false);

                inschrijvingScreenText.setVisible(true);
                voorToernooi.setVisible(true);
                voorMasterclass.setVisible(true);
                inschrijvingButtonTerug.setVisible(true);
                toernooInschrijvingControleren.setVisible(true);
            }
        });
        inschrijvingButton.setBounds(415,300,150,50);
        panel.add(inschrijvingButton);

        //TOERNOOI SCREEN
        toernooiScreenText = new JLabel("Screen text voor toernooi");
        toernooiScreenText.setBounds(20, 20, 560, 180);
        toernooiScreenText.setVisible(false);
        panel.add(toernooiScreenText);

        toernooiTerug = new JButton( new AbstractAction("Terug") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                toernooiTerug.setVisible(false);
                toernooiToevoegen.setVisible(false);
                toernooicontroleren.setVisible(false);

                toernooiScreenText.setVisible(false);

                toernooiButton.setVisible(true);
                gastenButton.setVisible(true);
                masterClassButton.setVisible(true);
                inschrijvingButton.setVisible(true);

                screenText.setVisible(true);
            }
        });
        toernooiTerug.setBounds(15, 300, 310, 50);
        toernooiTerug.setVisible(false);
        panel.add(toernooiTerug);

        toernooiToevoegen = new JButton( new AbstractAction("Toernooi toevoegen") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                JDialog d = new ToernooiToevoegen();
            }
        });
        toernooiToevoegen.setBounds(175, 200, 150, 50);
        toernooiToevoegen.setVisible(false);
        panel.add(toernooiToevoegen);

        toernooicontroleren = new JButton( new AbstractAction("Toernooi controleren") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                JDialog d = new ToernooiControleren();
            }
        });
        toernooicontroleren.setBounds(15, 200, 150, 50);
        toernooicontroleren.setVisible(false);
        panel.add(toernooicontroleren);

        //GASTEN SCREEN
        gastenScreenText = new JLabel("Screen text voor gasten");
        gastenScreenText.setBounds(20, 20, 560, 180);
        gastenScreenText.setVisible(false);
        panel.add(gastenScreenText);

        gastenTerug = new JButton( new AbstractAction("Terug") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                gastenTerug.setVisible(false);
                gastenToevoegen.setVisible(false);
                gastencontroleren.setVisible(false);

                gastenScreenText.setVisible(false);

                toernooiButton.setVisible(true);
                gastenButton.setVisible(true);
                masterClassButton.setVisible(true);
                inschrijvingButton.setVisible(true);

                screenText.setVisible(true);
            }
        });
        gastenTerug.setBounds(15, 300, 310, 50);
        gastenTerug.setVisible(false);
        panel.add(gastenTerug);

        gastenToevoegen = new JButton( new AbstractAction("Gasten toevoegen") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                JDialog d = new GastToevoegen();
            }
        });
        gastenToevoegen.setBounds(175, 200, 150, 50);
        gastenToevoegen.setVisible(false);
        panel.add(gastenToevoegen);

        gastencontroleren = new JButton( new AbstractAction("Gasten controleren") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                JDialog d = new GastControleren();
            }
        });
        gastencontroleren.setBounds(15, 200, 150, 50);
        gastencontroleren.setVisible(false);
        panel.add(gastencontroleren);


        //MASTERCLASS SCREEN
        masterClassScreenText = new JLabel("Screen text voor masterclass");
        masterClassScreenText.setBounds(20, 20, 560, 180);
        masterClassScreenText.setVisible(false);
        panel.add(masterClassScreenText);

        masterClassTerug = new JButton( new AbstractAction("Terug") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                masterClassTerug.setVisible(false);
                masterClassToevoegen.setVisible(false);
                masterClasscontroleren.setVisible(false);

                masterClassScreenText.setVisible(false);

                toernooiButton.setVisible(true);
                gastenButton.setVisible(true);
                masterClassButton.setVisible(true);
                inschrijvingButton.setVisible(true);

                screenText.setVisible(true);
            }
        });
        masterClassTerug.setBounds(15, 300, 310, 50);
        masterClassTerug.setVisible(false);
        panel.add(masterClassTerug);

        masterClassToevoegen = new JButton( new AbstractAction("Masterclass toevoegen") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                JDialog d = new MasterclassToevoegen();
            }
        });
        masterClassToevoegen.setBounds(175, 200, 150, 50);
        masterClassToevoegen.setVisible(false);
        panel.add(masterClassToevoegen);

        masterClasscontroleren = new JButton( new AbstractAction("Masterclass controleren") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                new MasterclassControleren();
            }
        });
        masterClasscontroleren.setBounds(15, 200, 150, 50);
        masterClasscontroleren.setVisible(false);
        panel.add(masterClasscontroleren);

        //INSCHRIJVING SCREEN
        inschrijvingScreenText = new JLabel("Screen text voor inschrijvingen");
        inschrijvingScreenText.setBounds(20, 20, 560, 180);
        inschrijvingScreenText.setVisible(false);
        panel.add(inschrijvingScreenText);

        inschrijvingButtonTerug = new JButton( new AbstractAction("Terug") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                inschrijvingButtonTerug.setVisible(false);
                voorToernooi.setVisible(false);
                voorMasterclass.setVisible(false);
                toernooInschrijvingControleren.setVisible(false);

                inschrijvingScreenText.setVisible(false);

                toernooiButton.setVisible(true);
                gastenButton.setVisible(true);
                masterClassButton.setVisible(true);
                inschrijvingButton.setVisible(true);

                screenText.setVisible(true);
            }
        });
        inschrijvingButtonTerug.setBounds(15, 300, 310, 50);
        inschrijvingButtonTerug.setVisible(false);
        panel.add(inschrijvingButtonTerug);

        voorToernooi = new JButton( new AbstractAction("Toernooi inschrijven") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                gastenTerug.setVisible(false);
                gastenToevoegen.setVisible(false);
                gastencontroleren.setVisible(false);

                gastenScreenText.setVisible(false);

                new ToernooiInschrijven();
            }
        });
        voorToernooi.setBounds(175, 200, 150, 50);
        voorToernooi.setVisible(false);
        panel.add(voorToernooi);

        voorMasterclass = new JButton( new AbstractAction("Masterclass inschrijven") {
            @Override
            public void actionPerformed( ActionEvent e ) {
                gastenTerug.setVisible(false);
                gastenToevoegen.setVisible(false);
                gastencontroleren.setVisible(false);

                gastenScreenText.setVisible(false);

               // new MasterclassInschrijven();
            }
        });
        voorMasterclass.setBounds(15, 200, 150, 50);
        voorMasterclass.setVisible(false);
        panel.add(voorMasterclass);

        toernooInschrijvingControleren = new JButton(new AbstractAction("<html>Toernooi inschrijving<br> controleren</html>") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog d = new ToernooiInschrijvingControleren();
                d.setSize(750,500);
                d.setVisible(true);
            }
        });
        toernooInschrijvingControleren.setBounds(375,200,150,50);
        toernooInschrijvingControleren.setVisible(false);
        panel.add(toernooInschrijvingControleren);
    }
}
