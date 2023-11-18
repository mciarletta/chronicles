package learn.chronicles.data;

import learn.chronicles.models.Board;
import learn.chronicles.models.BoardGame;
import learn.chronicles.models.Box;
import learn.chronicles.models.GameInstance;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public class BoardGameJdbcRepository implements BoardGameRepository{

    private static final String SELECT = """
            SELECT
            	board_game_id,
                board_game_name,
                height,
                board_slots,
                places_per_board,
                skin
            FROM board_game
            """;

    private final JdbcTemplate jdbcTemplate;

    private final GameInstanceJdbcTemplateRepository gameInstanceJdbcTemplateRepository;

    public BoardGameJdbcRepository(JdbcTemplate jdbcTemplate, GameInstanceJdbcTemplateRepository gameInstanceJdbcTemplateRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.gameInstanceJdbcTemplateRepository = gameInstanceJdbcTemplateRepository;
    }


    @Override
    public List<BoardGame> findAll() {
        String sql = SELECT + ";";
        return jdbcTemplate.query(sql, new BoardGameMapper());
    }

    @Override
    public BoardGame findById(int id) {
        String sql = SELECT + "where board_game_id = ?;";
        return jdbcTemplate.query(sql, new BoardGameMapper(), id)
                .stream().findFirst().orElse(null);
    }


    @Override
    public BoardGame create(BoardGame boardGame) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("board_game")
                .usingGeneratedKeyColumns("board_game_id");

        HashMap<String, Object> args = new HashMap<>();
        args.put("board_game_name", boardGame.getName());
        args.put("height", boardGame.getHeight());
        args.put("board_slots", boardGame.getBoardSlots());
        args.put("skin", boardGame.getSkin());
        args.put("places_per_board", boardGame.getPlacesPerBoard());

        int id = insert.executeAndReturnKey(args).intValue();
        boardGame.setId(id);

        return boardGame;
    }

    @Override
    public boolean update(BoardGame boardGame) {
        String sql = """
            update board_game set
                board_game_name = ?,
                height = ?,
                board_slots = ?,
                places_per_board = ?,
                skin = ?
            WHERE board_game_id = ?;
            """;

        return jdbcTemplate.update(sql,
                boardGame.getName(),
                boardGame.getHeight(),
                boardGame.getBoardSlots(),
                boardGame.getPlacesPerBoard(),
                boardGame.getSkin(),
                boardGame.getId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int id) {
        //this is going to cause a huge amount of integrity violations.

        jdbcTemplate.update("delete from board where board_game_id = ?;", id);
        jdbcTemplate.update("delete from figure where board_game_id = ?;", id);
        jdbcTemplate.update("delete from die where board_game_id = ?;", id);
        jdbcTemplate.update("delete from card where board_game_id = ?;", id);

        List<GameInstance> gameInstances = gameInstanceJdbcTemplateRepository.findByBoardGameId(id);

        if (gameInstances != null){
            for (GameInstance gi: gameInstances){
                gameInstanceJdbcTemplateRepository.deleteById(gi.getId());
            }
        }


        jdbcTemplate.update("delete from board_game where board_game_id = ?;", id);

        return true;
    }
}
