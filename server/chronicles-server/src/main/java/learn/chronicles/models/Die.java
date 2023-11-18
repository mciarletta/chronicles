package learn.chronicles.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Die {

    private int id;

    @Min(value = 1, message = "Board Game Id must be a positive integer")
    private int BoardGameId;

    @NotNull(message = "Category is required")
    @NotBlank(message = "Category cannot be blank")
    @Size(max = 50, message = "die category must be less than 50 characters in length")
    private String category;
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 75, message = "die name must be less than 75 characters in length")
    private String name;
    @NotNull(message = "color is required")
    @NotBlank(message = "color cannot be blank")
    @Size(max = 50, message = "die color must be less than 50 characters in length")
    private String color;

    @NotNull(message = "background is required")
    @NotBlank(message = "background cannot be blank")
    @Size(max = 50, message = "die background must be less than 50 characters in length")
    private String background;

    @Size(max = 5, message = "die sides must be less than 5 characters in length")
    private String side1;
    @Size(max = 5, message = "die sides must be less than 5 characters in length")
    private String side2;
    @Size(max = 5, message = "die sides must be less than 5 characters in length")
    private String side3;
    @Size(max = 5, message = "die sides must be less than 5 characters in length")
    private String side4;
    @Size(max = 5, message = "die sides must be less than 5 characters in length")
    private String side5;
    @Size(max = 5, message = "die sides must be less than 5 characters in length")
    private String side6;

    private boolean rolling;
    @Size(max = 5, message = "die sides must be less than 5 characters in length")
    private String winningSide;

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Die die = (Die) o;
        return id == die.id && BoardGameId == die.BoardGameId && rolling == die.rolling && Objects.equals(category, die.category) && Objects.equals(name, die.name) && Objects.equals(color, die.color) && Objects.equals(background, die.background) && Objects.equals(side1, die.side1) && Objects.equals(side2, die.side2) && Objects.equals(side3, die.side3) && Objects.equals(side4, die.side4) && Objects.equals(side5, die.side5) && Objects.equals(side6, die.side6) && Objects.equals(winningSide, die.winningSide);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, BoardGameId, category, name, color, background, side1, side2, side3, side4, side5, side6, rolling, winningSide);
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getSide1() {
        return side1;
    }

    public void setSide1(String side1) {
        this.side1 = side1;
    }

    public String getSide2() {
        return side2;
    }

    public void setSide2(String side2) {
        this.side2 = side2;
    }

    public String getSide3() {
        return side3;
    }

    public void setSide3(String side3) {
        this.side3 = side3;
    }

    public String getSide4() {
        return side4;
    }

    public void setSide4(String side4) {
        this.side4 = side4;
    }

    public String getSide5() {
        return side5;
    }

    public void setSide5(String side5) {
        this.side5 = side5;
    }

    public String getSide6() {
        return side6;
    }

    public void setSide6(String side6) {
        this.side6 = side6;
    }

    public boolean isRolling() {
        return rolling;
    }

    public void setRolling(boolean rolling) {
        this.rolling = rolling;
    }

    public String getWinningSide() {
        return winningSide;
    }

    public void setWinningSide(String winningSide) {
        this.winningSide = winningSide;
    }

    public Die(int id, int boardGameId, String category, String name, String color, String background, String side1, String side2, String side3, String side4, String side5, String side6, boolean rolling, String winningSide) {
        this.id = id;
        BoardGameId = boardGameId;
        this.category = category;
        this.name = name;
        this.color = color;
        this.background = background;
        this.side1 = side1;
        this.side2 = side2;
        this.side3 = side3;
        this.side4 = side4;
        this.side5 = side5;
        this.side6 = side6;
        this.rolling = rolling;
        this.winningSide = winningSide;
    }
}
