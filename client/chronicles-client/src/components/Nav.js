import { Link } from "react-router-dom";
import {  Nav, Navbar, NavDropdown } from "react-bootstrap";
import { useContext } from "react";
import AuthContext from "./contexts/AuthContext";
import avatar from "../files/avatar.png"



function Navigation() {


  const auth = useContext(AuthContext);

  function userAvatar() {
    if (auth.user && auth.user.avatar){
      return auth.user.avatar;
    } else {
      return avatar;
    }
  }

  return (
    <Navbar>
      <Navbar.Toggle aria-controls="basic-navbar-nav" />
      <Navbar.Brand className="ms-4" as={Link} to="/">
              <img src={userAvatar()} alt="Avatar Icon" height="40" width="40" className="rounded" />
            </Navbar.Brand>
      <Navbar.Collapse id="basic-navbar-nav">
        <Nav className="w-100 px-2 d-flex justify-content-between">
          {auth.user &&<div className="d-lg-flex">
            <NavDropdown title={auth.user.username}>
            <NavDropdown.Item as={Link} to="/mydash">
                My Dash
              </NavDropdown.Item>
              <NavDropdown.Item as={Link} to="/"  onClick={auth.logout}>
                Log Out
              </NavDropdown.Item>
            </NavDropdown>
          </div>}

          <div className="d-lg-flex align-items-center">
            {!auth.user && (
              <Nav.Link as={Link} to="/login">
                Log In
              </Nav.Link>
            )}



          </div>
        </Nav>
      </Navbar.Collapse>
    </Navbar>
  );
}

export default Navigation;
