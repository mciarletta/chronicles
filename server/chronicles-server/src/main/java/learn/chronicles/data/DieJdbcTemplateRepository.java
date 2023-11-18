package learn.chronicles.data;

import learn.chronicles.models.Die;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class DieJdbcTemplateRepository implements DieRepository{

    private static final String SELECT = """
             SELECT
            	die_id,
                board_game_id,
                category,
                die_name,
                color,
            	background,
                side1,
            	side2,
                side3,
                side4,
                side5,
                side6,
            	rolling,
                winning_side
            FROM die
            """;

    private final JdbcTemplate jdbcTemplate;

    public DieJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Die findById(int id) {
        String sql = SELECT + "where die_id = ?;";
        return jdbcTemplate.query(sql, new DieMapper(), id)
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<Die> findByBoardGameId(int id) {
        String sql = SELECT + "where board_game_id = ?;";
        return jdbcTemplate.query(sql, new DieMapper(), id);    }

    @Override
    public Die create(Die die) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("die")
                .usingGeneratedKeyColumns("die_id");

        HashMap<String, Object> args = new HashMap<>();
        args.put("board_game_id", die.getBoardGameId());
        args.put("category", die.getCategory());
        args.put("die_name", die.getName());
        args.put("color", die.getColor());
        args.put("background", die.getBackground());
        args.put("side1", die.getSide1());
        args.put("side2", die.getSide2());
        args.put("side3", die.getSide3());
        args.put("side4", die.getSide4());
        args.put("side5", die.getSide5());
        args.put("side6", die.getSide6());
        args.put("rolling", die.isRolling());
        args.put("winning_side", die.getWinningSide());

        int id = insert.executeAndReturnKey(args).intValue();
        die.setId(id);

        return die;
    }

    @Override
    public boolean update(Die die) {
        String sql = """
            update die set
                board_game_id = ?,
                category = ?,
                die_name = ?,
                color = ?,
                background = ?,
                side1 = ?,
                side2 = ?,
                side3 = ?,
                side4 = ?,
                side5 = ?,
                side6 = ?,
                rolling = ?,
                winning_side = ?
            WHERE die_id = ?;
            """;

        return jdbcTemplate.update(sql,
                die.getBoardGameId(),
                die.getCategory(),
                die.getName(),
                die.getColor(),
                die.getBackground(),
                die.getSide1(),
                die.getSide2(),
                die.getSide3(),
                die.getSide4(),
                die.getSide5(),
                die.getSide6(),
                die.isRolling(),
                die.getWinningSide(),
                die.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update("delete from die where die_id = ?;", id) > 0;
    }
}
