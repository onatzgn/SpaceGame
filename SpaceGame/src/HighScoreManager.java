import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HighScoreManager {
    private static final String HIGH_SCORE_FILE_PATH = "highscores.txt";
    private static final int MAX_HIGH_SCORES = 8;

    public static void saveHighScore(String username, int score) {
        List<HighScore> highScores = getHighScores();
        highScores.add(new HighScore(username, score));
        Collections.sort(highScores, Comparator.comparingInt(HighScore::getScore).reversed());
        
        if (highScores.size() > MAX_HIGH_SCORES) {
            highScores.remove(MAX_HIGH_SCORES);
        }
        saveHighScores(highScores);
    }

    public static List<HighScore> getHighScores() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGH_SCORE_FILE_PATH))) {
            return (List<HighScore>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }

    private static void saveHighScores(List<HighScore> highScores) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE_PATH))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
