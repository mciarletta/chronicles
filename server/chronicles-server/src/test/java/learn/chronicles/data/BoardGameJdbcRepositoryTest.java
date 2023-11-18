package learn.chronicles.data;

import learn.chronicles.models.BoardGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static learn.chronicles.TestHelper.makeBoardGame;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BoardGameJdbcRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    BoardGameJdbcRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("call set_known_good_state();");
    }

    @Test
    void ShouldFindAll() {
        List<BoardGame> expected = List.of(makeBoardGame(1), makeBoardGame(2));
        List<BoardGame> actual = repository.findAll();

        assertEquals(actual, expected);
    }

    @Test
    void ShouldFindById() {
        BoardGame expected = makeBoardGame(1);
        BoardGame actual = repository.findById(1);

        assertEquals(actual, expected);
    }

    @Test
    void ShouldNotFindById() {
        assertNull(repository.findById(99));
    }

    @Test
    void ShouldCreate() {
        BoardGame arg = makeBoardGame(3);
        arg.setId(0);
        BoardGame expected = makeBoardGame(3);
        BoardGame actual = repository.create(arg);

        assertEquals(actual, expected);

        actual = repository.findById(3);
        assertEquals(actual, expected);
    }


    @Test
    void ShouldUpdate() {
        BoardGame arg = makeBoardGame(2);
        arg.setName("updated");
        BoardGame expected = makeBoardGame(2);
        expected.setName("updated");
        boolean actual = repository.update(arg);
        assertTrue(actual);


        BoardGame boardGame = repository.findById(2);
        assertEquals(boardGame, expected);
    }

    @Test
    void ShouldNotUpdate() {
        BoardGame arg = makeBoardGame(99);
        arg.setName("updated");

        assertFalse(repository.update(arg));

    }

    @Test
    void ShouldFDeleteById() {
        assertTrue(repository.deleteById(2));

        assertNull(repository.findById(2));
    }


}