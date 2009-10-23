/*
 * Copyright (c) 2008 Thomas Weise for sigoa
 * Simple Interface for Global Optimization Algorithms
 * http://www.sigoa.org/
 *
 * E-Mail           : info@sigoa.org
 * Creation Date    : TODO
 * Creator          : Thomas Weise
 * Original Filename: wsc.c2008.parser.Stage.java
 * Last modification: TODO
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

package test.org.sigoa.wsc.c2009.parser;

import java.util.Collections;
import java.util.List;

import org.sfc.collections.CollectionUtils;
import org.sfc.text.Textable;
import org.sfc.xml.sax.SAXWriter;

/**
 * the stage
 *
 * @author Thomas Weise
 */
public class Stage extends Textable {
  /**
   * the provided services
   */
  final List<OntologyElement> m_provided;

  /**
   * the known list
   */
  final List<OntologyElement> m_known;

  /**
   * the wanted list
   */
  final List<OntologyElement> m_wanted;

  /**
   * the services
   */
  final List<Service> m_serv;

  /**
   * valid?
   */
  boolean m_valid;

  /**
   * the minimally involved services
   */
  int m_minServices;

  /**
   * the possible combinations
   */
  double m_combos;

  /**
   * the length
   */
  int m_length;

  /**
   * the runtime
   */
  long m_runtime;
  
/**
 * the throughput
 */
  long m_throughput;
  
  /**
   * Create a new stage
   *
   * @param sup
   */
  public Stage(final Stage sup) {
    super();
    this.m_known = CollectionUtils.createList();
    this.m_wanted = CollectionUtils.createList();
    this.m_serv = CollectionUtils.createList();
    if (sup != null) {
      this.m_serv.addAll(sup.m_serv);
      this.m_provided = sup.m_provided;
      this.m_known.addAll(sup.m_known);
      this.m_wanted.addAll(sup.m_wanted);
      this.m_valid = sup.m_valid;
      this.m_minServices = sup.m_minServices;
      this.m_length = sup.m_length;
    } else {
      this.m_valid = true;
      this.m_provided = this.m_known;
    }
    this.m_throughput = Long.MAX_VALUE;
    this.m_combos = 1l;
  }

  /**
   * can the process be executed up to this stage?
   *
   * @return yes/no
   */
  public boolean canExecute() {
    return this.m_valid;
  }

  /**
   * is this process satisfying all wanted outputs?
   *
   * @return yes/no
   */
  public boolean isSatisfying() {
    return this.m_wanted.size() <= 0;
  }

  /**
   * is this process already a correct and complete solution?
   *
   * @return
   */
  public boolean isCorrectSoltion() {
    return (this.canExecute() && this.isSatisfying());
  }

