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
  createCard,
  deleteCard,
  getCard,
  getCards,
  updateCard,
} from "../services/GameInstanceAPI";
import MoveInfo from "../GameComponents/MoveInfo";

export default function AddEditCards({ boardGameId }) {
  const [errors, setErrors] = useState([]);
  const [success, setSuccess] = useState(false);
  const [selectMessage, setSelectMessage] = useState("");
  const [boards, setBaords] = useState([]);
  const [selection, setSelection] = useState();

  const INITIAL_BOARD = {
    id: "",
    boardGameId: boardGameId,
    category: "cards",
    name: "",
    type: "",
    cardFront: "",
    cardBack: "",
    show: false,
    text: "",
    inHand: "",
    variables: ""
  };

  const [board, setBoard] = useState(INITIAL_BOARD);

  useEffect(() => {
    //grab the boards list
    getCards(boardGameId)
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
    getCards(boardGameId)
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
      getCard(selection)
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
    category: "cards",
    name: "",
    type: "",
    cardFront: "",
    cardBack: "",
    show: false,
    text: "",
    inHand: "",
    variables: ""
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
        await updateCard(board, selection);
        setSuccess(true);
        setSelectMessage(`Card updated!`);
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
        await createCard(board);
        setSuccess(true);
        setSelectMessage(`Card created!`);
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
      await deleteCard(selection);
      setSuccess(true);
      setSelectMessage(`Card deleted!`);
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
        <h4>Edit/Create Cards</h4>
        <Row className="justify-content-center my-4 shadow">
          <Col xl={6} md={6} className="border rounded">
            {errors &&
              errors.map((error, i) => (
                <Alert varient="danger" key={i}>
                  {error}
                </Alert>
              ))}

            <Form onSubmit="" className="p-2">
              <Form.Label htmlFor="select">Cards</Form.Label>
              <Form.Select
                name="select"
                onChange={(event) => setSelection(event.target.value)}
                aria-label="Select a card"
              >
                <option value="">Edit a Card</option>
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

              <Form.Group className="mb-3" controlId="type">
                <Form.Label>Type</Form.Label>
                <Form.Control
                  required
                  name="type"
                  onChange={handleChange}
                  value={board.type}
                  type="text"
                />
              </Form.Group>
            

              <Form.Group className="mb-3" controlId="cardFront">
                <Form.Label>Card's Front URL</Form.Label>
                <Form.Control
                  name="cardFront"
                  onChange={handleChange}
                  value={board.cardFront}
                  type="text"
                />
              </Form.Group>

              <div className="d-flex justify-content-center my-4">
                {board.cardFront && (
                  <Image
                    src={board.cardFront}
                    alt="card front preview"
                    style={{ height: "6rem" }}
                  ></Image>
                )}
              </div>

              <Form.Group className="mb-3" controlId="cardBack">
                <Form.Label>Card's Back URL</Form.Label>
                <Form.Control
                  name="cardBack"
                  onChange={handleChange}
                  value={board.cardBack}
                  type="text"
                />
              </Form.Group>

              <div className="d-flex justify-content-center my-4">
                {board.cardBack && (
                  <Image
                    src={board.cardBack}
                    alt="card front preview"
                    style={{ height: "6rem" }}
                  ></Image>
                )}
              </div>

              <Form.Group className="mb-3" controlId="text">
                <Form.Label>Text</Form.Label>
                <Form.Control
                  required
                  name="text"
                  onChange={handleChange}
                  value={board.text}
                  as="textarea" rows={3}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="variables">
                <Form.Label>Variables (Write the name of the variable followed by a comma followed by it's numerical value. Do not put spaces. Ex: HP,10,MP,20)</Form.Label>
                <Form.Control
                  required
                  name="variables"
                  onChange={handleChange}
                  value={board.variables}
                  as="textarea" rows={3}
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
