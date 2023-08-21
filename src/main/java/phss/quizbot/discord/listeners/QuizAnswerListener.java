package phss.quizbot.discord.listeners;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import phss.quizbot.data.repository.impl.UserRepository;
import phss.quizbot.manager.SessionManager;
import phss.quizbot.quiz.QuizQuestion;
import phss.quizbot.quiz.session.QuizActiveSession;
import phss.quizbot.user.UserAccount;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class QuizAnswerListener extends ListenerAdapter {

    UserRepository userRepository;
    SessionManager sessionManager;

    public QuizAnswerListener(UserRepository userRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (sessionManager.isUserInAnySession(event.getAuthor().getIdLong())) {
            User author = event.getAuthor();
            UserAccount userAccount = userRepository.get(author.getIdLong()).get();
            QuizActiveSession session = (QuizActiveSession) sessionManager.getSessionByUserId(author.getIdLong()).get();
            QuizQuestion.QuizQuestionAnswer answer = getAnswerByMessageInput(session, event.getMessage().getContentRaw());

            if (session.isPaused()) return;
            if (answer != null) {
                event.getMessage().delete().queue();

                session.setPaused(true);
                session.getSessionView().updateMessageWithCurrentAnswered();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        boolean isQuizFinished = session.processAnswer(answer);
                        if (isQuizFinished) {
                            sessionManager.endSession(userAccount, session);
                            session.getSessionView().deleteMessage();
                        } else {
                            session.getSessionView().updateMessage();
                        }

                        session.setPaused(false);
                    }
                }, TimeUnit.SECONDS.toMillis(3));
            }
        }
    }

    private QuizQuestion.QuizQuestionAnswer getAnswerByMessageInput(QuizActiveSession session, String input) {
        int selectedAnswerPosition;
        try {
            selectedAnswerPosition = Integer.parseInt(input);
        } catch (NumberFormatException ignored) {
            return null;
        }

        QuizQuestion currentQuestion = session.getCurrentQuestion();

        if (selectedAnswerPosition > currentQuestion.getAnswers().size() || selectedAnswerPosition < currentQuestion.getAnswers().size()) return null;
        return currentQuestion.getAnswers().get(selectedAnswerPosition - 1);
    }

}