  /**
   * Append this object's textual representation to a string builder.
   *
   * @param w
   *          The writer builder to append to.
   * @param apriori
   * @param fin
   * @see #toString()
   */
  public void toWriter(final SAXWriter w, final boolean apriori,
      final boolean fin) {
    int i, j;

    w.startElement("stage");//$NON-NLS-1$
    w.attribute("when", //$NON-NLS-1$
        apriori ? "before" : //$NON-NLS-1$
            "after");//$NON-NLS-1$
    // w.attribute("validity", String.valueOf(this.m_valid));//$NON-NLS-1$
    w.startElement("knownConcepts");//$NON-NLS-1$
    w.attribute("number", String.valueOf(i = this.m_known.size()));//$NON-NLS-1$
    Collections.sort(this.m_known);
    for (j = 0; j < i; j++) {
      if (j > 0)
        w.write(", ");//$NON-NLS-1$
      w.writeObject(this.m_known.get(j));
    }
    w.endElement();

    w.startElement("wantedConcepts");//$NON-NLS-1$
    w.attribute("number", String.valueOf(i = this.m_wanted.size()));//$NON-NLS-1$
    Collections.sort(this.m_wanted);
    for (j = 0; j < i; j++) {
      if (j > 0)
        w.write(", ");//$NON-NLS-1$
      w.writeObject(this.m_wanted.get(j));
    }
    w.endElement();

    // w.startElement("summary");//$NON-NLS-1$

    w.startElement("isValid");//$NON-NLS-1$
    w.attribute("value", String.valueOf(this.canExecute()));//$NON-NLS-1$
    if (this.canExecute()) {
      w.write("This process can be executed.");}//$NON-NLS-1$}
    else {
      w
          .write("This process CANNOT be executed. It has input parameters which have not been provided");//$NON-NLS-1$
    }
    w.endElement();

    w.startElement("isSatisfying");//$NON-NLS-1$
    w.attribute("value", String.valueOf(this.isSatisfying()));//$NON-NLS-1$
    if (this.isSatisfying()) {
      w
          .write("If executed, this process will provide us with all wanted concepts.");}//$NON-NLS-1$
    else {
      w
          .write("Up to this stage, this process does not provide all wanted concepts.");}//$NON-NLS-1$
    w.endElement();

    w.startElement("isSolution");//$NON-NLS-1$
    w.attribute("value", String.valueOf(this.isCorrectSoltion()));//$NON-NLS-1$
    if (this.isCorrectSoltion()) {
      w
          .write("After the process has been executed up to this stage, the challenge has been solved correctly.");//$NON-NLS-1$
    } else {
      if (!(this.canExecute())) {
        w
            .write("This process CANNOT be executed, hence, it also does not solve the challenge.");//$NON-NLS-1$
      }
      if (!(this.isSatisfying())) {
        if (!(this.canExecute()))
          w.writeChar(' ');
        w
            .write("Since this process (up to this stage) does not provide all wanted outputs, it is no solution (yet).");//$NON-NLS-1$
      }
    }
    w.endElement();

    w.startElement("structure");//$NON-NLS-1$
    w.attribute("combinations", //$NON-NLS-1$
        (this.m_combos <= Long.MAX_VALUE) ? String
            .valueOf(((long) (this.m_combos + 0.99d))) : String
            .valueOf(this.m_combos));
    w.attribute("minServices", String.valueOf(this.m_minServices)); //$NON-NLS-1$
    w.attribute("minLength", String.valueOf(this.m_length)); //$NON-NLS-1$

    w.write("This process involves at least ");//$NON-NLS-1$
    w.writeInt(this.m_minServices);
    w
        .write(" services. The length of the shortest possible execution sequence is ");//$NON-NLS-1$
    w.writeInt(this.m_length);
    if (this.m_length < this.m_minServices) {
      w.write(" (it is shorter due to parallelism). ");//$NON-NLS-1$
    } else {
      w.write(". ");//$NON-NLS-1$
    }
    w.write("In total, this process allows for ");//$NON-NLS-1$
    if (this.m_combos <= Long.MAX_VALUE) {
      w.writeLong(((long) (this.m_combos + 0.9d)));
    } else {
      w.writeDouble(this.m_combos);
    }
    w.write(" possible combinations.");//$NON-NLS-1$

    w.endElement();

    i = this.m_serv.size();
    if (fin && (i > 0)) {
      j = 0;
      for (--i; i >= 0; i--) {
        if (!(this.m_serv.get(i).m_used))
          j++;
      }
      w.startElement("redundancy");//$NON-NLS-1$
      w.attribute("redundantServices", String.valueOf(j));//$NON-NLS-1$
      if (j <= 0) {
        w.write("No redundant services have been discovered.");//$NON-NLS-1$
      } else {
        w.write("There are ");//$NON-NLS-1$
        w.writeInt(j);
        w.write(" redundant services: ");//$NON-NLS-1$
        for (i = (this.m_serv.size() - 1); i >= 0; i--) {
          if (!(this.m_serv.get(i).m_used)) {
            w.write(this.m_serv.get(i).m_name);
            if ((--j) > 0)
              w.write(", ");//$NON-NLS-1$
          }
        }

        w.writeChar('.');
      }
      w.endElement();
    }

    if((this.m_throughput>0)&&(this.m_throughput<Long.MAX_VALUE)){
    w.startElement("best-throughput");//$NON-NLS-1$
    w.writeLong(this.m_throughput);
    w.endElement();}
    
    if((this.m_runtime>0)&&(this.m_runtime<Long.MAX_VALUE)){
    w.startElement("best-responsetime");//$NON-NLS-1$
    w.writeLong(this.m_runtime);
    w.endElement();}
    
    // w.endElement();
    w.endElement();

  }

