package learn.chronicles.models;

import javax.validation.constraints.*;
import java.util.Objects;

public class BoardGame {

    private int id;

    @NotBlank(message = "name is required")
    @NotNull(message = "name is required")
    @Size(max = 100, message = "board game name must be less than 100 characters in length")
    private String name;

    @Min(value = 600, message = "height must be greater than 600")
    @Max(value = 2048, message = "height must be less than 2048")
    private int height;
    @Min(value = 1, message = "you must have at least 1 board slot")
    private int boardSlots;

    @Min(value = 1, message = "you must have at least 1 place per board")
    private int placesPerBoard;

    @Size(max = 250, message = "Box skin must be less than 250 characters in length")
    private String skin;



    public BoardGame(int id, String name, int height, int boardSlots, int placesPerBoard, String skin) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.boardSlots = boardSlots;
        this.placesPerBoard = placesPerBoard;
        this.skin = skin;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardGame boardGame = (BoardGame) o;
        return id == boardGame.id && height == boardGame.height && boardSlots == boardGame.boardSlots && placesPerBoard == boardGame.placesPerBoard && Objects.equals(name, boardGame.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, height, boardSlots, placesPerBoard);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBoardSlots() {
        return boardSlots;
    }

    public void setBoardSlots(int boardSlots) {
        this.boardSlots = boardSlots;
    }

    public int getPlacesPerBoard() {
        return placesPerBoard;
    }

    public void setPlacesPerBoard(int placesPerBoard) {
        this.placesPerBoard = placesPerBoard;
    }
}
