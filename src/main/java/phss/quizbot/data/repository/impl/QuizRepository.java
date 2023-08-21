package phss.quizbot.data.repository.impl;

import org.json.simple.parser.ParseException;
import phss.quizbot.data.QuizDataSource;
import phss.quizbot.data.repository.DataRepository;
import phss.quizbot.quiz.Quiz;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class QuizRepository implements DataRepository<UUID, Quiz> {

    HashMap<UUID, Quiz> quizzes;

    final private QuizDataSource quizDataSource;

    public QuizRepository(QuizDataSource quizDataSource) {
        this.quizDataSource = quizDataSource;
        this.quizzes = quizDataSource.loadAllQuizzes();
    }

    @Override
    public Optional<Quiz> get(UUID key) {
        return Optional.ofNullable(quizzes.get(key));
    }

    public Optional<Quiz> getByName(String name) {
        return quizzes.values().stream().filter(quiz -> quiz.getName().equals(name)).findFirst();
    }

    @Override
    public void save(Quiz data) {
    }

    public void save(File file) {
        try {
            Quiz quiz = quizDataSource.loadQuizByFile(file);
            quizzes.put(quiz.getQuizId(), quiz);
        } catch (IOException | ParseException exception) {
            throw new RuntimeException(exception);
        }
    }

    @Override
    public void delete(Quiz data) {
    }

    @Override
    public List<Quiz> getData() {
        return new ArrayList<>(quizzes.values());
    }

    @Override
    public int getDataAmount() {
        return quizzes.size();
    }

}
