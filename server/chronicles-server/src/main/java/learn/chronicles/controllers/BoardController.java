package learn.chronicles.controllers;

import learn.chronicles.domain.BoardService;
import learn.chronicles.domain.Result;
import learn.chronicles.models.Board;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static learn.chronicles.controllers.ResultToResponseEntity.toResponseEntity;


@RestController
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


//----------------------Reads------------------------------------//

    //findByBoardGameId
    @GetMapping("/board-game/{id}")
    public List<Board> findByBoardGameId(@PathVariable int id) {
        return boardService.findByBoardGameId(id);
    }

    //findbyid
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        Board board = boardService.findById(id);
        Result<Board> result = new Result<>();
        if (board == null) {
            result.addErrorMessage("This Board Game does not exist.");
            return toResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        result.setPayload(board);
        return toResponseEntity(result, HttpStatus.OK);
    }

//----------------------Create---------------------------------//

    //add
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid Board board, BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<Board> boardResult = boardService.add(board);

        if (!boardResult.isSuccess()){
            return new ResponseEntity<>(boardResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(boardResult, HttpStatus.CREATED);
    }

//----------------------Update----------------------------//

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid Board board, BindingResult result){
        //check if the id matches the id in the request body
        if (id != board.getId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<Board> boardResult = boardService.update(board);

        if (!boardResult.isSuccess()){
            return new ResponseEntity<>(boardResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(boardResult, HttpStatus.NO_CONTENT);


    }

//------------------Delete--------------------------------//

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        Result<Void> result = boardService.deleteById(id);
        return toResponseEntity(result, HttpStatus.NO_CONTENT);
    }

}
