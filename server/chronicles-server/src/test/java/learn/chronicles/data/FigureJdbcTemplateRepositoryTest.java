package learn.chronicles.data;

import learn.chronicles.models.Figure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static learn.chronicles.TestHelper.makeFigure;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class FigureJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    FigureJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("call set_known_good_state();");
    }

    @Test
    void findById() {
        Figure expected = makeFigure(1);
        Figure actual = repository.findById(1);

        assertEquals(actual, expected);
    }

    @Test
    void NotFindById() {
        assertNull(repository.findById(99));
    }

    @Test
    void findByBoardGameId() {
        List<Figure> expected = List.of(makeFigure(1));
        List<Figure> actual = repository.findByBoardGameId(1);

        assertEquals(actual, expected);
    }

    @Test
    void NotFindByBoardGameId() {
        assertEquals(0, repository.findByBoardGameId(99).size());
    }

    @Test
    void create() {
        Figure arg = makeFigure(3);
        arg.setId(0);
        Figure expected = makeFigure(3);
        Figure actual = repository.create(arg);

        assertEquals(actual, expected);

        actual = repository.findById(3);
        assertEquals(actual, expected);
    }

    @Test
    void update() {
        Figure arg = makeFigure(2);
        arg.setName("updated");
        Figure expected = makeFigure(2);
        expected.setName("updated");

        boolean actual = repository.update(arg);

        assertTrue(actual);

        Figure actualBoard = repository.findById(2);
        assertEquals(actualBoard, expected);
    }

    @Test
    void deleteById() {
        assertTrue(repository.deleteById(2));

        assertNull(repository.findById(2));
    }

}