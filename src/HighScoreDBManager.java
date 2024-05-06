import java.sql.*;

public class HighScoreDBManager {

    private static final String DB_URL = "jdbc:sqlite:identifier.sqlite";

    static {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS HighScores (" +
                    "score INT" +
                    ")");
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM HighScores");
            rs.next();
            int count = rs.getInt("count");
            if (count == 0) {
                stmt.executeUpdate("INSERT INTO HighScores(score) VALUES(0)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveHighScore(int score) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE HighScores SET score = ?")) {
            pstmt.setInt(1, score);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int loadHighScore() {
        int highScore = 0;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT max(score) AS maxScore FROM HighScores")) {
            if (rs.next()) {
                highScore = rs.getInt("maxScore");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return highScore;
    }
}
