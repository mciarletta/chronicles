import { useState, useEffect } from "react";
import {
  Button,
  Col,
  Row,
  Alert,
  Form,
  Image,
  Container,
} from "react-bootstrap";
import {
  createBoard,
  deleteBoard,
  getBoard,
  getBoards,
  updateBoard,
} from "../services/GameInstanceAPI";
import MoveInfo from "../GameComponents/MoveInfo";

export default function AddEditBoards({ boardGameId }) {
  const [errors, setErrors] = useState([]);
  const [success, setSuccess] = useState(false);
  const [selectMessage, setSelectMessage] = useState("");
  const [boards, setBaords] = useState([]);
  const [selection, setSelection] = useState();

  const INITIAL_BOARD = {
    id: "",
    boardGameId: boardGameId,
    category: "boards",
    name: "",
    skin: "",
  };

  const [board, setBoard] = useState(INITIAL_BOARD);

  useEffect(() => {
    //grab the boards list
    getBoards(boardGameId)
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
    getBoards(boardGameId)
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
      getBoard(selection)
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
        category: "boards",
        name: "",
        skin: "",
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
        await updateBoard(board, selection);
        setSuccess(true);
        setSelectMessage(`Board updated!`);
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
        await createBoard(board);
        setSuccess(true);
        setSelectMessage(`Board created!`);
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
      await deleteBoard(selection);
      setSuccess(true);
      setSelectMessage(`Board deleted!`);
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
        <h4>Edit/Create Boards</h4>
        <Row className="justify-content-center my-4 shadow">
          <Col xl={6} md={6} className="border rounded">
            {errors &&
              errors.map((error, i) => (
                <Alert varient="danger" key={i}>
                  {error}
                </Alert>
              ))}

            <Form onSubmit="" className="p-2">
              <Form.Label htmlFor="select">Boards</Form.Label>
              <Form.Select
                name="select"
                onChange={(event) => setSelection(event.target.value)}
                aria-label="Select a board game"
              >
                <option value="">Edit a Board</option>
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

              <Form.Group className="mb-3" controlId="skin">
                <Form.Label>Skin</Form.Label>
                <Form.Control
                  name="skin"
                  onChange={handleChange}
                  value={board.skin}
                  type="text"
                />
              </Form.Group>

              <div className="d-flex justify-content-center my-4">
                {board.skin && (
                  <Image
                    src={board.skin}
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
