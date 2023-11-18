package learn.chronicles.models;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AppUser implements UserDetails {

    private int appUserId;

    @NotBlank(message = "Username is required")
    @NotNull(message = "Username is required")
    private String username;

    @NotBlank(message = "email is required")
    @NotNull(message = "email is required")
    private String email;
    @NotBlank(message = "password is required")
    @NotNull(message = "password is required")
    private String password;
    private boolean enabled;

    private String color;

    private String avatar;
    private Collection<GrantedAuthority> authorities;

    public AppUser() {
    }

    public AppUser(int appUserId, String username, String email, String password, boolean enabled, String color, String avatar, List<String> roles) {
        this.appUserId = appUserId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = enabled;
        this.color = color;
        this.avatar = avatar;
        this.authorities = convertRolesToAuthorities(roles);
    }

    private static Collection<GrantedAuthority> convertRolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private void setRoles(List<String> roles){
        this.authorities = convertRolesToAuthorities(roles);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return new ArrayList<>(authorities);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public int getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(int appUserId) {
        this.appUserId = appUserId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return appUserId == appUser.appUserId && enabled == appUser.enabled && Objects.equals(username, appUser.username) && Objects.equals(email, appUser.email) && Objects.equals(password, appUser.password) && Objects.equals(authorities, appUser.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appUserId, username, email, password, enabled, authorities);
    }
}
