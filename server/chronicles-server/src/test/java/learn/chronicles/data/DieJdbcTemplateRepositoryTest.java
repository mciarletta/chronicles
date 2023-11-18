package learn.chronicles.data;

import learn.chronicles.models.Die;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static learn.chronicles.TestHelper.makeDie;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DieJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DieJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("call set_known_good_state();");
    }

    @Test
    void findById() {
        Die expected = makeDie(1);
        Die actual = repository.findById(1);

        assertEquals(actual, expected);
    }

    @Test
    void NotFindById() {
        assertNull(repository.findById(99));
    }

    @Test
    void findByBoardGameId() {
        List<Die> expected = List.of(makeDie(1));
        List<Die> actual = repository.findByBoardGameId(1);

        assertEquals(actual, expected);
    }

    @Test
    void NotFindByBoardGameId() {
        assertEquals(0, repository.findByBoardGameId(99).size());
    }

    @Test
    void create() {
        Die arg = makeDie(3);
        arg.setId(0);
        Die expected = makeDie(3);
        Die actual = repository.create(arg);

        assertEquals(actual, expected);

        actual = repository.findById(3);
        assertEquals(actual, expected);
    }

    @Test
    void update() {
        Die arg = makeDie(2);
        arg.setName("updated");
        Die expected = makeDie(2);
        expected.setName("updated");

        boolean actual = repository.update(arg);

        assertTrue(actual);

        Die actualBoard = repository.findById(2);
        assertEquals(actualBoard, expected);
    }

    @Test
    void deleteById() {
        assertTrue(repository.deleteById(2));

        assertNull(repository.findById(2));
    }

}