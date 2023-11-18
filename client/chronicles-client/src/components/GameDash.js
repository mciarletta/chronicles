import AuthContext from "./contexts/AuthContext";
import { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getBoardGameById, getBox, getGameInstancesByUserId } from "./services/GameInstanceAPI";
import { useNavigate } from "react-router-dom";
import Game from "./Game";

export default function GameDash() {
  const auth = useContext(AuthContext);
  const [verified, setVerified] = useState(false);
  const [boardGameId, setBoardGameId] = useState();
  const [boxFromDb, setBoxFromDb] = useState();
  const [boardGameInfo, setBoardGameInfo] = useState();
  const [gameInstance, setGameInstance] = useState();
  const { id } = useParams();
  const navigate = useNavigate();

  useEffect(() => {
    //check to see if the user is part of this game instance
    getGameInstancesByUserId(auth.user.app_user_id)
      .then((data) => {
        //check the instances
        for (const gi of data) {
          if (gi.id === Number(id)) {
            //then there is a match
            setVerified(true);
            setBoardGameId(gi.boardGameId);
            setGameInstance(gi);
          }
        }
      })
      .catch((err) => {
        navigate("/notfound", {
          state: { message: err.message },
        });
      });
  }, [auth.user.app_user_id, id, navigate]);

  useEffect(() => {
    //get a box of the board game
    if (boardGameId) {
      getBox(boardGameId)
        .then((data) => makeGameBox(data))
        .catch((err) => {
          navigate("/notfound", {
            state: { message: err.message },
          });
        });

        getBoardGameById(boardGameId)
        .then((data) => setBoardGameInfo(data))
        .catch((err) => {
          navigate("/notfound", {
            state: { message: err.message },
          });
        });
    }
  }, [boardGameId, navigate]);

  function makeGameBox(data) {
    //creat a new array
    let gameBox = [];

    //and inded for the box
    let boxIndex = 0;

    //put the pieces into the new box with a boxId
    for (const categoryKey in data) {
      if (data.hasOwnProperty(categoryKey)) {
        const category = data[categoryKey];

        for (const piece of category) {
          const newPiece = { boxId: boxIndex, ...piece };
          boxIndex++;
          gameBox.push(newPiece);
        }
      }
    }

    //set the new boxFromDb state
    setBoxFromDb(gameBox)
  }

  return <>{verified && boxFromDb && <Game boxFromDb={boxFromDb} gameInstance={gameInstance} boardGameInfo={boardGameInfo}></Game>}</>;
}
