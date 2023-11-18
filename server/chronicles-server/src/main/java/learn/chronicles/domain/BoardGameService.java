package learn.chronicles.domain;

import learn.chronicles.data.BoardGameRepository;
import learn.chronicles.models.BoardGame;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class BoardGameService {

    private final BoardGameRepository repository;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public BoardGameService(BoardGameRepository repository) {
        this.repository = repository;
    }

    //----------------------Reads------------------------------------//
    public List<BoardGame> findAll() {
        return repository.findAll();
    }

    public BoardGame findById(int id) {
        return repository.findById(id);
    }

    //----------------------Create---------------------------------//

    public Result<BoardGame> add(BoardGame boardGame) {

        Result<BoardGame> result = validate(boardGame);
        if (!result.isSuccess()) {
            return result;
        }

        if (boardGame.getId() != 0) {
            result.addErrorMessage("board game id cannot be set for `create` operation.");
            return result;
        }

        if (result.isSuccess()) {
            BoardGame bg = repository.create(boardGame);
            result.setPayload(bg);
        }

        return result;
    }

    //----------------------Update----------------------------//

    public Result<BoardGame> update(BoardGame boardGame) {

        Result<BoardGame> result = validate(boardGame);
        if (!result.isSuccess()) {
            return result;
        }

        if (boardGame.getId() <= 0) {
            result.addErrorMessage("board game id is required");
            return result;
        }

        boolean success = repository.update(boardGame);
        if (!success) {
            result.addErrorMessage("could not update board game id " + boardGame.getId());
        }

        return result;
    }

    //------------------Delete--------------------------------//

    public Result<Void> deleteById(int id) {
        Result<Void> result = new Result<>();
        boolean success = repository.deleteById(id);
        if (!success) {
            result.addErrorMessage("could not delete board game id " + id);
        }
        return result;
    }

    //------------------Validations-------------------------------//

    private Result<BoardGame> validate(BoardGame boardGame) {
        Result<BoardGame> result = new Result<>();

        if (boardGame == null){
            result.addErrorMessage("board game cannot be null.");
            return result;
        }

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<BoardGame>> violations = validator.validate(boardGame);

        if (!violations.isEmpty()){
            for (ConstraintViolation<BoardGame> violation: violations){
                result.addErrorMessage(violation.getMessage());
            }
            return result;
        }

        if (!isPerfectSquare(boardGame.getBoardSlots())){
            result.addErrorMessage("board game slots must be a perfect square.");
            return result;
        }

        if (!isPerfectSquare(boardGame.getPlacesPerBoard())){
            result.addErrorMessage("board game places per board must be a perfect square.");
            return result;
        }

        return result;
    }

    private boolean isPerfectSquare(int num){

        // just in case, but this should not get through the validator
        if (num < 0) {
            return false;
        }

        // Calculate the square root
        double squareRoot = Math.sqrt(num);

        // Check if the square of number is a whole number by comparing its floor and ceil which should be the same if its a whole number
        return Math.floor(squareRoot) == Math.ceil(squareRoot);
    }


}