  /**
   * Process one stage
   *
   * @param tw
   *          the text writer
   * @param sup
   *          the state
   * @param e
   *          the structure element
   * @param sss
   * @return the stage for the next level
   */
  public static final Stage process(final SAXWriter tw, final Stage sup,
      final SolutionElement e) {

    tw.flush();
    if (e instanceof Solution)
      return processSolution(tw, sup, ((Solution) e));
    if (e instanceof ServiceInvocation)
      return processInvocation(tw, sup, ((ServiceInvocation) e));
    if (e instanceof Sequence) {
      return processSequence(tw, sup, ((Sequence) e));
    }
    if (e instanceof Parallel) {
      return processParallel(tw, sup, ((Parallel) e));
    }
    if (e instanceof Alternatives) {
      return processAlternatives(tw, sup, ((Alternatives) e));
    }
    return null;
  }

  /**
   * Process one stage
   *
   * @param tw
   *          the text writer
   * @param sup
   *          the state
   * @param e
   *          the structure element
   */
  public static final void process(final SAXWriter tw, final Stage sup,
      final List<?> e) {
    int i, j, c, cor, ex, sat, minLength, minServices, redundant;
    double totalCombos;
    Object o;
    Stage s;
    long bR, bT;

    tw.startElement("solutions"); //$NON-NLS-1$
    c = e.size();
    tw.attribute("number", String.valueOf(c));//$NON-NLS-1$

    totalCombos = -1l;
    minLength = -1;
    minServices = -1;
    redundant = 0;
    bR = Long.MAX_VALUE;
    bT = 0;

    ex = cor = sat = 0;
    for (i = 0; i < c; i++) {
      o = e.get(i);
      if (o instanceof SolutionElement) {
        ((SolutionElement) (o)).setAlternatives(null);
        s = process(tw, new Stage(sup), ((SolutionElement) (o)));
        for (j = (s.m_serv.size() - 1); j >= 0; j--) {
          if (!(s.m_serv.get(j).m_used))
            redundant++;
        }
        if (s.canExecute())
          ex++;
        if (s.isSatisfying())
          sat++;
        if (s.isCorrectSoltion()) {
          cor++;
          if (totalCombos <= 0)
            totalCombos = s.m_combos;
          else
            totalCombos += s.m_combos;

          if (minLength <= 0)
            minLength = s.m_length;
          else
            minLength = Math.min(minLength, s.m_length);

          if (minServices <= 0)
            minServices = s.m_minServices;
          else
            minServices = Math.min(minServices, s.m_minServices);
          
          bT = Math.max(bT, s.m_throughput);
          bR = Math.min(bR, s.m_runtime);
        }
        Service.uncheck(s.m_serv);
      }
    }

    tw.startElement("result");//$NON-NLS-1$

    tw.startElement("validity");//$NON-NLS-1$
    tw.attribute("numYes", String.valueOf(ex));//$NON-NLS-1$
    tw.attribute("numNo", String.valueOf(c - ex));//$NON-NLS-1$
    if (c == ex) {
      tw.write("All proposed BPEL processes can be executed.");//$NON-NLS-1$
    } else {
      if (ex == 0) {
        tw.write("None of the proposed BPEL processes can be executed.");//$NON-NLS-1$
      } else {
        tw.writeInt(ex);
        tw.write(" of the proposed BPEL processes can be executed but ");//$NON-NLS-1$
        tw.writeInt(c - ex);
        tw.write(" CANNOT.");//$NON-NLS-1$
      }
    }
    tw.endElement();

    tw.startElement("satisfaction");//$NON-NLS-1$
    tw.attribute("numYes", String.valueOf(sat));//$NON-NLS-1$
    tw.attribute("numNo", String.valueOf(c - sat));//$NON-NLS-1$
    if (c == sat) {
      tw
          .write("All proposed BPEL processes provide all wanted output parameters.");//$NON-NLS-1$
    } else {
      if (sat == 0) {
        tw
            .write("None of the proposed BPEL processes provides all wanted output parameters.");//$NON-NLS-1$
      } else {
        tw.writeInt(sat);
        tw
            .write(" of the proposed BPEL processes provide all wanted output parameters but ");//$NON-NLS-1$
        tw.writeInt(c - sat);
        tw.write(" do not.");//$NON-NLS-1$
      }
    }
    tw.endElement();

    tw.startElement("solutions");//$NON-NLS-1$
    tw.attribute("numYes", String.valueOf(cor));//$NON-NLS-1$
    tw.attribute("numNo", String.valueOf(c - cor));//$NON-NLS-1$
    if (c == cor) {
      tw
          .write("All proposed BPEL processes are correct solutions for the challenge.");//$NON-NLS-1$
    } else {
      if (cor == 0) {
        tw
            .write("None of the proposed BPEL processes is a correct solution for the challenge.");//$NON-NLS-1$
      } else {
        tw.writeInt(cor);
        tw
            .write(" of the proposed BPEL processes correctly solve the challenge but ");//$NON-NLS-1$
        tw.writeInt(c - cor);
        tw.write(" do not.");//$NON-NLS-1$
      }
    }
    tw.endElement();

    tw.startElement("combinations");//$NON-NLS-1$
    tw.attribute("count", //$NON-NLS-1$
        (totalCombos <= Long.MAX_VALUE) ? String.valueOf(Math.max(0l,
            (long) (0.99d + totalCombos))) : String.valueOf(Math.max(0,
            totalCombos)));
    if (totalCombos > 0) {
      if (totalCombos <= Long.MAX_VALUE) {
        tw.writeLong((long) (totalCombos + 0.99d));
      } else {
        tw.writeDouble(totalCombos);
      }
      tw
          .write(" execution sequences that solve the challenge were found in total.");//$NON-NLS-1$
    } else {
      tw
          .write("No execution sequence that solves the challenge was found.");//$NON-NLS-1$
    }
    tw.endElement();

    tw.startElement("minLength");//$NON-NLS-1$
    tw.attribute("value", //$NON-NLS-1$
        (minLength > 0) ? String.valueOf(minLength) : "infinite");//$NON-NLS-1$
    if (minLength > 0) {
      tw
          .write("The shortest execution sequence that solves the challenge found is ");//$NON-NLS-1$
      tw.writeInt(minLength);
      tw.writeChar('.');
    } else {
      tw
          .write("No execution sequence that solves the challenge was found.");//$NON-NLS-1$
    }
    tw.endElement();

    tw.startElement("minService");//$NON-NLS-1$
    tw.attribute("value", //$NON-NLS-1$
        minServices > 0 ? String.valueOf(minServices) : "infinite");//$NON-NLS-1$
    if (minServices > 0) {
      tw
          .write("The execution sequence involving the least number of services for solving the challenge found consists of ");//$NON-NLS-1$
      tw.writeInt(minServices);
      tw.write(" services.");//$NON-NLS-1$
    } else {
      tw
          .write("No execution sequence that solves the challenge was found.");//$NON-NLS-1$
    }
    tw.endElement();
    
    tw.startElement("best-responsetime");//$NON-NLS-1$
    if(bR<Long.MAX_VALUE){
      tw.write("The best response time of any correct composition found is ");//$NON-NLS-1$
      tw.writeLong(bR);
      tw.write(".");//$NON-NLS-1$
    } else{
      tw.write("No correct execution sequence found, therefore runtime is not considered.");//$NON-NLS-1$
    }
    tw.endElement();
    
    tw.startElement("best-throughput");//$NON-NLS-1$
    if(bT>0){
      tw.write("The best throughput of any correct composition found is ");//$NON-NLS-1$
      tw.writeLong(bT);
      tw.write(".");//$NON-NLS-1$
    } else{
      tw.write("No correct execution sequence found, therefore throughput is not considered.");//$NON-NLS-1$
    }
    tw.endElement();

    tw.startElement("redundanceStat");//$NON-NLS-1$
    tw.attribute("redundantServices", //$NON-NLS-1$
        String.valueOf(redundant));

    if (redundant <= 0) {
      tw.write("No redundant services have been discovered.");//$NON-NLS-1$
    } else {
      tw.write("There are ");//$NON-NLS-1$
      tw.writeInt(redundant);
      tw.write(" redundant services.");//$NON-NLS-1$
    }

    tw.endElement();

    tw.endElement();

    tw.endElement();
  }

