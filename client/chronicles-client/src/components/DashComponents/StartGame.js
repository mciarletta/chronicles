import { useState, useContext } from "react";
import AuthContext from "../contexts/AuthContext";
import { useEffect } from "react";
import { Button, Col, Row, Alert, Form } from "react-bootstrap";
import {
  getBoardGames,
  createGameInstance,
  addUserToGame,
} from "../services/GameInstanceAPI";
import MoveInfo from "../GameComponents/MoveInfo";

export default function StartGame() {
  const auth = useContext(AuthContext);
  const [boardGames, setBoardgame] = useState([]);
  const [errors, setErrors] = useState([]);
  const [selection, setSelection] = useState("");
  const [success, setSuccess] = useState(false);
  const [selectMessage, setSelectMessage] = useState("");

  //to create a game instance, we need:
  //a boardGame id
  //when making the instance, tie the user id to it

  useEffect(() => {
    //grab the boardgames list
    getBoardGames()
      .then((data) => setBoardgame(data))
      .catch ((error) => {
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }});
  }, []);

  //submit button function
  async function handleSubmit(event) {
    event.preventDefault();

    //clear the errors
    setErrors([]);

    //make the game instance object to send and the appUserInfo
    const gameInstance = { boardGameId: selection, saveState: null, log: null };

    let appUserInfo = { gameInstanceId: "", appUserId: auth.user.app_user_id };

    //create the game instance

    //add the user to the game instance
    try {
      const response = await createGameInstance(gameInstance);
      //set the gameinstance id
      appUserInfo.gameInstanceId = response.id;
    } catch (error) {
      if (error.length) {
        setErrors(error);
      } else {
        setErrors([error]);
      }
    }

    try {
      await addUserToGame(appUserInfo);
      setSuccess(true);
      setSelectMessage(`Game instance successfully created. Add friends and join the game in the 'Game's I'm In' tab.`)
    } catch (error) {
      if (error.length) {
        setErrors(error);
      } else {
        setErrors([error]);
      }
    }
  }

  const closeMoveInfo = () => {
    setSuccess(false);
  }

  const handleNothing = () => {
    setErrors(["Please select a game."])
  }

  return (
    <>
      <h2>Create Games</h2>
      <Row className="justify-content-center my-4 shadow">
        <Col xl={6} md={6} className="border rounded">
         
        
          {errors && errors.length > 0 && errors.map((error, i) => (
              <Alert varient="danger" key={i}>
                {error}
              </Alert>
              
            ))}

          <Form onSubmit={handleSubmit} className="p-2">
            <Form.Label htmlFor="select">Board Games</Form.Label>
            <Form.Select
              name="select"
              onChange={(event) => setSelection(event.target.value)}
              aria-label="Select a board game"
            >
              <option  value="">
                Select a Game
              </option>
              {boardGames &&
                boardGames.map((bg, index) => {
                  return (
                    <option key={bg.name + index} value={bg.id}>
                      {bg.name}
                    </option>
                  );
                })}
            </Form.Select>
            <div className="text-center my-2">
              {selection === "" ?  <Button
                size="sm"
                variant="dark"
                className="mx-2 bg-gradient"
                onClick={handleNothing}
              >
                Create Instance
              </Button> :  <Button
                type="submit"
                size="sm"
                variant="success"
                className="mx-2 bg-gradient"
              >
                Create Instance
              </Button>}
             
            </div>
          </Form>
        </Col>
      </Row>
      {success &&   <MoveInfo
            closeMoveInfo={closeMoveInfo}
            message={selectMessage}
          ></MoveInfo>}
    </>
  );
}
