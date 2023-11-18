import { useState, useEffect } from "react";
import {
  Button,
  Col,
  Row,
  Alert,
  Form,
  Container,
} from "react-bootstrap";
import {
  createDie,
  deleteDie,
  getDie,
  getDice,
  updateDie,
} from "../services/GameInstanceAPI";
import MoveInfo from "../GameComponents/MoveInfo";

export default function AddEditDice({ boardGameId }) {
  const [errors, setErrors] = useState([]);
  const [success, setSuccess] = useState(false);
  const [selectMessage, setSelectMessage] = useState("");
  const [boards, setBaords] = useState([]);
  const [selection, setSelection] = useState();

  const INITIAL_BOARD = {
    id: "",
    boardGameId: boardGameId,
    category: "die",
    name: "",
    color: "",
    background: "",
    side1: "",
    side2: "",
    side3: "",
    side4: "",
    side5: "",
    side6: "",
    rolling: false,
    winningSide: ""

  };

  const [board, setBoard] = useState(INITIAL_BOARD);

  useEffect(() => {
    //grab the boards list
    getDice(boardGameId)
      .then((data) => setBaords(data))
      .catch((error) => {
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }
      });
  }, [boardGameId]);

  function updateBoardsList() {
    //grab the boards list
    getDice(boardGameId)
      .then((data) => setBaords(data))
      .catch((error) => {
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }
      });
  }

  useEffect(() => {
    //grab the board to update
    if (selection) {
      getDie(selection)
        .then((data) => setBoard(data))
        .catch((error) => {
          if (error.length) {
            setErrors(error);
          } else {
            setErrors([error]);
          }
        });
    } else {
      setBoard({
        id: "",
    boardGameId: boardGameId,
    category: "die",
    name: "",
    color: "",
    background: "",
    side1: "",
    side2: "",
    side3: "",
    side4: "",
    side5: "",
    side6: "",
    rolling: false,
    winningSide: ""
      });
    }
  }, [selection, boardGameId]);

  //submit button function
  async function handleSubmit(event) {
    event.preventDefault();

    //clear the errors
    setErrors([]);

    //check to see if there is a selection
    if (selection) {
      try {
        //set the idea for the update
        board.id = selection;
        await updateDie(board, selection);
        setSuccess(true);
        setSelectMessage(`Die updated!`);
      } catch (error) {
        console.log(error);
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }
      }
    } else {
      try {
        await createDie(board);
        setSuccess(true);
        setSelectMessage(`Die created!`);
      } catch (error) {
        console.log(error);
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }
      }
    }

    updateBoardsList();
    setBoard(INITIAL_BOARD);
  }

  const closeMoveInfo = () => {
    setSuccess(false);
  };

  const handleChange = (evt) => {
    const nextGame = { ...board };
    if (evt.target.type === "number") {
      nextGame[evt.target.name] = handleNumberChange(evt);
    } else {
      nextGame[evt.target.name] = evt.target.value;
    }
    setBoard(nextGame);
  };

  const handleNumberChange = (evt) => {
    let nextValue = evt.target.value;
    nextValue = parseInt(nextValue, 10);
    return isNaN(nextValue) ? evt.target.value : nextValue;
  };

  async function handleDelete() {
    //clear the errors
    setErrors([]);

    try {
      await deleteDie(selection);
      setSuccess(true);
      setSelectMessage(`Die deleted!`);
    } catch (error) {
      console.log(error);
      if (error.length) {
        setErrors(error);
      } else {
        setErrors([error]);
      }
    }

    updateBoardsList();
    setBoard(INITIAL_BOARD);


  }

  return (
    <>
      <Container>
        <h4>Edit/Create Dice</h4>
        <Row className="justify-content-center my-4 shadow">
          <Col xl={6} md={6} className="border rounded">
            {errors &&
              errors.map((error, i) => (
                <Alert varient="danger" key={i}>
                  {error}
                </Alert>
              ))}

            <Form onSubmit="" className="p-2">
              <Form.Label htmlFor="select">Dice</Form.Label>
              <Form.Select
                name="select"
                onChange={(event) => setSelection(event.target.value)}
                aria-label="Select a die"
              >
                <option value="">Edit a Die</option>
                {boards &&
                  boards.map((board, index) => {
                    return (
                      <option key={board.name + index} value={board.id}>
                        {board.name}
                      </option>
                    );
                  })}
              </Form.Select>
            </Form>

            <Form onSubmit={handleSubmit} className="p-2">
              <Form.Group className="mb-3" controlId="name">
                <Form.Label>Name</Form.Label>
                <Form.Control
                  required
                  name="name"
                  onChange={handleChange}
                  value={board.name}
                  type="text"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="color">
                <Form.Label htmlFor="color">Text Color</Form.Label>
                <Form.Control
                  type="color"
                  id="color"
                  name="color"
                  value={board.color}
                  title="Choose a color"
                  onChange={handleChange}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="background">
                <Form.Label htmlFor="color">Background Color</Form.Label>
                <Form.Control
                  type="color"
                  id="background"
                  name="background"
                  value={board.background}
                  title="Choose a color"
                  onChange={handleChange}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="side1">
                <Form.Label>Side 1</Form.Label>
                <Form.Control
                  name="side1"
                  onChange={handleChange}
                  value={board.side1}
                  type="text"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="side2">
                <Form.Label>Side 2</Form.Label>
                <Form.Control
                  name="side2"
                  onChange={handleChange}
                  value={board.side2}
                  type="text"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="side3">
                <Form.Label>Side 3</Form.Label>
                <Form.Control
                  name="side3"
                  onChange={handleChange}
                  value={board.side3}
                  type="text"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="side4">
                <Form.Label>Side 4</Form.Label>
                <Form.Control
                  name="side4"
                  onChange={handleChange}
                  value={board.side4}
                  type="text"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="side5">
                <Form.Label>Side 5</Form.Label>
                <Form.Control
                  name="side5"
                  onChange={handleChange}
                  value={board.side5}
                  type="text"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="side6">
                <Form.Label>Side 6</Form.Label>
                <Form.Control
                  name="side6"
                  onChange={handleChange}
                  value={board.side6}
                  type="text"
                />
              </Form.Group>

              <div className="text-center my-2">
                <Button
                  type="submit"
                  size="sm"
                  variant="success"
                  className="mx-2 bg-gradient"
                >
                  {selection ? "Update" : "Create"}
                </Button>
                {selection && (
                  <Button
                    size="sm"
                    variant="danger"
                    className="mx-2 bg-gradient"
                    onClick={handleDelete}
                  >
                    Delete
                  </Button>
                )}
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
      </Container>
    </>
  );
}