  /**
   * read a challenge
   *
   * @param challenge
   * @param oe
   * @return the stage
   */
  public static final Stage readChallenge(final Object challenge,
      final OntologyElement oe) {
    List<Service> l;
    Service s;
    Stage x;

    l = WSDLReader.readWSDL(oe, challenge);
    s = l.get(0);
    x = new Stage(null);
    x.m_known.addAll(s.m_in);
    x.m_wanted.addAll(s.m_out);

    return x;

  }

  /**
   * Process one stage
   *
   * @param tw
   *          the text writer
   * @param sup
   *          the state
   * @param e
   *          the structure element
   * @param sss
   * @return the new stage
   */
  private static final Stage processSolution(final SAXWriter tw,
      final Stage sup, final Solution e) {
    Stage s;
    int i;
    List<SolutionElement> se;

    tw.startElement("solution"); //$NON-NLS-1$
    tw.attribute("name", e.m_name); //$NON-NLS-1$

    sup.toWriter(tw, true, false);
    s = new Stage(sup);
    se = e.getSolutionElements();
    for (i = 0; i < se.size(); i++) {

      s = process(tw, s, se.get(i));

    }
    
    

    s.toWriter(tw, false, true);
    // tw.startElement("validity");//$NON-NLS-1$
    // if (s.m_valid)
    // tw.write("This process can be executed.");//$NON-NLS-1$
    // else
    // tw.write("This process CANNOT be executed.");//$NON-NLS-1$
    // tw.endElement();
    // tw.startElement("satisfaction");//$NON-NLS-1$
    // if (s.m_wanted.size() <= 0)
    // tw.write("All wanted concepts have been provided.");//$NON-NLS-1$
    // else
    // tw.write("There are still wanted concepts missing.");//$NON-NLS-1$
    // tw.endElement();
    // tw.startElement("correctness");//$NON-NLS-1$
    // if (s.m_valid && (s.m_wanted.size() <= 0))
    // tw.write("This solution is correct.");//$NON-NLS-1$
    // else
    // tw.write("This solution is INCORRECT.");//$NON-NLS-1$
    // tw.endElement();
    tw.endElement();

    return s;
  }

