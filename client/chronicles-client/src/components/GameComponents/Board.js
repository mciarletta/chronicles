import React, { useState, useEffect, useCallback } from "react";

export default function Board({
  size,
  name,
  handleSubSquareClick,
  x,
  y,
  boardCol,
  boardRow,
  boardHeight,
  subSquareSize,
  skin,
  rotate
}) {
  //state updated subSquares
  const [subSquares, setSubSquares] = useState([]);

  //--note: useCallback is used because the the dependencies in the useEffect
  // otherwise it would re-render indefinetly

  //hand the onclick event
  const logCoordinates = useCallback(
    (event, row, col, boardCol, boardRow) => {
      // Send the information about the clicked sub-square and Board
      handleSubSquareClick({
        row: row,
        col: col,
        boardCol: boardCol,
        boardRow: boardRow,
        xPos: event.clientX,
        yPos: event.clientY,
      });
    },
    [handleSubSquareClick]
  );

  useEffect(() => {
    // Generate sub-squares as JSX elements
    const newSubSquares = [];
    for (let i = 0; i < size; i++) {
      for (let j = 0; j < size; j++) {
        //iterate and push new subSquares into the array
        newSubSquares.push(
          <div
            key={`${i}-${j}`}
            id={`${name}-${i}-${j}`}
            className="boardSubSquare"
            style={{
              width: subSquareSize + "px",
              height: subSquareSize + "px",
              top: i * subSquareSize + "px",
              left: j * subSquareSize + "px",
            }}
            //add an onclick function to each subsquare
            onClick={(event) => logCoordinates(event, i, j, boardCol, boardRow)}
          />
        );
      }
    }

    // Update state to re-render with new sub-squares
    setSubSquares(newSubSquares);
  }, [size, name, logCoordinates, subSquareSize, boardCol, boardRow]);

  //returns a board class div filled with an array of subsquares
  return (
    <>
      <div
        className="board"
        style={{
          top: y,
          left: x,
          height: boardHeight,
          width: boardHeight,
        }}
      >
        <img
          src={skin}
          alt="your board"
          style={{ height: boardHeight, width: boardHeight, transform: `rotate(${rotate}deg)`, transition: '3s' }}
        />
        {subSquares}
      </div>
    </>
  );
}
