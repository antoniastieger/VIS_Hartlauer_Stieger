/*
package at.fhooe.sail.vis.general;

public class EnvData {
    private String mSensorName;
    private long mTimestamp;
    private int[] mValues;

    public EnvData(String _sensorName, long _timestamp, int[] _values) {
        mSensorName = _sensorName;
        mTimestamp = _timestamp;
        mValues = _values;
    }

    // Constructor, toString, and any additional functionality
}
*/

package at.fhooe.sail.vis.general;

/**
 * Represents environmental data, including sensor name, timestamp, and values.
 */
public class EnvData {
    private String mSensorName;
    private long mTimestamp;
    private int[] mValues;

    /**
     * Constructs an EnvData object with the given parameters.
     *
     * @param _sensorName The name of the sensor.
     * @param _timestamp  The timestamp of the data.
     * @param _values     The array of sensor values.
     */
    public EnvData(String _sensorName, long _timestamp, int[] _values) {
        mSensorName = _sensorName;
        mTimestamp = _timestamp;
        mValues = _values;
    }

    // Constructor, toString, and any additional functionality
}
