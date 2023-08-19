package phss.quizbot.quiz;

import java.util.List;
import java.util.UUID;

public class Quiz {

    UUID quizId;

    String name;
    String description;

    List<QuizQuestion> questions;

    long creatorId;

    public Quiz(String name, String description, List<QuizQuestion> questions, long creatorId) {
        this.quizId = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.questions = questions;
        this.creatorId = creatorId;
    }

    public UUID getQuizId() {
        return quizId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public long getCreatorId() {
        return creatorId;
    }

}
