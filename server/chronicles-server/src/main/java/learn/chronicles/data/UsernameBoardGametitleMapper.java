package learn.chronicles.data;

import learn.chronicles.models.UsernameGameTitle;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsernameBoardGametitleMapper implements RowMapper<UsernameGameTitle> {
    @Override
    public UsernameGameTitle mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UsernameGameTitle(
                rs.getString("username"),
                rs.getString("board_game_name")
        );
    }
}
