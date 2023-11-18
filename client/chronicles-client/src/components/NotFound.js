import { Container } from "react-bootstrap";
import { useLocation } from "react-router-dom";
import { useContext } from "react";
import AuthContext from "./contexts/AuthContext";

//just a simple page for 404 errors
export default function NotFound() {
  const location = useLocation();
  const auth = useContext(AuthContext);

  //lets try loggin that user out for fun
  auth.logout();

  return (
    <Container>
    <div>
      {location.state && location.state.message && (
        <p>{location.state.message}</p>
      )}
      {!location.state && (
        <div>
          <h1>404</h1>
          <p>Page not found</p>
        </div>
      )}
    </div>
    </Container>
  );
}
