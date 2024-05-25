package Database;
import com.google.firebase.database.*;

public class DatabaseManager {

    private final DatabaseReference databaseReference;

    private int currentScore;

    private final String userID = UserIdentifier.getUserId();

    public DatabaseManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference("scores");
    }

    public void writeScore(int score) {
        databaseReference.child(userID).setValueAsync(score);
    }

    public void readScores() {
        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int highscore = dataSnapshot.getValue(Integer.class);
                setCurrentScore(highscore);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error reading highest score: " + databaseError.getMessage());
            }
        });
    }

    public int getCurrentScore() {
        return currentScore;
    }

    private void setCurrentScore(int score) {
        this.currentScore = score;
    }
}
