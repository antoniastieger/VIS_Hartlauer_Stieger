package at.fhooe.sail.vis;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class WindXmlParser extends DefaultHandler {
    private String currentElement;
    private WindInfo windInfo;

    public WindInfo parse(InputStream inputStream) {
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(inputStream, this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return windInfo;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = qName;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();

        if (!value.isEmpty()) { // Überprüfe, ob der Wert nicht leer ist
            if ("speed".equals(currentElement)) {
                windInfo = new WindInfo(Double.parseDouble(value), windInfo != null ? windInfo.getDeg() : 0);
            } else if ("deg".equals(currentElement)) {
                windInfo = new WindInfo(windInfo != null ? windInfo.getSpeed() : 0.0, Integer.parseInt(value));
            }
        }
    }

    public static void main(String[] args) {
        String xmlFragment = "<wind>\n" +
                " <speed>50.25</speed>\n" +
                " <deg>225</deg>\n" +
                "</wind>";

        //System.out.println("XML Fragment: " + xmlFragment);

        try (InputStream inputStream = new ByteArrayInputStream(xmlFragment.getBytes())) {
            WindXmlParser parser = new WindXmlParser();
            WindInfo windInfo = parser.parse(inputStream);

            System.out.println("Speed: " + windInfo.getSpeed());
            System.out.println("Wind direction: " + windInfo.getDeg());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
