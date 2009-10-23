/*
 * Created on 16.02.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package de.vs.unikassel.logger;

import java.io.PrintStream;


/**
 * @author bleul
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SystemOutWriter extends PrintStream {
    public SystemOutWriter() {
        super(new CustomizedOutputStream());
        System.setOut(this);
        System.setErr(this);
    }

    public void addListener(SystemOutListener listener) {
        ((CustomizedOutputStream) this.out).addListener(listener);
    }

    public void removeListener(SystemOutListener listener) {
        ((CustomizedOutputStream) this.out).removeListener(listener);
    }

    public void removeAllListener() {
        ((CustomizedOutputStream) this.out).removeAllListener();
    }
}
