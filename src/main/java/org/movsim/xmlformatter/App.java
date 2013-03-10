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

//            document = manipulateDocument(document);

            final XMLOutputter outputter = new XMLOutputter();
            Format format = Format.getPrettyFormat();
            format.setIndent("    ");
            format.setLineSeparator("\n");
            outputter.setFormat(format);

            final PrintWriter writer = getWriter(args[0]);
            outputter.output(document, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }

    }

    @SuppressWarnings({ "unchecked", "unused" })
    private static Document manipulateDocument(Document document) {
        
        //Changes order of width and link Element
        final Element root = document.getRootElement();
        List<Element> roads = root.getChildren("road");
        for (Element road : roads) {
            Element lanes = road.getChild("lanes");
            Element laneSection = lanes.getChild("laneSection");
            Element right = laneSection.getChild("right");
            List<Element> lanesEl = right.getChildren("lane");
            for (Element lane : lanesEl) {
                Element width = lane.getChild("width");
                lane.removeChild("width");
                lane.addContent(width);
            }
        }
        return document;
    }

    public static PrintWriter getWriter(String filename) {
        try {
            final PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filename, false)));
            return writer;
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
