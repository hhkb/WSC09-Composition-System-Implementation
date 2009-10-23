/*
 * Copyright (c) 2006 Thomas Weise
 * 
 * E-Mail           : tweise@gmx.de
 * Creation Date    : 2006-04-20 15:58:11
 * Original Filename: test.org.sigoa.wsc.c2007.kb.KnowledgeBase.java
 * Version          : 1.0.3
 * Last modification: 2006-05-08
 *                by: Thomas Weise
 * 
 * License          : GNU LESSER GENERAL PUBLIC LICENSE
 *                    Version 2.1, February 1999
 *                    You should have received a copy of this license along
 *                    with this library; if not, write to the Free Software
 *                    Foundation, Inc., 59 Temple Place, Suite 330, Boston,
 *                    MA 02111-1307, USA or download the license under
 *                    http://www.gnu.org/copyleft/lesser.html.
 *                    
 * Warranty         : This software is provided "as is" without any
 *                    warranty; without even the implied warranty of
 *                    merchantability or fitness for a particular purpose.
 *                    See the Gnu Lesser General Public License for more
 *                    details.
 */

package test.org.sigoa.wsc.c2007.kb;

import java.io.Serializable;

/**
 * <p>
 * The knowledge base can be querried for information on semantic concepts.
 * It also is used as service register. The shared instance of this class
 * holds the information on all the available concepts in form of a hashed
 * concept tree as well as the hash of all available services.
 * </p>
 * <p>
 * Notice: Services that are similar to each other are stored as liked list
 * where only the list's head is hashed. This improves the speed of service
 * composition since it reduces the possible count of paths in the search
 * tree.
 * 
 * @see Service#nextEqual()
 *      </p>
 * @author Thomas Weise
 */
public final class KnowledgeBase implements Serializable {
  /**
   * The serial version uid.
   */
  private static final long serialVersionUID = 1;

  /**
   * The concept hash.
   */
  private transient Concept[] m_data;

  /**
   * The count of concepts stored.
   */
  private transient int m_count;

  /**
   * The count of services stored.
   */
  private transient int m_service_count;

  /**
   * The service register.
   */
  private transient Service[] m_services;

  /**
   * The count of hashed services.
   */
  private transient int m_service_h;

  /**
   * Set this to true if memory consumption is dodgy
   */
  public static volatile boolean LOW_MEM = false;

  /**
   * Create a new, empty knowledge base.
   */
  public KnowledgeBase() {
    super();
    this.m_data = new Concept[64 * 1024];
    this.m_services = new Service[1024];
  }

  /**
   * This method is used to insert a concept into the knowledge base. The
   * knowledge base's basic hash will be updated, and, if needed, resized.
   * Concept's generalization often cannot be resolved directly, since we
   * cannot make any assumptions on the order of concept's occurences in
   * the xsd file.
   * 
   * @param name
   *          The name of the new concept to store.
   * @param generalization
   *          The concept that is a generalization of the new one. Or, in
   *          other words, the concept which is specialized by the new one.
   */
  public final void insertConcept(final String name,
      final String generalization) {
    int c, l, i, hc;
    Concept[] d, dx;
    Concept c1, c2;

    d = this.m_data;
    l = d.length;
    hc = name.hashCode();

    dummy: {
      // look if we found a "dummy concept"
      i = (l - 1);
      for (c2 = d[hc & i]; c2 != null; c2 = c2.m_next) {
        if (c2.m_name.equals(name)) {
          l--;
          break dummy;
        }
      }

      c = this.m_count;
      // we need to insert at least one new concept -> update hash
      this.m_count = c + 1;

      if ((3 * c) >= (l << 1)) {
        i = l - 1;
        l <<= 1;
        dx = new Concept[l];

        for (--l; i >= 0; i--) {
          for (c1 = d[i]; c1 != null; c1 = c2) {
            c2 = c1.m_next;
            c = (c1.m_name.hashCode() & l);

            c1.m_next = dx[c];
            dx[c] = c1;
          }
        }

        this.m_data = d = dx;
      } else
        l--;

      // insert new concept.
      c = (hc & l);
      d[c] = c2 = new Concept(name, d[c], hc);
    }

    if (generalization == null)
      return;

    hc = generalization.hashCode();

    // search generalization
    for (c1 = d[hc & l]; c1 != null; c1 = c1.m_next) {
      if (c1.m_name.equals(generalization)) {
        c2.m_generalization = c1;
        c1.addConcept(c2);
        return;
      }
    }

    // generalization not found! -> insert dummy concept.

    c = (hc & l);
    d[c] = c2.m_generalization = new Concept(generalization, d[c], hc);
    c2.m_generalization.addConcept(c2);
    this.m_count++;
  }

  /**
   * Find a concept in the knowledge base.
   * 
   * @param name
   *          The name of the concept to find.
   * @return The concept of the name searched, or <code>null</code> if
   *         none could be found.
   */
  public final Concept getConcept(final String name) {
    Concept[] d;
    Concept s;

    d = this.m_data;

    for (s = d[name.hashCode() & (d.length - 1)]; s != null; s = s.m_next) {
      if (s.m_name.equals(name))
        return s;
    }

    return null;
  }

