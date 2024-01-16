package at.fhooe.sail.vis;

public class WindInfo {
    private double speed;
    private int deg;

    public WindInfo(double speed, int deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDeg() {
        return deg;
    }
}
