package learn.chronicles.models;

import java.util.Objects;

public class AppUserGameInstance {

    private int gameInstanceId;

    private int appUserId;

    public AppUserGameInstance(int gameInstanceId, int appUserId) {
        this.gameInstanceId = gameInstanceId;
        this.appUserId = appUserId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUserGameInstance that = (AppUserGameInstance) o;
        return gameInstanceId == that.gameInstanceId && appUserId == that.appUserId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameInstanceId, appUserId);
    }

    public int getGameInstanceId() {
        return gameInstanceId;
    }

    public void setGameInstanceId(int gameInstanceId) {
        this.gameInstanceId = gameInstanceId;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }
}
