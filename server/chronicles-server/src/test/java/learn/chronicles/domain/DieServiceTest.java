package learn.chronicles.domain;

import learn.chronicles.data.DieRepository;
import learn.chronicles.models.Card;
import learn.chronicles.models.Die;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static learn.chronicles.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class DieServiceTest {


    @MockBean
    DieRepository repository;

    @Autowired
    DieService service;

    @Test
    void findById() {
        Die expected = makeDie(1);

        when(repository.findById(anyInt())).thenReturn(makeDie(1));

        Die actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void findByIdBG() {
        List<Die> expected = List.of(makeDie(1));

        when(repository.findByBoardGameId(anyInt())).thenReturn(List.of(makeDie(1)));

        List<Die> actual = service.findByBoardGameId(1);
        assertEquals(expected, actual);
    }

    @Test
    void NotFindByIdBG() {
        List<Die> expected = null;

        when(repository.findByBoardGameId(anyInt())).thenReturn(null);

        List<Die> actual = service.findByBoardGameId(99);
        assertEquals(expected, actual);
    }

    @Test
    void add() {
        Result<Die> expected = makeResult(null, makeDie(1));
        Die arg = makeDie(1);
        arg.setId(0);

        when(repository.create(any())).thenReturn(makeDie(1));

        Result<Die> actual = service.add(arg);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Result<Die> expected = makeResult(null, null);
        Die arg = makeDie(1);

        when(repository.update(any())).thenReturn(true);

        Result<Die> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void deleteById() {
        Result<Void> expected = makeResult(null, null);

        when(repository.deleteById(anyInt())).thenReturn(true);

        Result<Void> actual = service.deleteById(1);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddNull() {
        Result<Die> expected = makeResult("die cannot be null.", null);
        Result<Die> actual = service.add(null);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotAddWithId() {
        Result<Die> expected = makeResult( "die id cannot be set for `create` operation.", null);
        Result<Die> actual = service.add(makeDie(1));
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankName() {
        Result<Die> expected = makeResult( "Name cannot be blank", null);
        Die arg = makeDie(1);
        arg.setName("");
        Result<Die> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullName() {
        Result<Die> expected = makeResult( "Name is required", null);
        Die arg = makeDie(109);
        arg.setName(null);
        Result<Die> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("Name is required"));
    }

    @Test
    void shouldNotAddWithLongName() {
        Result<Die> expected = makeResult( "die name must be less than 75 characters in length", null);
        Die arg = makeDie(1);
        arg.setName("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Die> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongCategory() {
        Result<Die> expected = makeResult( "die category must be less than 50 characters in length", null);
        Die arg = makeDie(1);
        arg.setCategory("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Die> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddTooLongColor() {
        Result<Die> expected = makeResult("die color must be less than 50 characters in length", null);
        Die die = makeDie(3);
        die.setColor("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");


        Result<Die> actual = service.add(die);

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankColor() {
        Result<Die> expected = makeResult( "color cannot be blank", null);
        Die arg = makeDie(1);
        arg.setColor("");
        Result<Die> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullColor() {
        Result<Die> expected = makeResult( "color is required", null);
        Die arg = makeDie(40);
        arg.setColor(null);
        Result<Die> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("color is required"));
    }

    @Test
    void shouldNotAddTooLongBackground() {
        Result<Die> expected = makeResult("die background must be less than 50 characters in length", null);
        Die die = makeDie(3);
        die.setBackground("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");


        Result<Die> actual = service.add(die);

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankBackground() {
        Result<Die> expected = makeResult( "background cannot be blank", null);
        Die arg = makeDie(1);
        arg.setBackground("");
        Result<Die> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullBackground() {
        Result<Die> expected = makeResult( "background is required", null);
        Die arg = makeDie(5);
        arg.setBackground(null);
        Result<Die> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("background is required"));
    }

    @Test
    void shouldNotAddTooLongSides() {
        Result<Die> expected = makeResult("die sides must be less than 5 characters in length", null);
        Die die = makeDie(7);
        die.setSide1("too long");
        Result<Die> actual = service.add(die);
        assertEquals(expected, actual);

        die.setSide2("too long");
        actual = service.add(die);
        assertTrue(actual.getErrorMessages().contains("die sides must be less than 5 characters in length"));

        die.setSide3("too long");
        actual = service.add(die);
        assertTrue(actual.getErrorMessages().contains("die sides must be less than 5 characters in length"));

        die.setSide4("too long");
        actual = service.add(die);
        assertTrue(actual.getErrorMessages().contains("die sides must be less than 5 characters in length"));

        die.setSide5("too long");
        actual = service.add(die);
        assertTrue(actual.getErrorMessages().contains("die sides must be less than 5 characters in length"));

        die.setSide6("too long");
        actual = service.add(die);
        assertTrue(actual.getErrorMessages().contains("die sides must be less than 5 characters in length"));

        die.setWinningSide("too long");
        actual = service.add(die);
        assertTrue(actual.getErrorMessages().contains("die sides must be less than 5 characters in length"));
    }


    @Test
    void shouldNotAddWithBadBoardGameId() {
        Result<Die> expected = makeResult( "board game id not found.", null);
        expected.setNotFound();
        Die arg = makeDie(1);
        arg.setBoardGameId(99);
        Result<Die> actual = service.add(arg);
        assertEquals(actual, expected);
    }

}