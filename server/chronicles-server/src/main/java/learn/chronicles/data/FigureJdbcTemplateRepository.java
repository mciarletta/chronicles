package learn.chronicles.data;

import learn.chronicles.models.Figure;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public class FigureJdbcTemplateRepository implements FigureRepository{

    private static final String SELECT = """
            SELECT
            	figure_id,
                board_game_id,
                category,
                figure_name,
                figure_skin,
                color,
                scale
            FROM figure
            """;

    private final JdbcTemplate jdbcTemplate;

    public FigureJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Figure findById(int id) {
        String sql = SELECT + "where figure_id = ?;";
        return jdbcTemplate.query(sql, new FigureMapper(), id)
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<Figure> findByBoardGameId(int id) {
        String sql = SELECT + "where board_game_id = ?;";
        return jdbcTemplate.query(sql, new FigureMapper(), id);    }

    @Override
    public Figure create(Figure figure) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("figure")
                .usingGeneratedKeyColumns("figure_id");

        HashMap<String, Object> args = new HashMap<>();
        args.put("board_game_id", figure.getBoardGameId());
        args.put("category", figure.getCategory());
        args.put("figure_name", figure.getName());
        args.put("figure_skin", figure.getSkin());
        args.put("color", figure.getColor());
        args.put("scale", figure.getScale());

        int id = insert.executeAndReturnKey(args).intValue();
        figure.setId(id);

        return figure;
    }

    @Override
    public boolean update(Figure figure) {
        String sql = """
            update figure set
                board_game_id = ?,
                category = ?,
                figure_name = ?,
                figure_skin = ?,
                color = ?,
                scale = ?
            WHERE figure_id = ?;
            """;

        return jdbcTemplate.update(sql,
                figure.getBoardGameId(),
                figure.getCategory(),
                figure.getName(),
                figure.getSkin(),
                figure.getColor(),
                figure.getScale(),
                figure.getId()) > 0;
    }

    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update("delete from figure where figure_id = ?;", id) > 0;
    }
}