  /**
   * Obtain the count of concepts stored.
   * 
   * @return The count of concepts stored.
   */
  public final int getConceptCount() {
    return this.m_count;
  }

  /**
   * Obtain the count of services stored.
   * 
   * @return The count of services stored.
   */
  public final int getServiceCount() {
    return this.m_service_count;
  }

  /**
   * Store the given service in the knowledge base.
   * 
   * @param service
   *          The service to be stored.
   */
  public final void storeService(final Service service) {
    Concept[] o;
    Service[] s, s2;
    int i, hc, l;
    Service x, y;

    o = service.m_out;

    if (o.length > 0) {
      this.m_service_count++;
      s = o[0].m_services;

      for (i = (o[0].m_serviceCount - 1); i >= 0; i--) {
        if (service.isServiceEqual(s[i])) {
          return;
        }
      }

      for (i = (o.length - 1); i >= 0; i--) {
        o[i].addService(service);
      }

      s = this.m_services;
      l = s.length;
      if ((3 * (this.m_service_h++)) > (l << 1)) {
        i = (l - 1);
        l <<= 1;
        s2 = new Service[l];
        for (--l; i >= 0; i--) {
          for (x = s[i]; x != null; x = y) {
            y = x.m_next;
            hc = (x.m_name.hashCode() & l);
            x.m_next = s2[hc];
            s2[hc] = x;
          }
        }
        this.m_services = s = s2;
      } else
        l--;

      hc = (service.m_name.hashCode() & l);
      service.m_next = s[hc];
      s[hc] = service;
    }
  }

  /**
   * Obtain the service of the given name from the service knowledge base.
   * 
   * @param name
   *          The name of the service to retrieve.
   * @return The service specified, or <code>null</code> if no service of
   *         the given name is stored.
   */
  public final Service getService(final String name) {
    Service x, y;

    for (x = this.m_services[name.hashCode()
        & (this.m_services.length - 1)]; x != null; x = x.m_next) {
      for (y = x; y != null; y = y.m_equal) {
        if (name.equals(y.m_name))
          return y;
      }
    }

    return null;
  }

  /**
   * Initialize the knowledge base.
   * 
   * @param xsdFile
   *          The xsd file containing the concepts.
   * @param wsdlDir
   *          The directory containing the wsdl files.
   */
  public final void initialize(final Object xsdFile, final Object wsdlDir) {
    try {
      new XSDReader(this).parse(xsdFile);
      new WSDLReader(this).parseDirectory(wsdlDir, true);
      if (!(LOW_MEM))
        this.summary();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  /**
   * Initialize the knowledge base by using a thread.
   * 
   * @param xsdFile
   *          The xsd file containing the concepts.
   * @param wsdlDir
   *          The directory containing the wsdl files.
   * @return The waitable to wait for.
   */
  public final Completion initializeByThread(final Object xsdFile,
      final Object wsdlDir) {
    InitializerThread t;

    t = new InitializerThread(xsdFile, wsdlDir);
    t.start();

    return t.m_c;
  }

  /**
   * Finalize initialization
   */
  private final void summary() {
    int i;
    Concept c;
    ServiceList sl;
    Concept[] cc;

    sl = new ServiceList(-1);
    cc = this.m_data;

    for (i = (cc.length - 1); i >= 0; i--) {
      for (c = cc[i]; c != null; c = c.m_next) {
        c.getPromising(sl);
        sl.m_count = 0;
      }
    }
  }

  /**
   * The completion waiter.
   * 
   * @author Thomas Weise
   */
  public static final class Completion {
    /**
     * the boolean value.
     */
    private volatile boolean m_b;

    /**
     * the constructor
     */
    Completion() {
      super();
    }

    /**
     * Wait for the completion.
     */
    public final void waitFor() {
      for (;;) {
        synchronized (this) {
          if (this.m_b)
            return;
          try {
            this.wait();
          } catch (Throwable tt) {
            tt.printStackTrace();
          }
        }
      }
    }

    /**
     * Trigger the completion.
     */
    final void trigger() {
      synchronized (this) {
        this.m_b = true;
        this.notifyAll();
      }
    }
  }

  /**
   * The initializer thread.
   * 
   * @author Thomas Weise
   */
  private final class InitializerThread extends Thread {
    /**
     * the completion.
     */
    final Completion m_c;

    /** the xsd file */
    private final Object m_xsdFile;

    /** the wsdl directory */
    private final Object m_wsdlDir;

    /**
     * create the thread
     * 
     * @param xsdFile
     *          The xsd file containing the concepts.
     * @param wsdlDir
     *          The directory containing the wsdl files.
     */
    InitializerThread(final Object xsdFile, final Object wsdlDir) {
      super();
      this.m_c = new Completion();
      this.m_xsdFile = xsdFile;
      this.m_wsdlDir = wsdlDir;
    }

    /**
     * do the work
     */
    @Override
    public final void run() {
      KnowledgeBase.this.initialize(this.m_xsdFile, this.m_wsdlDir);
      this.m_c.trigger();
    }

  }
}
