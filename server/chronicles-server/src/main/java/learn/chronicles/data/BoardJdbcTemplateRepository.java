package learn.chronicles.data;

import learn.chronicles.models.Board;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class BoardJdbcTemplateRepository implements  BoardRepository{
    private static final String SELECT = """
            SELECT
            	board_id,
                board_game_id,
                category,
                board_name,
                board_skin
            FROM board
            """;

    private final JdbcTemplate jdbcTemplate;

    public BoardJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Board findById(int id) {
        String sql = SELECT + "where board_id = ?;";
        return jdbcTemplate.query(sql, new BoardMapper(), id)
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<Board> findByBoardGameId(int id) {
        String sql = SELECT + "where board_game_id = ?;";
        return jdbcTemplate.query(sql, new BoardMapper(), id);
    }

    @Override
    public Board create(Board board) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("board")
                .usingGeneratedKeyColumns("board_id");

        HashMap<String, Object> args = new HashMap<>();
        args.put("board_game_id", board.getBoardGameId());
        args.put("category", board.getCategory());
        args.put("board_name", board.getName());
        args.put("board_skin", board.getSkin());

        int id = insert.executeAndReturnKey(args).intValue();
        board.setId(id);

        return board;
    }

    @Override
    public boolean update(Board board) {
        String sql = """
            update board set
                board_game_id = ?,
                category = ?,
                board_name = ?,
                board_skin = ?
            WHERE board_id = ?;
            """;

        return jdbcTemplate.update(sql,
                board.getBoardGameId(),
                board.getCategory(),
                board.getName(),
                board.getSkin(),
                board.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update("delete from board where board_id = ?;", id) > 0;
    }
}
