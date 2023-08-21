package phss.quizbot.manager;

import phss.quizbot.data.repository.impl.SessionRepository;
import phss.quizbot.discord.view.SessionView;
import phss.quizbot.quiz.Quiz;
import phss.quizbot.quiz.session.QuizActiveSession;
import phss.quizbot.quiz.session.QuizSession;
import phss.quizbot.user.UserAccount;

import java.util.HashMap;
import java.util.Optional;

public class SessionManager {

    HashMap<Long, QuizSession> sessions = new HashMap<>();

    final private SessionRepository sessionRepository;

    public SessionManager(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public boolean isUserInAnySession(long userId) {
        return sessions.containsKey(userId);
    }

    public Optional<QuizSession> getSessionByUserId(long userId) {
        return Optional.ofNullable(sessions.get(userId));
    }

    public QuizActiveSession createActiveSession(UserAccount userAccount, Quiz quiz, SessionView sessionView) {
        QuizActiveSession quizSession = new QuizActiveSession(userAccount, quiz, sessionView);
        sessions.put(userAccount.getUserId(), quizSession);

        return quizSession;
    }

    public void endSession(UserAccount userAccount, QuizSession session) {
        sessions.remove(userAccount.getUserId());
        userAccount.getCompletedQuizzes().add(session.getSessionId());

        sessionRepository.save(session);
    }

}
