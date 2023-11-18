package learn.chronicles.data;

import learn.chronicles.models.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static learn.chronicles.TestHelper.makeCard;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class CardJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    CardJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("call set_known_good_state();");
    }

    @Test
    void findById() {
        Card expected = makeCard(1);
        Card actual = repository.findById(1);

        assertEquals(actual, expected);
    }

    @Test
    void NotFindById() {
        assertNull(repository.findById(99));
    }

    @Test
    void findByBoardGameId() {
        List<Card> expected = List.of(makeCard(1));
        List<Card> actual = repository.findByBoardGameId(1);

        assertEquals(actual, expected);
    }

    @Test
    void NotFindByBoardGameId() {
        assertEquals(0, repository.findByBoardGameId(99).size());
    }

    @Test
    void create() {
        Card arg = makeCard(3);
        arg.setId(0);
        Card expected = makeCard(3);
        Card actual = repository.create(arg);

        assertEquals(actual, expected);

        actual = repository.findById(3);
        assertEquals(actual, expected);
    }

    @Test
    void update() {
        Card arg = makeCard(2);
        arg.setName("updated");
        Card expected = makeCard(2);
        expected.setName("updated");

        boolean actual = repository.update(arg);

        assertTrue(actual);

        Card actualBoard = repository.findById(2);
        assertEquals(actualBoard, expected);
    }

    @Test
    void deleteById() {
        assertTrue(repository.deleteById(2));

        assertNull(repository.findById(2));
    }
}