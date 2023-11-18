package learn.chronicles.models;

import learn.chronicles.App;

import java.util.List;
import java.util.Map;

public class Room {

    private int id;

    private Map<String, UserListResponse> users;

    public Room(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Map<String, UserListResponse> getUsers() {
        return users;
    }

    public void setUsers(Map<String, UserListResponse> users) {
        this.users = users;
    }

}
