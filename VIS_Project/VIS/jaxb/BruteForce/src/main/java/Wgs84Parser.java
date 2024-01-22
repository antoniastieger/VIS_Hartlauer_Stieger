//
// Created by Antonia Stieger on 16.01.2024.
//

/**
 * Parses a WGS84 XML fragment and extracts latitude and longitude values.
 * The XML format is assumed to have latitude and longitude tags.
 * Example:
 * {@code
 * <wgs84>
 *   <latitude>48.31</latitude>
 *   <longitude>14.29</longitude>
 * </wgs84>
 * }
 */
public class Wgs84Parser {

    /**
     * Entry point for the Wgs84Parser application.
     * Parses a WGS84 XML fragment and prints the extracted latitude and longitude values.
     *
     * @param _args Command-line arguments (not used).
     */
    public static void main(String[] _args) {
        String xmlFragment = "<wgs84>\n" +
                "<latitude>48.31</latitude>\n" +
                "<longitude>14.29</longitude>\n" +
                "</wgs84>";

        Wgs84Container wgs84Container = parseWgs84(xmlFragment);

        System.out.println("Latitude: " + wgs84Container.getLatitude());
        System.out.println("Longitude: " + wgs84Container.getLongitude());
    }

    /**
     * Parses a WGS84 XML fragment and creates a {@link Wgs84Container} object
     * containing the extracted latitude and longitude values.
     *
     * @param _xmlFragment WGS84 XML fragment to parse.
     * @return Wgs84Container object with latitude and longitude values.
     */
    public static Wgs84Container parseWgs84(String _xmlFragment) {
        // Extract latitude and longitude using String class methods
        String latitude = extractTagValue(_xmlFragment, "latitude");
        String longitude = extractTagValue(_xmlFragment, "longitude");

        // Create a Wgs84Container and set the values
        Wgs84Container wgs84Container = new Wgs84Container();
        wgs84Container.setLatitude(latitude);
        wgs84Container.setLongitude(longitude);

        return wgs84Container;
    }

    /**
     * Extracts the value between specified start and end tags in an XML fragment.
     *
     * @param _xml      XML fragment to extract value from.
     * @param _tagName  Name of the XML tag.
     * @return Extracted value between the specified tags.
     */
    private static String extractTagValue(String _xml, String _tagName) {
        String startTag = "<" + _tagName + ">";
        String endTag = "</" + _tagName + ">";

        int startIndex = _xml.indexOf(startTag) + startTag.length();
        int endIndex = _xml.indexOf(endTag);

        return _xml.substring(startIndex, endIndex);
    }
}
