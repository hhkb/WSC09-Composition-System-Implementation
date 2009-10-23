/*
 * Created on 21.02.2006
 *
 * 
 */
package de.vs.unikassel.GuiComponents;

import javax.swing.*;


/**
 * @author bleul
 *
 * 
 */
public class GuiPics {
    public final static ImageIcon OPEN_ICON = createImageIcon(
            "/de/unikassel/vs/images/open.gif");
    public final static ImageIcon SAVE_ICON = createImageIcon(
            "/de/unikassel/vs/images/save.gif");
    public final static ImageIcon REMOVE_ICON = createImageIcon(
            "/de/unikassel/vs/images/close.gif");
    public final static ImageIcon EXIT_ICON = createImageIcon(
            "/de/unikassel/vs/images/exit.gif");
    public final static ImageIcon APPLY_ICON = createImageIcon(
            "/de/unikassel/vs/images/apply.gif");
    public final static ImageIcon CANCEL_ICON = createImageIcon(
            "/de/unikassel/vs/images/cancel.gif");
    public final static ImageIcon NEW_ICON = createImageIcon(
            "/de/unikassel/vs/images/new.gif");
    public final static ImageIcon LOGO_ICON = createImageIcon(
            "/de/unikassel/vs/images/addologolittle.gif");
    public final static ImageIcon ADDO_LOGO_ICON = createImageIcon(
            "/de/unikassel/vs/images/addologo.gif");
    public final static ImageIcon SERVICEOPEN_ICON = createImageIcon(
            "/de/unikassel/vs/images/service.gif");
    public final static ImageIcon SERVICE_ICON = createImageIcon(
            "/de/unikassel/vs/images/serviceopen.gif");
    public final static ImageIcon LEAF_ICON = createImageIcon(
            "/de/unikassel/vs/images/leaf.gif");
    public final static ImageIcon VIEWD_ICON = createImageIcon(
            "/de/unikassel/vs/images/viewd.gif");
    public final static ImageIcon SOURCE_ICON = createImageIcon(
            "/de/unikassel/vs/images/source.gif");
    public final static ImageIcon DEST_ICON = createImageIcon(
            "/de/unikassel/vs/images/dest.gif");
    public final static ImageIcon DOCUMENT_ICON = createImageIcon(
            "/de/unikassel/vs/images/content.gif");

    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GuiPics.class.getResource(path);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);

            return null;
        }
    }
}
