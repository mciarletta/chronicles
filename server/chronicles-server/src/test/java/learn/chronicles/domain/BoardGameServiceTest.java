package learn.chronicles.domain;

import learn.chronicles.data.BoardGameRepository;
import learn.chronicles.models.BoardGame;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static learn.chronicles.TestHelper.makeBoardGame;
import static learn.chronicles.TestHelper.makeResult;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class BoardGameServiceTest {

    @MockBean
    BoardGameRepository repository;

    @Autowired
    BoardGameService service;


    @Test
    void findById() {
        BoardGame expected = makeBoardGame(1);

        when(repository.findById(anyInt())).thenReturn(makeBoardGame(1));

        BoardGame actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void add() {
        Result<BoardGame> expected = makeResult(null, makeBoardGame(1));
        BoardGame arg = makeBoardGame(1);
        arg.setId(0);

        when(repository.create(any())).thenReturn(makeBoardGame(1));

        Result<BoardGame> actual = service.add(arg);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Result<BoardGame> expected = makeResult(null, null);
        BoardGame arg = makeBoardGame(1);

        when(repository.update(any())).thenReturn(true);

        Result<BoardGame> actual = service.update(arg);
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
    void shouldNotAddNullGame() {
        Result<BoardGame> expected = makeResult("board game cannot be null.", null);
        Result<BoardGame> actual = service.add(null);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotAddGameWithId() {
        Result<BoardGame> expected = makeResult( "board game id cannot be set for `create` operation.", null);
        Result<BoardGame> actual = service.add(makeBoardGame(1));
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddGameWithNonPerfectSquareSlots() {
        Result<BoardGame> expected = makeResult( "board game slots must be a perfect square.", null);
        Result<BoardGame> actual = service.add(makeBoardGame(2));
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddGameWithNonPerfectSquarePlaces() {
        Result<BoardGame> expected = makeResult( "board game places per board must be a perfect square.", null);
        BoardGame arg = makeBoardGame(1);
        arg.setPlacesPerBoard(2);
        Result<BoardGame> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddGameWithBlankName() {
        Result<BoardGame> expected = makeResult( "name is required", null);
        BoardGame arg = makeBoardGame(1);
        arg.setName("");
        Result<BoardGame> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("name is required"));
    }

    @Test
    void shouldNotAddGameWithNullName() {
        Result<BoardGame> expected = makeResult( "name is required", null);
        BoardGame arg = makeBoardGame(1);
        arg.setName(null);
        Result<BoardGame> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("name is required"));
    }

    @Test
    void shouldNotAddGameWithLongName() {
        Result<BoardGame> expected = makeResult( "board game name must be less than 100 characters in length", null);
        BoardGame arg = makeBoardGame(1);
        arg.setName("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<BoardGame> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddGameWithHeightLessThan600() {
        Result<BoardGame> expected = makeResult( "height must be greater than 600", null);
        BoardGame arg = makeBoardGame(1);
        arg.setHeight(599);
        Result<BoardGame> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddGameWithHeightMoreThan2048() {
        Result<BoardGame> expected = makeResult( "height must be less than 2048", null);
        BoardGame arg = makeBoardGame(1);
        arg.setHeight(2049);
        Result<BoardGame> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddGameWithLessThan1BoardSlot() {
        Result<BoardGame> expected = makeResult( "you must have at least 1 board slot", null);
        BoardGame arg = makeBoardGame(1);
        arg.setBoardSlots(0);
        Result<BoardGame> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddGameWithLessThan1Place() {
        Result<BoardGame> expected = makeResult( "you must have at least 1 place per board", null);
        BoardGame arg = makeBoardGame(1);
        arg.setPlacesPerBoard(-1);
        Result<BoardGame> actual = service.add(arg);
        assertEquals(expected, actual);
    }


    //------------non-updates---------------//

    @Test
    void shouldNotUpdateNullGame() {
        Result<BoardGame> expected = makeResult("board game cannot be null.", null);
        Result<BoardGame> actual = service.update(null);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotUpdateGameWithBadId() {
        Result<BoardGame> expected = makeResult( "could not update board game id 99", null);
        BoardGame arg = makeBoardGame(1);
        arg.setId(99);
        Result<BoardGame> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateGameWithNonPerfectSquareSlots() {
        Result<BoardGame> expected = makeResult( "board game slots must be a perfect square.", null);
        Result<BoardGame> actual = service.update(makeBoardGame(2));
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateGameWithNonPerfectSquarePlaces() {
        Result<BoardGame> expected = makeResult( "board game places per board must be a perfect square.", null);
        BoardGame arg = makeBoardGame(1);
        arg.setPlacesPerBoard(2);
        Result<BoardGame> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateGameWithBlankName() {
        Result<BoardGame> expected = makeResult( "name is required", null);
        BoardGame arg = makeBoardGame(1);
        arg.setName("");
        Result<BoardGame> actual = service.update(arg);
        assertTrue(actual.getErrorMessages().contains("name is required"));
    }

    @Test
    void shouldNotUpdateGameWithNullName() {
        Result<BoardGame> expected = makeResult( "name is required", null);
        BoardGame arg = makeBoardGame(1);
        arg.setName(null);
        Result<BoardGame> actual = service.update(arg);
        assertTrue(actual.getErrorMessages().contains("name is required"));
    }

    @Test
    void shouldNotUpdateGameWithLongName() {
        Result<BoardGame> expected = makeResult( "board game name must be less than 100 characters in length", null);
        BoardGame arg = makeBoardGame(1);
        arg.setName("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<BoardGame> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateGameWithHeightLessThan600() {
        Result<BoardGame> expected = makeResult( "height must be greater than 600", null);
        BoardGame arg = makeBoardGame(1);
        arg.setHeight(599);
        Result<BoardGame> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateGameWithHeightMoreThan2048() {
        Result<BoardGame> expected = makeResult( "height must be less than 2048", null);
        BoardGame arg = makeBoardGame(1);
        arg.setHeight(2049);
        Result<BoardGame> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateGameWithLessThan1BoardSlot() {
        Result<BoardGame> expected = makeResult( "you must have at least 1 board slot", null);
        BoardGame arg = makeBoardGame(1);
        arg.setBoardSlots(0);
        Result<BoardGame> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateGameWithLessThan1Place() {
        Result<BoardGame> expected = makeResult( "you must have at least 1 place per board", null);
        BoardGame arg = makeBoardGame(1);
        arg.setPlacesPerBoard(-1);
        Result<BoardGame> actual = service.update(arg);
        assertEquals(expected, actual);
    }
}