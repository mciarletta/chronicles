package learn.chronicles.domain;

import learn.chronicles.data.BoardGameRepository;
import learn.chronicles.data.BoardRepository;
import learn.chronicles.models.Board;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

@Service
public class BoardService {

    private final BoardRepository repository;

    private final BoardGameRepository boardGameRepository;

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public BoardService(BoardRepository repository, BoardGameRepository boardGameRepository) {
        this.repository = repository;
        this.boardGameRepository = boardGameRepository;
    }


    //----------------------Reads------------------------------------//

    public Board findById(int id) {
        return repository.findById(id);
    }

    public List<Board> findByBoardGameId(int id) {
        return repository.findByBoardGameId(id);
    }


    //----------------------Create---------------------------------//

    public Result<Board> add(Board board) {

        Result<Board> result = validate(board);
        if (!result.isSuccess()) {
            return result;
        }

        if (board.getId() != 0) {
            result.addErrorMessage("board id cannot be set for `create` operation.");
            return result;
        }

        if (result.isSuccess()) {
            Board bg = repository.create(board);
            result.setPayload(bg);
        }

        return result;
    }

    //----------------------Update----------------------------//

    public Result<Board> update(Board board) {

        Result<Board> result = validate(board);
        if (!result.isSuccess()) {
            return result;
        }

        if (board.getId() <= 0) {
            result.addErrorMessage("board id is required");
            return result;
        }

        boolean success = repository.update(board);
        if (!success) {
            result.addErrorMessage("could not update board id " + board.getId());
        }

        return result;
    }

    //------------------Delete--------------------------------//

    public Result<Void> deleteById(int id) {
        Result<Void> result = new Result<>();
        boolean success = repository.deleteById(id);
        if (!success) {
            result.addErrorMessage("could not delete board id " + id);
        }
        return result;
    }

    //------------------Validations-------------------------------//

    private Result<Board> validate(Board board) {
        Result<Board> result = new Result<>();

        if (board == null){
            result.addErrorMessage("board cannot be null.");
            return result;
        }

        if (boardGameRepository.findById(board.getBoardGameId()) == null){
            result.addErrorMessage("board game id not found.");
            result.setNotFound();
            return result;
        }

        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<Board>> violations = validator.validate(board);

        if (!violations.isEmpty()){
            for (ConstraintViolation<Board> violation: violations){
                result.addErrorMessage(violation.getMessage());
            }
            return result;
        }

        return result;
    }


}
