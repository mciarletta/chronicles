package learn.chronicles.controllers;

import learn.chronicles.domain.FigureService;
import learn.chronicles.domain.Result;
import learn.chronicles.models.Figure;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static learn.chronicles.controllers.ResultToResponseEntity.toResponseEntity;

@RestController
@RequestMapping("/api/figure")
public class FigureController {



    private final FigureService figureService;

    public FigureController(FigureService figureService) {
        this.figureService = figureService;
    }


//----------------------Reads------------------------------------//

    //findByBoardGameId
    @GetMapping("/board-game/{id}")
    public List<Figure> findByBoardGameId(@PathVariable int id) {
        return figureService.findByBoardGameId(id);
    }

    //findbyid
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        Figure figure = figureService.findById(id);
        Result<Figure> result = new Result<>();
        if (figure == null) {
            result.addErrorMessage("This Figure Game does not exist.");
            return toResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        result.setPayload(figure);
        return toResponseEntity(result, HttpStatus.OK);
    }

//----------------------Create---------------------------------//

    //add
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid Figure figure, BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<Figure> figureResult = figureService.add(figure);

        if (!figureResult.isSuccess()){
            return new ResponseEntity<>(figureResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(figureResult, HttpStatus.CREATED);
    }

//----------------------Update----------------------------//

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid Figure figure, BindingResult result){
        //check if the id matches the id in the request body
        if (id != figure.getId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<Figure> figureResult = figureService.update(figure);

        if (!figureResult.isSuccess()){
            return new ResponseEntity<>(figureResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(figureResult, HttpStatus.NO_CONTENT);


    }

//------------------Delete--------------------------------//

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        Result<Void> result = figureService.deleteById(id);
        return toResponseEntity(result, HttpStatus.NO_CONTENT);
    }


}
