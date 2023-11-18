package learn.chronicles.data;
import learn.chronicles.models.Card;

import java.util.List;

public interface CardRepository {

    Card findById(int id);

    List<Card> findByBoardGameId(int id);

    Card create(Card card);

    boolean update(Card card);

    boolean deleteById(int id);
}
