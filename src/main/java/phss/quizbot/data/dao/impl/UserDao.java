package phss.quizbot.data.dao.impl;

import phss.quizbot.data.dao.DataDao;
import phss.quizbot.user.UserAccount;

import java.util.HashMap;

public class UserDao implements DataDao<Long, UserAccount> {

    @Override
    public HashMap<Long, UserAccount> loadAll() {
        return new HashMap<>();
    }

    @Override
    public void save(UserAccount data) {

    }

    @Override
    public void delete(UserAccount data) {

    }

}
