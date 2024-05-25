package Database;

import java.util.UUID;
import java.util.prefs.Preferences;

public class UserIdentifier {
    private static final String USER_ID_KEY = "user_id";
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(UserIdentifier.class);

    public static String getUserId() {
        String userId = PREFERENCES.get(USER_ID_KEY, null);
        if (userId == null) {
            userId = generateAndStoreUserId();
        }
        return userId;
    }

    private static String generateAndStoreUserId() {
        String userId = UUID.randomUUID().toString();
        PREFERENCES.put(USER_ID_KEY, userId);
        return userId;
    }
}
