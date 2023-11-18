package learn.chronicles.controllers;


import learn.chronicles.domain.GameInstanceService;
import learn.chronicles.domain.Result;
import learn.chronicles.models.AppUserGameInstance;
import learn.chronicles.models.GameInstance;
import learn.chronicles.models.UsernameGameTitle;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static learn.chronicles.controllers.ResultToResponseEntity.toResponseEntity;

@RestController
@RequestMapping("/api/game-instance")
public class GameInstanceController {


    private final GameInstanceService gameInstanceService;

    public GameInstanceController(GameInstanceService gameInstanceService) {
        this.gameInstanceService = gameInstanceService;
    }


//----------------------Reads------------------------------------//

    //findByBoardGameId
    @GetMapping("/board-game/{id}")
    public List<GameInstance> findByBoardGameId(@PathVariable int id) {
        return gameInstanceService.findByBoardGameId(id);
    }




    //findByUserId
    @GetMapping("/user/{id}")
    public List<GameInstance> findByUserId(@PathVariable int id) {
        return gameInstanceService.findByUserId(id);
    }

    //findUsers and title
    @GetMapping("/users-title/{id}")
    public List<UsernameGameTitle> findUsersAndTitle(@PathVariable int id) {
        return gameInstanceService.findUsernames(id);
    }

    //findbyid
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        GameInstance gameInstance = gameInstanceService.findById(id);
        Result<GameInstance> result = new Result<>();
        if (gameInstance == null) {
            result.addErrorMessage("This GameInstance Game does not exist.");
            return toResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        result.setPayload(gameInstance);
        return toResponseEntity(result, HttpStatus.OK);
    }

//----------------------Create---------------------------------//

    //add
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid GameInstance gameInstance, BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<GameInstance> gameInstanceResult = gameInstanceService.add(gameInstance);

        if (!gameInstanceResult.isSuccess()){
            return new ResponseEntity<>(gameInstanceResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(gameInstanceResult, HttpStatus.CREATED);
    }

    //addUser
    @PostMapping("/user")
    public ResponseEntity<?> addUser(@RequestBody AppUserGameInstance appUserGameInstance){

        Result<AppUserGameInstance> gameInstanceResult = gameInstanceService.addUser(appUserGameInstance);

        if (!gameInstanceResult.isSuccess()){
            return new ResponseEntity<>(gameInstanceResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(gameInstanceResult, HttpStatus.CREATED);
    }

//----------------------Update----------------------------//

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid GameInstance gameInstance, BindingResult result){
        //check if the id matches the id in the request body
        if (id != gameInstance.getId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<GameInstance> gameInstanceResult = gameInstanceService.update(gameInstance);

        if (!gameInstanceResult.isSuccess()){
            return new ResponseEntity<>(gameInstanceResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(gameInstanceResult, HttpStatus.NO_CONTENT);


    }

//------------------Delete--------------------------------//

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        Result<Void> result = gameInstanceService.deleteById(id);
        return toResponseEntity(result, HttpStatus.NO_CONTENT);
    }

    //remove users by id
    @DeleteMapping("/user")
    public ResponseEntity<?> removeUsers(@RequestBody AppUserGameInstance appUserGameInstance){
        Result<Void> result = gameInstanceService.removeUserByUserId(appUserGameInstance.getAppUserId(), appUserGameInstance.getGameInstanceId());
        return toResponseEntity(result, HttpStatus.NO_CONTENT);
    }



}
