import { Container } from "react-bootstrap";
import cardBackErr from "../../files/cardback.png";
import cardFrontErr from "../../files/cardfront.png";
import { useEffect, useState } from "react";

export default function SharedHand({ cards, showCard, name="Shared Space", itemId=-999 }) {
    //force the state to update
    const [content, setContent] = useState('');


  const playerCardArea = {
    height: "100%",
    width: "100%",
    backgroundColor: "#6a7680",
  };


  const handleCardBackError = (event) => {
    event.target.src = cardBackErr; // Set the fallback image source
  };

  const handleCardFrontError = (event) => {
    event.target.src = cardFrontErr; // Set the fallback image source
  };

  //get all the cards in the players hand
  const playerCards = cards.filter((obj) => obj.inHand === itemId);

  // Sort the cards so the types are in order
  const organizedByType = playerCards.sort((a, b) => {
    const nameA = a.type.toUpperCase(); // Convert names to uppercase for case-insensitive sorting
    const nameB = b.type.toUpperCase();

    if (nameA < nameB) {
      return -1;
    }
    if (nameA > nameB) {
      return 1;
    }
    return 0;
  });
  
  useEffect(() => {
    //this will force a re-render of the card
    setContent(Math.random)
  }, [cards])

  return (
    <Container style={playerCardArea} className="rounded shadow">
      <div>{name}</div>
      
        {organizedByType.map((card) => {
    return (
      <img
        key={card.boxId + content}
        src={card.show ? (card.cardFront === null ? cardFrontErr : card.cardFront) : (card.cardBack === null ? cardBackErr : card.cardBack)}
        className="rounded float-start card"
        alt={card.name}
        onError={card.show ? handleCardFrontError : handleCardBackError}
        onClick={() => showCard(card)}
      ></img>
    );
  })}
    </Container>
  );
}
