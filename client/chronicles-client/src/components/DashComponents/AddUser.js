import { Form, Modal, Button } from "react-bootstrap";
import {
  addUserToGame,
  getUserIdfromUsername,
} from "../services/GameInstanceAPI";
import { useState } from "react";
import FormErrors from "./FormErrors";

export default function AddUser({ show, handleClose, gi, forceRefresh }) {
  const [username, setUsername] = useState("");
  const [errors, setErrors] = useState([]);

  async function handleAddUser() {

    let appUserInfo = { gameInstanceId: gi.id, appUserId: "" };

    try {
        setErrors([]);

      const response = await getUserIdfromUsername(username);

      appUserInfo.appUserId = response.appUserId;

    } catch (error) {
      console.log(error);
      if (error.length) {
        setErrors(error);
      } else {
        setErrors([error]);
      }
    }

    try {
        await addUserToGame(appUserInfo);
  
        closeThisModale();


      } catch (error) {
        console.log(error);
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }
      }


  }

  function closeThisModale(){
    forceRefresh();
    handleClose();
  }


  return (
    <>
      <Modal show={show} onHide={handleClose}>
        <Modal.Header closeButton>
          {gi && (
            <Modal.Title>Add User to game: {gi.boardGameName}</Modal.Title>
          )}
        </Modal.Header>
        <Modal.Body>
          Please enter the username of the user you wish to add to this game.
          <Form >
            <Form.Group className="mb-3" controlId="username">
              <Form.Label>Username</Form.Label>
              <Form.Control
                type="text"
                placeholder="McGee"
                name="username"
                value={username}
                onChange={(event) => setUsername(event.target.value)}
              />
            </Form.Group>
            <FormErrors errors={errors} />
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" size="sm" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="danger" size="sm" onClick={handleAddUser}>
            Add User
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
