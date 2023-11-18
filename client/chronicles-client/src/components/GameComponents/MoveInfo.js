import { Alert, Button } from "react-bootstrap";
import { motion } from "framer-motion";

export default function MoveInfo({
  message = "Select to Move",
  closeMoveInfo,
}) {

  const moveInfoStyle = {
    position: "fixed",
    zIndex: 1,
    border: "1px solid #ccc",
  };

  const slideInAnimation = {
    hidden: { x: -100 }, // Start position outside the viewport
    visible: { x: 0 }, // End position at 0 (visible)
  };

  return (
    <>
      <motion.div
        initial="hidden" // Initial animation state
        animate="visible" // Animation state to transition to
        variants={slideInAnimation}
        transition={{ type: "spring", stiffness: 120 }}
        style={{ position: 'relative' }}
      >
        <Alert variant="success" style={moveInfoStyle}>
          {message}
          <Button
            className="ms-5"
            onClick={closeMoveInfo}
            size="sm"
            variant="outline-secondary"
          >
            Cancel
          </Button>
        </Alert>
      </motion.div>
    </>
  );
}
