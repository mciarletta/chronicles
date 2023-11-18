package learn.chronicles.data;

import learn.chronicles.models.AppUserGameInstance;
import learn.chronicles.models.GameInstance;
import learn.chronicles.models.UsernameGameTitle;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Repository
public class GameInstanceJdbcTemplateRepository implements GameInstanceRepository {

    private static final String SELECT = """
            SELECT
            	g.game_instance_id,
            	g.board_game_id,
            	g.save_state,
            	g.log,
            	b.board_game_name,
            	b.skin
            FROM game_instance g
            INNER JOIN board_game b on b.board_game_id = g.board_game_id
            """;

    private final JdbcTemplate jdbcTemplate;

    public GameInstanceJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GameInstance findById(int id) {
        String sql = SELECT + "where g.game_instance_id = ?;";
        GameInstance gameInstance = jdbcTemplate.query(sql, new GameInstanceMapper(), id)
                .stream().findFirst().orElse(null);

        if (gameInstance != null) {
            addUsers(gameInstance);
        }

        return gameInstance;
    }

    @Override
    public List<GameInstance> findByUserId(int id) {
        String sql = """
                SELECT
                	gi.game_instance_id,
                	gi.board_game_id,
                	gi.save_state,
                	gi.log,
                	b.board_game_name,
                	b.skin
                FROM game_instance gi
                inner join app_user_game_instance augi on augi.game_instance_id = gi.game_instance_id
                inner join app_user ap on ap.app_user_id = augi.app_user_id
                inner join board_game b on gi.board_game_id = b.board_game_id
                WHERE ap.app_user_id = ?;
                                """;
        List<GameInstance> gameInstances = jdbcTemplate.query(sql, new GameInstanceMapper(), id);
        if (!gameInstances.isEmpty()){
            for (GameInstance gi: gameInstances){
                addUsers(gi);
            }
        }
        return gameInstances;
    }

    @Override
    public List<GameInstance> findByBoardGameId(int id) {
        String sql = SELECT + "where b.board_game_id = ?;";
        return jdbcTemplate.query(sql, new GameInstanceMapper(), id);
    }

    @Override
    public List<UsernameGameTitle> findUsersInAGameInstance(int id) {
        String sql = """
                SELECT
                u.username,
                b.board_game_name
                FROM app_user u
                INNER JOIN app_user_game_instance g on g.app_user_id = u.app_user_id
                INNER JOIN game_instance i on i.game_instance_id = g.game_instance_id
                INNER JOIN board_game b on b.board_game_id = i.board_game_id
                WHERE i.game_instance_id = ?;
                """;
        return jdbcTemplate.query(sql, new UsernameBoardGametitleMapper(), id);
    }

    @Override
    public GameInstance create(GameInstance gameInstance) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("game_instance")
                .usingGeneratedKeyColumns("game_instance_id");

        HashMap<String, Object> args = new HashMap<>();
        args.put("board_game_id", gameInstance.getBoardGameId());
        args.put("save_state", gameInstance.getSaveState());
        args.put("log", gameInstance.getLog());

        int id = insert.executeAndReturnKey(args).intValue();
        gameInstance.setId(id);

        return gameInstance;
    }

    @Override
    public boolean update(GameInstance gameInstance) {
        String sql = """
                update game_instance set
                    board_game_id = ?,
                    save_state = ?,
                    log = ?
                WHERE game_instance_id = ?;
                """;

        return jdbcTemplate.update(sql,
                gameInstance.getBoardGameId(),
                gameInstance.getSaveState(),
                gameInstance.getLog(),
                gameInstance.getId()) > 0;
    }

    @Override
    public AppUserGameInstance addUser(AppUserGameInstance appUserGameInstance) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("app_user_game_instance");

        HashMap<String, Object> args = new HashMap<>();
        args.put("game_instance_id", appUserGameInstance.getGameInstanceId());
        args.put("app_user_id", appUserGameInstance.getAppUserId());

        insert.execute(args);

        return appUserGameInstance;

    }

    @Override
    public boolean removeUserByUserId(int userId, int gameInstanceId) {
        return jdbcTemplate.update("delete from app_user_game_instance where app_user_id = ? and game_instance_id = ?;",
                userId, gameInstanceId) > 0;
    }


    @Override
    @Transactional
    public boolean deleteById(int id) {


        jdbcTemplate.update("delete from app_user_game_instance where game_instance_id = ?;", id);

        jdbcTemplate.update("delete from game_instance where game_instance_id = ?;", id);


        return true;
    }

    public void addUsers(GameInstance gameInstance) {
        String sql = """
                SELECT
                u.username
                FROM app_user u
                INNER JOIN app_user_game_instance g on g.app_user_id = u.app_user_id
                INNER JOIN game_instance i on i.game_instance_id = g.game_instance_id
                WHERE i.game_instance_id = ?;
                """;
        List<String> users = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("username"), gameInstance.getId());
        gameInstance.setUsernames(users);
    }
}
