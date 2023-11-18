package learn.chronicles.domain;

import learn.chronicles.data.BoardGameRepository;
import learn.chronicles.data.BoardRepository;
import learn.chronicles.models.Board;
import learn.chronicles.models.BoardGame;
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
class BoardServiceTest {

    @MockBean
    BoardRepository repository;

    @Autowired
    BoardService service;

    @Test
    void findByIdBG() {
        List<Board> expected = List.of(makeBoard(1));

        when(repository.findByBoardGameId(anyInt())).thenReturn(List.of(makeBoard(1)));

        List<Board> actual = service.findByBoardGameId(1);
        assertEquals(expected, actual);
    }

    @Test
    void NotFindByIdBG() {
        List<Board> expected = null;

        when(repository.findByBoardGameId(anyInt())).thenReturn(null);

        List<Board> actual = service.findByBoardGameId(99);
        assertEquals(expected, actual);
    }


    @Test
    void findById() {
        Board expected = makeBoard(1);

        when(repository.findById(anyInt())).thenReturn(makeBoard(1));

        Board actual = service.findById(1);
        assertEquals(expected, actual);
    }



    @Test
    void add() {
        Result<Board> expected = makeResult(null, makeBoard(1));
        Board arg = makeBoard(1);
        arg.setId(0);

        when(repository.create(any())).thenReturn(makeBoard(1));

        Result<Board> actual = service.add(arg);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Result<Board> expected = makeResult(null, null);
        Board arg = makeBoard(1);

        when(repository.update(any())).thenReturn(true);

        Result<Board> actual = service.update(arg);
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
        Result<Board> expected = makeResult("board cannot be null.", null);
        Result<Board> actual = service.add(null);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotAddWithId() {
        Result<Board> expected = makeResult( "board id cannot be set for `create` operation.", null);
        Result<Board> actual = service.add(makeBoard(1));
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankName() {
        Result<Board> expected = makeResult( "Name cannot be blank", null);
        Board arg = makeBoard(1);
        arg.setName("");
        Result<Board> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullName() {
        Result<Board> expected = makeResult( "Name is required", null);
        Board arg = makeBoard(15);
        arg.setName(null);
        Result<Board> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("Name is required"));
    }

    @Test
    void shouldNotAddWithLongName() {
        Result<Board> expected = makeResult( "board name must be less than 75 characters in length", null);
        Board arg = makeBoard(1);
        arg.setName("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Board> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongCategory() {
        Result<Board> expected = makeResult( "board category must be less than 50 characters in length", null);
        Board arg = makeBoard(1);
        arg.setCategory("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Board> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongSkin() {
        Result<Board> expected = makeResult( "board skin must be less than 250 characters in length", null);
        Board arg = makeBoard(1);
        arg.setSkin("1234567123456789089012345678901234567890123456789012345678901234567890123456789011234567123456789089012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789123456712345678908901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678923456789012345678901234567890");
        Result<Board> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBadBoardGameId() {
        Result<Board> expected = makeResult( "board game id not found.", null);
        expected.setNotFound();
        Board arg = makeBoard(1);
        arg.setBoardGameId(99);
        Result<Board> actual = service.add(arg);
        assertEquals(actual, expected);
    }

    //------------non-updates---------------//

    @Test
    void shouldNotUpdateNull() {
        Result<Board> expected = makeResult("board cannot be null.", null);
        Result<Board> actual = service.update(null);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotUpdateWithBadId() {
        Result<Board> expected = makeResult( "could not update board id 99", null);
        Board arg = makeBoard(1);
        arg.setId(99);
        Result<Board> actual = service.update(arg);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotUpdateWithBlankName() {
        Result<Board> expected = makeResult( "Name cannot be blank", null);
        Board arg = makeBoard(1);
        arg.setName("");
        Result<Board> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateWithNullName() {
        Result<Board> expected = makeResult( "Name is required", null);
        Board arg = makeBoard(119);
        arg.setName(null);
        Result<Board> actual = service.update(arg);
        assertTrue(actual.getErrorMessages().contains("Name is required"));
    }

    @Test
    void shouldNotUpdateWithLongName() {
        Result<Board> expected = makeResult( "board name must be less than 75 characters in length", null);
        Board arg = makeBoard(1);
        arg.setName("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Board> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateWithLongCategory() {
        Result<Board> expected = makeResult( "board category must be less than 50 characters in length", null);
        Board arg = makeBoard(1);
        arg.setCategory("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Board> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateWithLongSkin() {
        Result<Board> expected = makeResult( "board skin must be less than 250 characters in length", null);
        Board arg = makeBoard(1);
        arg.setSkin("1234567123456789089012345678901234567890123456789012345678901234567890123456789011234567123456789089012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789123456712345678908901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678923456789012345678901234567890");
        Result<Board> actual = service.update(arg);
        assertEquals(expected, actual);
    }

}