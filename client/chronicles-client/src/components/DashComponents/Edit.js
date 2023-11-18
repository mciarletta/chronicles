import { useState } from "react";
import { useEffect } from "react";
import {  Col, Row, Alert, Form } from "react-bootstrap";
import {
  getBoardGames,
} from "../services/GameInstanceAPI";
import AddEditBoards from "./AddEditBoards";
import AddEditFigures from "./AddEditFigures"
import AddEditDice from "./AddEditDice";
import AddEditCards from "./AddEditCards";

export default function Edit() {
  const [boardGames, setBoardgame] = useState([]);
  const [errors, setErrors] = useState([]);
  const [selection, setSelection] = useState("");




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

 

  return (
    <>
      <h2>Edit Board Games</h2>
      <Row className="justify-content-center my-4 shadow">
        <Col xl={6} md={6} className="border rounded">
         
        
          {errors &&
            errors.map((error, i) => (
              <Alert varient="danger" key={i}>
                {error}
              </Alert>
            ))}
          <Form onSubmit="" className="p-2">
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

          </Form>
        </Col>
      </Row>

    {/* Boards */}
      <Row>
    {selection && <AddEditBoards boardGameId={selection}></AddEditBoards>}
      </Row>

    {/* Cards */}
    <Row>
    {selection && <AddEditCards boardGameId={selection}></AddEditCards>}
      </Row>
    {/* Dice */}

    <Row>
    {selection && <AddEditDice boardGameId={selection}></AddEditDice>}
      </Row>

    {/* Figures */}
    <Row>
    {selection && <AddEditFigures boardGameId={selection}></AddEditFigures>}
      </Row>


    </>
  );
}
