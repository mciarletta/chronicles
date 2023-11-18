package learn.chronicles.data;

import learn.chronicles.models.Die;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DieMapper implements RowMapper<Die> {
    @Override
    public Die mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Die(
                rs.getInt("die_id"),
                rs.getInt("board_game_id"),
                rs.getString("category"),
                rs.getString("die_name"),
                rs.getString("color"),
                rs.getString("background"),
                rs.getString("side1"),
                rs.getString("side2"),
                rs.getString("side3"),
                rs.getString("side4"),
                rs.getString("side5"),
                rs.getString("side6"),
                rs.getBoolean("rolling"),
                rs.getString("winning_side")
        );
    }
}
