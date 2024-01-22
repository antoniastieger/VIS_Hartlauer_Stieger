package at.fhooe.sail.vis;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * The HighscoreParser class is responsible for parsing JSON strings containing highscore data
 * and converting them into a list of HighscoreEntry objects.
 */
public class HighscoreParser {

    /**
     * Parses the given JSON string to extract highscore entries.
     *
     * @param _jsonString The JSON string containing highscore data.
     * @return A list of HighscoreEntry objects parsed from the JSON string.
     */
    public List<HighscoreEntry> parseHighscores(String _jsonString) {
        List<HighscoreEntry> highscores = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(_jsonString);
        if (jsonObject.has("Highscore")) {
            JSONArray highscoreArray = jsonObject.getJSONArray("Highscore");

            for (int i = 0; i < highscoreArray.length(); i++) {
                JSONObject entryObject = highscoreArray.getJSONObject(i);
                String name = entryObject.getString("Name");
                int score = entryObject.getInt("Score");
                int time = entryObject.getInt("Time");

                HighscoreEntry highscoreEntry = new HighscoreEntry(name, score, time);
                highscores.add(highscoreEntry);
            }
        }

        return highscores;
    }

    /**
     * Example main method demonstrating the usage of the HighscoreParser class.
     *
     * @param _args Command-line arguments (not used in this example).
     */
    public static void main(String[] _args) {
        // Example usage
        String jsonString = "{" +
                "\"Highscore\": [" +
                "{\"Name\":\"TBD Name 01\", \"Score\":10, \"Time\":10}," +
                "{\"Name\":\"TBD Name 02\", \"Score\":10, \"Time\":11}," +
                "{\"Name\":\"TBD Name 03\", \"Score\":9, \"Time\":9}" +
                "]" +
                "}";

        HighscoreParser highscoreParser = new HighscoreParser();
        List<HighscoreEntry> highscores = highscoreParser.parseHighscores(jsonString);

        // You can now work with the list of HighscoreEntry objects
        for (HighscoreEntry entry : highscores) {
            System.out.println("Name: " + entry.getmName() + ", Score: " + entry.getmScore() + ", Time: " + entry.getmTime());
        }
    }
}
