//
// Created by Antonia Stieger on 16.01.2024.
//

public class Wgs84Parser {
    public static void main(String[] args) {
        String xmlFragment = "<wgs84>\n" +
                "<latitude>48.31</latitude>\n" +
                "<longitude>14.29</longitude>\n" +
                "</wgs84>";

        Wgs84Container wgs84Container = parseWgs84(xmlFragment);

        // Now, you can use wgs84Container to access latitude and longitude values
        System.out.println("Latitude: " + wgs84Container.getLatitude());
        System.out.println("Longitude: " + wgs84Container.getLongitude());
    }

    public static Wgs84Container parseWgs84(String xmlFragment) {
        // Extract latitude and longitude using String class methods
        String latitude = extractTagValue(xmlFragment, "latitude");
        String longitude = extractTagValue(xmlFragment, "longitude");

        // Create a Wgs84Container and set the values
        Wgs84Container wgs84Container = new Wgs84Container();
        wgs84Container.setLatitude(latitude);
        wgs84Container.setLongitude(longitude);

        return wgs84Container;
    }

    private static String extractTagValue(String xml, String tagName) {
        String startTag = "<" + tagName + ">";
        String endTag = "</" + tagName + ">";

        int startIndex = xml.indexOf(startTag) + startTag.length();
        int endIndex = xml.indexOf(endTag);

        return xml.substring(startIndex, endIndex);
    }
}
