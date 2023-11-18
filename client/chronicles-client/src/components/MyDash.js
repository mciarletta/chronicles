import {
  Container,
  Row,
  Col,
  ToggleButton,
  ToggleButtonGroup,
} from "react-bootstrap";
import AuthContext from "./contexts/AuthContext";
import { useContext, useEffect, useState } from "react";
import StartGame from "./DashComponents/StartGame"
import GamesImIn from "./DashComponents/GamesImIn";
import MyAccount from "./DashComponents/MyAccount";
import CreateBoardGame from "./DashComponents/CreateBoardGame";
import Edit from "./DashComponents/Edit";

export default function MyDash() {
  const auth = useContext(AuthContext);
  const [option, setOption] = useState("games")

  useEffect(() => {

  }, [option])


  return (
    <>
      <Container className="shadow-lg">
        <Row>
          <Col xs={3}>
            <h1>My Dash</h1>
            <ToggleButtonGroup
              type="radio"
              name="options"
              defaultValue={option}
              vertical
            >
              <ToggleButton
                id="games"
                value="games"
                className="my-2 rounded bg-gradient"
                variant="dark"
                onClick={() => setOption("games")}
                
              >
                Games I'm in
              </ToggleButton>
              <ToggleButton
                id="start"
                value="start"
                className="my-2 rounded bg-gradient"
                variant="dark"
                onClick={() => setOption("start")}

              >
                Create a Game
              </ToggleButton>
              <ToggleButton
                id="account"
                value="account"
                className="my-2 rounded bg-gradient"
                variant="dark"
                onClick={() => setOption("account")}

              >
                My Account
              </ToggleButton>
              <ToggleButton
                id="create"
                value={3}
                className="my-2 rounded bg-gradient"
                variant="dark"
                disabled={!auth.user.authorities.includes("ADMIN")}
                onClick={() => setOption("create")}

              >
                Create a new Board Game
              </ToggleButton>

              <ToggleButton
                id="edit"
                value={4}
                className="my-2 rounded bg-gradient"
                variant="dark"
                disabled={!auth.user.authorities.includes("ADMIN")}
                onClick={() => setOption("edit")}

              >
                Edit a Board Game
              </ToggleButton>
            </ToggleButtonGroup>
          </Col>
          <Col>
          {option === "start"  && <StartGame></StartGame>}
          {option === "games"  && <GamesImIn></GamesImIn>}
          {option === "account"  && <MyAccount></MyAccount>}
          {option === "create"  && <CreateBoardGame></CreateBoardGame>}
          {option === "edit"  && <Edit></Edit>}




          </Col>
        </Row>
      </Container>
    </>
  );
}
