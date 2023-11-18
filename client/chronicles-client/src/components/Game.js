import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import Board from "./GameComponents/Board";
import Piece from "./GameComponents/Piece";
import PiecesPopupMenu from "./GameComponents/PiecesPopupMenu";
import { over } from "stompjs";
import SockJS from "sockjs-client";
import { Row, Col, Button, ButtonGroup, Container } from "react-bootstrap";
import SelectedHighlight from "./GameComponents/SelectedHighlight";
import MoveInfo from "./GameComponents/MoveInfo";
import BoxMenu from "./GameComponents/BoxMenu";
import BoxContext from "./contexts/BoxContext";
import DiscardContext from "./contexts/DiscardContext";
import BlankBoard from "./GameComponents/BlankBoard";
import BoardOptions from "./GameComponents/BoardOptions";
import GameError from "./GameComponents/GameError";
import Die from "./GameComponents/Die";
import DieMenu from "./GameComponents/DieMenu";
import PlayerHands from "./GameComponents/PlayerHands";
import PlayersContext from "./contexts/PlayersContext";
import BoardsContext from "./contexts/BoardsContext";
import ViewCard from "./GameComponents/ViewCard";
import Logger from "./GameComponents/Logger";
import AuthContext from "./contexts/AuthContext";
import {
  saveGameInstance,
  getGameInstancesById,
} from "./services/GameInstanceAPI";
import SharedHand from "./GameComponents/SharedHand";

//create a null stompClient used for websocket connections
let stompClient = null;

const categories = ["boards", "figures", "cards", "Discarded Cards"];

const initialDiscardPile = [];

/**
 * Renders the visual display for a board game.
 * @returns
 */
