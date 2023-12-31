package learn.chronicles.data;

import learn.chronicles.models.AppUser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;

@Repository
public class AppUserJdbcTemplateRepository implements AppUserRepository{
    private final JdbcTemplate jdbcTemplate;

    public AppUserJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    @Override
    public AppUser findByUsername (String username){
        List<String> roles = getRolesByUsername(username);

        final String sql = "select app_user_id, username, email, password_hash, enabled, color, avatar "
                + "from app_user "
                + "where username = ?;";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), username)
                .stream()
                .findFirst().orElse(null);

    }

    @Override
    public AppUser findById(int id) {
        List<String> roles = getRolesById(id);

        final String sql = "select app_user_id, username, email, password_hash, enabled, color, avatar "
                + "from app_user "
                + "where app_user_id = ?;";

        return jdbcTemplate.query(sql, new AppUserMapper(roles), id)
                .stream()
                .findFirst().orElse(null);
    }

    @Transactional
    @Override
    public AppUser create(AppUser user) {

        final String sql = "insert into app_user (username, email, password_hash) values (?, ?, ?);";

        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        user.setAppUserId(keyHolder.getKey().intValue());

        updateRoles(user);

        return user;
    }

    @Transactional
    @Override
    public boolean update(AppUser user) {

        final String sql = "update app_user set "
                + "username = ?, "
                + "email = ?, "
                + "enabled = ?, "
                + "password_hash = ?, "
                + "color = ?, "
                + "avatar = ? "
                + "where app_user_id = ?";

        jdbcTemplate.update(sql,
                user.getUsername(), user.getEmail(), user.isEnabled(),user.getPassword(), user.getColor(), user.getAvatar(), user.getAppUserId());

        updateRoles(user);

        return true;
    }

    @Override
    public boolean updateColorAndAvatar(String color, String avatar, int id) {
        final String sql = "update app_user set "
                + "color = ?, "
                + "avatar = ? "
                + "where app_user_id = ?";

       return jdbcTemplate.update(sql,
                color, avatar, id) > 0;
    }

    @Transactional
    @Override
    public boolean delete(AppUser user){
        //set transactional to make sure both the roles and user are deleted for a transaction to occur
        deleteRoles(user);

        jdbcTemplate.update("delete from app_user_game_instance where app_user_id = ?;", user.getAppUserId());


        jdbcTemplate.update("delete from app_user where app_user_id = ?;", user.getAppUserId());

        //should only return true if it is not rolled back


        return true;
    }

    public void deleteRoles(AppUser user){
        jdbcTemplate.update("delete from app_user_role where app_user_id = ?;", user.getAppUserId());
    }

    private void updateRoles(AppUser user) {
        // delete all roles, then re-add
        jdbcTemplate.update("delete from app_user_role where app_user_id = ?;", user.getAppUserId());

        Collection<GrantedAuthority> authorities = user.getAuthorities();

        if (authorities == null) {
            return;
        }

        for (GrantedAuthority role : authorities) {
            String sql = "insert into app_user_role (app_user_id, app_role_id) "
                    + "select ?, app_role_id from app_role where `name` = ?;";
            jdbcTemplate.update(sql, user.getAppUserId(), role.getAuthority());
        }
    }


    private List<String> getRolesByUsername(String username) {
        final String sql = "select r.name "
                + "from app_user_role ur "
                + "inner join app_role r on ur.app_role_id = r.app_role_id "
                + "inner join app_user au on ur.app_user_id = au.app_user_id "
                + "where au.username = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), username);
    }

    private List<String> getRolesById(int id) {
        final String sql = "select r.name "
                + "from app_user_role ur "
                + "inner join app_role r on ur.app_role_id = r.app_role_id "
                + "inner join app_user au on ur.app_user_id = au.app_user_id "
                + "where au.app_user_id = ?";
        return jdbcTemplate.query(sql, (rs, rowId) -> rs.getString("name"), id);
    }
}
