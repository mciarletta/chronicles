package learn.chronicles.controllers;


import learn.chronicles.domain.CardService;
import learn.chronicles.domain.Result;
import learn.chronicles.models.Card;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static learn.chronicles.controllers.ResultToResponseEntity.toResponseEntity;

@RestController
@RequestMapping("/api/card")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


//----------------------Reads------------------------------------//

    //findByBoardGameId
    @GetMapping("/board-game/{id}")
    public List<Card> findByBoardGameId(@PathVariable int id) {
        return cardService.findByBoardGameId(id);
    }

    //findbyid
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        Card card = cardService.findById(id);
        Result<Card> result = new Result<>();
        if (card == null) {
            result.addErrorMessage("This Card Game does not exist.");
            return toResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        result.setPayload(card);
        return toResponseEntity(result, HttpStatus.OK);
    }

//----------------------Create---------------------------------//

    //add
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid Card card, BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<Card> cardResult = cardService.add(card);

        if (!cardResult.isSuccess()){
            return new ResponseEntity<>(cardResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(cardResult, HttpStatus.CREATED);
    }

//----------------------Update----------------------------//

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid Card card, BindingResult result){
        //check if the id matches the id in the request body
        if (id != card.getId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<Card> cardResult = cardService.update(card);

        if (!cardResult.isSuccess()){
            return new ResponseEntity<>(cardResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(cardResult, HttpStatus.NO_CONTENT);


    }

//------------------Delete--------------------------------//

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        Result<Void> result = cardService.deleteById(id);
        return toResponseEntity(result, HttpStatus.NO_CONTENT);
    }


}