export default function Game({ boxFromDb, boardGameInfo, gameInstance }) {
  const navigate = useNavigate();
  //------------------------States------------------------------------------------//
  const auth = useContext(AuthContext);

  //holds state information for the list of boards and pieces out of the box
  const [pieces, setPieces] = useState([]); //took out storedBoards for now
  const [boards, setBoards] = useState([]);
  const [popupMenuIsVisible, setpopupMenuIsVisible] = useState(false);
  const [popupPosition, setPopupPosition] = useState({ x: 0, y: 0 });
  const [highlighted, setHighlighted] = useState([]);
  const [selecting, setSelecting] = useState(false);
  const [piecesAtClick, setPiecesAtClick] = useState([]);
  const [box, setBox] = useState(boxFromDb);
  const [discardPile, setDiscardPile] = useState(initialDiscardPile);
  const [selectMessage, setSelectMessage] = useState("");
  const [pieceFromBox, setPieceFromBox] = useState(null);
  const [showError, setShowError] = useState({ show: false, message: "" });
  const [die, setDie] = useState([]);
  const [rolling, setRolling] = useState(false);
  const [cards, setCards] = useState([]);
  const [viewCard, setViewCard] = useState(false);
  const [cardToView, setCardToView] = useState();
  const [messages, setMessages] = useState([]);
  const [players, setPlayers] = useState([]);
  const [connected, setConnected] = useState(false);

  //------------------------Local Storage------------------------------------------------//

  const boardHeight =
    boardGameInfo.height / Math.sqrt(boardGameInfo.boardSlots);
  const placeHeight = boardHeight / Math.sqrt(boardGameInfo.placesPerBoard);
  const boardCoordinates = getCoordinatesAndPosition(
    Math.sqrt(boardGameInfo.boardSlots),
    boardHeight
  ); //the number of boards is 9, the size of the game area is 800
  const placeCoordinates = getCoordinatesAndPosition(
    Math.sqrt(boardGameInfo.placesPerBoard),
    placeHeight
  );

  //make a game state
  function makeSaveState() {
    //get all the bits and add them to a mega list
    let saveState = [];
    saveState.push(boards);
    saveState.push(pieces);
    saveState.push(die);
    saveState.push(cards);
    saveState.push(box);
    saveState.push(discardPile);
    saveState.push(messages);

    const ss = JSON.stringify(saveState);
    // const pay = '"' + ss + '"';

    let newGameInstance = { ...gameInstance, saveState: ss };

    saveGameInstance(gameInstance.id, newGameInstance)
      .then(() => addMessage("Game Saved!"))
      .catch((e) => addMessage("Something went wrong when saving: " + e));
  }

  function loadState() {
    //grab the instance info, get a new one in case there was a save
    getGameInstancesById(gameInstance.id)
      .then((data) => loadFromData(data))
      .catch((e) => addMessage("Something went wrong when loading: " + e));
  }

  function loadFromData(data) {
    const saveInfo = JSON.parse(data.saveState);
    sendBoards(saveInfo[0]);
    sendPiececs(saveInfo[1]);
    sendDice(saveInfo[2]);
    sendCards(saveInfo[3]);
    sendBox(saveInfo[4]);
    sendDiscard(saveInfo[5]);
    sendLog(saveInfo[6]);
  }

  /**
   * Calculates a grid of subsquares. Only works for squares currently
   * @param {*} size the length of the side of the square (rows or columns)
   * @param {*} height the length of the side in pixels
   * @returns a list of coordinates giving the top left position of each subsquare in pixels
   */
  function getCoordinatesAndPosition(size, height) {
    //create an empty list
    const coordinates = [];

    //iterate through the rows and columns (in this case, they are the same since it's square)
    for (let i = 0; i < size; i++) {
      for (let j = 0; j < size; j++) {
        //push the new info into the list
        coordinates.push({
          col: i,
          row: j,
          top: j * height,
          left: i * height,
        });
      }
    }

    //return the list of coordinates
    return coordinates;
  }

  //the game instance room
  const gameRoom = gameInstance.id;

  //holds the calculated info for the game area used throughout
  let GameAreaInfo = {
    height: boardGameInfo.height, //note at the momemnt, if this changes, boards that are already placed need to be updated to recalculate thier dimensions
    boardSlots: boardGameInfo.boardSlots,
    boardHeight: boardHeight,
    placesPerBoard: boardGameInfo.placesPerBoard,
    placeHeight: placeHeight,
    boardCoordinates: boardCoordinates,
    placeCoordinates: placeCoordinates,
  };

  //--------------------------------WebSocket Connections----------------------------//
  const connect = () => {
    //The End point is specified in the server. Using the default ws end point.
    let Sock = new SockJS("http://localhost:8080/ws");

    stompClient = over(Sock);

    stompClient.connect({}, onConnected, onError);
  };

  const onConnected = () => {
    //subscribe to endpoints to listen for messages, takes a callback function
    //uses the gameRoom to set an endpoint particular to this game
    stompClient.subscribe(`/room/boards/${gameRoom}`, handleBoardUpdate);
    stompClient.subscribe(`/room/pieces/${gameRoom}`, handlePieceUpdate);
    stompClient.subscribe(`/room/dice/${gameRoom}`, handleDiceUpdate);
    stompClient.subscribe(`/room/cards/${gameRoom}`, handleCardUpdate);
    stompClient.subscribe(`/room/box/${gameRoom}`, handleBoxUpdate);
    stompClient.subscribe(`/room/log/${gameRoom}`, handleLogUpdate);
    stompClient.subscribe(`/room/highlight/${gameRoom}`, handleHighlightUpdate);
    stompClient.subscribe(`/room/discard/${gameRoom}`, handleDiscardUpdate);
    stompClient.subscribe(`/room/${gameRoom}`, handleRoomUpdate);

    //send username info
    stompClient.send(
      `/app/room/add/${gameRoom}`,
      {},
      `${auth.user.username},${auth.user.avatar},${auth.user.color},${auth.user.app_user_id}`
    );

    //set the connection state
    setConnected(true);
  };

  const onError = (err) => {
    setShowError({
      show: true,
      message:
        "Error connecting to the server. Try turning it off and on again.",
    });
  };

  window.addEventListener("beforeunload", () => {
    // Close the WebSocket connection before leaving the page
    stompClient.disconnect();
  });

  //updates the room when recieving information
  const handleRoomUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    const userList = [];

    for (let i = 0; i < payloadData.length; i++) {
      const user = payloadData[i];
      userList.push(user);
    }

    // Update the state
    setPlayers([...userList]);
  };

  //updates the boards when recieving information
  const handleBoardUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    //update the boards
    setBoards(payloadData);
  };

  //updates the pieces when recieving information
  const handlePieceUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    // Update the state using the functional form of setPieces
    setPieces(payloadData);
  };

  const handleDiceUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    //update the boards
    setDie(payloadData);

    //check if they are rolling
    if (payloadData) {
      for (const d of payloadData) {
        if (d.rolling) {
          setRolling(true);
          return;
        }
      }
    }
    setRolling(false);
  };

  const handleCardUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    //update the boards
    setCards(payloadData);
  };

  const handleBoxUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    //update the boards
    setBox(payloadData);
  };

  const handleLogUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    //check if it's an array update
    if (Array.isArray(payloadData)) {
      setMessages([...messages, ...payloadData]);
    } else {
      //update the log
      setMessages((previous) => [...previous, payloadData]);
    }
  };

  const handleHighlightUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    //update the boards
    setHighlighted(payloadData);
  };

  const handleDiscardUpdate = (payload) => {
    // Parse the payload body as JSON
    const payloadData = JSON.parse(payload.body);

    //update the boards
    setDiscardPile(payloadData);
  };

  //----------send functions--------------//

  function sendBoards(boards) {
    try {
      stompClient.send(
        `/app/room/boardsUpdate/${gameRoom}`,
        {},
        JSON.stringify(boards)
      );
    } catch {
      navigate("/error", {
        state: { message: "You have been logged out due to inactivity." },
      });
    }
  }

  function sendPiececs(pieces) {
    try {
      stompClient.send(
        `/app/room/piecesUpdate/${gameRoom}`,
        {},
        JSON.stringify(pieces)
      );
    } catch {
      navigate("/error", {
        state: { message: "You have been logged out due to inactivity." },
      });
    }
  }

  function sendDice(dice) {
    try {
      stompClient.send(
        `/app/room/diceUpdate/${gameRoom}`,
        {},
        JSON.stringify(dice)
      );
    } catch {
      navigate("/error", {
        state: { message: "You have been logged out due to inactivity." },
      });
    }
  }

  function sendCards(cards) {
    try {
      stompClient.send(
        `/app/room/cardsUpdate/${gameRoom}`,
        {},
        JSON.stringify(cards)
      );
    } catch {
      navigate("/error", {
        state: { message: "You have been logged out due to inactivity." },
      });
    }
  }

  function sendLog(log) {
    try {
      stompClient.send(
        `/app/room/logUpdate/${gameRoom}`,
        {},
        JSON.stringify(log)
      );
    } catch {
      navigate("/error", {
        state: { message: "You have been logged out due to inactivity." },
      });
    }
  }

  function sendHighlights(highlights) {
    try {
      stompClient.send(
        `/app/room/highlightUpdate/${gameRoom}`,
        {},
        JSON.stringify(highlights)
      );
    } catch {
      navigate("/error", {
        state: { message: "You have been logged out due to inactivity." },
      });
    }
  }

  function sendBox(box) {
    try {
      stompClient.send(
        `/app/room/boxUpdate/${gameRoom}`,
        {},
        JSON.stringify(box)
      );
    } catch {
      navigate("/error", {
        state: { message: "You have been logged out due to inactivity." },
      });
    }
  }

  function sendDiscard(discard) {
    try {
      stompClient.send(
        `/app/room/discardUpdate/${gameRoom}`,
        {},
        JSON.stringify(discard)
      );
    } catch {
      navigate("/error", {
        state: { message: "You have been logged out due to inactivity." },
      });
    }
  }

  //--------------------------------Setting Boards and Pieces----------------------------//

  //takes a row and col position to put down a board. Event is ignored, but needed in params
  const setBoard = (event, row = 1, col = 1, board) => {
    //find the top and left of the board you wish to position at a specified row and col combination
    const coordinates = GameAreaInfo.boardCoordinates.find(
      (obj) => obj.row === row && obj.col === col
    );

    //set a board to update based on the board information
    const boardToUpdate = {
      boxId: board.boxId,
      id: board.id,
      name: board.name,
      size: Math.sqrt(GameAreaInfo.placesPerBoard), //get the length of the side (rows or columns)
      top: coordinates.top,
      left: coordinates.left,
      subSquareSize: GameAreaInfo.placeHeight,
      col: col,
      row: row,
      skin: board.skin,
      category: board.category,
      rotate: 0,
    };

    // add the board
    const boardList = [...boards, boardToUpdate];

    setBoards(boardList);

    addMessage(
      `${auth.user.username} set a board ${board.name} at position ${col}, ${row}`
    );

    //send the updated boards over the websocket
    sendBoards(boardList);
  };

  const rotateBoard = (boardCol, boardRow, rotate, name) => {
    //find the board to rotate, it must be at the boardRow and boardCol position
    const selectedBoard = boards.filter(
      (obj) => obj.col === boardCol && obj.row === boardRow
    );

    //update its rotation
    selectedBoard[0].rotate += rotate;

    //update the boards
    const boardList = [...boards];
    setBoards(boardList);

    //get the pieces on that board
    const selectedPieces = pieces.filter(
      (obj) => obj.boardCol === boardCol && obj.boardRow === boardRow
    );

    addMessage(`${auth.user.username} rotates ${name} ${rotate} degrees`);
    sendBoards(boardList);

    //check if there are any pieices first
    if (!selectedPieces.length) {
      return;
    }

    //update the pieces on that board to rotate accordingly
    const length = Math.sqrt(boardGameInfo.placesPerBoard);
    switch (rotate) {
      case 90:
        for (const p of selectedPieces) {
          const pNewCol = length - p.row - 1;
          const pNewRow = p.col;
          p.col = pNewCol;
          p.row = pNewRow;
        }
        break;

      case 180:
        for (const p of selectedPieces) {
          const pNewCol = length - p.col - 1;
          const pNewRow = length - p.row - 1;
          p.col = pNewCol;
          p.row = pNewRow;
        }
        break;

      //270 deg
      default:
        //swith the row and col and then for the col, get the length - the orginal row -1
        for (const p of selectedPieces) {
          const pNewCol = p.row;
          const pNewRow = length - p.col - 1;
          p.col = pNewCol;
          p.row = pNewRow;
        }
    }

    //update the pieces
    const piecesList = [...pieces];
    setPieces(piecesList);

    //send the update to the room
    sendPiececs(piecesList);
  };

  const swapBoard = (b, bs) => {
    //hold the b coordinates
    const bRow = b.row;
    const bCol = b.col;
    const bsRow = bs.row;
    const bsCol = bs.col;

    //hold the tops and left
    const bTop = b.top;
    const bLeft = b.left;
    const bsTop = bs.top;
    const bsLeft = bs.left;

    //get the boards
    //get the pieces on that board
    const bBoard = boards.filter(
      (obj) => obj.col === b.col && obj.row === b.row
    );

    const bsBoard = boards.filter(
      (obj) => obj.col === bs.col && obj.row === bs.row
    );

    //swap them
    bBoard[0].row = bsRow;
    bBoard[0].col = bsCol;
    bBoard[0].top = bsTop;
    bBoard[0].left = bsLeft;

    bsBoard[0].row = bRow;
    bsBoard[0].col = bCol;
    bsBoard[0].top = bTop;
    bsBoard[0].left = bLeft;

    //update the boards
    const boardList = [...boards];
    setBoards(boardList);

    //get the pieces ofrom the boards
    const bBoardPieces = pieces.filter(
      (obj) => obj.boardCol === bCol && obj.boardRow === bRow
    );
    const bsBoardPieces = pieces.filter(
      (obj) => obj.boardCol === bsCol && obj.boardRow === bsRow
    );

    //swap them
    if (bBoardPieces.length) {
      for (const p of bBoardPieces) {
        p.boardCol = bsCol;
        p.boardRow = bsRow;
      }
    }

    if (bsBoardPieces.length) {
      for (const p of bsBoardPieces) {
        p.boardCol = bCol;
        p.boardRow = bRow;
      }
    }

    if (bsBoardPieces.length || bBoardPieces.length) {
      //update the pieces
      const piecesList = [...pieces];
      setPieces(piecesList);
      sendPiececs(piecesList);
    }

    addMessage(`${auth.user.username} swaps ${b.name} with ${bs.name}`);
    //send the update to the room
    sendBoards(boardList);
  };

  //called when any board's subsquare is clicked
  const handleSubSquareClick = ({
    row,
    col,
    boardCol,
    boardRow,
    xPos,
    yPos,
  }) => {
    //set a highlighted element here since it is clicked
    const newHighlight = {
      user: auth.user.username, //TODO: get the username from the db
      gamePosX:
        col * GameAreaInfo.placeHeight + GameAreaInfo.boardHeight * boardCol,
      gamePosY:
        row * GameAreaInfo.placeHeight + GameAreaInfo.boardHeight * boardRow,
      color: auth.user.color,
    };

    //check to see if this user already has a highlighted object
    const existingObjectIndex = highlighted.findIndex(
      (obj) => obj.user === newHighlight.user
    );

    if (existingObjectIndex !== -1) {
      // If an object with the same name already exists, update it
      const updatedList = [...highlighted];
      updatedList[existingObjectIndex] = {
        ...highlighted[existingObjectIndex],
        gamePosX: newHighlight.gamePosX,
        gamePosY: newHighlight.gamePosY,
      };
      setHighlighted(updatedList);
      sendHighlights(updatedList);
    } else {
      // Otherwise, add a new object
      setHighlighted([...highlighted, newHighlight]);
      sendHighlights([...highlighted, newHighlight]);
    }

    //look to see if there are any pieces at this spot
    const newPiecesAtClick = pieces.find(
      (obj) =>
        obj.row === row &&
        obj.col === col &&
        obj.boardCol === boardCol &&
        obj.boardRow === boardRow
    );
    //if there is a piece there and we aren't selecting, popup the menu
    if (newPiecesAtClick && selecting === false) {
      //this pop up occurs when there is a piece and gives options for that piece in question as a pop up
      setpopupMenuIsVisible(true);
      setPopupPosition({ x: xPos, y: yPos });
      setPiecesAtClick([newPiecesAtClick]);
    } else {
      setPiecesAtClick([]);
    }

    if (selecting && pieceFromBox && pieceFromBox.category === "figures") {
      //set the figure on the board
      setPiece(null, row, col, boardCol, boardRow, pieceFromBox);

      //post to the log
      addMessage(
        auth.user.username + " places " + pieceFromBox.name + " on the board"
      );

      //turn off selecting
      setSelecting(false);

      //remove the piece from the box based on its id
      const updatedBox = box.filter((obj) => obj.boxId !== pieceFromBox.boxId);

      //update the box
      setBox(updatedBox);
      sendBox(updatedBox);

      //clear the piecefrombox state
      setPieceFromBox(null);
      return;
    } else {
      //we don't want to do anything rash, get rid of the piece from the box
      setPieceFromBox(null);
    }

    //if there's no piece there, and you are selecting, move there
    if (!newPiecesAtClick && selecting) {
      //get the piece
      let pieceToMoveIndex = pieces.findIndex(
        (obj) =>
          obj.row === piecesAtClick[0].row &&
          obj.col === piecesAtClick[0].col &&
          obj.boardCol === piecesAtClick[0].boardCol &&
          obj.boardRow === piecesAtClick[0].boardRow &&
          obj.name === piecesAtClick[0].name
      );

      const updatedValues = {
        row: row,
        col: col,
        boardCol: boardCol,
        boardRow: boardRow,
      };

      //update the coordinates at that index
      pieces[pieceToMoveIndex] = {
        ...pieces[pieceToMoveIndex],
        ...updatedValues,
      };

      setPieces(pieces);
      setSelecting(false);

      //send the updated pieces over the websocket
      sendPiececs(pieces);

      addMessage(
        `${auth.user.username} moves ${piecesAtClick[0].name} at position ${col}, ${row} on the board at ${boardCol}, ${boardRow}`
      );
    }

    //if there is a piece there and you are moving, then do nothing
    if (newPiecesAtClick && selecting) {
      setSelecting(false);
    }
  };

  //to be passed to the popup menu to be called when exiting the menu
  const cancelPopupMenu = () => {
    setpopupMenuIsVisible(false);
  };

  const closeMoveInfo = () => {
    setSelecting(false);
  };

  const selectingMoveInfo = () => {
    setSelectMessage("Select to move piece.");
    setSelecting(true);
  };

  const setPiece = (
    event,
    row = 1,
    col = 1,
    boardCol = 1,
    boardRow = 1,
    piece
  ) => {
    const pieceToUpdate = {
      boxId: piece.boxId,
      id: piece.id,
      row: row,
      col: col,
      boardCol: boardCol,
      boardRow: boardRow,
      name: piece.name,
      skin: piece.skin,
      category: piece.category,
      color: piece.color,
      scale: piece.scale,
    };

    //add the piece
    const piecesList = [...pieces, pieceToUpdate];

    //update the state
    setPieces(piecesList);

    sendPiececs(piecesList);

    addMessage(
      `${auth.user.username} set a piece ${piece.name} at position ${col}, ${row} on the board at ${boardCol}, ${boardRow}`
    );
  };

  //-----------------------------taking and returning from the box-------------------------//

  const takeOutOfBox = (piece) => {
    setSelectMessage(`Select position to place ${piece.name}.`);
    setSelecting(true);
    setPieceFromBox(piece);
  };

  const handleBlankBoardClick = (row, col) => {
    //make sure you are selecting and have a board piece ready
    if (selecting && pieceFromBox && pieceFromBox.category === "boards") {
      //set the board
      setBoard(null, row, col, pieceFromBox);

      //turn off selecting
      setSelecting(false);

      //remove the piece from the box based on its id
      const updatedBox = box.filter((obj) => obj.boxId !== pieceFromBox.boxId);

      //update the box
      setBox(updatedBox);
      sendBox(updatedBox);

      //clear the piecefrombox state
      setPieceFromBox(null);
    }
  };

  const returnToBox = (board = null) => {
    //turn off selecting
    setSelecting(false);

    let pieceToReturn;
    //check to see if we are returning a board, cause if we are, we need to make sure there is nothing on it
    if (board) {
      //check for pieces on this board. we can do this by matching the boardCol and boardRow
      const piecesOnBoard = pieces.find(
        (obj) => obj.boardCol === board.col && obj.boardRow === board.row
      );

      if (piecesOnBoard) {
        //leave a nice message
        setShowError({
          show: true,
          message:
            "You need to remove all pieces from the board before returning to the box. Deal with it.",
        });
        return;
      }

      //get rid of the cards on the board back to the box
      const cardsOnBoard = cards.filter((c) => c.inHand === -(board.boxId + 1));
      if (cardsOnBoard){
        //leave a nice message
        setShowError({
          show: true,
          message:
            "You need to remove all cards from the board before returning to the box. Deal with it.",
        });
        return;
      }
    }

    if (board) {
      //get the board info that is needed
      pieceToReturn = {
        boxId: board.boxId,
        id: board.id,
        category: board.category,
        name: board.name,
        skin: board.skin,
      };
    } else {
      //we only need certain attributes from the piece, get them now from the piecesAtClick
      pieceToReturn = {
        boxId: piecesAtClick[0].boxId,
        id: piecesAtClick[0].id,
        category: piecesAtClick[0].category,
        name: piecesAtClick[0].name,
        skin: piecesAtClick[0].skin,
      };
    }

    //get the piece from the piecesAtclick, this is an array so we need the 0'th element, will i allow multiple pieces per subsquare? probably not
    const updatedBox = [...box, pieceToReturn];

    //update the box
    setBox(updatedBox);
    sendBox(updatedBox);
    addMessage(
      auth.user.username + " returns " + pieceToReturn.name + " to the box."
    );

    if (board) {
      //update the boards
      const updatedBoards = boards.filter(
        (obj) => obj.boxId !== pieceToReturn.boxId
      );

      setBoards(updatedBoards);

      sendBoards(updatedBoards);
    } else {
      //clear the piece at click state
      setPiecesAtClick([]);

      //clear the piece from the pieces
      const updatedPieces = pieces.filter(
        (obj) => obj.boxId !== pieceToReturn.boxId
      );

      //update the pieces state
      setPieces(updatedPieces);

      sendPiececs(updatedPieces);
    }
  };

  const closeGameError = () => {
    setShowError({ show: false, message: "" });
  };

  //------------dice stuff---------------------//

  const takeOutDie = (dieToTakeOut) => {
    //add to the die state
    const updatedDie = [...die, dieToTakeOut];
    setDie(updatedDie);

    sendDice(updatedDie);

    //remove that die from the box
    const updatedBox = box.filter((obj) => obj.boxId !== dieToTakeOut.boxId);
    setBox(updatedBox);
    sendBox(updatedBox);

    addMessage(`${auth.user.username} takes out a ${dieToTakeOut.name}`);
  };

  const takeOutAllDice = () => {
    //find all the dice
    const dieToTakeOut = box.filter((obj) => obj.category === "die");
    //add to the die state
    const updatedDie = [...die, ...dieToTakeOut];
    setDie(updatedDie);

    sendDice(updatedDie);

    //remove that die from the box
    const updatedBox = box.filter((obj) => obj.category !== "die");
    setBox(updatedBox);
    sendBox(updatedBox);

    addMessage(`${auth.user.username} takes out all the dice.`);
  };

  const returnDie = (dieToReturn) => {
    //add to the box
    const updatedBox = [...box, dieToReturn];
    setBox(updatedBox);

    //remove that die from the die list
    const updatedDie = die.filter((obj) => obj.boxId !== dieToReturn.boxId);
    setDie(updatedDie);
    sendDice(updatedDie);

    addMessage(`${auth.user.username} puts back a ${dieToReturn.name}`);
  };

  const returnAllDie = () => {
    //add to the box
    const updatedBox = [...box, ...die];
    setBox(updatedBox);
    sendBox(updatedBox);

    //remove all die from the die list
    setDie([]);
    sendDice([]);

    addMessage(`${auth.user.username} puts back all the dice.`);
  };

  const rollDice = () => {
    //change all the dice rolling values to true
    const updatedDice = [...die];
    for (const d of updatedDice) {
      d.rolling = true;
    }

    //set the state
    setDie(updatedDice);
    sendDice(updatedDice);
    setRolling(true);

    //get the dice names
    let diceNames = [];
    for (const d of die) {
      diceNames.push(d.name);
    }
    addMessage(`${auth.user.username} rolls dice: ${diceNames}!`);
  };

  const stopRoll = () => {
    //change all the dice rolling values to false
    const updatedDice = [...die];
    for (const d of updatedDice) {
      //get a random side and set it as the result
      const randomNumber1 = Math.floor(Math.random() * 6);
      d.rolling = false;

      switch (randomNumber1) {
        case 0:
          d.winningSide = `1,${d.side1}`;
          break;
        case 1:
          d.winningSide = `2,${d.side2}`;
          break;
        case 2:
          d.winningSide = `3,${d.side3}`;
          break;
        case 3:
          d.winningSide = `4,${d.side4}`;
          break;
        case 4:
          d.winningSide = `5,${d.side5}`;
          break;
        default:
          d.winningSide = `6,${d.side6}`;
      }
    }

    //set the state
    setDie(updatedDice);
    sendDice(updatedDice);
    setRolling(false);

    //get the dice results
    let diceResults = [];
    for (const d of die) {
      if (d.winningSide) {
        const winningSplit = getWinningSideInfo(d.winningSide);
        diceResults.push(winningSplit);
      }
    }
    addMessage(`${auth.user.username} rolled dice: ${diceResults}!`);
  };

  function getWinningSideInfo(winningSide) {
    if (winningSide) {
      const winningSplit = winningSide.split(",");
      if (winningSplit[1] !== "null") return winningSplit[1];
    }
  }

  //----------------------card stuff -----------------------------//

  const putCardInHand = (card, player) => {
    //remove the card from the box if from there
    const updatedBox = box.filter((obj) => obj.boxId !== card.boxId);

    //update the box state
    setBox(updatedBox);
    sendBox(updatedBox);

    //remove the card from the discardpile if from there
    const updatedDiscardPile = discardPile.filter(
      (obj) => obj.boxId !== card.boxId
    );

    //update the box state
    setDiscardPile(updatedDiscardPile);
    sendDiscard(updatedDiscardPile);

    //update the card
    let updatedCard;

    if (player < 0) {
      updatedCard = { ...card, inHand: player, show: false };
    } else {
      updatedCard = { ...card, inHand: player.app_user_id, show: false };
    }

    //check to see if we are updated a current card by filtering it out
    const filtedCards = cards.filter((obj) => obj.boxId !== card.boxId);

    //add the cards to the cards in state
    const updatedCardsState = [...filtedCards, updatedCard];

    //set the new cards state
    setCards(updatedCardsState);
    sendCards(updatedCardsState);

    if (player < 0) {
      addMessage(`${card.type} is now in a shared space.`);
    } else {
      addMessage(`${card.type} is now in ${player.name}'s hand.`);
    }
  };

  const closeViewCard = () => {
    setViewCard(false);
  };

  const showCard = (card) => {
    //only see cards that you are allowed to see, this means it must match the current player's id
    if (
      auth.user.app_user_id === card.inHand ||
      card.show === true ||
      card.inHand < 0
    ) {
      setViewCard(true);
      setCardToView(card);
    }
  };

  const updateCard = (card, show) => {
    //update the card
    const updatedCard = { ...card, show: show };

    if (show === true) {
      addMessage(`${auth.user.username} shows ${card.name}`);
    }

    //check to see if we are updated a current card by filtering it out
    const filtedCards = cards.filter((obj) => obj.boxId !== card.boxId);

    //add the cards to the cards in state
    const updatedCardsState = [...filtedCards, updatedCard];

    //set the new cards state
    setCards(updatedCardsState);
    sendCards(updatedCardsState);
  };

  const returnCardToBox = (card) => {
    //remove the card from the cards state
    const updatedCards = cards.filter((obj) => obj.boxId !== card.boxId);

    //update the cards state
    setCards(updatedCards);
    sendCards(updatedCards);

    //remove the card from the discardpile if from there
    const updatedDiscardPile = discardPile.filter(
      (obj) => obj.boxId !== card.boxId
    );

    //update the box state
    setDiscardPile(updatedDiscardPile);
    sendDiscard(updatedDiscardPile);

    //get the box contents and add the card
    const updatedBox = [...box, card];

    //update the box state
    setBox(updatedBox);
    sendBox(updatedBox);

    addMessage(`${card.type} is now in the box.`);
  };

  const discardCard = (card) => {
    //remove the card from the cards state
    const updatedCards = cards.filter((obj) => obj.boxId !== card.boxId);

    //update the cards state
    setCards(updatedCards);
    sendCards(updatedCards);

    //get the context of the discard pile and add the card
    const updatedDiscardPile = [...discardPile, card];

    //update the discardpile state
    setDiscardPile(updatedDiscardPile);
    sendDiscard(updatedDiscardPile);

    addMessage(`${card.type} is discarded.`);
  };

  const returnAllDiscard = () => {
    //combine the box and discard pile
    const updatedBox = [...discardPile, ...box];

    //set the box state
    setBox(updatedBox);
    sendBox(updatedBox);

    //clear the discard pile
    setDiscardPile([]);
    sendDiscard([]);
  };

  const updateStatsOnCard = (card, newVariables) => {
    //update the card with the new variables
    const updatedCard = { ...card, variables: newVariables };

    //filter out this card from the cards
    const filteredCards = cards.filter((obj) => obj.boxId !== card.boxId);

    //put in the update card
    const updatedCards = [...filteredCards, updatedCard];

    //update the state
    setCards(updatedCards);
    sendCards(updatedCards);
  };
  //----------Logging------------//
  const addMessage = (newMessage) => {
    const mes = newMessage;
    // setMessages((prevMessages) => [...prevMessages, mes]);
    // sendLog([...messages, mes]);
    sendLog(mes);
  };

  //----------------------------Rotate, Scale, and gameArea Styles---------------//
  //rotation states
  const [rotationAngle, setRotationAngle] = useState(0);
  const [rotationAngleZ, setRotationAngleZ] = useState(0);

  //roates the gameArea as if on a lazy susan
  const rotateElement = (sign) => {
    if (sign === "+") {
      setRotationAngleZ(rotationAngleZ + 10);
    } else {
      setRotationAngleZ(rotationAngleZ - 10);
    }
  };

  //rotates the view from birds eye to horizontal
  const rotateElementX = (sign) => {
    if (sign === "+") {
      if (rotationAngle + 10 >= 90) {
        //keep the view from flipping over
        setRotationAngle(0);
      } else {
        setRotationAngle(rotationAngle + 10);
      }
    } else {
      if (rotationAngle - 10 <= 0) {
        //keep the view from flipping over
        setRotationAngle(80);
      } else {
        setRotationAngle(rotationAngle - 10);
      }
    }
  };

  //resets the view to a birds eye perspective
  const resetView = () => {
    setRotationAngle(0);
    setVscale(1);
    setRotationAngleZ(0);
  };

  //scale sate
  const [vscale, setVscale] = useState(1);

  //adjusts the scale based on the button click
  const scaleView = (sign) => {
    if (sign === "-") {
      setVscale(vscale - 0.1);
    } else {
      setVscale(vscale + 0.1);
    }
  };

  //sets the style for the GameArea
  const gameAreaStyle = {
    transform: `perspective(750px) translate3d(0px, 0px, -250px) rotateX(${rotationAngle}deg) rotateZ(${rotationAngleZ}deg) scale(${vscale}, ${vscale})`,
    boxShadow: "0 70px 40px -20px rgba(0, 0, 0, 0.2)",
    transition: "0.4s ease-in-out transform",
    height: GameAreaInfo.height,
    width: GameAreaInfo.height,
    transformStyle: "preserve-3d",
  };

  return (
    <>
      <Container>
        <Row>
          <Col>
            <h1>{boardGameInfo.name}</h1>
          </Col>
        </Row>
      </Container>
      {!connected && (
        <Button className="bg-gradient m-3" onClick={() => connect()}>
          Connect
        </Button>
      )}
      {connected && (
        <Container className="m-2 p-3 border shadow-lg" fluid>
          <Row className="p-2">
            <Col>
              <Button
                size="sm"
                className="me-2 bg-gradient"
                onClick={() => makeSaveState()}
              >
                Save Game
              </Button>
              <Button
                size="sm"
                variant="warning"
                className="bg-gradient"
                onClick={() => loadState()}
              >
                Load Game
              </Button>
            </Col>
          </Row>

          {/* element that hold the popup menu information when selecting pieces */}
          {popupMenuIsVisible && (
            <PiecesPopupMenu
              xPos={popupPosition.x}
              yPos={popupPosition.y}
              cancelPopupMenu={cancelPopupMenu}
              selectingMoveInfo={selectingMoveInfo}
              returnToBox={returnToBox}
              discardCard={discardCard}
              piecesAtClick={piecesAtClick}
            ></PiecesPopupMenu>
          )}

          {/* view card element */}
          {viewCard && (
            <ViewCard
              returnCardToBox={returnCardToBox}
              updateCard={updateCard}
              putCardInHand={putCardInHand}
              players={players}
              card={cardToView}
              closeViewCard={closeViewCard}
              discardCard={discardCard}
              updateStatsOnCard={updateStatsOnCard}
              boards={boards}
            ></ViewCard>
          )}

          {/* Logger */}
          <Row className="m-2">
            <Col className="border">
              <Logger messages={messages}></Logger>
            </Col>
          </Row>
          <Row className="m-2">
            {/* For the Box menu */}
            <Col>
              <BoxContext.Provider value={box}>
                <PlayersContext.Provider value={players}>
                  <DiscardContext.Provider value={discardPile}>
                  <BoardsContext.Provider value={boards}>

                    <BoxMenu
                      categories={categories}
                      takeOutOfBox={takeOutOfBox}
                      putCardInHand={putCardInHand}
                      returnCardToBox={returnCardToBox}
                      returnAllDiscard={returnAllDiscard}
                    ></BoxMenu>
                    </BoardsContext.Provider>
                  </DiscardContext.Provider>
                </PlayersContext.Provider>
              </BoxContext.Provider>
            </Col>

            {/* For the Boards menu */}
            <Col>
              <BoardOptions
                boards={boards}
                returnBoardToBox={returnToBox}
                rotateBoard={rotateBoard}
                swapBoard={swapBoard}
              ></BoardOptions>
            </Col>

            {/* dice */}
            <Col>
              <Row>
                <DieMenu box={box} takeOutDie={takeOutDie}></DieMenu>
                <ButtonGroup>
                  <Button size="sm" variant="dark" onClick={takeOutAllDice}>
                    All dice
                  </Button>
                  <Button size="sm" variant="dark" onClick={returnAllDie}>
                    Return All
                  </Button>
                  {rolling ? (
                    <Button size="sm" variant="danger" onClick={stopRoll}>
                      Stop
                    </Button>
                  ) : (
                    <Button size="sm" variant="success" onClick={rollDice}>
                      Roll!
                    </Button>
                  )}
                </ButtonGroup>
              </Row>
              <Row>
                {die.map((d, index) => {
                  return (
                    <Col
                      className="my-2"
                      lg={3}
                      md={4}
                      sm={3}
                      xs={2}
                      key={"die" + d.boxId + index}
                    >
                      <Die
                        sides={[1, 2, 3, 4, 5, 6]}
                        size={35}
                        die={d}
                        returnDie={returnDie}
                        rolling={rolling}
                      ></Die>
                    </Col>
                  );
                })}
              </Row>
            </Col>
          </Row>

          {/* The shared space row */}
          <Row className="m-2">
            <Col  md={4} sm={6} xs={12} className="my-2"> 
              <SharedHand cards={cards} showCard={showCard}></SharedHand>
              </Col>

              {boards.map((b, index) => {
                console.log("test");
                return (
                  <Col key={b.name + index} sm={6} md={4} xs={12} className="my-2">
                    <SharedHand cards={cards} showCard={showCard} name={b.name} itemId={-(b.boxId + 1)}></SharedHand>
                  </Col>
                );
              })}
          </Row>

          {/* The player Row */}
          <Row className="m-2">
            {/* The player hands */}

            {players &&
              players.map((p, index) => {
                return (
                  <Col key={p.name + index}>
                    <PlayerHands
                      player={p}
                      cards={cards}
                      showCard={showCard}
                      playerId={p.app_user_id}
                    ></PlayerHands>
                  </Col>
                );
              })}
          </Row>

          <Row className="m-2">
            {/* The Game Area------------------------------------------------------ */}
            <Col xs={12} className="d-flex flex-column border game-view p-2">
              {/* Game Area buttons */}
              <Row className="justify-content-center">
                <Col sm={6} md={5} lg={4}>
                  <ButtonGroup className="d-flex justify-content-end">
                    <Button
                      variant="dark"
                      size="sm"
                      onClick={() => scaleView("-")}
                      style={{ zIndex: 2 }} //in case of an overlap issue
                    >
                      -
                    </Button>
                    <Button
                      variant="dark"
                      size="sm"
                      onClick={() => scaleView("+")}
                      style={{ zIndex: 2 }}
                    >
                      +
                    </Button>
                    <Button
                      variant="dark"
                      size="sm"
                      onClick={() => rotateElement("-")}
                      style={{ zIndex: 2 }}
                    >
                      &#x2190;
                    </Button>
                    <Button
                      variant="dark"
                      size="sm"
                      onClick={() => rotateElement("+")}
                      style={{ zIndex: 2 }}
                    >
                      &#x2192;
                    </Button>

                    <Button
                      variant="dark"
                      size="sm"
                      onClick={() => rotateElementX("+")}
                      style={{ zIndex: 2 }}
                    >
                      &#x2191;
                    </Button>
                    <Button
                      variant="dark"
                      size="sm"
                      onClick={() => rotateElementX("-")}
                      style={{ zIndex: 2 }}
                    >
                      &#x2193;
                    </Button>
                    <Button
                      variant="dark"
                      size="sm"
                      onClick={resetView}
                      style={{ zIndex: 2 }}
                    >
                      Reset
                    </Button>
                  </ButtonGroup>
                </Col>
              </Row>

              <Row>
                <Col>
                  {selecting && (
                    <MoveInfo
                      closeMoveInfo={closeMoveInfo}
                      message={selectMessage}
                    ></MoveInfo>
                  )}
                  <GameError
                    showError={showError}
                    closeGameError={closeGameError}
                  ></GameError>
                </Col>
                {/* Info when moving a piece */}
              </Row>

              <Row className="justify-content-center">
                <Col
                  xs={12}
                  className="d-block"
                  style={gameAreaStyle}
                  id="gameArea"
                >
                  {/* Map out the pieces */}
                  {pieces.map((p, index) => {
                    //get the subsquare top and left
                    const coordinates = GameAreaInfo.placeCoordinates.find(
                      (obj) => obj.row === p.row && obj.col === p.col
                    );

                    //get the board offset to use for placing the piece relative to it's board
                    const boardOffsetTop =
                      GameAreaInfo.boardHeight * p.boardRow;
                    const boardOffsetLeft =
                      GameAreaInfo.boardHeight * p.boardCol;

                    return (
                      <Piece
                        key={p.boxId + index}
                        coordinates={coordinates}
                        boardOffsetTop={boardOffsetTop}
                        boardOffsetLeft={boardOffsetLeft}
                        height={GameAreaInfo.placeHeight}
                        scale={vscale}
                        skin={p.skin}
                        color={p.color}
                        scaleSize={p.scale}
                      ></Piece>
                    );
                  })}

                  {/* Map out the boards */}
                  {boards.map((board, index) => {
                    return (
                      <Board
                        size={board.size}
                        name={board.name}
                        boardCol={board.col}
                        boardRow={board.row}
                        boardHeight={GameAreaInfo.boardHeight}
                        y={board.top}
                        x={board.left}
                        subSquareSize={board.subSquareSize}
                        skin={board.skin}
                        key={board.boxId + index}
                        handleSubSquareClick={handleSubSquareClick}
                        rotate={board.rotate}
                      />
                    );
                  })}

                  {/* map out the empty boards */}
                  {GameAreaInfo.boardCoordinates.map((coordinates, index) => {
                    if (
                      !boards.find(
                        (obj) =>
                          obj.col === coordinates.col &&
                          obj.row === coordinates.row
                      )
                    ) {
                      //there is no board currently there, put in the blank
                      return (
                        <BlankBoard
                          key={coordinates.col + "-" + coordinates.row + index}
                          boardHeight={GameAreaInfo.boardHeight}
                          top={coordinates.top}
                          left={coordinates.left}
                          row={coordinates.row}
                          col={coordinates.col}
                          handleBlankBoardClick={handleBlankBoardClick}
                        ></BlankBoard>
                      );
                    } else {
                      return null;
                    }
                  })}

                  {/* Map out the highlights */}
                  {highlighted.map((s, index) => {
                    return (
                      <SelectedHighlight
                        key={s.user + s.gamePosX + s.gamePosY + index}
                        posX={s.gamePosX}
                        posY={s.gamePosY}
                        height={GameAreaInfo.placeHeight}
                        color={s.color}
                      ></SelectedHighlight>
                    );
                  })}
                </Col>
              </Row>
            </Col>
          </Row>
        </Container>
      )}
    </>
  );
}
