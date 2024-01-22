package at.fhooe.sail.vis;

/**
 * The WindInfo class represents information about wind speed and direction.
 * It provides methods to access and retrieve wind speed and direction values.
 */
public class WindInfo {
    /**
     * The wind speed in meters per second.
     */
    private double mSpeed;

    /**
     * The wind direction in degrees.
     */
    private int mDeg;

    /**
     * Constructs a WindInfo object with the specified speed and direction.
     *
     * @param _speed The wind speed in meters per second.
     * @param _deg   The wind direction in degrees.
     */
    public WindInfo(double _speed, int _deg) {
        this.mSpeed = _speed;
        this.mDeg = _deg;
    }

    /**
     * Gets the wind speed.
     *
     * @return The wind speed in meters per second.
     */
    public double getSpeed() {
        return mSpeed;
    }

    /**
     * Gets the wind direction.
     *
     * @return The wind direction in degrees.
     */
    public int getDeg() {
        return mDeg;
    }
}
