package learn.chronicles.models;

import java.util.List;
import java.util.Objects;

public class Box {

    private List<Board> boards;

    private List<Card> cards;

    private List<Die> dice;

    private List<Figure> figures;

    public Box(List<Board> boards, List<Card> cards, List<Die> dice, List<Figure> figures) {
        this.boards = boards;
        this.cards = cards;
        this.dice = dice;
        this.figures = figures;
    }

    public List<Board> getBoards() {
        return boards;
    }

    public void setBoards(List<Board> boards) {
        this.boards = boards;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public List<Die> getDice() {
        return dice;
    }

    public void setDice(List<Die> dice) {
        this.dice = dice;
    }

    public List<Figure> getFigures() {
        return figures;
    }

    public void setFigures(List<Figure> figures) {
        this.figures = figures;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Box box = (Box) o;
        return Objects.equals(boards, box.boards) && Objects.equals(cards, box.cards) && Objects.equals(dice, box.dice) && Objects.equals(figures, box.figures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boards, cards, dice, figures);
    }
}
