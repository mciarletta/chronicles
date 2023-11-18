package learn.chronicles.security;

import learn.chronicles.data.AppUserRepository;
import learn.chronicles.domain.AppUserService;
import learn.chronicles.domain.Result;
import learn.chronicles.models.AppUser;
import learn.chronicles.models.Board;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static learn.chronicles.TestHelper.*;
import static learn.chronicles.TestHelper.makeBoard;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AppUserServiceTest {

    @MockBean
    AppUserRepository repository;

    @MockBean
    PasswordEncoder passwordEncoder;

    @Autowired
    AppUserService service;

    @Test
    void shouldThrowUsernameNotFoundExceptionForMissingUser() {
        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("missing_username");
        });
    }

    @Test
    void findById() {
        AppUser expected = makeAppUser(1);

        when(repository.findById(anyInt())).thenReturn(makeAppUser(1));

        AppUser actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldAddValidUser() {
        AppUser expected = new AppUser(5, "test1", "test1@test.com", "hashed_password", true, null, null, List.of("USER"));

        when(passwordEncoder.encode(any())).thenReturn("hashed_password");
        when(repository.create(any())).thenReturn(expected);

        Result<AppUser> actual = service.create( "test1","test1@test.com", "P@ssw0rd!");

        assertTrue(actual.isSuccess());
        assertEquals(expected, actual.getPayload());
    }

    @Test
    void shouldNotAddNullUsername() {
        Result<AppUser> expected = makeResult("username is required", null);

        Result<AppUser> actual = service.create(null, "test1@test.com","P@ssw0rd!");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddBlankUsername() {
        Result<AppUser> expected = makeResult("username is required", null);

        Result<AppUser> actual = service.create("", "test1@test.com", "P@ssw0rd!");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddNullPassword() {
        Result<AppUser> expected = makeResult("password is required", null);

        Result<AppUser> actual = service.create("username","username@example.com", null);

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddBlankPassword() {
        Result<AppUser> expected = makeResult("password is required", null);

        Result<AppUser> actual = service.create("user", "username@example.com", "");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddTooShortPassword() {
        Result<AppUser> expected = makeResult("password must be at least 8 character and contain a digit, a lower-case letter, and upper-case letter, and a non-digit/non-letter", null);

        Result<AppUser> actual = service.create("user", "username@example.com", "P@ssw0r");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddPasswordWithNoDigit() {
        Result<AppUser> expected = makeResult("password must be at least 8 character and contain a digit, a lower-case letter, and upper-case letter, and a non-digit/non-letter", null);

        Result<AppUser> actual = service.create("user","username@example.com", "P@ssword");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddPasswordWithNoSpecialCharacter() {
        Result<AppUser> expected = makeResult("password must be at least 8 character and contain a digit, a lower-case letter, and upper-case letter, and a non-digit/non-letter", null);

        Result<AppUser> actual = service.create("user","username@example.com", "Passw0rd");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddPasswordWithNoLetters() {
        Result<AppUser> expected = makeResult("password must be at least 8 character and contain a digit, a lower-case letter, and upper-case letter, and a non-digit/non-letter", null);

        Result<AppUser> actual = service.create("user","username@example.com", "9455702@");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddPasswordWithOnlyLowerCase() {
        Result<AppUser> expected = makeResult("password must be at least 8 character and contain a digit, a lower-case letter, and upper-case letter, and a non-digit/non-letter", null);

        Result<AppUser> actual = service.create("user","username@example.com", "9@password");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddPasswordWithOnlyUpperCase() {
        Result<AppUser> expected = makeResult("password must be at least 8 character and contain a digit, a lower-case letter, and upper-case letter, and a non-digit/non-letter", null);

        Result<AppUser> actual = service.create("user","username@example.com", "9@PASSWORD");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddDuplicateUsername() {
        AppUser existing = new AppUser(5, "username","existing@test.com", "password", true, null, null, List.of("USER"));

        when(repository.findByUsername("username")).thenReturn(existing);

        Result<AppUser> expected = makeResult("the provided username already exists", null);

        Result<AppUser> actual = service.create("username","existing@test.com", "P@ssw0rd!");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotCreateTooLongUserName() {
        Result<AppUser> expected = makeResult("username must be less than 50 characters", null);

        Result<AppUser> actual = service.create("123456789012345678901234567890123456789012345678901234567890", "test1@test.com","P@ssw0rd!");

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotCreateTooLongEmail() {
        Result<AppUser> expected = makeResult("email must be less than 100 characters", null);

        Result<AppUser> actual = service.create("username", "T1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890est1@test.com","P@ssw0rd!");

        assertEquals(expected.getErrorMessages().get(0), actual.getErrorMessages().get(0));
    }

    @Test
    void shouldNotUpdateTooLongColor() {
        Result<AppUser> expected = makeResult("color must be less than 50 characters", null);
        AppUser user = makeAppUser(3);
        user.setColor("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");


        Result<AppUser> actual = service.update(user);

        assertEquals(expected.getErrorMessages().get(0), actual.getErrorMessages().get(1));
    }

    @Test
    void shouldNotUpdateTooLongAvatar() {
        Result<AppUser> expected = makeResult("avatar must be less than 250 characters", null);
        AppUser user = makeAppUser(3);
        user.setAvatar("123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");


        Result<AppUser> actual = service.update(user);

        assertEquals(expected.getErrorMessages().get(0), actual.getErrorMessages().get(1));
    }

}