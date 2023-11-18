package learn.chronicles.domain;


import learn.chronicles.data.AppUserRepository;
import learn.chronicles.models.AppUser;
import learn.chronicles.models.Card;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AppUserService implements UserDetailsService {
    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    public AppUserService(AppUserRepository repository,
                          PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = repository.findByUsername(username);

        if (appUser == null || !appUser.isEnabled()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        return appUser;
    }

    public AppUser findById(int id) {
        return repository.findById(id);
    }

    public AppUser findByUsername(String username) {
        return repository.findByUsername(username);
    }


    public Result<AppUser> create(String username, String email, String password) {
        Result<AppUser> result = validate(username, email, password, "", "");

        try {
            if (loadUserByUsername(username) != null) {
                result.addErrorMessage("the provided username already exists");
            }
        } catch (UsernameNotFoundException e) {
            // good!
        }

        if (!result.isSuccess()) {
            return result;
        }

        password = encoder.encode(password);

        AppUser appUser = new AppUser(0, username, email, password, true, "", "", List.of("USER"));

        try {
            appUser = repository.create(appUser);
            result.setPayload(appUser);
        } catch (DuplicateKeyException e) {
            //this grabs the particular problem because it checks both username and email and makes it clear to the user
            String[] errors = Objects.requireNonNull(e.getMessage()).split("'");
            result.addErrorMessage(String.format("The provided username/email %s already exists", errors[1]));
        }

        return result;
    }

    /**
     * Updates user information.
     *
     * @param user the user to be updated.
     *             The password should have been entered by the user, not pulled from the db
     * @return the updated AppUser payloaded in a result  info or an error message in the result
     */
    public Result<AppUser> update(AppUser user) {
        //a bit of validation
        Result<AppUser> result = validate(user.getUsername(), user.getEmail(), user.getPassword(), user.getColor(), user.getAvatar());
        if (!result.isSuccess()) {
            return result;
        }

        //encode the password
        user.setPassword(encoder.encode(user.getPassword()));

        if (user != null && user.getAppUserId() <= 0) {
            result.addErrorMessage("User `id` should be set.");
        }

        if (result.isSuccess()) {
            boolean success = repository.update(user);
            if (!success) {
                //if the user couldn't be found then we would reach this scenario and should update the type of the result
                result.addErrorMessage(String.format("Could not update user %s.", user.getUsername()));
            }
        }

        return result;

    }

    public Result<?> updateColorAndAvatar(String color, String avatar, int id) {
        Result<?> result = new Result<>();
        //a bit of validation
        if (color == null || color.isBlank() || avatar == null || avatar.isBlank()) {
            result.addErrorMessage("Check to make sure nothing is null or blank");
        }

        if (findById(id) == null) {
            result.addErrorMessage("Could not find that user.");
        }

        if (!result.isSuccess()) {
            return result;
        }


        if (result.isSuccess()) {
            boolean success = repository.updateColorAndAvatar(color, avatar, id);
            if (!success) {
                //if the user couldn't be found then we would reach this scenario and should update the type of the result
                result.addErrorMessage("Failed to update");
            }
        }

        return result;

    }

    public Result<AppUser> delete(AppUser user) {
        Result<AppUser> result = new Result<>();
        boolean success = repository.delete(user);
        if (!success) {
            result.addErrorMessage(String.format("Could not delete user %s.", user.getUsername()));
        }
        return result;
    }

    private Result<AppUser> validate(String username, String email, String password, String color, String avatar) {
        Result<AppUser> result = new Result<>();
        if (username == null || username.isBlank()) {
            result.addErrorMessage("username is required");
            return result;
        }

        if (password == null || password.isBlank()) {
            result.addErrorMessage("password is required");
            return result;
        }

        if (email == null || email.isBlank()) {
            result.addErrorMessage("email is required");
            return result;
        }

        if (username.length() > 50) {
            result.addErrorMessage("username must be less than 50 characters");
        }

        if (email.length() > 100) {
            result.addErrorMessage("email must be less than 100 characters");
        }

        if (!EmailValidator.getInstance().isValid(email)) {
            result.addErrorMessage("email must be in a valid format");
        }


        if (!isValidPassword(password)) {
            result.addErrorMessage(
                    "password must be at least 8 character and contain a digit, a lower-case letter, and upper-case letter, and a non-digit/non-letter");
        }

        if (color != null && color.length() > 50) {
            result.addErrorMessage("color must be less than 50 characters");
        }

        if (avatar != null && avatar.length() > 250) {
            result.addErrorMessage("avatar must be less than 250 characters");
        }

        return result;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }

        int digits = 0;
        int lowerCaseLetters = 0;
        int upperCaseLetters = 0;
        int others = 0;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                digits++;
            } else if (Character.isLetter(c) && Character.isUpperCase(c)) {
                upperCaseLetters++;
            } else if (Character.isLetter(c) && !Character.isUpperCase(c)) {
                lowerCaseLetters++;
            } else {
                others++;
            }
        }

        return digits > 0 && lowerCaseLetters > 0 && upperCaseLetters > 0 && others > 0;
    }
}
