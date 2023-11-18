package learn.chronicles.models;

import javax.validation.constraints.*;
import java.util.Objects;

public class Figure {

    private int id;

    @Min(value = 1, message = "Board Game Id must be a positive integer")
    private int BoardGameId;

    @NotNull(message = "Category is required")
    @NotBlank(message = "Category cannot be blank")
    @Size(max = 50, message = "figure category must be less than 50 characters in length")
    private String category;
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 75, message = "figure name must be less than 75 characters in length")
    private String name;

    @Size(max = 250, message = "figure skin must be less than 250 characters in length")
    private String skin;

    @NotNull(message = "color is required")
    @NotBlank(message = "color cannot be blank")
    @Size(max = 50, message = "figure color must be less than 50 characters in length")
    private String color;

    @DecimalMin(value = "0.1")
    @DecimalMax(value = "9.9")
    private double scale;

    public Figure(int id, int boardGameId, String category, String name, String skin, String color, double scale) {
        this.id = id;
        BoardGameId = boardGameId;
        this.category = category;
        this.name = name;
        this.skin = skin;
        this.color = color;
        this.scale = scale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Figure figure = (Figure) o;
        return id == figure.id && BoardGameId == figure.BoardGameId && Objects.equals(category, figure.category) && Objects.equals(name, figure.name) && Objects.equals(skin, figure.skin) && Objects.equals(color, figure.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, BoardGameId, category, name, skin, color);
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }
}
