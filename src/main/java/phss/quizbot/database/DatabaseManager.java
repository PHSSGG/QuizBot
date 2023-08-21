package phss.quizbot.database;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import java.util.ArrayList;

public class DatabaseManager {

    public static final String DATABASE = "quizbot";

    public static final String USER_TABLE = "users";
    public static final String SESSIONS_TABLE = "sessions";

    MongoClient client;
    MongoDatabase usersDatabase;
    MongoDatabase sessionsDatabase;

    public DatabaseManager start(String uri) {
        client = MongoClients.create(uri);

        usersDatabase = createDatabase(USER_TABLE);
        sessionsDatabase = createDatabase(SESSIONS_TABLE);

        return this;
    }

    public MongoDatabase createDatabase(String table) {
        MongoDatabase mongoDatabase = client.getDatabase(DATABASE);

        if (!mongoDatabase.listCollectionNames().into(new ArrayList<>()).contains(table))
            mongoDatabase.createCollection(table);

        return mongoDatabase;
    }

    public MongoClient getClient() {
        return client;
    }

    public MongoDatabase getUsersDatabase() {
        return usersDatabase;
    }

    public MongoDatabase getSessionsDatabase() {
        return sessionsDatabase;
    }

}
