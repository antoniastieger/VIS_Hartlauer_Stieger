package at.fhooe.sail.vis;

public class HighscoreEntry {
    private String name;
    private int score;
    private int time;

    public HighscoreEntry(String name, int score, int time) {
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }
}
