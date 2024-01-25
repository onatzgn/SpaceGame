import java.io.Serializable;

public class HighScore implements Serializable, Comparable<HighScore>{
    private String username;
    private int score;

    public HighScore(String username, int score) {
        this.username = username;
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
    @Override
    public int compareTo(HighScore o) {
        return Integer.compare(o.score, this.score);
    }

}
