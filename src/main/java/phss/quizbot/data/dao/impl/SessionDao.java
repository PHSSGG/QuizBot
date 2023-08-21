package phss.quizbot.data.dao.impl;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import phss.quizbot.data.dao.DataDao;
import phss.quizbot.data.repository.impl.QuizRepository;
import phss.quizbot.data.repository.impl.UserRepository;
import phss.quizbot.database.DatabaseManager;
import phss.quizbot.quiz.Quiz;
import phss.quizbot.quiz.session.QuizSession;
import phss.quizbot.user.UserAccount;

import java.util.HashMap;
import java.util.UUID;

public class SessionDao implements DataDao<UUID, QuizSession> {

    final private MongoDatabase database;
    final private UserRepository userRepository;
    final private QuizRepository quizRepository;

    public SessionDao(MongoDatabase database, UserRepository userRepository, QuizRepository quizRepository) {
        this.database = database;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public HashMap<UUID, QuizSession> loadAll() {
        HashMap<UUID, QuizSession> sessions = new HashMap<>();
        for (Document document : database.getCollection(DatabaseManager.SESSIONS_TABLE).find()) {
            UUID sessionId = UUID.fromString(document.getString("session_id"));
            long userId = document.getLong("user_id");
            UUID quizId = UUID.fromString(document.getString("quiz_id"));

            UserAccount userAccount = userRepository.get(userId).orElse(userRepository.createAccount(userId));
            Quiz quiz = quizRepository.get(quizId).orElse(null);

            sessions.put(sessionId, new QuizSession(sessionId, userAccount, quiz));
        }

        return sessions;
    }

    @Override
    public void save(QuizSession data) {
        Document document = new Document()
                .append("session_id", data.getSessionId().toString())
                .append("user_id", data.getUserAccount().getUserId())
                .append("quiz_id", data.getQuiz().getQuizId().toString());
        database.getCollection(DatabaseManager.SESSIONS_TABLE).insertOne(document);
    }

    @Override
    public void delete(QuizSession data) {
        database.getCollection(DatabaseManager.SESSIONS_TABLE).findOneAndDelete(Filters.eq("session_id", data.getSessionId().toString()));
    }

}
