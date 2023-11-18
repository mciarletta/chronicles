package learn.chronicles.data;

import learn.chronicles.models.Card;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CardMapper implements RowMapper<Card> {
    @Override
    public Card mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Card(
                rs.getInt("card_id"),
                rs.getInt("board_game_id"),
                rs.getString("category"),
                rs.getString("card_name"),
                rs.getString("card_type"),
                rs.getString("card_front"),
                rs.getString("card_back"),
                rs.getBoolean("card_show"),
                rs.getString("card_text"),
                rs.getInt("in_hand"),
                rs.getString("card_variables")
                );
    }
}
