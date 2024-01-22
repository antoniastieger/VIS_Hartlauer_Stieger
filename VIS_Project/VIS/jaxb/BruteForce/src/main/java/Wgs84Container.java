//
// Created by Antonia Stieger on 16.01.2024.
//

/**
 * Represents a container for WGS84 latitude and longitude values.
 */
public class Wgs84Container {
    private String latitude;
    private String longitude;

    /**
     * Gets the latitude value.
     *
     * @return Latitude value.
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the latitude value.
     *
     * @param _latitude Latitude value to set.
     */
    public void setLatitude(String _latitude) {
        this.latitude = _latitude;
    }

    /**
     * Gets the longitude value.
     *
     * @return Longitude value.
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the longitude value.
     *
     * @param _longitude Longitude value to set.
     */
    public void setLongitude(String _longitude) {
        this.longitude = _longitude;
    }
}

