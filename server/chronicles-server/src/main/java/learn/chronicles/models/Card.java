package learn.chronicles.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class Card {

    private int id;

    @Min(value = 1, message = "Board Game Id must be a positive integer")
    private int BoardGameId;

    @NotNull(message = "Category is required")
    @NotBlank(message = "Category cannot be blank")
    @Size(max = 50, message = "card category must be less than 50 characters in length")
    private String category;
    @NotNull(message = "Name is required")
    @NotBlank(message = "Name cannot be blank")
    @Size(max = 75, message = "card name must be less than 75 characters in length")
    private String name;
    @NotNull(message = "type is required")
    @NotBlank(message = "type cannot be blank")
    @Size(max = 75, message = "card type must be less than 75 characters in length")
    private String type;

    @Size(max = 250, message = "card front must be less than 250 characters in length")
    private String cardFront;
    @Size(max = 250, message = "card back must be less than 250 characters in length")
    private String cardBack;

    private boolean show;

    @NotNull(message = "text is required")
    @NotBlank(message = "text cannot be blank")
    @Size(max = 500, message = "card text must be less than 500 characters in length")
    private String text;

    private int inHand;

    @Size(max = 500, message = "card variables must be less than 500 characters in length")
    private String variables;

    public Card(int id, int boardGameId, String category, String name, String type, String cardFront, String cardBack, boolean show, String text, int inHand, String variables) {
        this.id = id;
        BoardGameId = boardGameId;
        this.category = category;
        this.name = name;
        this.type = type;
        this.cardFront = cardFront;
        this.cardBack = cardBack;
        this.show = show;
        this.text = text;
        this.inHand = inHand;
        this.variables = variables;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardFront() {
        return cardFront;
    }

    public void setCardFront(String cardFront) {
        this.cardFront = cardFront;
    }

    public String getCardBack() {
        return cardBack;
    }

    public void setCardBack(String cardBack) {
        this.cardBack = cardBack;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getInHand() {
        return inHand;
    }

    public void setInHand(int inHand) {
        this.inHand = inHand;
    }

    public String getVariables() {
        return variables;
    }

    public void setVariables(String variables) {
        this.variables = variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id == card.id && BoardGameId == card.BoardGameId && show == card.show && inHand == card.inHand && Objects.equals(category, card.category) && Objects.equals(name, card.name) && Objects.equals(type, card.type) && Objects.equals(cardFront, card.cardFront) && Objects.equals(cardBack, card.cardBack) && Objects.equals(text, card.text) && Objects.equals(variables, card.variables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, BoardGameId, category, name, type, cardFront, cardBack, show, text, inHand, variables);
    }
}
