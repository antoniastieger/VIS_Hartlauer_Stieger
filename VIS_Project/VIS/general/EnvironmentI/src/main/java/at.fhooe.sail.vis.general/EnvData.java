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

import java.io.Serializable;
import java.rmi.Remote;
import java.util.Arrays;

/**
 * Represents environmental data, including sensor name, timestamp, and values.
 * This class implements the Serializable interface for object serialization,
 * and it may be used remotely by implementing the Remote interface.
 */
public class EnvData implements Serializable, Remote {

    /**
     * The name of the sensor.
     */
    private String mSensorName;

    /**
     * The timestamp of the environmental data.
     */
    private long mTimestamp;

    /**
     * The array of sensor values.
     */
    private int[] mValues;

    /**
     * Constructs an EnvData object with the given parameters.
     *
     * @param _sensorName The name of the sensor.
     * @param _timestamp  The timestamp of the data.
     * @param _values     The array of sensor values.
     */
    public EnvData(String _sensorName, long _timestamp, int[] _values) {
        this.mSensorName = _sensorName;
        this.mTimestamp = _timestamp;
        this.mValues = _values;
    }

    /**
     * Getter for mSensorName
     * @return value of mSensorName
     */
    public String getSensorName() {
        return mSensorName;
    }

    /**
     * Getter for mTimestamp
     * @return value of getTimestamp
     */
    public long getTimestamp() {
        return mTimestamp;
    }

    /**
     * Getter for mValues
     * @return value of getValues
     */
    public int[] getValues() {
        return mValues;
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

    /**
     * Returns a string representing the greeting or message.
     *
     * @return a String representing the greeting or message.
     * @throws EnvDataException if there is an issue during the remote method invocation.
     */
    public String saySomething() throws EnvDataException {
        try {
            // Your logic here
            return "EnvData says: " + toString();
        } catch (Exception _e) {
            throw new EnvDataException("Error in saySomething method", _e);
        }
    }

    /**
     * Custom exception class for saySomething method.
     * This exception is thrown if there is an issue during the remote method invocation.
     */
    public static class EnvDataException extends Exception {
        public EnvDataException(String _message, Throwable _cause) {
            super(_message, _cause);
        }
    }
}