  /**
   * Process one stage
   *
   * @param tw
   *          the text writer
   * @param sup
   *          the state
   * @param e
   *          the structure element
   * @param sss
   * @return
   */
  private static final Stage processInvocation(final SAXWriter tw,
      final Stage sup, final ServiceInvocation e) {
    List<OntologyElement> l1, l2;
    final Service s;
    Service kkk;
    final Stage next;
    int i, j;
    OntologyElement e1, e2;
    boolean f, yyy;
    StringBuilder sb;

    next = new Stage(sup);
    s = e.getService();
    if (s == null)
      return sup;

    tw.startElement("invocation"); //$NON-NLS-1$
    sup.toWriter(tw, true, false);
    s.toSAX(tw);

    Service.addIfNew(next.m_serv, s);
    
//    next.m_runtime += s.m_responsetime;
//    next.m_throughput = Math.min(next.m_throughput, s.m_throughput);
    
    next.m_runtime = s.m_responsetime;
    next.m_throughput = s.m_throughput;
    
    l1 = s.m_in;
    l2 = next.m_known;
    sb = new StringBuilder();
    yyy = true;
    for (i = l1.size() - 1; i >= 0; i--) {
      e1 = l1.get(i);

      f = false;
      for (j = (l2.size() - 1); j >= 0; j--) {
        e2 = l2.get(j);
        if (e1.subsumes(e2)) {
          if (yyy) {
            tw.startElement("parameters");//$NON-NLS-1$
            yyy = false;
          }
          tw.startElement("resolution");//$NON-NLS-1$
          tw.attribute("of", e1.m_name);//$NON-NLS-1$
          tw.attribute("by", e2.m_name);//$NON-NLS-1$
          if (f)
            tw.attribute("multiple",//$NON-NLS-1$
                "true");//$NON-NLS-1$

          sb.setLength(0);
          kkk = Service.checkUsed(e2, sup);
          if ((kkk != null) && (kkk.checkUsed(e2))) {
            sb.append(kkk.m_name);
            sb.append(" provides ");//$NON-NLS-1$
          }

          e1.getConnection(e2, sb);

          tw.writeObject(sb);
          tw.endElement();
          f = true;
        }
      }
      if (!f) {
        if (yyy) {
          tw.startElement("parameters");//$NON-NLS-1$
          yyy = false;
        }
        tw.startElement("unsatisfied");//$NON-NLS-1$
        tw.attribute("what", e1.m_name);//$NON-NLS-1$
        next.m_valid = false;
        tw.endElement();
      }
    }
    if (!yyy)
      tw.endElement();

    l1 = s.m_out;
    l2 = next.m_wanted;
    sb = new StringBuilder();
    yyy = true;
    for (i = l2.size() - 1; i >= 0; i--) {
      e2 = l2.get(i);

      f = false;
      for (j = (l1.size() - 1); j >= 0; j--) {
        e1 = l1.get(j);
        if (e2.subsumes(e1)) {
          if (yyy) {
            tw.startElement("wanted");//$NON-NLS-1$
            yyy = false;
          }
          tw.startElement("resolution");//$NON-NLS-1$
          tw.attribute("of", e2.m_name);//$NON-NLS-1$
          tw.attribute("by", e1.m_name);//$NON-NLS-1$
          sb.setLength(0);
          kkk = Service.checkUsed(e2, next);
          if ((kkk != null) && (kkk.checkUsed(e1))) {
            sb.append(kkk.m_name);
            sb.append(" provides ");//$NON-NLS-1$
          }
          e2.getConnection(e1, sb);
          tw.writeObject(sb);
          tw.endElement();

          if (!f)
            l2.remove(i);
          f = true;
        }
      }
    }
    if (!yyy)
      tw.endElement();

    next.m_minServices++;
    next.m_length++;
    next.m_known.addAll(l1);

    next.toWriter(tw, false, false);
    tw.endElement();

    return next;
  }

