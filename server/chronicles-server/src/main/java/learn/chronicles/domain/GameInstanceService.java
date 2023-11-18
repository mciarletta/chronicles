package learn.chronicles.domain;

import learn.chronicles.data.AppUserRepository;
import learn.chronicles.data.GameInstanceRepository;
import learn.chronicles.models.AppUserGameInstance;
import learn.chronicles.models.GameInstance;
import learn.chronicles.models.UsernameGameTitle;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class GameInstanceService {

    private final GameInstanceRepository repository;

    private final AppUserRepository appUserRepository;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public GameInstanceService(GameInstanceRepository repository, AppUserRepository appUserRepository) {
        this.repository = repository;
        this.appUserRepository = appUserRepository;
    }


//----------------------Reads------------------------------------//

    public GameInstance findById(int id) {
        return repository.findById(id);
    }

    public List<GameInstance> findByUserId(int id) {
        return repository.findByUserId(id);
    }

    public List<GameInstance> findByBoardGameId(int id) {
        return repository.findByBoardGameId(id);
    }

    public List<UsernameGameTitle> findUsernames(int id) {
        return repository.findUsersInAGameInstance(id);
    }


//----------------------Create---------------------------------//

    public Result<GameInstance> add(GameInstance gameInstance) {

        Result<GameInstance> result = validate(gameInstance);
        if (!result.isSuccess()) {
            return result;
        }

        if (gameInstance.getId() != 0) {
            result.addErrorMessage("gameInstance id cannot be set for `create` operation.");
            return result;
        }

        if (result.isSuccess()) {
            GameInstance bg = repository.create(gameInstance);
            result.setPayload(bg);
        }

        return result;
    }

//----------------------Update----------------------------//

    public Result<GameInstance> update(GameInstance gameInstance) {

        Result<GameInstance> result = validate(gameInstance);
        if (!result.isSuccess()) {
            return result;
        }

        if (gameInstance.getId() <= 0) {
            result.addErrorMessage("gameInstance id is required");
            return result;
        }

        boolean success = repository.update(gameInstance);
        if (!success) {
            result.addErrorMessage("could not update gameInstance id " + gameInstance.getId());
        }

        return result;
    }

    public Result<AppUserGameInstance> addUser(AppUserGameInstance appUserGameInstance){
        Result<AppUserGameInstance> result = new Result<>();

        //some validation right here
        if (appUserGameInstance == null){
            result.addErrorMessage("appUserGameInstance is required");
            return result;
        }

        //check if there is a game instance
        if (repository.findById(appUserGameInstance.getGameInstanceId()) == null){
            result.addErrorMessage("game instance id was not found");
            result.setNotFound();
            return result;
        }

        //check if there is a app user
        if (appUserRepository.findById(appUserGameInstance.getAppUserId()) == null){
            result.addErrorMessage("app user id was not found");
            result.setNotFound();
            return result;
        }

        if (result.isSuccess()) {
            AppUserGameInstance ap = repository.addUser(appUserGameInstance);
            result.setPayload(ap);
        }

        return result;
    }


//------------------Delete--------------------------------//

    public Result<Void> deleteById(int id) {
        Result<Void> result = new Result<>();
        boolean success = repository.deleteById(id);
        if (!success) {
            result.addErrorMessage("could not delete gameInstance id " + id);
        }
        return result;
    }

    public Result<Void> removeUserByUserId(int userId, int gameInstanceId) {
        Result<Void> result = new Result<>();
        boolean success = repository.removeUserByUserId(userId, gameInstanceId);
        if (!success) {
            result.addErrorMessage("could not delete user from gameInstance id " + gameInstanceId);
        }
        return result;
    }

//------------------Validations-------------------------------//

    private Result<GameInstance> validate(GameInstance gameInstance) {
        Result<GameInstance> result = new Result<>();

        if (gameInstance == null){
            result.addErrorMessage("gameInstance cannot be null.");
            return result;
        }

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<GameInstance>> violations = validator.validate(gameInstance);

        if (!violations.isEmpty()){
            for (ConstraintViolation<GameInstance> violation: violations){
                result.addErrorMessage(violation.getMessage());
            }
            return result;
        }

        return result;
    }

}
