package phss.quizbot.data.dao.impl;

import phss.quizbot.data.dao.DataDao;
import phss.quizbot.quiz.session.QuizSession;

import java.util.HashMap;
import java.util.UUID;

public class SessionDao implements DataDao<UUID, QuizSession> {

    @Override
    public HashMap<UUID, QuizSession> loadAll() {
        return new HashMap<>();
    }

    @Override
    public void save(QuizSession data) {

    }

    @Override
    public void delete(QuizSession data) {

    }

}
