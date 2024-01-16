package at.fhooe.sail.vis;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HighscoreParser {

    public List<HighscoreEntry> parseHighscores(String jsonString) {
        List<HighscoreEntry> highscores = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonString);
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

    public static void main(String[] args) {
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
            System.out.println("Name: " + entry.getName() + ", Score: " + entry.getScore() + ", Time: " + entry.getTime());
        }
    }
}
