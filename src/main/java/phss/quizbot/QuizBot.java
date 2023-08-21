package phss.quizbot;

import phss.quizbot.data.QuizDataSource;
import phss.quizbot.data.dao.impl.SessionDao;
import phss.quizbot.data.dao.impl.UserDao;
import phss.quizbot.data.repository.impl.QuizRepository;
import phss.quizbot.data.repository.impl.SessionRepository;
import phss.quizbot.data.repository.impl.UserRepository;
import phss.quizbot.database.DatabaseManager;
import phss.quizbot.discord.DiscordBot;
import phss.quizbot.manager.SessionManager;

public class QuizBot {

    public static void main(String[] args) {
        QuizRepository quizRepository = new QuizRepository(new QuizDataSource());
        DatabaseManager databaseManager = new DatabaseManager().start(args[0]);

        UserDao userDao = new UserDao(databaseManager.getUsersDatabase());
        UserRepository userRepository = new UserRepository(userDao);

        SessionDao sessionDao = new SessionDao(databaseManager.getSessionsDatabase(), userRepository, quizRepository);
        SessionRepository sessionRepository = new SessionRepository(sessionDao);

        DiscordBot discordBot = new DiscordBot(userRepository, quizRepository, sessionRepository, new SessionManager(sessionRepository));
        try {
            discordBot.start(args[1]);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
