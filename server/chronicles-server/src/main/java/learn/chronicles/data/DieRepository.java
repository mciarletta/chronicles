package learn.chronicles.data;

import learn.chronicles.models.Die;

import java.util.List;

public interface DieRepository {

    Die findById(int id);

    List<Die> findByBoardGameId(int id);

    Die create(Die die);

    boolean update(Die die);

    boolean deleteById(int id);
}
