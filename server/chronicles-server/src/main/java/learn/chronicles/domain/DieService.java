package learn.chronicles.domain;

import learn.chronicles.data.BoardGameRepository;
import learn.chronicles.data.DieRepository;
import learn.chronicles.models.Card;
import learn.chronicles.models.Die;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class DieService {


    private final DieRepository repository;


    private final BoardGameRepository boardGameRepository;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public DieService(DieRepository repository, BoardGameRepository boardGameRepository) {
        this.repository = repository;
        this.boardGameRepository = boardGameRepository;
    }


    //----------------------Reads------------------------------------//

    public Die findById(int id) {
        return repository.findById(id);
    }

    public List<Die> findByBoardGameId(int id) {
        return repository.findByBoardGameId(id);
    }


    //----------------------Create---------------------------------//

    public Result<Die> add(Die die) {

        Result<Die> result = validate(die);
        if (!result.isSuccess()) {
            return result;
        }

        if (die.getId() != 0) {
            result.addErrorMessage("die id cannot be set for `create` operation.");
            return result;
        }

        if (result.isSuccess()) {
            Die bg = repository.create(die);
            result.setPayload(bg);
        }

        return result;
    }

    //----------------------Update----------------------------//

    public Result<Die> update(Die die) {

        Result<Die> result = validate(die);
        if (!result.isSuccess()) {
            return result;
        }

        if (die.getId() <= 0) {
            result.addErrorMessage("die id is required");
            return result;
        }

        boolean success = repository.update(die);
        if (!success) {
            result.addErrorMessage("could not update die id " + die.getId());
        }

        return result;
    }

    //------------------Delete--------------------------------//

    public Result<Void> deleteById(int id) {
        Result<Void> result = new Result<>();
        boolean success = repository.deleteById(id);
        if (!success) {
            result.addErrorMessage("could not delete die id " + id);
        }
        return result;
    }

    //------------------Validations-------------------------------//

    private Result<Die> validate(Die die) {
        Result<Die> result = new Result<>();

        if (die == null){
            result.addErrorMessage("die cannot be null.");
            return result;
        }

        if (boardGameRepository.findById(die.getBoardGameId()) == null){
            result.addErrorMessage("board game id not found.");
            result.setNotFound();
            return result;
        }

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Die>> violations = validator.validate(die);

        if (!violations.isEmpty()){
            for (ConstraintViolation<Die> violation: violations){
                result.addErrorMessage(violation.getMessage());
            }
            return result;
        }

        return result;
    }
}
