package learn.chronicles.data;
import learn.chronicles.models.AppUser;
import learn.chronicles.models.AppUserGameInstance;
import learn.chronicles.models.GameInstance;
import learn.chronicles.models.UsernameGameTitle;

import java.util.List;

public interface GameInstanceRepository {

    GameInstance findById(int id);

    List<GameInstance> findByUserId(int id);

    List<GameInstance> findByBoardGameId(int id);

    List<UsernameGameTitle> findUsersInAGameInstance(int id);


    GameInstance create(GameInstance gameInstance);

    boolean update(GameInstance gameInstance);

    AppUserGameInstance addUser(AppUserGameInstance appUserGameInstance);

    boolean removeUserByUserId(int userId, int gameInstanceId);


    boolean deleteById(int id);
}
