package phss.quizbot.data.repository.impl;

import phss.quizbot.data.dao.impl.SessionDao;
import phss.quizbot.data.repository.DataRepository;
import phss.quizbot.quiz.session.QuizSession;

import java.util.*;
import java.util.function.Predicate;

public class SessionRepository implements DataRepository<UUID, QuizSession> {

    HashMap<UUID, QuizSession> sessions;

    final private SessionDao sessionDao;

    public SessionRepository(SessionDao sessionDao) {
        this.sessionDao = sessionDao;
        this.sessions = sessionDao.loadAll();
    }

    @Override
    public Optional<QuizSession> get(UUID key) {
        return Optional.ofNullable(sessions.get(key));
    }

    public Optional<QuizSession> find(Predicate<? super QuizSession> predicate) {
        return sessions.values().stream().filter(predicate).findFirst();
    }

    @Override
    public void save(QuizSession data) {
        sessions.put(data.getSessionId(), data);
        sessionDao.save(data);
    }

    @Override
    public void delete(QuizSession data) {
        sessions.remove(data.getSessionId());
        sessionDao.delete(data);
    }

    @Override
    public List<QuizSession> getData() {
        return new ArrayList<>(sessions.values());
    }

    @Override
    public int getDataAmount() {
        return sessions.size();
    }

}
