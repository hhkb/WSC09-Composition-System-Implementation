package de.vs.unikassel.logger;

import java.io.OutputStream;

import java.util.Vector;


public class CustomizedOutputStream extends OutputStream {
    private Vector SystemOutListeners = new Vector();

    public CustomizedOutputStream() {
        super();
    }

    public void write(int i) {
        this.printOut("" + i);
    }

    public void write(byte[] b, int off, int len) {
        this.printOut(new String(b, off, len));
    }

    public void write(byte[] b) {
        this.printOut(new String(b));
    }

    public void addListener(SystemOutListener listener) {
        this.SystemOutListeners.add(listener);
    }

    public void removeListener(SystemOutListener listener) {
        this.SystemOutListeners.remove(listener);
    }

    public void removeAllListener() {
        this.SystemOutListeners.removeAllElements();
    }

    public void printOut(String message) {
        for (int i = 0; i < this.SystemOutListeners.size(); i++) {
            SystemOutListener listener = (SystemOutListener) this.SystemOutListeners.get(i);
            listener.SystemPrint(message);
        }
    }
}