  /**
   * Process one stage
   *
   * @param tw
   *          the text writer
   * @param sup
   *          the state
   * @param e
   *          the structure element
   * @param sss
   * @return
   */
  private static final Stage processSequence(final SAXWriter tw,
      final Stage sup, final Sequence e) {
    Stage s;
    List<SolutionElement> se;
    int i, k;
    double v;
    long wT, sR;

    se = e.getSolutionElements();
    k = se.size();
    if (k <= 0)
      return sup;

    tw.startElement("sequence"); //$NON-NLS-1$
    sup.toWriter(tw, true, false);
    s = new Stage(sup);

    sR = 0;
    wT = Long.MAX_VALUE;
    v = 1d;
    for (i = 0; i < k; i++) {
      s = process(tw, new Stage(s), se.get(i));
      if (s.m_combos > 0)
        v *= s.m_combos;
      wT = Math.min(wT, s.m_throughput);
      sR += s.m_runtime;
    }

    s.m_combos = v;
    s.m_throughput = wT;
    s.m_runtime = sR;
    s.toWriter(tw, false, false);
    tw.endElement();
    return s;
  }

  /**
   * Process one stage
   *
   * @param tw
   *          the text writer
   * @param sup
   *          the state
   * @param e
   *          the structure element
   * @param sss
   * @return
   */
  private static final Stage processParallel(final SAXWriter tw,
      final Stage sup, final Parallel e) {
    Stage s;
    List<SolutionElement> se;
    int i, j, k, v;
    Stage ss;
    boolean b;
    long wR, wT;

    se = e.getSolutionElements();
    k = se.size();
    if (k <= 0)
      return sup;

    tw.startElement("parallel"); //$NON-NLS-1$
    sup.toWriter(tw, true, false);
    s = new Stage(sup);

    s.m_wanted.clear();
    v = s.m_minServices;
    b = true;

    wR = 0;
    wT = Long.MAX_VALUE;
    for (i = 0; i < k; i++) {
      ss = process(tw, new Stage(sup), se.get(i));
      s.m_valid &= ss.m_valid;
      Service.addIfNew(s.m_serv, ss.m_serv);
      s.m_length = Math.max(s.m_length, ss.m_length);
      s.m_minServices += Math.max(0, (ss.m_minServices - v));
      wR = Math.max(wR, ss.m_runtime);
      wT = Math.min(wT, ss.m_throughput);

      if (ss.m_combos > 0)
        s.m_combos *= ss.m_combos;

      Service.addIfNew(s.m_known, ss.m_known);

      if (b) {
        s.m_wanted.addAll(ss.m_wanted);
        b = false;
      } else {
        for (j = s.m_wanted.size() - 1; j >= 0; j--) {
          if (!(ss.m_wanted.contains(s.m_wanted.get(j))))
            s.m_wanted.remove(j);
        }
      }
    }

    s.m_runtime = wR;
    s.m_throughput = wT;
    s.toWriter(tw, false, false);
    tw.endElement();
    return s;
  }

