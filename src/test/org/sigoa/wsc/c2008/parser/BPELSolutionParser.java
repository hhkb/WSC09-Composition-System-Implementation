package test.org.sigoa.wsc.c2008.parser;

import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.sfc.collections.CollectionUtils;
import org.sfc.io.IO;

/**
 * A marker-interface.
 * 
 * @author Marc Kirchhoff
 */
interface SolutionElement {/**/
  /**
   * get the services
   * 
   * @param a
   * @return
   */
  public List<Service> setAlternatives(final List<Alternatives> a);
}

/**
 * Base-class of all structure-elements (<sequence>, <flow>, <switch>).
 * 
 * @author Marc Kirchhoff
 */
abstract class StructureElement implements SolutionElement {

  /**
   * The structure-elements (<sequence>, <flow>, <switch>, <invoke>) that
   * this structure-elements contains.
   */
  protected List<SolutionElement> msolutionElements;

  /**
   * get the services
   * 
   * @param a
   * @return
   */
  public List<Service> setAlternatives(final List<Alternatives> a) {
    int i;
    List<Service> l;
    l = CollectionUtils.createList();
    for (i = this.msolutionElements.size() - 1; i >= 0; i--) {
      Service
          .addIfNew(l, this.msolutionElements.get(i).setAlternatives(a));
    }
    return l;
  }

  /**
   * Creates a new structure-element.
   */
  public StructureElement() {
    this.msolutionElements = CollectionUtils.createList();
  }

  /**
   * Returns the structure-elements.
   * 
   * @return The structure-elements.
   */
  public List<SolutionElement> getSolutionElements() {
    return this.msolutionElements;
  }

  /**
   * Set the structure-elements.
   * 
   * @param solutionElements
   *          The structure-elements.
   */
  public void setSolutionElements(List<SolutionElement> solutionElements) {
    this.msolutionElements = solutionElements;
  }
}

/**
 * Represents a service-invocation.
 * 
 * @author Marc Kirchhoff
 */
class ServiceInvocation implements SolutionElement {

  /**
   * The name of the service.
   */
  private Service mservice;

  /**
   * Constructs a new <code>ServiceInvocation</code> with the specified
   * service.
   * 
   * @param service
   *          The name of the
   */
  public ServiceInvocation(Service service) {
    this.mservice = service;
  }

  /**
   * get the services
   * 
   * @param a
   * @return
   */
  public List<Service> setAlternatives(final List<Alternatives> a) {
    List<Service> l;
    this.mservice.m_a = a;
    l = CollectionUtils.createList();
    l.add(this.mservice);
    return l;
  }

  /**
   * Returns the name of the service.
   * 
   * @return The name of the service.
   */
  public Service getService() {
    return this.mservice;
  }

  /**
   * Sets the name of the service.
   * 
   * @param service
   *          The name of the service.
   */
  public void setServiceName(Service service) {
    this.mservice = service;
  }
}

/**
 * Represent an <switch>-element.
 * 
 * @author Marc Kirchhoff
 */
class Alternatives extends StructureElement {
/**
 * the services
 */
  List<Service> m_servs;
  
  /**
   * Constructs a new <code>Alternatives</code>-element.
   */
  public Alternatives() {
    super();
  }

  /**
   * get the services
   * 
   * @param a
   * @return
   */
  @Override
  public List<Service> setAlternatives(final List<Alternatives> a) {
    List<Alternatives> xx;

    xx = CollectionUtils.createList();
    if (a != null) {
      xx.addAll(a);
    }
    xx.add(this);
    // if(a!=null) throw new RuntimeException();
    return (this.m_servs = super.setAlternatives(xx));
  }

}

/**
 * Represents an <sequence>-element.
 * 
 * @author Marc Kirchhoff
 */
class Sequence extends StructureElement {

  /**
   * Creates a new sequence-element.
   */
  public Sequence() {
    super();
  }
}

/**
 * Represents an <flow>-element.
 * 
 * @author Marc Kirchhoff
 */
class Parallel extends StructureElement {

  /**
   * Creates a new parallel-element.
   */
  public Parallel() {
    super();
  }
}

/**
 * Represents a solution.
 * 
 * @author Marc Kirchhoff
 */
