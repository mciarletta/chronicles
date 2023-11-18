package learn.chronicles.controllers;

import learn.chronicles.domain.*;
import learn.chronicles.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static learn.chronicles.controllers.ResultToResponseEntity.toResponseEntity;

@RestController
@RequestMapping("/api/board-game")
public class BoardGameController {

    private final BoardGameService boardGameService;

    private final BoardService boardService;

    private final CardService cardService;

    private final DieService dieService;

    private final FigureService figureService;

    public BoardGameController(BoardGameService boardGameService, BoardService boardService, CardService cardService, DieService dieService, FigureService figureService) {
        this.boardGameService = boardGameService;
        this.boardService = boardService;
        this.cardService = cardService;
        this.dieService = dieService;
        this.figureService = figureService;
    }


    //----------------------Reads------------------------------------//

    //findall
    @GetMapping
    public List<BoardGame> findAll() {
        return boardGameService.findAll();
    }

    //findbyid
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        BoardGame boardGame = boardGameService.findById(id);
        Result<BoardGame> result = new Result<>();
        if (boardGame == null) {
            result.addErrorMessage("This Board Game does not exist.");
            return toResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        result.setPayload(boardGame);
        return toResponseEntity(result, HttpStatus.OK);
    }

    //make a box
    @GetMapping("/box/{id}")
    public Box getBox(@PathVariable int id) {
        List<Board> boards = boardService.findByBoardGameId(id);
        List<Card> cards = cardService.findByBoardGameId(id);
        List<Die> dice =  dieService.findByBoardGameId(id);
        List<Figure> figures = figureService.findByBoardGameId(id);

        return new Box(boards,cards,dice,figures);
    }

    //----------------------Create---------------------------------//

    //add
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid BoardGame boardGame, BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<BoardGame> resultNew = boardGameService.add(boardGame);
        return toResponseEntity(resultNew, HttpStatus.CREATED);
    }

    //----------------------Update----------------------------//

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody BoardGame boardGame){
        //check if the id matches the id in the request body
        if (id != boardGame.getId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        Result<BoardGame> result = boardGameService.update(boardGame);
        return toResponseEntity(result, HttpStatus.NO_CONTENT);
    }

    //------------------Delete--------------------------------//

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        Result<Void> result = boardGameService.deleteById(id);
        return toResponseEntity(result, HttpStatus.NO_CONTENT);
    }
}
