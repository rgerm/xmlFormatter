package org.movsim.xmlformatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.xml.sax.InputSource;

public class App {
    public static void main(String[] args) {
        try {

            final SAXBuilder builder = new SAXBuilder();
            builder.setValidation(false);
            builder.setFeature("http://xml.org/sax/features/validation", false);
            builder.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            builder.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            Document document = builder.build(getInputSourceFromFilename(args[0]));

            // document = manipulateDocument(document);

            final XMLOutputter outputter = new XMLOutputter();
            Format format = Format.getPrettyFormat();
            format.setIndent("    ");
            outputter.setFormat(format);

            final PrintWriter writer = getWriter(args[0]);
            outputter.output(document, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Document manipulateDocument(Document document) {
        final Element root = document.getRootElement();
        Element simulation = root.getChild("SIMULATION");
        final Element output = simulation.getChild("OUTPUT");
        Element routes = output.getChild("ROUTES");
        output.removeChild("ROUTES");
        List sim = simulation.getChildren();
        int indexOutput = sim.indexOf(output);
        if (routes != null) {
            sim.add(indexOutput, routes.detach());
            System.out.println("detached routes at: " + indexOutput);
        }
        return document;
    }

    public static PrintWriter getWriter(String filename) {
        try {
            final PrintWriter fstr = new PrintWriter(new BufferedWriter(new FileWriter(filename, false)));
            return fstr;
        } catch (final java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputSource getInputSourceFromFilename(String filename) {
        final File inputFile = new File(filename);
        InputSource inputSource = null;
        try {
            inputSource = new InputSource(new FileInputStream(inputFile));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return inputSource;
    }
}
