package phss.quizbot.data.dao;

import java.util.HashMap;

public interface DataDao <K, V> {

    HashMap<K, V> loadAll();

    void save(V data);
    void delete(V data);

}
