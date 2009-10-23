This readme explains how your Composition-System must look like to participate on the this years WSC-competition.

Web-Service:
First of all each Composition-System must implement a Web Service that can be used by the WSC-Client 
to communicate with the Composition-System. The WSDL-file that describes the interface of this service 
is located in the de.vs.unikassel.query.server-package. The package also contains
a Java-interface (CompositionSystemInterface.java) and a test-implementation (CompositionSystemImpl.java) that can be used
by Java-implementations. If you use another language then Java you have to use the WSDL-file to create a 
service-implementation on you own. 

The Web Service offers three methods:
- initialize(String wsdlServiceDescription, String owlTaxonomy)
- startQuery(String wsdlQuery, String callbackURL)
- stopComposition()

initialize:
The "initialize"-method is called by the WSC-Client to initialize the Composition-System.
It takes the URL of the WSDL-Service-Descriptions-document and the URL of the OWL-Taxonomy-document as a String-parameter.
An implementation of this method should contain the code to parse the WSDL- and the OWL-document.

startQuery:
The "startQuery"-method is called by the WSC-Client to start a query.
It takes the WSDL-Query-document and the URL of the callback-service as a string parameter.
As described in the WSDL-document of the Service this operation is a one-way operation, so the WSC-Client
doesn't expect a return-value. Instead of sending the Result-BPEL-document as a return-value the
Composition-System must call the callback-service of the WSC-Client to transmit the result-document.
The WSDL-file of this callback interface is located in the de.vs.unikassel.query.client.callback_interface-package.
The callback-service offers only one method (result) that takes the Result-BPEL-document as an input-parameter.
This method must be called by the Composition-System when the composition is completed.
A Java-stub created with Jax can be found in the de.vs.unikassel.query.client.callback_interface.stub-package.
If you use e.g. C# or C++ you have to create your own stub from the callback-wsdl-document.

stopComposition:
This method is used by the WSC-client to stop the composition.


GUI:
If you have implemented the service-interface described above, you can use the WSC-Client GUI in the
de.vs.unikassel.query.client.gui-package to control your Composition-System.
The GUI consist of four panels. The first panel ("URL") contains a textfield where you have to enter
the URL of your Composition-System-Service.
The second panel ("Initialize") can be used to initialize the Composition-System. The first textfield on
this panel takes the URL of the WSDL-Description-file and the second textfield takes the URL of the
OWL-Taxonomy-file. If you have entered the path-informations and you press the "Send Query"-button the
WSC-Client will call the "initialize"-method of the Composition-System.
The third panel ("Query") can be used to send queries to the Composition-System. The first 
(WSDL Query) takes the path of the WSDL-Query-document. 
Before you can send the query-document to the Composition-System you must start the 
callback-service of the WSC-Client ("Start"-button) which is used by the Composition-System to transmit
the result-document. When you have started the callback-service you can press the "Send Query"-button to send
the wsdl-query-document to the Composition-System. The WSC-client will call the "startQuery"-operation and
waits until the Composition-System call the "result"-operation of the callback-service.
When the result-document arrives the WSC-Client compares the result-document with the master-solution
and calculates a challenge-score (not yet implemented). The challenge-score and the time-measurement is then displayed on
a result-dialog. The result-dialog also offers a button to display the result-document.
The last panel on the WSC-Client GUI displays the challenge-score and the time-measurement of the last query.
The textbook below the challenge-score displays some debug-informations.