import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useState, useCallback, useEffect } from "react";
import Nav from "./components/Nav";
import Footer from "./components/Footer";
import NotFound from "./components/NotFound";
import Login from "./components/Login";
import AuthContext from "./components/contexts/AuthContext";
import { refreshToken } from "./components/services/AuthApi";
import Landing from "./components/Landing";
import SignUp from "./components/SignUp";
import MyDash from "./components/MyDash";
import GameDash from "./components/GameDash";
import ResetFunContext from "./components/contexts/ResetFuncContext";

//set a timeout to refresh tokens
const TIMEOUT_MILLISECONDS = 2 * 60 * 1000;

// Define a variable for the localStorage token item key
const LOCAL_STORAGE_TOKEN_KEY = "chronicles-token";

function App() {
  const [user, setUser] = useState(null);
  // Define a state variable to track if
  // the restore login attempt has completed
  const [initialized, setInitialized] = useState(false);

  // Define a useEffect hook callback function to attempt
  // to restore the user's token from localStorage
  const resetUser = useCallback(() => {
    refreshToken()
      .then((user) => {
        setUser(user);
        setTimeout(resetUser, TIMEOUT_MILLISECONDS);
      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => setInitialized(true));
  }, []);

  useEffect(() => {
    resetUser();
  }, [resetUser]);

  const logout = () => {
    setUser(null);
    // NEW: remove the token from localStorage
    localStorage.removeItem(LOCAL_STORAGE_TOKEN_KEY);
  };

  const auth = {
    user: user ? { ...user } : null,
    handleLoggedIn(user) {
      setUser(user);
      setTimeout(resetUser, TIMEOUT_MILLISECONDS);
    },
    hasAuthority(authority) {
      return user?.authorities.includes(authority);
    },
    logout,
  };

  if (!initialized) {
    return null;
  }

  const renderWithAuthority = (Component, ...authorities) => {
    for (let authority of authorities) {
      if (auth.hasAuthority(authority)) {
        return <Component />;
      }
    }
    return <NotFound />;
  };

  return (
    <AuthContext.Provider value={auth}>
      <ResetFunContext.Provider value={{ resetUser }}>
        <Router>
          <Nav />
          <Routes>
            <Route path="/" element={<Landing />} />
            <Route path="/login" element={<Login />} />
            <Route path="/sign-up" element={<SignUp />} />

            <Route
              path="/mydash"
              element={renderWithAuthority(MyDash, "USER", "ADMIN")}
            />

            <Route
              path="/game/:id"
              element={renderWithAuthority(GameDash, "USER", "ADMIN")}
            />
            <Route path="/error" element={<NotFound />} />

            <Route path="*" element={<NotFound />} />
          </Routes>
          <Footer />
        </Router>
      </ResetFunContext.Provider>
    </AuthContext.Provider>
  );
}

export default App;
