package learn.chronicles;


import learn.chronicles.data.FigureJdbcTemplateRepository;
import learn.chronicles.domain.Result;
import learn.chronicles.models.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestHelper {

    public static final int VALID_ID = 5;


    public static AppUser makeAppUser(int id) {
        return new AppUser(
                id,
                String.format("username_%s", id),
                String.format("appuser%s@app.com", id),
                String.format("password_hash_%s", id),
                true,
                String.format("color_%s", id),
                String.format("avatar_%s", id),
                List.of(String.format("TEST_ROLE_%s", id))
                );
    }

    public static BoardGame makeBoardGame(int id) {
        return new BoardGame(
                id,
                String.format("name_%s", id),
                (id + 800),
                id,
                id,
                null);
    }

    public static Board makeBoard(int id) {
        return new Board(
                id,
                1,
                String.format("category_%s", id),
                String.format("name_%s", id),
                String.format("skin_%s", id));
    }

    public static Card makeCard(int id) {
        return new Card(
                id,
                1,
                String.format("category_%s", id),
                String.format("name_%s", id),
                String.format("type_%s", id),
                String.format("front_%s", id),
                String.format("back_%s", id),
                false,
                String.format("text_%s", id),
                0,
                String.format("variables_%s", id));
    }

    public static Die makeDie(int id) {
        return new Die(
                id,
                1,
                String.format("category_%s", id),
                String.format("name_%s", id),
                String.format("color_%s", id),
                String.format("background_%s", id),
                null,
                null,
                null,
                null,
                null,
                null,
                false,
                null);
    }

    public static Figure makeFigure(int id) {
        return new Figure(
                id,
                1,
                String.format("category_%s", id),
                String.format("name_%s", id),
                String.format("skin_%s", id),
                "color",
                1.0);
    }

    public static GameInstance makeGameInstance(int id) {
        GameInstance gameInstance = new GameInstance();
        gameInstance.setId(id);
        gameInstance.setBoardGameId(1);
        gameInstance.setSaveState(String.format("save_state_%s", id));
        gameInstance.setLog(String.format("log_%s", id));
        gameInstance.setBoardGameName(String.format("board_game_%s", id));
        gameInstance.setSkin(null);
        return gameInstance;

    }

    public static <T> Result<T> makeResult(String message, T payload) {
        Result<T> result = new Result<>();
        if (message != null) {
            result.addErrorMessage(message);
        }
        result.setPayload(payload);
        return result;
    }
}
