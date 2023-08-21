package phss.quizbot.quiz.session;

import phss.quizbot.discord.view.SessionView;
import phss.quizbot.quiz.Quiz;
import phss.quizbot.user.UserAccount;

public class QuizActiveSession extends QuizSession {

    SessionView sessionView;

    public QuizActiveSession(UserAccount userAccount, Quiz quiz, SessionView sessionView) {
        super(userAccount, quiz);
        this.sessionView = sessionView;
    }

    public SessionView getSessionView() {
        return sessionView;
    }

}
