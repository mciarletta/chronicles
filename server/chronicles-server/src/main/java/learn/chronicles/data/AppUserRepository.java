package learn.chronicles.data;


import learn.chronicles.models.AppUser;

public interface AppUserRepository {

    AppUser findByUsername (String username);

    AppUser findById (int id);

    AppUser create(AppUser user);

    boolean update(AppUser user);

    boolean updateColorAndAvatar(String color, String avatar, int id);

    boolean delete(AppUser user);
}
