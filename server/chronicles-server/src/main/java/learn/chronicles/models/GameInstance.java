package learn.chronicles.models;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameInstance {

    private int id;

    @Min(value = 1, message = "Board Game Id must be a positive integer")
    private int BoardGameId;
    @Size(max = 10000000, message = "save state is too large")
    private String saveState;

    @Size(max = 60000, message = "log is too large")
    private String log;

    private List<String> usernames = new ArrayList<>();

    private String boardGameName;

    private String skin;

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getBoardGameName() {
        return boardGameName;
    }

    public void setBoardGameName(String boardGameName) {
        this.boardGameName = boardGameName;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameInstance that = (GameInstance) o;
        return id == that.id && BoardGameId == that.BoardGameId && Objects.equals(saveState, that.saveState) && Objects.equals(log, that.log);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, BoardGameId, saveState, log);
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

    public String getSaveState() {
        return saveState;
    }

    public void setSaveState(String saveState) {
        this.saveState = saveState;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }
}
