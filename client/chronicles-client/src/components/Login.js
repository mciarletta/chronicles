import {  Button, Form, Alert, Container, Col, Row } from "react-bootstrap";
import { useState, useContext } from "react";
import { useNavigate, useLocation, Link } from "react-router-dom";
import { login } from "./services/AuthApi";
import AuthContext from "./contexts/AuthContext";

export default function Login() {
  //navigate stuff
  const navigate = useNavigate();
  const location = useLocation();


  //our states
  const [user, setUser] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState([]);

  //our contenxt for authentication
  const auth = useContext(AuthContext);


  //submit button function
  const handleSubmit = async (event) => {
    event.preventDefault();

    //clear the errors
    setErrors([]);

    //go through the login
    login({username: user, password: password})
    .then(data => {
      auth.handleLoggedIn(data);
      navigate("/mydash");
    })
    .catch(err => {
      setErrors(['Invalid username/password.']);
    });

  };

  return (
    <>
    <Container>
      {location.state &&
      <Row className="d-flex justify-content-center"> 
        <Col xs={6}><Alert varient="success">
        {location.state && location.state.message}
        </Alert></Col>
    </Row>}
      <Row className="justify-content-center my-4">

      <Col xl={6} md={6} className="border rounded" >
    {errors.map((error, i) => (
        <Alert varient="danger" key={i}>{error}</Alert>
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

        <div className="text-center my-2">
        <Button type="submit" size="sm" variant="primary" className="mx-2 bg-gradient" >
          Log In
        </Button>
        <Button variant="secondary" size="sm" as={Link} to="/" className="bg-gradient">
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

