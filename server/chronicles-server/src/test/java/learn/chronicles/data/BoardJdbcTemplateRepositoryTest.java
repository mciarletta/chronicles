package learn.chronicles.data;

import learn.chronicles.models.Board;
import learn.chronicles.models.BoardGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static learn.chronicles.TestHelper.makeBoard;
import static learn.chronicles.TestHelper.makeBoardGame;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BoardJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BoardJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("call set_known_good_state();");
    }

    @Test
    void findById() {
        Board expected = makeBoard(1);
        Board actual = repository.findById(1);

        assertEquals(actual, expected);
    }

    @Test
    void NotFindById() {
        assertNull(repository.findById(99));
    }

    @Test
    void findByBoardGameId() {
        List<Board> expected = List.of(makeBoard(1));
        List<Board> actual = repository.findByBoardGameId(1);

        assertEquals(actual, expected);
    }

    @Test
    void NotFindByBoardGameId() {
        assertEquals(0, repository.findByBoardGameId(99).size());
    }

    @Test
    void create() {
        Board arg = makeBoard(3);
        arg.setId(0);
        Board expected = makeBoard(3);
        Board actual = repository.create(arg);

        assertEquals(actual, expected);

        actual = repository.findById(3);
        assertEquals(actual, expected);
    }

    @Test
    void update() {
        Board arg = makeBoard(2);
        arg.setName("updated");
        Board expected = makeBoard(2);
        expected.setName("updated");

        boolean actual = repository.update(arg);

        assertTrue(actual);

        Board actualBoard = repository.findById(2);
        assertEquals(actualBoard, expected);
    }

    @Test
    void deleteById() {
        assertTrue(repository.deleteById(2));

        assertNull(repository.findById(2));
    }
}