package learn.chronicles.domain;

import learn.chronicles.data.FigureRepository;
import learn.chronicles.models.Die;
import learn.chronicles.models.Figure;
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
class FigureServiceTest {

    @MockBean
    FigureRepository repository;

    @Autowired
    FigureService service;

    @Test
    void findById() {
        Figure expected = makeFigure(1);

        when(repository.findById(anyInt())).thenReturn(makeFigure(1));

        Figure actual = service.findById(1);
        assertEquals(expected, actual);
    }

    @Test
    void findByIdBG() {
        List<Figure> expected = List.of(makeFigure(1));

        when(repository.findByBoardGameId(anyInt())).thenReturn(List.of(makeFigure(1)));

        List<Figure> actual = service.findByBoardGameId(1);
        assertEquals(expected, actual);
    }

    @Test
    void NotFindByIdBG() {
        List<Figure> expected = null;

        when(repository.findByBoardGameId(anyInt())).thenReturn(null);

        List<Figure> actual = service.findByBoardGameId(99);
        assertEquals(expected, actual);
    }

    @Test
    void add() {
        Result<Figure> expected = makeResult(null, makeFigure(1));
        Figure arg = makeFigure(1);
        arg.setId(0);

        when(repository.create(any())).thenReturn(makeFigure(1));

        Result<Figure> actual = service.add(arg);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Result<Figure> expected = makeResult(null, null);
        Figure arg = makeFigure(1);

        when(repository.update(any())).thenReturn(true);

        Result<Figure> actual = service.update(arg);
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
        Result<Figure> expected = makeResult("figure cannot be null.", null);
        Result<Figure> actual = service.add(null);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotAddWithId() {
        Result<Figure> expected = makeResult( "figure id cannot be set for `create` operation.", null);
        Result<Figure> actual = service.add(makeFigure(1));
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankName() {
        Result<Figure> expected = makeResult( "Name cannot be blank", null);
        Figure arg = makeFigure(1);
        arg.setName("");
        Result<Figure> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullName() {
        Result<Figure> expected = makeResult( "Name is required", null);
        Figure arg = makeFigure(2);
        arg.setName(null);
        Result<Figure> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("Name is required"));
    }

    @Test
    void shouldNotAddWithLongName() {
        Result<Figure> expected = makeResult( "figure name must be less than 75 characters in length", null);
        Figure arg = makeFigure(1);
        arg.setName("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Figure> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongCategory() {
        Result<Figure> expected = makeResult( "figure category must be less than 50 characters in length", null);
        Figure arg = makeFigure(1);
        arg.setCategory("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Figure> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithLongSkin() {
        Result<Figure> expected = makeResult( "figure skin must be less than 250 characters in length", null);
        Figure arg = makeFigure(1);
        arg.setSkin("1234567123456789089012345678901234567890123456789012345678901234567890123456789011234567123456789089012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789123456712345678908901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678923456789012345678901234567890");
        Result<Figure> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddTooLongColor() {
        Result<Figure> expected = makeResult("figure color must be less than 50 characters in length", null);
        Figure figure = makeFigure(3);
        figure.setColor("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");


        Result<Figure> actual = service.add(figure);

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithBlankColor() {
        Result<Figure> expected = makeResult( "color cannot be blank", null);
        Figure arg = makeFigure(1);
        arg.setColor("");
        Result<Figure> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotAddWithNullColor() {
        Result<Figure> expected = makeResult( "color is required", null);
        Figure arg = makeFigure(13);
        arg.setColor(null);
        Result<Figure> actual = service.add(arg);
        assertTrue(actual.getErrorMessages().contains("color is required"));
    }

    //------------non-updates---------------//

    @Test
    void shouldNotUpdateNull() {
        Result<Figure> expected = makeResult("figure cannot be null.", null);
        Result<Figure> actual = service.update(null);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotUpdateWithBadId() {
        Result<Figure> expected = makeResult( "could not update figure id 99", null);
        Figure arg = makeFigure(1);
        arg.setId(99);
        Result<Figure> actual = service.update(arg);
        assertEquals(expected, actual);
    }


    @Test
    void shouldNotUpdateWithBlankName() {
        Result<Figure> expected = makeResult( "Name cannot be blank", null);
        Figure arg = makeFigure(1);
        arg.setName("");
        Result<Figure> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateWithNullName() {
        Result<Figure> expected = makeResult( "Name is required", null);
        Figure arg = makeFigure(20);
        arg.setName(null);
        Result<Figure> actual = service.update(arg);
        assertTrue(actual.getErrorMessages().contains("Name is required"));
    }

    @Test
    void shouldNotUpdateWithLongName() {
        Result<Figure> expected = makeResult( "figure name must be less than 75 characters in length", null);
        Figure arg = makeFigure(1);
        arg.setName("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Figure> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateWithLongCategory() {
        Result<Figure> expected = makeResult( "figure category must be less than 50 characters in length", null);
        Figure arg = makeFigure(1);
        arg.setCategory("12345671234567890890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");
        Result<Figure> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateWithLongSkin() {
        Result<Figure> expected = makeResult( "figure skin must be less than 250 characters in length", null);
        Figure arg = makeFigure(1);
        arg.setSkin("1234567123456789089012345678901234567890123456789012345678901234567890123456789011234567123456789089012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789123456712345678908901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678923456789012345678901234567890");
        Result<Figure> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateTooLongColor() {
        Result<Figure> expected = makeResult("figure color must be less than 50 characters in length", null);
        Figure figure = makeFigure(3);
        figure.setColor("12345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");


        Result<Figure> actual = service.update(figure);

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateWithBlankColor() {
        Result<Figure> expected = makeResult( "color cannot be blank", null);
        Figure arg = makeFigure(14);
        arg.setColor("");
        Result<Figure> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotUpdateWithNullColor() {
        Result<Figure> expected = makeResult( "color is required", null);
        Figure arg = makeFigure(1);
        arg.setColor(null);
        Result<Figure> actual = service.update(arg);
        assertTrue(actual.getErrorMessages().contains("color is required"));
    }

    @Test
    void shouldNotAddWithBadBoardGameId() {
        Result<Figure> expected = makeResult( "board game id not found.", null);
        expected.setNotFound();
        Figure arg = makeFigure(1);
        arg.setBoardGameId(99);
        Result<Figure> actual = service.add(arg);
        assertEquals(actual, expected);
    }

}