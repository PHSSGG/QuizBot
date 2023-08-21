package phss.quizbot.quiz.session;

import phss.quizbot.quiz.Quiz;
import phss.quizbot.quiz.QuizQuestion;
import phss.quizbot.user.UserAccount;

import java.util.HashMap;
import java.util.UUID;

public class QuizSession {

    UUID sessionId;

    UserAccount userAccount;
    Quiz quiz;

    int currentQuestion;
    HashMap<Integer, Boolean> answers;

    boolean isPaused = false;

    public QuizSession(UserAccount userAccount, Quiz quiz) {
        this(UUID.randomUUID(), userAccount, quiz, 0, new HashMap<>());
    }

    public QuizSession(UUID sessionId, UserAccount userAccount, Quiz quiz, int currentQuestion, HashMap<Integer, Boolean> answers) {
        this.sessionId = sessionId;
        this.userAccount = userAccount;
        this.quiz = quiz;
        this.currentQuestion = currentQuestion;
        this.answers = answers;
    }

    public QuizQuestion getCurrentQuestion() {
        try {
            return quiz.getQuestions().get(currentQuestion);
        } catch (IndexOutOfBoundsException ignored) {
            return null;
        }
    }

    public boolean processAnswer(QuizQuestion.QuizQuestionAnswer answer) {
        answers.put(currentQuestion, answer.isCorrect());
        currentQuestion += 1;

        return currentQuestion >= quiz.getQuestions().size();
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public HashMap<Integer, Boolean> getAnswers() {
        return answers;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

}
