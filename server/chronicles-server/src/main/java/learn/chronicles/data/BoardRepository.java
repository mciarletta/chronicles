package learn.chronicles.data;

import learn.chronicles.models.Board;

import java.util.List;

public interface BoardRepository {

    Board findById(int id);

    List<Board> findByBoardGameId(int id);

    Board create(Board board);

    boolean update(Board board);

    boolean deleteById(int id);
}
