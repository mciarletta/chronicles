import { useState, useContext } from "react";
import AuthContext from "../contexts/AuthContext";
import { useEffect } from "react";
import { Button, Row, Col, Card, ListGroup, Modal, Image } from "react-bootstrap";
import {
  getGameInstancesByUserId,
  leaveGame,
} from "../services/GameInstanceAPI";
import { Link } from "react-router-dom";
import AddUser from "./AddUser";
import FormErrors from "./FormErrors";

export default function GamesImIn() {
  const auth = useContext(AuthContext);
  const [gameInstances, setGameInstances] = useState();
  const [show, setShow] = useState(false);
  const [showAdd, setShowAdd] = useState(false);
  const [addGi, setAddGi] = useState();
  const [errors, setErrors] = useState([]);

  const [delId, setDelId] = useState();

  const handleLeaveGame = (gameId) => {
    const appUserInfo = {
      gameInstanceId: gameId,
      appUserId: auth.user.app_user_id,
    };
    leaveGame(appUserInfo)
      .then(() => {
        setShow(false);
        loadGameInfo();
        setErrors([]);
      })
      .catch((error) => {
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }
      });
  };

  function loadGameInfo() {
    getGameInstancesByUserId(auth.user.app_user_id)
      .then((data) => {
        setGameInstances(data);
        setErrors([]);
      })
      .catch((error) => {
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }
      });
  }

  useEffect(() => {
    getGameInstancesByUserId(auth.user.app_user_id)
      .then((data) => {
        setGameInstances(data);
        setErrors([]);
      })
      .catch((error) => {
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }
      });
  }, [auth.user.app_user_id]);

  const handleClose = () => {
    setShow(false);
  };

  const handleShow = (id) => {
    setShow(true);
    setDelId(id);
  };

  const handleShowAdd = (gi) => {
    setShowAdd(true);
    setAddGi(gi);
  };

  const handleCloseAdd = () => {
    setShowAdd(false);
  };

  return (
    <>
      <AddUser
        show={showAdd}
        gi={addGi}
        handleClose={handleCloseAdd}
        forceRefresh={loadGameInfo}
      ></AddUser>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          <Modal.Title>Warning</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          You are about to remove yourself from this game.
        </Modal.Body>
        <Modal.Footer>
          <Button
            variant="secondary"
            size="sm"
            className="bg-gradient"
            onClick={handleClose}
          >
            Cancel
          </Button>
          <Button
            variant="danger"
            size="sm"
            className="bg-gradient"
            onClick={() => handleLeaveGame(delId)}
          >
            Remove and Delete
          </Button>
        </Modal.Footer>
      </Modal>
      <h2>Games I'm In</h2>
      <Row className="m-2 p-2">
        {gameInstances &&
          gameInstances.map((gi, index) => {
            return (
              <Col className="my-2" key={gi.id + index}>
                <Card style={{ width: "18rem" }} className="shadow">
                  <Card.Title className="m-2">{gi.boardGameName}</Card.Title>
                  <div className="d-flex justify-content-center my-4">
                    {gi.skin && (
                      <Image
                        src={gi.skin}
                        alt="card front preview"
                        style={{ height: "6rem" }}
                      ></Image>
                    )}
                  </div>
                  <ListGroup className="list-group-flush">
                    {gi.usernames.map((name, index) => {
                      return (
                        <ListGroup.Item key={index + name}>
                          {name}
                        </ListGroup.Item>
                      );
                    })}
                  </ListGroup>
                  <Card.Body>
                    <Button
                      as={Link}
                      size="sm"
                      variant="success"
                      className="bg-gradient"
                      to={`/game/${gi.id}`}
                    >
                      Enter Game
                    </Button>
                    <Button
                      className="mx-3 gradient"
                      size="sm"
                      variant="secondary"
                      onClick={() => handleShowAdd(gi)}
                    >
                      Add user
                    </Button>
                    <Button
                      size="sm"
                      className="my-3 gradiant"
                      variant="danger"
                      onClick={() => handleShow(gi.id)}
                    >
                      Leave Game
                    </Button>
                  </Card.Body>
                </Card>
              </Col>
            );
          })}
      </Row>
      <Row>{errors && <FormErrors errors={errors}></FormErrors>}</Row>
    </>
  );
}
