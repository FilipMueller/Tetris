package Database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DatabaseInitializer {

    public DatabaseInitializer() {

    }

    @PostConstruct
    public void initialize() {
        try (InputStream serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json")) {

            if (serviceAccount == null) {
                throw new FileNotFoundException("Resource not found: Database/serviceAccountKey.json");
            }

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://tetris-15139-default-rtdb.europe-west1.firebasedatabase.app")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
