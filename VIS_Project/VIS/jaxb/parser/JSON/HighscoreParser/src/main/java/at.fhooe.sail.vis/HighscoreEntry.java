package at.fhooe.sail.vis;

/**
 * The HighscoreEntry class represents an entry in the highscore list,
 * containing information such as name, score, and time.
 */
public class HighscoreEntry {

    /**
     * The name associated with the highscore entry.
     */
    private String mName;

    /**
     * The score associated with the highscore entry.
     */
    private int mScore;

    /**
     * The time associated with the highscore entry.
     */
    private int mTime;

    /**
     * Constructs a HighscoreEntry with the specified name, score, and time.
     *
     * @param _name  The name associated with the highscore entry.
     * @param _score The score associated with the highscore entry.
     * @param _time  The time associated with the highscore entry.
     */
    public HighscoreEntry(String _name, int _score, int _time) {
        this.mName = _name;
        this.mScore = _score;
        this.mTime = _time;
    }

    /**
     * Gets the name associated with the highscore entry.
     *
     * @return The name of the highscore entry.
     */
    public String getmName() {
        return mName;
    }

    /**
     * Gets the score associated with the highscore entry.
     *
     * @return The score of the highscore entry.
     */
    public int getmScore() {
        return mScore;
    }

    /**
     * Gets the time associated with the highscore entry.
     *
     * @return The time of the highscore entry.
     */
    public int getmTime() {
        return mTime;
    }
}
