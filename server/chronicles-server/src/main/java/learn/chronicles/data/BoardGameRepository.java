package learn.chronicles.data;

import learn.chronicles.models.BoardGame;
import learn.chronicles.models.Box;

import java.util.List;

public interface BoardGameRepository {

    List<BoardGame> findAll();

    BoardGame findById(int id);

    BoardGame create(BoardGame boardGame);

    boolean update(BoardGame boardGame);

    boolean deleteById(int id);
}
