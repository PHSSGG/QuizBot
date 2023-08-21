package phss.quizbot.data.repository;

import java.util.List;
import java.util.Optional;

public interface DataRepository <K, V> {

    Optional<V> get(K key);

    void save(V data);
    void delete(V data);

    List<V> getData();
    int getDataAmount();

}