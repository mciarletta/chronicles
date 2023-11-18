package learn.chronicles.data;

import learn.chronicles.models.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static learn.chronicles.TestHelper.makeAppUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserJdbcTemplateRepositoryTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    AppUserJdbcTemplateRepository repository;

    @BeforeEach
    void setup() {
        jdbcTemplate.execute("call set_known_good_state();");
    }

    @Test
    void shouldFindByUsername() {
        AppUser expected = makeAppUser(1);

        AppUser actual = repository.findByUsername("username_1");

        assertEquals(expected, actual);
    }

    @Test
    void shouldFindById() {
        AppUser expected = makeAppUser(1);

        AppUser actual = repository.findById(1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindById() {
        assertNull(repository.findById(100));
    }

    @Test
    void shouldNotFindMissing() {
        AppUser actual = repository.findByUsername("faker2000");
        assertNull(actual);
    }

    @Test
    void shouldCreateAppUser() {
        AppUser appUser = new AppUser(0, "username_3","test3@app.com", "hashed_pass_3", true, null, null, List.of("TEST_ROLE_1"));
        AppUser expected = new AppUser(3, "username_3", "test3@app.com", "hashed_pass_3", true, null, null, List.of("TEST_ROLE_1"));

        AppUser actual = repository.create(appUser);

        assertEquals(expected, actual);

        assertEquals(expected, repository.findByUsername("username_3"));
    }

    @Test
    void shouldUpdateAppUser(){
        AppUser appUser = new AppUser(2, "username_2_NEW","test2@app.com", "hashed_pass_2", true, null, null, List.of("TEST_ROLE_1"));
        assertTrue(repository.update(appUser));

        assertEquals(repository.findByUsername("username_2_NEW"), appUser);

    }

    @Test
    void shouldDeleteAppUser(){
        AppUser appUser = new AppUser(2, "username_2","test2@app.com", "hashed_pass_2", true, null, null, List.of("TEST_ROLE_1"));

        assertTrue(repository.delete(appUser));

        assertNull(repository.findByUsername("username_2"));
    }



}