package learn.chronicles.domain;

import learn.chronicles.data.BoardGameRepository;
import learn.chronicles.data.CardRepository;
import learn.chronicles.models.Board;
import learn.chronicles.models.Card;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class CardService {


    private final CardRepository repository;

    private final BoardGameRepository boardGameRepository;


    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public CardService(CardRepository repository, BoardGameRepository boardGameRepository) {
        this.repository = repository;
        this.boardGameRepository = boardGameRepository;
    }


    //----------------------Reads------------------------------------//

    public Card findById(int id) {
        return repository.findById(id);
    }

    public List<Card> findByBoardGameId(int id) {
        return repository.findByBoardGameId(id);
    }


    //----------------------Create---------------------------------//

    public Result<Card> add(Card card) {

        Result<Card> result = validate(card);
        if (!result.isSuccess()) {
            return result;
        }

        if (card.getId() != 0) {
            result.addErrorMessage("card id cannot be set for `create` operation.");
            return result;
        }

        if (result.isSuccess()) {
            Card bg = repository.create(card);
            result.setPayload(bg);
        }

        return result;
    }

    //----------------------Update----------------------------//

    public Result<Card> update(Card card) {

        Result<Card> result = validate(card);
        if (!result.isSuccess()) {
            return result;
        }

        if (card.getId() <= 0) {
            result.addErrorMessage("card id is required");
            return result;
        }

        boolean success = repository.update(card);
        if (!success) {
            result.addErrorMessage("could not update card id " + card.getId());
        }

        return result;
    }

    //------------------Delete--------------------------------//

    public Result<Void> deleteById(int id) {
        Result<Void> result = new Result<>();
        boolean success = repository.deleteById(id);
        if (!success) {
            result.addErrorMessage("could not delete card id " + id);
        }
        return result;
    }

    //------------------Validations-------------------------------//

    private Result<Card> validate(Card card) {
        Result<Card> result = new Result<>();

        if (card == null){
            result.addErrorMessage("card cannot be null.");
            return result;
        }

        if (boardGameRepository.findById(card.getBoardGameId()) == null){
            result.addErrorMessage("board game id not found.");
            result.setNotFound();
            return result;
        }

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Card>> violations = validator.validate(card);

        if (!violations.isEmpty()){
            for (ConstraintViolation<Card> violation: violations){
                result.addErrorMessage(violation.getMessage());
            }
            return result;
        }

        return result;
    }
}
