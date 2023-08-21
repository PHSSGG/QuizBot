package phss.quizbot.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import phss.quizbot.quiz.Quiz;
import phss.quizbot.quiz.QuizQuestion;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class QuizDataSource {

    public HashMap<UUID, Quiz> loadAllQuizzes() {
        try {
            return tryToLoadQuizzes();
        } catch (URISyntaxException | IOException | ParseException exception) {
            throw new RuntimeException(exception);
        }
    }

    private HashMap<UUID, Quiz> tryToLoadQuizzes() throws URISyntaxException, IOException, ParseException {
        String quizzesFolderPath = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath()+"\\";
        File folder = new File(quizzesFolderPath + "/quizzes");
        if (!folder.exists()) {
            folder.mkdirs();
            createDefaultQuiz(folder);
        }

        HashMap<UUID, Quiz> quizzes = new HashMap<>();

        File[] quizFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));
        if (quizFiles != null) for (File file : quizFiles) {
            Quiz quiz = loadQuizByFile(file);
            quizzes.put(quiz.getQuizId(), quiz);
        }

        return quizzes;
    }

    public Quiz loadQuizByFile(File file) throws IOException, ParseException {
        Quiz quiz;
        boolean hasUpdate = false;

        try (FileReader fileReader = new FileReader(file)) {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(fileReader);

            String idString = (String) data.get("id");
            String name = (String) data.get("name");
            String description = (String) data.get("description");
            long creatorId = (long) data.get("creatorId");
            List<QuizQuestion> questions = getQuestionsFromData(data);

            if (idString.equals("0")) {
                quiz = new Quiz(name, description, questions, creatorId);
                hasUpdate = true;
            }
            else quiz = new Quiz(UUID.fromString(idString), name, description, questions, creatorId);
        }
        if (hasUpdate) {
            updateQuizFile(file, quiz);
        }

        return quiz;
    }

    private List<QuizQuestion> getQuestionsFromData(JSONObject data) {
        JSONArray jsonArray = (JSONArray) data.get("questions");
        return QuizQuestion.deserializeFromJsonArray(jsonArray);
    }

    private void updateQuizFile(File file, Quiz quiz) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            JSONObject data = new JSONObject();
            data.put("id", quiz.getQuizId().toString());
            data.put("name", quiz.getName());
            data.put("description", quiz.getDescription());
            data.put("creatorId", quiz.getCreatorId());
            data.put("questions", QuizQuestion.serializeToJsonArray(quiz.getQuestions()));

            fileWriter.write(data.toJSONString());
        }
    }

    private void createDefaultQuiz(File folder) {
        String fileName = "test.json";

        File file = new File(folder, fileName);
        if (!file.exists()) {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            try (InputStream inputStream = classloader.getResourceAsStream(fileName)) {
                Files.copy(Objects.requireNonNull(inputStream), file.toPath());
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

}
