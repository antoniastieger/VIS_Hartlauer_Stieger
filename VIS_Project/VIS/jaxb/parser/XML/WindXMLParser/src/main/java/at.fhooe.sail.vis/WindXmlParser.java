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

/**
 * The WindXmlParser is a SAX parser for parsing XML data containing wind information.
 * The class extends the DefaultHandler of SAXParser and implements the necessary callback methods
 * to process XML elements.
 */
public class WindXmlParser extends DefaultHandler {
    /**
     * Current XML element during parsing.
     */
    private String mCurrentElement;

    /**
     * The WindInfo object containing the parsed wind information.
     */
    private WindInfo mWindInfo;

    /**
     * Parses the input stream containing XML data to retrieve wind information.
     *
     * @param _inputStream The input stream containing the XML data.
     * @return The WindInfo object containing parsed wind information.
     */
    public WindInfo parse(InputStream _inputStream) {
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(_inputStream, this);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return mWindInfo;
    }

    @Override
    public void startElement(String _uri, String _localName, String _qName, Attributes _attributes) throws SAXException {
        mCurrentElement = _qName;
    }

    @Override
    public void characters(char[] _ch, int _start, int _length) throws SAXException {
        String value = new String(_ch, _start, _length).trim();

        if (!value.isEmpty()) {
            if ("speed".equals(mCurrentElement)) {
                mWindInfo = new WindInfo(Double.parseDouble(value), mWindInfo != null ? mWindInfo.getDeg() : 0);
            } else if ("deg".equals(mCurrentElement)) {
                mWindInfo = new WindInfo(mWindInfo != null ? mWindInfo.getSpeed() : 0.0, Integer.parseInt(value));
            }
        }
    }

    /**
     * The main method for testing the WindXmlParser with a sample XML fragment.
     *
     * @param _args Command-line arguments (not used).
     */
    public static void main(String[] _args) {
        String xmlFragment = "<wind>\n" +
                " <speed>50.25</speed>\n" +
                " <deg>225</deg>\n" +
                "</wind>";

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
