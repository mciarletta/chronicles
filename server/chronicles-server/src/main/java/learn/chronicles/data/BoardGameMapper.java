package learn.chronicles.data;

import learn.chronicles.models.BoardGame;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardGameMapper implements RowMapper<BoardGame> {
    @Override
    public BoardGame mapRow(ResultSet rs, int rowNum) throws SQLException {

            return new BoardGame(
                    rs.getInt("board_game_id"),
                    rs.getString("board_game_name"),
                    rs.getInt("height"),
                    rs.getInt("board_slots"),
                    rs.getInt("places_per_board"),
                    rs.getString("skin"));

    }
    }

