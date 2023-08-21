package phss.quizbot.quiz;

import java.util.List;

public class QuizQuestion {

    int position;
    String description;

    List<QuizQuestionAnswer> answers;

    public QuizQuestion(int position, String description, List<QuizQuestionAnswer> answers) {
        this.position = position;
        this.description = description;
        this.answers = answers;
    }

    public int getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public List<QuizQuestionAnswer> getAnswers() {
        return answers;
    }

    public static class QuizQuestionAnswer {
        String name;
        boolean isCorrect;

        public QuizQuestionAnswer(String name, boolean isCorrect) {
            this.name = name;
            this.isCorrect = isCorrect;
        }

        public String getName() {
            return name;
        }

        public boolean isCorrect() {
            return isCorrect;
        }
    }

}
