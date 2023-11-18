import { useState } from "react";
import { Button, Col, Row, Alert, Form, Image } from "react-bootstrap";
import {
  createBoardGame,
} from "../services/GameInstanceAPI";
import MoveInfo from "../GameComponents/MoveInfo";

const INITIAL_BOARD_GAME = {
  name: "",
  height: 800,
  boardSlots: "",
  placesPerBoard: "",
  skin: "",
};

export default function CreateBoardGame() {

  const [errors, setErrors] = useState([]);
  const [success, setSuccess] = useState(false);
  const [selectMessage, setSelectMessage] = useState("");

  const [boardGame, setBoardGame] = useState(INITIAL_BOARD_GAME);

  //submit button function
  async function handleSubmit(event) {
    event.preventDefault();

    //clear the errors
    setErrors([]);

    //update a color and avatar object

    try {
      await createBoardGame(boardGame);
      setSuccess(true);
      setSelectMessage(`Board Game created!`);
    } catch (error) {
      console.log(error);
      if (error.length) {
        setErrors(error);
      } else {
        setErrors([error]);
      }
    }
  }

  const closeMoveInfo = () => {
    setSuccess(false);
  };

  const handleChange = (evt) => {
    const nextGame = { ...boardGame };
    if (evt.target.type === "number") {
      nextGame[evt.target.name] = handleNumberChange(evt);
    } else {
      nextGame[evt.target.name] = evt.target.value;
    }
    setBoardGame(nextGame);
  };

  const handleNumberChange = (evt) => {
    let nextValue = evt.target.value;
    nextValue = parseInt(nextValue, 10);
    return isNaN(nextValue) ? evt.target.value : nextValue;
  };

  return (
    <>
      <h2>Create a New Board Game</h2>
      <Row className="justify-content-center my-4 shadow">
        <Col xl={6} md={6} className="border rounded">
          {errors &&
            errors.map((error, i) => (
              <Alert varient="danger" key={i}>
                {error}
              </Alert>
            ))}

          <Form onSubmit={handleSubmit} className="p-2">
            <Form.Group className="mb-3" controlId="name">
              <Form.Label>Name</Form.Label>
              <Form.Control
                required
                name="name"
                onChange={handleChange}
                value={boardGame.name}
                type="text"
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="boardSlots">
              <Form.Label>Board Slots</Form.Label>
              <Form.Control
                required
                name="boardSlots"
                onChange={handleChange}
                value={boardGame.boardSlots}
                type="number"
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="placesPerBoard">
              <Form.Label>Places Per Board</Form.Label>
              <Form.Control
                required
                name="placesPerBoard"
                onChange={handleChange}
                value={boardGame.placesPerBoard}
                type="number"
              />
            </Form.Group>

            <Form.Group className="mb-3" controlId="skin">
                <Form.Label>Skin</Form.Label>
                <Form.Control
                  name="skin"
                  onChange={handleChange}
                  value={boardGame.skin}
                  type="text"
                />
              </Form.Group>

              <div className="d-flex justify-content-center my-4">
                {boardGame.skin && (
                  <Image
                    src={boardGame.skin}
                    alt="skin preview"
                    style={{ height: "6rem" }}
                  ></Image>
                )}
              </div>

            <div className="text-center my-2">
              <Button
                type="submit"
                size="sm"
                variant="success"
                className="mx-2 bg-gradient"
              >
                Create
              </Button>
            </div>
          </Form>
        </Col>
      </Row>
      {success && (
        <MoveInfo
          closeMoveInfo={closeMoveInfo}
          message={selectMessage}
        ></MoveInfo>
      )}
    </>
  );
}
