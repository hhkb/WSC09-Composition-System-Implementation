/*
 * Created on 24.02.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.vs.unikassel.GuiComponents;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.event.ActionEvent;

import javax.swing.*;


/**
 * @author bleul
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SourceViewer extends JFrame implements ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SourceViewer() {
        // Handle Swing setup
        super("Source Viewer");
        setBounds(200, 200, 600, 450);
    }

    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    public void init(String contents) {
        JTextArea textarea = new JTextArea(contents);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton closebutton = new JButton("Close");
        closebutton.addActionListener(this);
        buttonPanel.add(closebutton);

        this.add(new JScrollPane(textarea));
        this.add(buttonPanel, BorderLayout.PAGE_END);

        this.setVisible(true);
    }
}
