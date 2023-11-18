package learn.chronicles.domain;

import learn.chronicles.data.AppUserRepository;
import learn.chronicles.data.DieRepository;
import learn.chronicles.data.GameInstanceJdbcTemplateRepository;
import learn.chronicles.data.GameInstanceRepository;
import learn.chronicles.models.AppUserGameInstance;
import learn.chronicles.models.Figure;
import learn.chronicles.models.GameInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static learn.chronicles.TestHelper.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class GameInstanceServiceTest {

    @MockBean
    GameInstanceJdbcTemplateRepository repository;

    @MockBean
    AppUserRepository appUserRepository;
    @Autowired
    GameInstanceService service;

    @Test
    void findById() {
        GameInstance expected = makeGameInstance(1);
        when(repository.findById(anyInt())).thenReturn(makeGameInstance(1));
        GameInstance actual = service.findById(1);

        assertEquals(actual, expected);
    }

    @Test
    void findByBoardGameId() {
        List<GameInstance> expected = List.of(makeGameInstance(1));
        when(repository.findByBoardGameId(anyInt())).thenReturn(List.of(makeGameInstance(1)));
        List<GameInstance> actual = service.findByBoardGameId(1);

        assertEquals(actual, expected);
    }

    @Test
    void findByUserId() {
        List<GameInstance> expected = List.of(makeGameInstance(1));
        when(repository.findByUserId(anyInt())).thenReturn(List.of(makeGameInstance(1)));
        List<GameInstance> actual = service.findByUserId(1);

        assertEquals(actual, expected);
    }

    @Test
    void add() {
        Result<GameInstance> expected = makeResult(null, makeGameInstance(1));
        GameInstance arg = makeGameInstance(1);
        arg.setId(0);

        when(repository.create(any())).thenReturn(makeGameInstance(1));

        Result<GameInstance> actual = service.add(arg);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Result<GameInstance> expected = makeResult(null, null);
        GameInstance arg = makeGameInstance(1);

        when(repository.update(any())).thenReturn(true);

        Result<GameInstance> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void deleteById() {
        Result<Void> expected = makeResult(null, null);

        when(repository.deleteById(anyInt())).thenReturn(true);

        Result<Void> actual = service.deleteById(1);
        assertEquals(expected, actual);
    }

    @Test
    void addUser(){
        Result<Void> expected = makeResult(null, null);

        when(repository.findById(anyInt())).thenReturn(makeGameInstance(1));

        when(appUserRepository.findById(anyInt())).thenReturn(makeAppUser(1));

        when(repository.addUser(any())).thenReturn(new AppUserGameInstance(1,1));

        Result<AppUserGameInstance> actual = service.addUser(new AppUserGameInstance(1,1));

        assertEquals(expected.isSuccess(), actual.isSuccess());

    }

    @Test
    void removeUser() {
        Result<Void> expected = makeResult(null, null);

        when(repository.removeUserByUserId(anyInt(), anyInt())).thenReturn(true);

        Result<Void> actual = service.removeUserByUserId(1,1);
        assertEquals(expected, actual);
    }

    //------------not add users-------------//

    @Test
    void NotAddUserBadGameInstance(){
        Result<AppUserGameInstance> expected = makeResult("game instance id was not found", null);
        expected.setNotFound();

        when(repository.findById(anyInt())).thenReturn(null);

        Result<AppUserGameInstance> actual = service.addUser(new AppUserGameInstance(1,1));

        assertEquals(expected, actual);

    }

    @Test
    void NotAddUserBadUserId(){
        Result<AppUserGameInstance> expected = makeResult("app user id was not found", null);
        expected.setNotFound();

        when(appUserRepository.findById(anyInt())).thenReturn(null);

        when(repository.findById(anyInt())).thenReturn(makeGameInstance(1));

        when(repository.addUser(any())).thenReturn(new AppUserGameInstance(1,1));

        Result<AppUserGameInstance> actual = service.addUser(new AppUserGameInstance(1,1));

        assertEquals(expected, actual);

    }

    @Test
    void notUpdateNullGameInstance (){
        Result<GameInstance> expected = makeResult("gameInstance cannot be null.", null);
        Result<GameInstance> actual = service.update(null);
        assertEquals(expected, actual);
    }

    @Test
    void notAddNullGameInstance (){
        Result<GameInstance> expected = makeResult("gameInstance cannot be null.", null);
        Result<GameInstance> actual = service.add(null);
        assertEquals(expected, actual);
    }

    @Test
    void notAddGameInstanceWithId (){
        Result<GameInstance> expected = makeResult("gameInstance id cannot be set for `create` operation.", null);
        Result<GameInstance> actual = service.add(makeGameInstance(2));
        assertEquals(expected, actual);
    }

    @Test
    void notUpdateGameInstanceWithBadId (){
        Result<GameInstance> expected = makeResult("gameInstance id is required", null);
        GameInstance arg = makeGameInstance(2);
        arg.setId(-2);
        Result<GameInstance> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void notUpdateGameInstanceNonExistingId (){
        Result<GameInstance> expected = makeResult("could not update gameInstance id 5", null);
        GameInstance arg = makeGameInstance(5);
        Result<GameInstance> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void notUpdateGameInstanceBadBGId (){
        Result<GameInstance> expected = makeResult("Board Game Id must be a positive integer", null);
        GameInstance arg = makeGameInstance(2);
        arg.setBoardGameId(0);
        Result<GameInstance> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void notAddGameInstanceBadBGId (){
        Result<GameInstance> expected = makeResult("Board Game Id must be a positive integer", null);
        GameInstance arg = makeGameInstance(2);
        arg.setBoardGameId(0);
        Result<GameInstance> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void notAddGameInstanceTooLongSaveState (){
        Result<GameInstance> expected = makeResult("save state is too large", null);
        GameInstance arg = makeGameInstance(2);
        arg.setSaveState("x".repeat(10000001));
        Result<GameInstance> actual = service.add(arg);
        assertEquals(expected, actual);
    }

    @Test
    void notUpdateGameInstanceTooLongSaveState (){
        Result<GameInstance> expected = makeResult("save state is too large", null);
        GameInstance arg = makeGameInstance(2);
        arg.setSaveState("x".repeat(10000001));
        Result<GameInstance> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void notUpdateGameInstanceTooLongLog (){
        Result<GameInstance> expected = makeResult("log is too large", null);
        GameInstance arg = makeGameInstance(2);
        arg.setLog("x".repeat(60001));
        Result<GameInstance> actual = service.update(arg);
        assertEquals(expected, actual);
    }

    @Test
    void notAddGameInstanceTooLongLog (){
        Result<GameInstance> expected = makeResult("log is too large", null);
        GameInstance arg = makeGameInstance(2);
        arg.setLog("x".repeat(60001));
        Result<GameInstance> actual = service.add(arg);
        assertEquals(expected, actual);
    }
}