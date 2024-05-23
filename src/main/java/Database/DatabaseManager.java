package Database;
import com.google.firebase.database.*;

public class DatabaseManager {

    private final DatabaseReference databaseReference;

    public DatabaseManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference("scores");
    }

    public void writeScore(int score) {
        databaseReference.child("score").setValueAsync(score);

    }

    public void readScores(ScoreCallback callback) {
        databaseReference.child("score").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int highscore = dataSnapshot.getValue(Integer.class);
                callback.onScoreRead(highscore);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error reading highest score: " + databaseError.getMessage());
                callback.onScoreRead(-1); // or handle the error as you see fit
            }
        });
    }
}
