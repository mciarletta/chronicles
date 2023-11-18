package learn.chronicles.data;

import learn.chronicles.models.Figure;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FigureMapper implements RowMapper<Figure> {
    @Override
    public Figure mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Figure(
                rs.getInt("figure_id"),
                rs.getInt("board_game_id"),
                rs.getString("category"),
                rs.getString("figure_name"),
                rs.getString("figure_skin"),
                rs.getString("color"),
                rs.getDouble("scale")
        );
    }
}
