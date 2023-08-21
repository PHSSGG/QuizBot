package phss.quizbot.data.dao.impl;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import phss.quizbot.data.dao.DataDao;
import phss.quizbot.database.DatabaseManager;
import phss.quizbot.user.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;

public class UserDao implements DataDao<Long, UserAccount> {

    final private MongoDatabase database;

    public UserDao(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public HashMap<Long, UserAccount> loadAll() {
        HashMap<Long, UserAccount> accounts = new HashMap<>();
        for (Document document : database.getCollection(DatabaseManager.USER_TABLE).find()) {
            long userId = document.getLong("user_id");
            accounts.put(userId, new UserAccount(userId, new ArrayList<>(), new ArrayList<>()));
        }

        return accounts;
    }

    @Override
    public void save(UserAccount data) {
        if (database.getCollection(DatabaseManager.USER_TABLE).find(Filters.eq("user_id", data.getUserId())).cursor().hasNext()) {
            return;
        }

        Document document = new Document().append("user_id", data.getUserId());
        database.getCollection(DatabaseManager.USER_TABLE).insertOne(document);
    }

    @Override
    public void delete(UserAccount data) {
        database.getCollection(DatabaseManager.USER_TABLE).findOneAndDelete(Filters.eq("user_id", data.getUserId()));
    }

}
