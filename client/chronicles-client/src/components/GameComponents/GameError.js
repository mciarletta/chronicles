import { Alert, Button } from "react-bootstrap";
import { motion } from "framer-motion";
import useClickAway from "use-click-away/useClickAway";
import { useRef } from "react";

export default function GameError({ closeGameError, showError }) {
  const clickRef = useRef("");

  useClickAway(clickRef, () => {
    closeGameError();
  });

  const moveInfoStyle = {
    position: "fixed",
    zIndex: 1,
    border: "1px solid #ccc",
  };

  const slideInAnimation = {
    hidden: { x: -100 }, // Start position outside the viewport
    visible: { x: 0 }, // End position at 0 (visible)
  };

  if (showError.show === true) {
    return (
      <>
        <motion.div
          initial="hidden" // Initial animation state
          animate="visible" // Animation state to transition to
          variants={slideInAnimation}
          transition={{ type: "spring", stiffness: 120 }}
          style={{ position: "relative" }}
        >
          <Alert variant="danger" ref={clickRef} style={moveInfoStyle}>
            {showError.message}
            <Button
              className="ms-5"
              onClick={closeGameError}
              size="sm"
              variant="outline-secondary"
            >
              Cancel
            </Button>
          </Alert>
        </motion.div>
      </>
    );
  } else {
    return null;
  }
}
