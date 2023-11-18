package learn.chronicles.data;

import learn.chronicles.models.AppUserGameInstance;
import learn.chronicles.models.Figure;
import learn.chronicles.models.GameInstance;
import learn.chronicles.models.UsernameGameTitle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static learn.chronicles.TestHelper.makeFigure;
import static learn.chronicles.TestHelper.makeGameInstance;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GameInstanceJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    GameInstanceJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("call set_known_good_state();");
    }

    @Test
    void findById() {
        GameInstance expected = makeGameInstance(1);
        GameInstance actual = repository.findById(1);

        assertEquals(actual, expected);
    }

    @Test
    void addsUsers(){
        GameInstance expected = makeGameInstance(1);

        repository.addUsers(expected);
        assertNotNull(expected.getUsernames());
    }

    @Test
    void findByUserId() {
        List<GameInstance> expected = List.of(makeGameInstance(1));
        List<GameInstance> actual = repository.findByUserId(1);

        assertEquals(actual, expected);
    }

    @Test
    void findsUsers(){
        List<UsernameGameTitle> expected = List.of(new UsernameGameTitle("username_1", "name_1"),new UsernameGameTitle("username_2", "name_1"));
        List<UsernameGameTitle> actual = repository.findUsersInAGameInstance(1);

        assertEquals(actual, expected);
    }

    @Test
    void findByBoardGameId() {
        List<GameInstance> expected = List.of(makeGameInstance(1), makeGameInstance(2));
        List<GameInstance> actual = repository.findByBoardGameId(1);

        assertEquals(actual, expected);
    }

    @Test
    void create() {
        GameInstance arg = makeGameInstance(3);
        arg.setId(0);
        GameInstance expected = makeGameInstance(3);
        GameInstance actual = repository.create(arg);

        assertEquals(actual, expected);

        actual = repository.findById(3);
        assertEquals(actual, expected);
    }

    @Test
    void update() {
        GameInstance arg = makeGameInstance(2);
        arg.setSaveState("updated");
        GameInstance expected = makeGameInstance(2);
        expected.setSaveState("updated");

        boolean actual = repository.update(arg);

        assertTrue(actual);

        GameInstance actualInstance = repository.findById(2);
        assertEquals(actualInstance, expected);
    }

    @Test
    void addUser() {
        AppUserGameInstance expected = new AppUserGameInstance(2,2);
        AppUserGameInstance actual = repository.addUser(expected);

        assertEquals(actual, expected);

    }

    @Test
    void removeUser() {
        assertTrue(repository.removeUserByUserId(1, 1));
    }

    @Test
    void deleteById() {
        assertTrue(repository.deleteById(1));
    }
}