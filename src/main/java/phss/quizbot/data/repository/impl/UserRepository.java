package phss.quizbot.data.repository.impl;

import phss.quizbot.data.dao.impl.UserDao;
import phss.quizbot.data.repository.DataRepository;
import phss.quizbot.user.UserAccount;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class UserRepository implements DataRepository<Long, UserAccount> {

    HashMap<Long, UserAccount> accounts;

    final private UserDao userDao;

    public UserRepository(UserDao userDao) {
        this.userDao = userDao;
        this.accounts = userDao.loadAll();
    }

    @Override
    public Optional<UserAccount> get(Long key) {
        return Optional.ofNullable(accounts.get(key));
    }

    public UserAccount createAccount(long userId) {
        UserAccount account = new UserAccount(userId, new ArrayList<>(), new ArrayList<>());

        save(account);
        return account;
    }

    @Override
    public void save(UserAccount data) {
        accounts.put(data.getUserId(), data);
        userDao.save(data);
    }

    @Override
    public void delete(UserAccount data) {
        accounts.remove(data.getUserId());
        userDao.delete(data);
    }

    @Override
    public List<UserAccount> getData() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public int getDataAmount() {
        return accounts.size();
    }

}
