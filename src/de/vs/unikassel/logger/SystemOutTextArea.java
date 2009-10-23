/*
 * Created on 16.02.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.vs.unikassel.logger;

import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JViewport;
import javax.swing.UIManager;


/**
 * @author bleul
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SystemOutTextArea extends JScrollPane implements SystemOutListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public JTextArea textbox = null;
    public SystemOutWriter systemwriter = null;
    public JViewport viewport = null;

    public SystemOutTextArea(JTextArea area) {
        super(area);
        this.textbox = area;
        this.textbox.setEditable(false);
        setWheelScrollingEnabled(true);
        systemwriter = new SystemOutWriter();
        systemwriter.addListener(this);
        viewport = this.getViewport();
    }

    public void SystemPrint(String message) {
        this.textbox.append(message);
        viewport = new JViewport();

        Point p = new Point(this.getHorizontalScrollBar().getMaximum(),
                this.getVerticalScrollBar().getMaximum());
        viewport.toViewCoordinates(p);
    }

    public static void createAndShowGUI() {
        //Make sure we have nice window decorations.	
        try {
            UIManager.setLookAndFeel(
                "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            JFrame.setDefaultLookAndFeelDecorated(true);
        }

        //Create and set up the window.
        JFrame frame = new JFrame("System Out Window");
        SystemOutTextArea tArea = new SystemOutTextArea(new JTextArea());
        frame.setBounds(100, 100, 500, 200);
        frame.add(tArea);

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

	public JTextArea getTextbox() {
		return textbox;
	}
}
