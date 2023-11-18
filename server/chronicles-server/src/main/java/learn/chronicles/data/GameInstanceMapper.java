package learn.chronicles.data;

import learn.chronicles.models.GameInstance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GameInstanceMapper implements RowMapper<GameInstance> {
    @Override
    public GameInstance mapRow(ResultSet rs, int rowNum) throws SQLException {
        GameInstance gameInstance = new GameInstance();
        gameInstance.setId(rs.getInt("game_instance_id"));
        gameInstance.setBoardGameId(rs.getInt("board_game_id"));
        gameInstance.setSaveState(rs.getString("save_state"));
        gameInstance.setLog(rs.getString("log"));
        gameInstance.setBoardGameName(rs.getString("board_game_name"));
        gameInstance.setSkin(rs.getString("skin"));

        return gameInstance;

    }
}
