package learn.chronicles.domain;

import learn.chronicles.data.BoardGameRepository;
import learn.chronicles.data.FigureRepository;
import learn.chronicles.models.Card;
import learn.chronicles.models.Figure;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class FigureService {

    private final FigureRepository repository;

    private final BoardGameRepository boardGameRepository;


    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public FigureService(FigureRepository repository, BoardGameRepository boardGameRepository) {
        this.repository = repository;
        this.boardGameRepository = boardGameRepository;
    }


    //----------------------Reads------------------------------------//

    public Figure findById(int id) {
        return repository.findById(id);
    }

    public List<Figure> findByBoardGameId(int id) {
        return repository.findByBoardGameId(id);
    }


    //----------------------Create---------------------------------//

    public Result<Figure> add(Figure figure) {

        Result<Figure> result = validate(figure);
        if (!result.isSuccess()) {
            return result;
        }

        if (figure.getId() != 0) {
            result.addErrorMessage("figure id cannot be set for `create` operation.");
            return result;
        }

        if (result.isSuccess()) {
            Figure bg = repository.create(figure);
            result.setPayload(bg);
        }

        return result;
    }

    //----------------------Update----------------------------//

    public Result<Figure> update(Figure figure) {

        Result<Figure> result = validate(figure);
        if (!result.isSuccess()) {
            return result;
        }

        if (figure.getId() <= 0) {
            result.addErrorMessage("figure id is required");
            return result;
        }

        boolean success = repository.update(figure);
        if (!success) {
            result.addErrorMessage("could not update figure id " + figure.getId());
        }

        return result;
    }

    //------------------Delete--------------------------------//

    public Result<Void> deleteById(int id) {
        Result<Void> result = new Result<>();
        boolean success = repository.deleteById(id);
        if (!success) {
            result.addErrorMessage("could not delete figure id " + id);
        }
        return result;
    }

    //------------------Validations-------------------------------//

    private Result<Figure> validate(Figure figure) {
        Result<Figure> result = new Result<>();

        if (figure == null){
            result.addErrorMessage("figure cannot be null.");
            return result;
        }

        if (boardGameRepository.findById(figure.getBoardGameId()) == null){
            result.addErrorMessage("board game id not found.");
            result.setNotFound();
            return result;
        }

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Figure>> violations = validator.validate(figure);

        if (!violations.isEmpty()){
            for (ConstraintViolation<Figure> violation: violations){
                result.addErrorMessage(violation.getMessage());
            }
            return result;
        }

        return result;
    }
}
