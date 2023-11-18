package learn.chronicles.data;

import learn.chronicles.models.Card;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
@Repository
public class CardJdbcTemplateRepository implements CardRepository{

    private static final String SELECT = """
            SELECT
            	card_id,
                board_game_id,
                category,
                card_name,
                card_type,
            	card_front,
            	card_back,
            	card_show,
                card_text,
                in_hand,
                card_variables
            FROM card
            """;

    private final JdbcTemplate jdbcTemplate;

    public CardJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Card findById(int id) {
        String sql = SELECT + "where card_id = ?;";
        return jdbcTemplate.query(sql, new CardMapper(), id)
                .stream().findFirst().orElse(null);
    }

    @Override
    public List<Card> findByBoardGameId(int id) {
        String sql = SELECT + "where board_game_id = ?;";
        return jdbcTemplate.query(sql, new CardMapper(), id);
    }

    @Override
    public Card create(Card card) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("card")
                .usingGeneratedKeyColumns("card_id");

        HashMap<String, Object> args = new HashMap<>();
        args.put("board_game_id", card.getBoardGameId());
        args.put("category", card.getCategory());
        args.put("card_name", card.getName());
        args.put("card_type", card.getType());
        args.put("card_front", card.getCardFront());
        args.put("card_back", card.getCardBack());
        args.put("card_show", card.isShow());
        args.put("card_text", card.getText());
        args.put("in-hand", card.getInHand());
        args.put("card_variables", card.getVariables());

        int id = insert.executeAndReturnKey(args).intValue();
        card.setId(id);

        return card;
    }

    @Override
    public boolean update(Card card) {
        String sql = """
            update card set
                board_game_id = ?,
                category = ?,
                card_name = ?,
                card_type = ?,
                card_front = ?,
                card_back = ?,
                card_show = ?,
                card_text = ?,
                in_hand = ?,
                card_variables = ?
            WHERE card_id = ?;
            """;

        return jdbcTemplate.update(sql,
                card.getBoardGameId(),
                card.getCategory(),
                card.getName(),
                card.getType(),
                card.getCardFront(),
                card.getCardBack(),
                card.isShow(),
                card.getText(),
                card.getInHand(),
                card.getVariables(),
                card.getId()) > 0;
    }


    @Override
    public boolean deleteById(int id) {
        return jdbcTemplate.update("delete from card where card_id = ?;", id) > 0;
    }
}
