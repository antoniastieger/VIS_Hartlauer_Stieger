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