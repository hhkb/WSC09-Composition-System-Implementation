/*
 * Copyright (c) 2006 Thomas Weise
 * Software Foundation Classes
 * http://sourceforge.net/projects/java-sfc
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2007-08-23
 * Creator          : Thomas Weise
 * Original Filename: org.sfc.utils.Log.java
 * Last modification: 2006-08-23
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to theFree Software
 *                    Foundation, Inc. 51 Franklin Street, Fifth Floor,
 *                    Boston, MA 02110-1301, USA or download the license
 *                    under http://www.gnu.org/licenses/lgpl.html or
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package org.sfc.utils;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.sfc.parallel.ErrorHandler;
import org.sfc.text.TextUtils;

/**
 * This class provides the logging methods
 * 
 * @author Thomas Weise
 */
public final class Log extends ErrorHandler implements
    UncaughtExceptionHandler {
  /**
   * The default uncaught exception handler
   */
  protected static final UncaughtExceptionHandler DEFAULT_EH = Thread
      .getDefaultUncaughtExceptionHandler();

  /**
   * the log class
   */
  private static final String LOG_CLASS = Log.class.getCanonicalName();

  /**
   * the log
   */
  private final static Log LOG;

  static {
    LOG = new Log();
    Thread.setDefaultUncaughtExceptionHandler(LOG);
  }

  /**
   * the default output logger
   */
  private Logger m_out;

  /**
   * Access the log
   * 
   * @return the log
   */
  public static final Log getLog() {
    if (LOG == null)
      throw new RuntimeException("NO LOG!"); //$NON-NLS-1$
    return LOG;
  }

  /**
   * Create the log
   */
  private Log() {
    super();
    Logger l;

    try {
      l = Logger.getAnonymousLogger();
    } catch (Throwable t) {
      l = null;
    }

    this.m_out = l;
  }

  /**
   * Set the logger to write to
   * 
   * @param log
   *          the logger
   */
  public synchronized void setLogger(final Logger log) {
    if (log != null)
      this.m_out = log;
  }

  /**
   * Obtain the current logger.
   * 
   * @return the current logger used to write to
   */
  public Logger getLogger() {
    return this.m_out;
  }

  /**
   * Log something.
   * 
   * @param level
   *          the log level (optional, if not used <code>null</code>)
   * @param message
   *          the message (optional, if not used <code>null</code>)
   * @param millis
   *          the system time (optional, if not used <code>-1</code>)
   * @param error
   *          the error (optional, if not used <code>null</code>)
   * @param thread
   *          the thread (optional, if not used <code>null</code>)
   * @param sourceClass
   *          the source class name (optional, if not used
   *          <code>null</code>)
   * @param sourceMethod
   *          the source method name (optional, if not used
   *          <code>null</code>)
   */
  public void log(final Level level, final String message,
      final long millis, final Throwable error, final Thread thread,
      final String sourceClass, final String sourceMethod) {
    LogRecord r;
    StackTraceElement stack[];
    StackTraceElement e;
    int i;
    String sm, sc;

    try {
      r = new LogRecord((level != null) ? level
          : ((error == null) ? Level.INFO : Level.SEVERE), message);
      if (millis > 0)
        r.setMillis(millis);
      if (error != null)
        r.setThrown(error);

      sm = sourceMethod;
      sc = sourceClass;

      if ((sm == null) || (sc == null)) {
        try {

          if (error != null) {
            e = error.getStackTrace()[0];
            if ((sc == null) || Utils.testEqual(sc, e.getClassName())) {
              if (sm == null)
                sm = e.getMethodName();
              if (sc == null)
                sc = e.getClassName();
            }
          } else {
            if (thread != null)
              stack = thread.getStackTrace();
            else
              stack = null;
            if (stack == null)
              stack = (new Throwable()).getStackTrace();

            i = 0;
            for (i = 0; i < stack.length; i++) {
              e = stack[i];
              if (Utils.testEqual(e.getClassName(), LOG_CLASS))
                break;
            }

            for (++i; i < stack.length; i++) {
              e = stack[i];
              if (!(Utils.testEqual(e.getClassName(), LOG_CLASS))) {
                if ((sc == null) || Utils.testEqual(sc, e.getClassName())) {
                  if (sm == null)
                    sm = e.getMethodName();
                  if (sc == null)
                    sc = e.getClassName();
                  break;
                }
              }
            }
          }
        } catch (Throwable xk) {
          //
        }
      }

      if (sm != null)
        r.setSourceMethodName(sm);
      if (sc != null)
        r.setSourceClassName(sc);

      // cannot use thread
      // see LogRecord#threadIds
      // if (thread != null)
      // r.setThreadID(((int) (thread.getId())));

      this.log(r);
    } catch (Throwable tt) {//      
    }
  }

  /**
   * Write the given object to the log.
   * 
   * @param object
   *          the object to be logged
   */
  public void log(final Object object) {
    try {
      this.log(TextUtils.toString(object));
    } catch (Throwable t) {
      this.log(t);
    }
  }

  /**
   * Log the specified string
   * 
   * @param value
   *          the string to be logged
   */
  public void log(final String value) {
    this.log(Level.INFO, value, -1, null, null, null, null);
  }

  /**
   * log an error
   * 
   * @param error
   *          the error to be logged
   */
  public void log(final Throwable error) {
    this.uncaughtException(null, error);
  }

  /**
   * Log the given log record
   * 
   * @param record
   *          the log record
   */
  private synchronized void log(final LogRecord record) {
    StringBuilder sb;
    try {
      if (record != null) {
        if (this.m_out != null)
          this.m_out.log(record);
        else {
          if (System.out != null) {
            sb = new StringBuilder(1024);
            appendLogRecord(record, sb);
            System.out.println(sb.toString());
          }
        }
      }
    } catch (Throwable tt) {//

    }
  }

  /**
   * Append the given log record to a string builder.
   * 
   * @param r
   *          the record
   * @param sb
   *          the string builder
   */
  protected static final void appendLogRecord(final LogRecord r,
      final StringBuilder sb) {
    String s;
    long l;
    Level x;
    boolean y;
    Throwable t;

    if (r != null) {
      l = r.getMillis();
      TextUtils.timeToString((l >= 0) ? l : System.currentTimeMillis(),
          false);

      s = r.getSourceClassName();
      if (s != null) {
        sb.append(' ');
        sb.append(s);
      }

      s = r.getSourceMethodName();
      if (s != null) {
        sb.append(' ');
        sb.append(s);
      }
      sb.append(TextUtils.LINE_SEPARATOR);

      y = false;
      x = r.getLevel();
      if (x != null) {
        sb.append(x.toString());
        y = true;
      }

      s = r.getMessage();
      if (s != null) {
        if (y) {
          sb.append(':');
          sb.append(' ');
        }
        sb.append(r.getMessage());
        y = true;
      }
      if (y)
        sb.append(TextUtils.LINE_SEPARATOR);

      t = r.getThrown();
      if (t != null) {
        TextUtils.appendError(t, sb);
        sb.append(TextUtils.LINE_SEPARATOR);
      }

      sb.append("SequenceNumber: "); //$NON-NLS-1$
      sb.append(r.getSequenceNumber());
      sb.append(TextUtils.LINE_SEPARATOR);

      sb.append("ThreadId: "); //$NON-NLS-1$
      sb.append(r.getThreadID());
    }
  }

  /**
   * Method invoked when the given thread terminates due to the given
   * uncaught exception.
   * <p>
   * Any exception thrown by this method will be ignored by the Java
   * Virtual Machine.
   * 
   * @param t
   *          the thread
   * @param e
   *          the exception
   */
  public void uncaughtException(final Thread t, final Throwable e) {

    this.log(Level.SEVERE, null, -1, e, t, null, null);
    try {
      if (DEFAULT_EH != null)
        DEFAULT_EH.uncaughtException(t, e);
    } catch (Throwable tx) {
      //
    } finally {
      Utils.invokeGC();
    }
  }

  /**
   * It is recommended that derived classes use this method for
   * error-processing.
   * 
   * @param t
   *          the error object
   */
  @Override
  protected void onError(final Throwable t) {
    this.log(t);
    super.onError(t);
  }
}