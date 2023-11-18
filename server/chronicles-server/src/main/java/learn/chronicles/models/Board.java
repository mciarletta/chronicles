package learn.chronicles.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Board {

    private int id;

    @Min(value = 1, message = "Board Game Id must be a positive integer")
    private int BoardGameId;

    @NotNull(message = "Category is required")
    @NotBlank(message = "Category cannot be blank")
    @Size(max = 50, message = "board category must be less than 50 characters in length")
    private String category;
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 75, message = "board name must be less than 75 characters in length")
    private String name;

    @Size(max = 250, message = "board skin must be less than 250 characters in length")
    private String skin;


    public Board(int boardId, int boardGameId, String category, String name, String skin) {
        this.id = boardId;
        BoardGameId = boardGameId;
        this.category = category;
        this.name = name;
        this.skin = skin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return id == board.id && BoardGameId == board.BoardGameId && Objects.equals(category, board.category) && Objects.equals(name, board.name) && Objects.equals(skin, board.skin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, BoardGameId, category, name, skin);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBoardGameId() {
        return BoardGameId;
    }

    public void setBoardGameId(int boardGameId) {
        BoardGameId = boardGameId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }
}
