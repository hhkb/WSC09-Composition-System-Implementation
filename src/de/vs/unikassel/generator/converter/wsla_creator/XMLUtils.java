/*
 * Created on 22.02.2006
 *
 * 
 */
package de.vs.unikassel.generator.converter.wsla_creator;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * @author bleul
 *
 * 
 */
public class XMLUtils {
	
    public static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public static String DocumentToString(final Document doc) {
        try {
            StringWriter writer = new StringWriter();
            Result l_s = new StreamResult(writer);

            doc.normalize();

            TransformerFactory.newInstance().newTransformer()
                              .transform(new DOMSource(doc), l_s);

            return writer.toString();
        } catch (Exception e) {
            System.err.println(e);

            return null;
        }
    }

    public static void DocumentToFile(final Document doc, File file) {
        // FileWriter writer = null;
        OutputStreamWriter outputStreamWriter = null;
        
        try {
           // writer = new FileWriter(file);
            
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
            
        } catch (Exception e) {
            System.err.println(e);

            return;
        }

        //Result l_s = new StreamResult(writer);
        Result l_s = new StreamResult(outputStreamWriter);
        
        doc.normalize();

        try {
            TransformerFactory.newInstance().newTransformer()
                              .transform(new DOMSource(doc), l_s);
            
            outputStreamWriter.close();
            //writer.close();
        } catch (Exception e) {
            System.err.println(e);

            return;
        }
    }

    public static void DocumentToStream(final Document doc, OutputStream stream) {
        Result l_s = new StreamResult(stream);

        doc.normalize();

        try {
            TransformerFactory.newInstance().newTransformer()
                              .transform(new DOMSource(doc), l_s);
            stream.close();
        } catch (Exception e) {
            System.err.println(e);

            return;
        }
    }

    public static void DocumentToWriter(final Document doc, Writer writer) {
        Result l_s = new StreamResult(writer);

        doc.normalize();

        try {
            TransformerFactory.newInstance().newTransformer()
                              .transform(new DOMSource(doc), l_s);
            writer.close();
        } catch (Exception e) {
            System.err.println(e);

            return;
        }
    }

    public static Document DocumentFromFile(File file) {
        DocumentBuilder dbuilder = null;

        try {
            XMLUtils.factory.setNamespaceAware(true);
            dbuilder = XMLUtils.factory.newDocumentBuilder();

            return dbuilder.parse(file);
        } catch (Exception e) {
            System.err.println(e);

            return null;
        }
    }

    public static Document DocumentFromStream(InputStream io) {
        DocumentBuilder dbuilder = null;

        try {
            XMLUtils.factory.setNamespaceAware(true);
            dbuilder = XMLUtils.factory.newDocumentBuilder();

            InputSource is = new InputSource(io);

            return dbuilder.parse(is);
        } catch (Exception e) {
            System.err.println(e);

            return null;
        }
    }

    public static Document DocumentFromString(String xmlstring) {
        InputStream in = new ByteArrayInputStream(xmlstring.getBytes());

        return XMLUtils.DocumentFromStream(in);
    }

    public static Document DocumentFromUrl(String url) {
        try {
            URL locator = new URL(url);

            return XMLUtils.DocumentFromURL(locator);
        } catch (Exception e) {
            System.err.println(e);

            return null;
        }
    }

    public static Document DocumentFromURL(URL url) {
        InputStream in = null;

        try {
            in = url.openStream();

            return XMLUtils.DocumentFromStream(in);
        } catch (Exception e) {
            System.err.println(e);

            return null;
        }
    }
}
