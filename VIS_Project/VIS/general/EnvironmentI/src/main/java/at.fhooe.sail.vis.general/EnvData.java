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

import java.util.Arrays;

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

    /**
     * Converts the EnvData object to its string representation.
     *
     * @return A string representation of the EnvData object, including its type, timestamp,
     * and values.
     *
     * The string is formatted as follows:
     * "EnvData{type='[type]', timestamp=[timestamp], values=[array_of_values]}"
     *
     * Example:
     * "EnvData{type='light', timestamp=123456789, values=[10, 20, 30]}"
     */
    @Override
    public String toString() {
        return "EnvData{" +
                "type='" + mSensorName + '\'' +
                ", timestamp=" + mTimestamp +
                ", values=" + Arrays.toString(mValues) +
                '}';
    }
}
