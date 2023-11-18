import { Button, Form, Alert, Container, Col, Row } from "react-bootstrap";
import { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { create } from "./services/AuthApi";
import AuthContext from "./contexts/AuthContext";

export default function SignUp() {
  //navigate stuff
  const navigate = useNavigate();

  //our states
  const [user, setUser] = useState("");
  const [password, setPassword] = useState("");
  const [repPassword, setRePassword] = useState("");
  const [email, setEmail] = useState("");

  const [errors, setErrors] = useState([]);

  //our contenxt for authentication
  const auth = useContext(AuthContext);

  //submit button function
  const handleSubmit = async (event) => {
    event.preventDefault();

    //clear the errors
    setErrors([]);

    //check to see if passwords match
    if (password !== repPassword){
      setErrors(["Passwords must match. Please try again."])
      return;
    }

    //go through the login
    create({ username: user, email: email, password: password })
      .then((data) => {
        auth.handleLoggedIn(data);
        navigate("/login", {
          state: { message: "SUCCESS! You can now login below." },
        });
      })
      .catch((err) => {
        setErrors([...err]);
      });
  };

  return (
    <>
      <Container>
        <Row className="justify-content-center my-4">
          <Col xl={6} md={6} className="border rounded">
            {errors.map((error, i) => (
              <Alert varient="danger" key={i}>
                {error}
              </Alert>
            ))}
            <Form onSubmit={handleSubmit}>
              <Form.Group className="mb-3" controlId="username">
                <Form.Label>Username</Form.Label>
                <Form.Control
                  required
                  name="userName"
                  onChange={(event) => setUser(event.target.value)}
                  value={user}
                  type="text"
                  placeholder="Username"
                  autoFocus
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="email">
                <Form.Label>Email</Form.Label>
                <Form.Control
                  required
                  name="userEmail"
                  onChange={(event) => setEmail(event.target.value)}
                  value={email}
                  type="text"
                  placeholder="Email"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="password">
                <Form.Label>Password</Form.Label>
                <Form.Control
                  required
                  name="userPassword"
                  onChange={(event) => setPassword(event.target.value)}
                  value={password}
                  type="password"
                  placeholder="Password"
                />
              </Form.Group>

              <Form.Group className="mb-3" controlId="password">
                <Form.Label>Re-enter Password</Form.Label>
                <Form.Control
                  required
                  name="rePassword"
                  onChange={(event) => setRePassword(event.target.value)}
                  value={repPassword}
                  type="password"
                  placeholder="Re-enter Password"
                />
              </Form.Group>

              <div className="text-center my-2">
                <Button
                  type="submit"
                  size="sm"
                  variant="primary"
                  className="mx-2"
                >
                  Sign Up
                </Button>
                <Button as={Link} variant="secondary" size="sm" to="/">
                  Cancel
                </Button>
              </div>
            </Form>
          </Col>
        </Row>
      </Container>
    </>
  );
}