class Solution extends StructureElement {

  /**
   * 
   */
  final String m_name;

  /**
   * Constructs an empty solution.
   * 
   * @param n
   */
  public Solution(String n) {
    super();
    this.m_name = n;
  }
}

/**
 * This class can be used to parse BPEL-solution-files.
 * 
 * @author Marc Kirchhoff
 */
public class BPELSolutionParser implements Runnable {

  /**
   * The logger.
   */
  private static final Logger logger = Logger
      .getLogger(BPELSolutionParser.class.getName());

  /**
   * The input-stream that contains the bpel-solution-document.
   */
  private InputStream mbpelSolutionDocumentIS;

  /**
   * The solutions that were parsed.
   */
  private List<Solution> msolutions;

  /**
   * the service list
   */
  private final List<Service> m_lst;

  /**
   * Constructs a new <code>BPELSolutionParser</code> with the specified
   * input-stream.
   * 
   * @param bpelSolutionDocument
   *          The inptu-stream that contains the bpel-solution-document.
   * @param lst
   *          the service list
   */
  public BPELSolutionParser(Object bpelSolutionDocument,
      final List<Service> lst) {
    this(IO.getInputStream(bpelSolutionDocument), lst);
  }

  /**
   * resolve a service
   * 
   * @param name
   *          the service name
   * @return the service
   */
  private final Service resolveService(final String name) {
    int i;
    Service s;
    String n;

    if (name.startsWith("service:")) //$NON-NLS-1$
      n = name.substring("service:".length());//$NON-NLS-1$
    else
      n = name;

    for (i = (this.m_lst.size() - 1); i >= 0; i--) {
      if ((s = this.m_lst.get(i)).m_name.equals(n))
        return s;
    }
    return null;
  }

  /**
   * Constructs a new <code>BPELSolutionParser</code> with the specified
   * input-stream.
   * 
   * @param bpelSolutionDocumentIS
   *          The inptu-stream that contains the bpel-solution-document.
   * @param lst
   *          the service list
   */
  public BPELSolutionParser(InputStream bpelSolutionDocumentIS,
      final List<Service> lst) {
    this.mbpelSolutionDocumentIS = bpelSolutionDocumentIS;
    this.msolutions = CollectionUtils.createList();
    this.m_lst = lst;
  }
  
  private boolean firstswitch = true;
  
