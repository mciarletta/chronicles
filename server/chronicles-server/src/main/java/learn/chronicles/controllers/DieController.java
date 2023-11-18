package learn.chronicles.controllers;


import learn.chronicles.domain.DieService;
import learn.chronicles.domain.Result;
import learn.chronicles.models.Card;
import learn.chronicles.models.Die;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static learn.chronicles.controllers.ResultToResponseEntity.toResponseEntity;

@RestController
@RequestMapping("/api/die")
public class DieController {



    private final DieService dieService;

    public DieController(DieService dieService) {
        this.dieService = dieService;
    }


//----------------------Reads------------------------------------//

    //findByBoardGameId
    @GetMapping("/board-game/{id}")
    public List<Die> findByBoardGameId(@PathVariable int id) {
        return dieService.findByBoardGameId(id);
    }

    //findbyid
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        Die die = dieService.findById(id);
        Result<Die> result = new Result<>();
        if (die == null) {
            result.addErrorMessage("This Die Game does not exist.");
            return toResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        result.setPayload(die);
        return toResponseEntity(result, HttpStatus.OK);
    }

//----------------------Create---------------------------------//

    //add
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid Die die, BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<Die> dieResult = dieService.add(die);

        if (!dieResult.isSuccess()){
            return new ResponseEntity<>(dieResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(dieResult, HttpStatus.CREATED);
    }

//----------------------Update----------------------------//

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid Die die, BindingResult result){
        //check if the id matches the id in the request body
        if (id != die.getId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<Die> dieResult = dieService.update(die);

        if (!dieResult.isSuccess()){
            return new ResponseEntity<>(dieResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(dieResult, HttpStatus.NO_CONTENT);


    }

//------------------Delete--------------------------------//

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        Result<Void> result = dieService.deleteById(id);
        return toResponseEntity(result, HttpStatus.NO_CONTENT);
    }

}
