package learn.chronicles.data;

import learn.chronicles.models.Board;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardMapper implements RowMapper<Board> {
    @Override
    public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Board(
                rs.getInt("board_id"),
                rs.getInt("board_game_id"),
                rs.getString("category"),
                rs.getString("board_name"),
                rs.getString("board_skin")
                );
    }
}
