package phss.quizbot.quiz;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
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

    public static JSONArray serializeToJsonArray(List<QuizQuestion> questions) {
        JSONArray jsonArray = new JSONArray();
        for (QuizQuestion question : questions) {
            JSONObject questionObject = new JSONObject();
            JSONObject questionData = new JSONObject();
            questionData.put("description", question.getDescription());

            JSONArray answersArray = new JSONArray();
            for (QuizQuestionAnswer answer : question.getAnswers()) {
                JSONObject answerObject = new JSONObject();
                JSONObject answerData = new JSONObject();
                answerData.put("name", answer.getName());
                answerData.put("isCorrect", answer.isCorrect());
                answerObject.put(answer.getName(), answerData);
                answersArray.add(answerObject);
            }
            questionData.put("answers", answersArray);

            questionObject.put(String.valueOf(question.getPosition()), questionData);
            jsonArray.add(questionObject);
        }
        return jsonArray;
    }

    public static List<QuizQuestion> deserializeFromJsonArray(JSONArray jsonArray) {
        List<QuizQuestion> questions = new ArrayList<>();
        for (Object object : jsonArray) {
            if (object instanceof JSONObject) {
                JSONObject questionObject = (JSONObject) object;
                questionObject.keySet().forEach(key -> {
                    String positionString = (String) key;
                    JSONObject questionData = (JSONObject) questionObject.get(positionString);
                    String description = (String) questionData.get("description");
                    JSONArray answersArray = (JSONArray) questionData.get("answers");

                    List<QuizQuestionAnswer> answers = new ArrayList<>();
                    answersArray.forEach(answerObj -> {
                        if (answerObj instanceof JSONObject) {
                            JSONObject answerObject = (JSONObject) answerObj;
                            answerObject.keySet().forEach(answerKey -> {
                                JSONObject answerData = (JSONObject) answerObject.get(answerKey);
                                String answerName = (String) answerData.get("name");
                                boolean isCorrect = (boolean) answerData.get("isCorrect");
                                answers.add(new QuizQuestionAnswer(answerName, isCorrect));
                            });
                        }
                    });

                    int position = Integer.parseInt(positionString);
                    questions.add(new QuizQuestion(position, description, answers));
                });
            }
        }
        return questions;
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
