import { ListGroup } from "react-bootstrap";
import useClickAway from "use-click-away/useClickAway";
import { useRef } from "react";


export default function PiecesPopupMenu({
  xPos,
  yPos,
  cancelPopupMenu,
  selectingMoveInfo,
  returnToBox,
  piecesAtClick
}) {
  const clickRef = useRef("");

  const popupStyle = {
    position: "fixed",
    top: yPos,
    left: xPos,
    zIndex: 5,
    border: "1px solid #ccc",
  };

  const handleSelect = () => {
    selectingMoveInfo();
    cancelPopupMenu();
  };

  const handleReturnToBox = () => {
    returnToBox();
    cancelPopupMenu();
  };

  useClickAway(clickRef, () => {
    cancelPopupMenu();
  });



  return (
        <div
          id="piecesPopUpMenu"
          className="popup"
          style={popupStyle}
          // onMouseLeave={cancelPopupMenu}
          ref={clickRef}
        >
          <ListGroup varient="flush">
            {piecesAtClick[0].name && piecesAtClick[0].color && <ListGroup.Item style={{color: piecesAtClick[0].color}}>{piecesAtClick[0].name}</ListGroup.Item>}
            <ListGroup.Item action onClick={handleSelect}>
              Move Position
            </ListGroup.Item>
            <ListGroup.Item action onClick={handleReturnToBox}>Return to Box</ListGroup.Item>
            <ListGroup.Item action onClick={cancelPopupMenu}>
              Cancel
            </ListGroup.Item>
          </ListGroup>
        </div>

  );
}
