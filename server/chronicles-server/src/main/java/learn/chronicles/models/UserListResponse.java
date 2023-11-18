package learn.chronicles.models;

import java.util.Map;
import java.util.Objects;

public class UserListResponse {
  private String name;

  private String color;

  private String avatar;

  private int app_user_id;

    public UserListResponse() {
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

    public int getApp_user_id() {
        return app_user_id;
    }

    public void setApp_user_id(int app_user_id) {
        this.app_user_id = app_user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserListResponse that = (UserListResponse) o;
        return app_user_id == that.app_user_id && Objects.equals(name, that.name) && Objects.equals(color, that.color) && Objects.equals(avatar, that.avatar);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, color, avatar, app_user_id);
    }
}

