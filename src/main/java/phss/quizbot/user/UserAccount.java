package phss.quizbot.user;

import phss.quizbot.quiz.Quiz;

import java.util.List;
import java.util.UUID;

public class UserAccount {

    long userId;

    List<Quiz> quizzes;
    List<UUID> completedQuizzes;

    public UserAccount(long userId, List<Quiz> quizzes, List<UUID> completedQuizzes) {
        this.userId = userId;
        this.quizzes = quizzes;
        this.completedQuizzes = completedQuizzes;
    }

    public long getUserId() {
        return userId;
    }

    public List<Quiz> getQuizzes() {
        return quizzes;
    }

    public List<UUID> getCompletedQuizzes() {
        return completedQuizzes;
    }

}