  /**
   * melt down
   *
   * @param l
   * @return
   */
  private static final List<OntologyElement> melt(
      final List<List<OntologyElement>> l) {
    int i, j, k;
    List<OntologyElement> x, q, res;
    OntologyElement p;

    res = CollectionUtils.createList();
    if (l.size() <= 0)
      return res;
    res.addAll(l.get(0));
    q = CollectionUtils.createList();

    for (i = (l.size() - 1); i > 0; i--) {
      x = l.get(i);
      q.clear();

      for (j = (res.size() - 1); j >= 0; j--) {
        for (k = (x.size() - 1); k >= 0; k--) {
          p = OntologyElement.getCommonParent(res.get(j), x.get(k));
          if (p != null) {
            OntologyElement.insertSpec(q, p);
          }
        }
      }

      x = res;
      res = q;
      q = x;
    }

    return res;
  }

  /**
   * Process one stage
   *
   * @param tw
   *          the text writer
   * @param sup
   *          the state
   * @param e
   *          the structure element
   * @param sss
   * @return
   */
  private static final Stage processAlternatives(final SAXWriter tw,
      final Stage sup, final Alternatives e) {
    Stage s;
    List<SolutionElement> se;
    int i, kk;
    double ssx;
    Stage ss;
    // List<OntologyElement> xx;
    List<List<OntologyElement>> k;
  long bR, bT;
    se = e.getSolutionElements();
    kk = se.size();

    if (kk <= 0)
      return sup;

    tw.startElement("alternatives"); //$NON-NLS-1$
    sup.toWriter(tw, true, false);

    s = new Stage(sup);
    // s.m_known.clear();
    s.m_wanted.clear();
    k = CollectionUtils.createList();

    ssx = 0d;
    bR = Long.MAX_VALUE;
    bT = 0;
    for (i = 0; i < kk; i++) {
      ss = process(tw, new Stage(sup), se.get(i));
      s.m_valid &= ss.m_valid;
      bR = Math.min(bR, ss.m_runtime);
      bT = Math.max(bT, ss.m_throughput);

      if (((i <= 0) || (s.m_minServices <= 0)) && (ss.m_minServices > 0)) {
        s.m_minServices = ss.m_minServices;
      } else {
        s.m_minServices = Math.min(ss.m_minServices, s.m_minServices);
      }

      if (((i <= 0) || (s.m_length <= 0)) && (ss.m_length > 0)) {
        s.m_length = ss.m_length;
      } else {
        s.m_length = Math.min(s.m_length, ss.m_length);
      }

      ssx += ss.m_combos;
      Service.addIfNew(s.m_serv, ss.m_serv);
      Service.addIfNew(s.m_wanted, ss.m_wanted);

      ss.m_known.removeAll(sup.m_known);
      k.add(ss.m_known);
    }

    s.m_runtime = bR;
    s.m_throughput = bT;
    Service.addIfNew(s.m_known, melt(k));
    s.m_combos = ssx;
    
    s.toWriter(tw, false, false);
    tw.endElement();
    return s;
  }
}
