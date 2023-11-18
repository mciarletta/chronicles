package learn.chronicles.domain;

import learn.chronicles.data.CardRepository;
import learn.chronicles.models.Board;
import learn.chronicles.models.Card;
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
class CardServiceTest {

    @MockBean
    CardRepository repository;

    @Autowired
    CardService service;

    @Test
    void findById() {
        Card expected = makeCard(1);

        when(repository.findById(anyInt())).thenReturn(makeCard(1));

        Card actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void findByIdBG() {
        List<Card> expected = List.of(makeCard(1));

        when(repository.findByBoardGameId(anyInt())).thenReturn(List.of(makeCard(1)));

        List<Card> actual = service.findByBoardGameId(1);
        assertEquals(expected, actual);
    }

    @Test
    void NotFindByIdBG() {
        List<Card> expected = null;

        when(repository.findByBoardGameId(anyInt())).thenReturn(null);

        List<Card> actual = service.findByBoardGameId(99);
        assertEquals(expected, actual);
    }

    @Test
    void add() {
        Result<Card> expected = makeResult(null, makeCard(1));
        Card arg = makeCard(1);
        arg.setId(0);

        when(repository.create(any())).thenReturn(makeCard(1));

        Result<Card> actual = service.add(arg);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Result<Card> expected = makeResult(null, null);
        Card arg = makeCard(1);

        when(repository.update(any())).thenReturn(true);

        Result<Card> actual = service.update(arg);
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
        Result<Card> expected = makeResult("card cannot be null.", null);
        Result<Card> actual = service.add(null);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotAddWithId() {
        Result<Card> expected = makeResult( "card id cannot be set for `create` operation.", null);
        Result<Card> actual = service.add(makeCard(1));
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankName() {
        Result<Card> expected = makeResult( "Name cannot be blank", null);
        Card arg = makeCard(1);
        arg.setName("");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullName() {
        Result<Card> expected = makeResult( "Name is required", null);
        Card arg = makeCard(19);
        arg.setName(null);
        Result<Card> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("Name is required"));
    }

    @Test
    void shouldNotAddWithLongName() {
        Result<Card> expected = makeResult( "card name must be less than 75 characters in length", null);
        Card arg = makeCard(1);
        arg.setName("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongCategory() {
        Result<Card> expected = makeResult( "card category must be less than 50 characters in length", null);
        Card arg = makeCard(1);
        arg.setCategory("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankType() {
        Result<Card> expected = makeResult( "type cannot be blank", null);
        Card arg = makeCard(1);
        arg.setType("");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullType() {
        Result<Card> expected = makeResult( "type is required", null);
        Card arg = makeCard(30);
        arg.setType(null);
        Result<Card> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("type is required"));
    }

    @Test
    void shouldNotAddWithLongType() {
        Result<Card> expected = makeResult( "card type must be less than 75 characters in length", null);
        Card arg = makeCard(1);
        arg.setType("12345671234567890890124567890123456789012345678901234567890123456789012345673456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongFront() {
        Result<Card> expected = makeResult( "card front must be less than 250 characters in length", null);
        Card arg = makeCard(1);
        arg.setCardFront("12345671234567890890124567890123456789012345678901234567890123456789012345456712345678908901245678901234567890123456789012345678901234567890123456734567890123456789012345678901234567890123456789014567123456789089012456789012345678901234567890123456789012345678901234567345678901234567890123456789012345678901234567890145671234567890890124567890123456789012345678901234567890123456789012345673456789012345678901234567890123456789012345678901673456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongBack() {
        Result<Card> expected = makeResult( "card back must be less than 250 characters in length", null);
        Card arg = makeCard(1);
        arg.setCardBack("12345671234567890890124567890123456789012345678901234567890123456789012345456712345678908901245678901234567890123456789012345678901234567890123456734567890123456789012345678901234567890123456789014567123456789089012456789012345678901234567890123456789012345678901234567345678901234567890123456789012345678901234567890145671234567890890124567890123456789012345678901234567890123456789012345673456789012345678901234567890123456789012345678901673456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongText() {
        Result<Card> expected = makeResult( "card text must be less than 500 characters in length", null);
        Card arg = makeCard(1);
        arg.setText("12345671234567890890124567894567123456789089012456789012345678901234567890123456789012345678901234545671234567890890124567890123456789012345678901234567890123454567123456789089012456789012345678901234567890123456789012345678901234545671234567890890124567890123456789012345678901234567890123450123456789012345678901234567890123456789012344567123456789089012456789012345678901234567890123456789012345678901234545671234567890890124567890123456789012345678901234567890123454567123456789089012456789012345678901234567890123456789012345678901234545671234567890890124567890123456789012345678901234567890123455456712345678908901245678901234567890123456789012345678901234567890123456734567890123456789012345678901234567890123456789014567123456789089012456789012345678901234567890123456789012345678901234567345678901234567890123456789012345678901234567890145671234567890890124567890123456789012345678901234567890123456789012345673456789012345678901234567890123456789012345678901673456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongVariables() {
        Result<Card> expected = makeResult( "card variables must be less than 500 characters in length", null);
        Card arg = makeCard(1);
        arg.setVariables("12345671234567890890124567894567123456789089012456789012345678901234567890123456789012345678901234545671234567890890124567890123456789012345678901234567890123454567123456789089012456789012345678901234567890123456789012345678901234545671234567890890124567890123456789012345678901234567890123450123456789012345678901234567890123456789012344567123456789089012456789012345678901234567890123456789012345678901234545671234567890890124567890123456789012345678901234567890123454567123456789089012456789012345678901234567890123456789012345678901234545671234567890890124567890123456789012345678901234567890123455456712345678908901245678901234567890123456789012345678901234567890123456734567890123456789012345678901234567890123456789014567123456789089012456789012345678901234567890123456789012345678901234567345678901234567890123456789012345678901234567890145671234567890890124567890123456789012345678901234567890123456789012345673456789012345678901234567890123456789012345678901673456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankText() {
        Result<Card> expected = makeResult( "text cannot be blank", null);
        Card arg = makeCard(1);
        arg.setText("");
        Result<Card> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullText() {
        Result<Card> expected = makeResult( "text is required", null);
        Card arg = makeCard(20);
        arg.setText(null);
        Result<Card> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("text is required"));
    }

    @Test
    void shouldNotAddWithBadBoardGameId() {
        Result<Card> expected = makeResult( "board game id not found.", null);
        expected.setNotFound();
        Card arg = makeCard(1);
        arg.setBoardGameId(99);
        Result<Card> actual = service.add(arg);
        assertEquals(actual, expected);
    }

}