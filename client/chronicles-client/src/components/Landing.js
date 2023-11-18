import { Button } from "react-bootstrap";
import { useContext } from "react";
import AuthContext from "./contexts/AuthContext";
import { Link } from 'react-router-dom';


export default function Landing() {
  const auth = useContext(AuthContext);

  return (
    <>
      <div className="jumbotron">
        <div className="p-4 text-center">
          <h1>Chronicles: The Board Game Simulator</h1>
          <div className="landing-buttons">
            {auth.user ? (
              <>
                <div className="text-light">Welcome Back {auth.user.username}
                <Button variant="success" size="sm" className="ms-2"  as={Link} to="/mydash" >
                  My Dash
                </Button>
                </div>
              </>
            ) : (
              <>
                <Button as={Link} variant="success" size="sm" className="me-2" to="/sign-up">
                  Signup
                </Button>

                <Button variant="primary" size="sm" as={Link} to="/login" >
                  Login
                </Button>
              </>
            )}
          </div>
        </div>
      </div>
    </>
  );
}
