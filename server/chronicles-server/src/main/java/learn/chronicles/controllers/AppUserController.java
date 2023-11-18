package learn.chronicles.controllers;

import learn.chronicles.App;
import learn.chronicles.domain.AppUserService;
import learn.chronicles.domain.Result;
import learn.chronicles.models.AppUser;
import learn.chronicles.models.ColorAndAvatar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static learn.chronicles.controllers.ResultToResponseEntity.toResponseEntity;

@RestController
@RequestMapping("/api/user")
public class AppUserController {



    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }


//----------------------Reads------------------------------------//


    //findbyid
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable int id) {
        AppUser appUser = appUserService.findById(id);
        Result<AppUser> result = new Result<>();
        if (appUser == null) {
            result.addErrorMessage("This AppUser Game does not exist.");
            return toResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        result.setPayload(appUser);
        return toResponseEntity(result, HttpStatus.OK);
    }

    //findbyUsername
    @GetMapping("/username/{username}")
    public ResponseEntity<?> findByUsername(@PathVariable String username) {
        AppUser user = appUserService.findByUsername(username);
        Result<AppUser> result = new Result<>();
        if (user == null) {
            result.addErrorMessage("This user does not exist.");
            return toResponseEntity(result, HttpStatus.NOT_FOUND);
        }
        result.setPayload(user);
        return toResponseEntity(result, HttpStatus.OK);
    }

//----------------------Create---------------------------------//

    //add
    @PostMapping
    public ResponseEntity<?> add(@RequestBody @Valid AppUser appUser, BindingResult result){

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<AppUser> appUserResult = appUserService.create(appUser.getUsername(), appUser.getEmail(), appUser.getPassword());

        if (!appUserResult.isSuccess()){
            return new ResponseEntity<>(appUserResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(appUserResult, HttpStatus.CREATED);
    }

//----------------------Update----------------------------//

    //update
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody @Valid AppUser appUser, BindingResult result){
        //check if the id matches the id in the request body
        if (id != appUser.getAppUserId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors().stream().toList(), HttpStatus.BAD_REQUEST);
        }

        Result<AppUser> appUserResult = appUserService.update(appUser);

        if (!appUserResult.isSuccess()){
            return new ResponseEntity<>(appUserResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(appUserResult, HttpStatus.NO_CONTENT);


    }

    @PutMapping("/color/{id}")
    public ResponseEntity<?> updateColorAndAvatar(@PathVariable int id, @RequestBody ColorAndAvatar colorAndAvatar){

        Result<?> appUserResult = appUserService.updateColorAndAvatar(colorAndAvatar.getColor(), colorAndAvatar.getAvatar(), id);

        if (!appUserResult.isSuccess()){
            return new ResponseEntity<>(appUserResult.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }

        return toResponseEntity(appUserResult, HttpStatus.NO_CONTENT);

    }

//------------------Delete--------------------------------//

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @RequestBody @Valid AppUser appUser, BindingResult result){
        //check if the id matches the id in the request body
        if (id != appUser.getAppUserId()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (result.hasErrors()){
            return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
        }

        Result<AppUser> appUserResult = appUserService.delete(appUser);
        return toResponseEntity(appUserResult, HttpStatus.NO_CONTENT);

    }


}
