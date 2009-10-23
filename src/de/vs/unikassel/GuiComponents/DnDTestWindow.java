/*
 * Created on 25.02.2006
 *
 * 
 */
package de.vs.unikassel.GuiComponents;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;

import java.io.*;

import javax.swing.*;


/**
 * @author bleul
 *
 * 
 */
public class DnDTestWindow extends JFrame implements DropTargetListener,
    ActionListener {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private JTextArea textarea = null;

    public DnDTestWindow() {
        super("Source Viewer");
        setBounds(200, 200, 600, 450);
        new DropTarget(this, this);
        init("");
    }

    public void actionPerformed(ActionEvent e) {
        this.dispose();
    }

    public void init(String contents) {
        textarea = new JTextArea(contents);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JButton closebutton = new JButton("Close");
        closebutton.addActionListener(this);
        buttonPanel.add(closebutton);

        this.add(new JScrollPane(textarea));
        this.add(buttonPanel, BorderLayout.PAGE_END);

        this.setVisible(true);
    }

    public void process(String s) {
        this.textarea.append(s);
    }

    public void dragEnter(DropTargetDragEvent evt) {
        // Called when the user is dragging and enters this drop target.
    }

    public void dragOver(DropTargetDragEvent evt) {
        // Called when the user is dragging and moves over this drop target.
    }

    public void dragExit(DropTargetEvent evt) {
        // Called when the user is dragging and leaves this drop target.
    }

    public void dropActionChanged(DropTargetDragEvent evt) {
        // Called when the user changes the drag action between copy or move.
    }

    public void drop(DropTargetDropEvent evt) {
        try {
            Transferable t = evt.getTransferable();

            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

                String s = (String) t.getTransferData(DataFlavor.stringFlavor);
                evt.getDropTargetContext().dropComplete(true);
                process(s);
            } else {
                evt.rejectDrop();
            }
        } catch (IOException e) {
            evt.rejectDrop();
        } catch (UnsupportedFlavorException e) {
            evt.rejectDrop();
        }
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
        DnDTestWindow frame = new DnDTestWindow();

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
}
