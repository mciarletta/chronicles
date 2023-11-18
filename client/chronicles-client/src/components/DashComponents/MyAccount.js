import { useState, useContext } from "react";
import AuthContext from "../contexts/AuthContext";
import ResetFunContext from "../contexts/ResetFuncContext";
import { useEffect } from "react";
import { Button, Col, Row, Alert, Form, Image } from "react-bootstrap";
import {
  getUserInfoFromId, updateColorAndAvatar,
} from "../services/GameInstanceAPI";
import MoveInfo from "../GameComponents/MoveInfo";

export default function MyAccount() {
  const auth = useContext(AuthContext);
  const { resetUser } = useContext(ResetFunContext);

  const [userInfo, setUserInfo] = useState([]);
  const [errors, setErrors] = useState([]);
  const [success, setSuccess] = useState(false);
  const [selectMessage, setSelectMessage] = useState("");
  const [color, setColor] = useState();
  const [avatar, setAvatar] = useState();

  useEffect(() => {
    //grab some user information
    getUserInfoFromId(auth.user.app_user_id)
      .then((data) => {
        setUserInfo(data);
        setColor(data.color);
        setAvatar(data.avatar);

      })
      .catch ((error) => {
        if (error.length) {
          setErrors(error);
        } else {
          setErrors([error]);
        }});
  }, [auth.user.app_user_id]);

  //submit button function
  async function handleSubmit(event) {
    event.preventDefault();

    //clear the errors
    setErrors([]);

    //update a color and avatar object
    const newColorAndAvatar = { color: color, avatar: avatar };

    try {
      await updateColorAndAvatar(newColorAndAvatar, auth.user.app_user_id);
      setSuccess(true);
      setSelectMessage(
        `Color and Avatar updated successfully. You may need to log out for changes to take effect.`
      );
    } catch (error) {
      console.log(error);
      if (error.length) {
        setErrors(error);
      } else {
        setErrors([error]);
      }    }

    resetUser();


  }

  const closeMoveInfo = () => {
    setSuccess(false);
  };


  return (
    <>
      <h2>My Account</h2>
      <Row className="justify-content-center my-4 shadow">
        <Col xl={6} md={6} className="border rounded">
          {errors &&
            errors.map((error, i) => (
              <Alert varient="danger" key={i}>
                {error}
              </Alert>
            ))}
          {userInfo && (
            <Form onSubmit={handleSubmit} className="p-2">
              <Form.Group className="mb-3" >
                <Form.Label htmlFor="color">Color</Form.Label>
                <Form.Control
                  type="color"
                  id="color"
                  name="color"
                  defaultValue={userInfo.color}
                  value={color}
                  title="Choose your color"
                  onChange={(event) => setColor(event.target.value)}
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="avatar">
                <Form.Label>Set Avatar URL</Form.Label>
                <Form.Control
                  required
                  name="avatar"
                  onChange={(event) => setAvatar(event.target.value)}
                  value={avatar}
                  type="text"
                  defaultValue={userInfo.avatar}
                  autoFocus
                />
              </Form.Group>

              <div className="d-flex justify-content-center my-4">
                {avatar && (
                  <Image
                    src={avatar}
                    alt="avatar preview"
                    style={{ height: "10rem" }}
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
                  Update
                </Button>
              </div>
            </Form>
          )}
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
