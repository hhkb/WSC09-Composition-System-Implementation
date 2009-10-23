package de.vs.unikassel.GuiComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class ADDOInfoBox extends JFrame {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JPanel jContentPane = null;
    private JLabel jLabelImage = null;
    private JButton jButtonOk = null;
    private JLabel jLabelText = null;
    private JLabel jLabelText1 = null;
    private JLabel jLabelText2 = null;
    private JLabel jLabelText3 = null;
    private JLabel jLabelText4 = null;
    private JLabel jLabelText5 = null;
    private JLabel jLabelText6 = null;

    /**
     * This is the default constructor
     */
    public ADDOInfoBox() {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     * @return void
     */
    private void initialize() {
        this.setSize(444, 530);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("About");
        this.setContentPane(getJContentPane());
        this.setResizable(false);
    }

    /**
     * This method initializes jContentPane
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJLabelImage(), null);
            jContentPane.add(getJButtonOk(), null);

            jLabelText = new JLabel();
            jLabelText.setBounds(new java.awt.Rectangle(10, 236, 421, 23));
            jLabelText.setText(
                "ADDO - Automatic Service Brokering in Service Oriented Architectures.");
            jContentPane.add(jLabelText, null);

            jLabelText1 = new JLabel();
            jLabelText1.setBounds(new java.awt.Rectangle(10, 265, 423, 23));
            jLabelText1.setText(
                "2006 University of Kassel, Distributed Systems Group");
            jContentPane.add(jLabelText1, null);

            jLabelText2 = new JLabel();
            jLabelText2.setBounds(new java.awt.Rectangle(10, 294, 422, 23));
            jLabelText2.setText("Programmed and Designed by Steffen Bleul");
            jContentPane.add(jLabelText2, null);

            jLabelText3 = new JLabel();
            jLabelText3.setBounds(new java.awt.Rectangle(10, 323, 422, 23));
            jLabelText3.setText("See: http://www.vs.uni-kassel.de/ADDO/");
            jContentPane.add(jLabelText3, null);
        }

        return jContentPane;
    }

    private JLabel getJLabelImage() {
        if (jLabelImage == null) {
            ImageIcon image = GuiPics.ADDO_LOGO_ICON;
            //int iHeight = image.getIconHeight();
            //int iWidth  = image.getIconWidth();
            jLabelImage = new JLabel(image);
            jLabelImage.setBounds(new java.awt.Rectangle(10, 5, 421, 216));
        }

        return jLabelImage;
    }

    /**
     * This method initializes jButtonOk
     *
     * @return javax.swing.JButton
     */
    private JButton getJButtonOk() {
        if (jButtonOk == null) {
            jButtonOk = new JButton();
            jButtonOk.setBounds(new java.awt.Rectangle(163, 448, 114, 42));
            jButtonOk.setText("OK");
            jButtonOk.addActionListener(new MyActionHandling(this));
        }

        return jButtonOk;
    }

    public static void createAndShowGUI() {
        //Make sure we have nice window decorations.	
        try {
            UIManager.setLookAndFeel(
                "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            JFrame.setDefaultLookAndFeelDecorated(true);
        }

        //JFrame.setDefaultLookAndFeelDecorated(true);
        //Create and set up the window.
        ADDOInfoBox frame = new ADDOInfoBox();

        //Display the window.
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    createAndShowGUI();
                }
            });
    }
} //  @jve:decl-index=0:visual-constraint="111,10"


class MyActionHandling implements ActionListener {
    private ADDOInfoBox aTheme = null;

    public MyActionHandling(ADDOInfoBox aTheme) {
        this.aTheme = aTheme;
    }

    public void actionPerformed(ActionEvent e) {
        aTheme.dispose();
    }
}