  /**
   * Parses the bpel-solution-file.
   * 
   * @throws XMLStreamException
   */
  public void parseSolutionFile() throws XMLStreamException {

    // Error checking.
    if (this.mbpelSolutionDocumentIS == null) {
      String errorMessage = "The BPEL-Solution-document input-stream is null."; //$NON-NLS-1$
      BPELSolutionParser.logger.severe(errorMessage);
      throw new IllegalArgumentException(errorMessage);
    }

    // Create an xml-parser.
    XMLInputFactory factory = XMLInputFactory.newInstance();
    XMLStreamReader parser = factory
        .createXMLStreamReader(this.mbpelSolutionDocumentIS);

    List<StructureElement> structureElements = null;
    StructureElement currentStructureElement = null;
    int cases = 0;
    
    // Parse the solution-file.
    while (parser.hasNext()) {
      int event = parser.next();

      switch (event) {

      case XMLStreamConstants.START_ELEMENT: {
        String elementName = parser.getLocalName();

        if (elementName.equals("case")) {//$NON-NLS-1$
          cases++;
          String caseName = parser.getAttributeValue(null, "name");//$NON-NLS-1$
          if (cases == 1) {
          //if (caseName.startsWith("Alternative-Solution")) {//$NON-NLS-1$
            Solution solution = new Solution(caseName);
            currentStructureElement = solution;
            structureElements = CollectionUtils.createList();
            structureElements.add(solution);
          }
        } else if (elementName.equals("sequence")) {//$NON-NLS-1$
          String sequenceName = parser.getAttributeValue(null, "name");//$NON-NLS-1$

          if (sequenceName == null || !sequenceName.equals("main")) {//$NON-NLS-1$
            Sequence sequence = new Sequence();
            if (currentStructureElement == null)
              return;
            if (structureElements == null)
              return;
            currentStructureElement.getSolutionElements().add(sequence);
            currentStructureElement = sequence;
            structureElements.add(sequence);
          }
        } else if (elementName.equals("flow")) {//$NON-NLS-1$
          Parallel parallel = new Parallel();
          if (currentStructureElement == null)
            return;
          if (structureElements == null)
            return;
          currentStructureElement.getSolutionElements().add(parallel);
          currentStructureElement = parallel;
          structureElements.add(parallel);
        } else if (elementName.equals("switch")) {//$NON-NLS-1$
          if (this.firstswitch) {
          	  this.firstswitch = false;        	  
          } else {
          		  //String switchName = parser.getAttributeValue(null, "name");//$NON-NLS-1$
          	  
          //if (switchName.startsWith("Alternative-Services")) {//$NON-NLS-1$
            Alternatives alternatives = new Alternatives();
            if (currentStructureElement == null)
              return;
            if (structureElements == null)
              return;
            currentStructureElement.getSolutionElements()
                .add(alternatives);
            currentStructureElement = alternatives;
            structureElements.add(alternatives);
          }
        } else if (elementName.equals("invoke")) {//$NON-NLS-1$
          String serviceName = parser.getAttributeValue(null, "name");//$NON-NLS-1$
          ServiceInvocation serviceInvocation = new ServiceInvocation(this
              .resolveService(serviceName));
          if (currentStructureElement == null)
            return;
          if (structureElements == null)
            return;
          currentStructureElement.getSolutionElements().add(
              serviceInvocation);
        } else if (!elementName.equals("process") && //$NON-NLS-1$ 
            !elementName.equals("receive")) {//$NON-NLS-1$
          String errorMessage = "The solution-file contains an invalid element: " + elementName;//$NON-NLS-1$
          BPELSolutionParser.logger.severe(errorMessage);
          throw new XMLStreamException(errorMessage);
        }

        break;
      }
      case XMLStreamConstants.END_ELEMENT: {
        String elementName = parser.getLocalName();

        if (elementName.equals("sequence") || //$NON-NLS-1$ 
            elementName.equals("flow")) {//$NON-NLS-1$
          if (structureElements == null)
            return;
          if (structureElements.size() > 0) {
            structureElements.remove(structureElements.size() - 1);
            currentStructureElement = structureElements
                .get(structureElements.size() - 1);
          }
        } else if (elementName.equals("case")) { //$NON-NLS-1$	
        	cases--;
        	
          if (structureElements == null)
            return;
          if (structureElements.size() == 1
              && structureElements.get(structureElements.size() - 1) instanceof Solution) {
            this.msolutions.add((Solution) structureElements
                .get(structureElements.size() - 1));
            structureElements.clear();
          }
        } else if (elementName.equals("switch")) {//$NON-NLS-1$
          if (structureElements == null)
            return;
          if (structureElements.size() > 0) {
            StructureElement structureElement = structureElements
                .get(structureElements.size() - 1);
            if (structureElement instanceof Alternatives) {
              structureElements.remove(structureElements.size() - 1);
              currentStructureElement = structureElements
                  .get(structureElements.size() - 1);
            }
          }
        }

        break;
      }
      default: {
        break;
      }
      }
    }
  }

  /**
   * Starts the parsing.
   */
//  @Override
  public void run() {

    try {
      parseSolutionFile();
    } catch (XMLStreamException streamException) {
      BPELSolutionParser.logger
          .severe("An error occurred during the parsing of the solution-file");//$NON-NLS-1$
      streamException.printStackTrace();
    }
  }

  /**
   * Returns the solutions.
   * 
   * @return The solutions.
   */
  public List<Solution> getSolutions() {
    return this.msolutions;
  }

  /**
   * @param bpelSolutionDocument
   *          The inptu-stream that contains the bpel-solution-document.
   * @param lst
   *          the service list
   * @return the services
   */
  public static final List<Solution> readBPEL(
      final Object bpelSolutionDocument, final List<Service> lst) {
    BPELSolutionParser p;

    p = new BPELSolutionParser(bpelSolutionDocument, lst);
    p.run();
    return p.getSolutions();
  }
}
