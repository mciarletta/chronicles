package learn.chronicles.models;

import java.util.Objects;

public class UsernameGameTitle {

    private String username;

    private String boardGameName;

    public UsernameGameTitle(String username, String boardGameName) {
        this.username = username;
        this.boardGameName = boardGameName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBoardGameName() {
        return boardGameName;
    }

    public void setBoardGameName(String boardGameName) {
        this.boardGameName = boardGameName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsernameGameTitle that = (UsernameGameTitle) o;
        return Objects.equals(username, that.username) && Objects.equals(boardGameName, that.boardGameName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, boardGameName);
    }
}
