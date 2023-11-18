import { Container } from "react-bootstrap";
import avatar from "../../files/avatar.png";
import cardBackErr from "../../files/cardback.png";
import cardFrontErr from "../../files/cardfront.png";
import { useEffect, useState } from "react";

export default function PlayerHands({ player, cards, showCard, playerId }) {
    //force the state to update
    const [content, setContent] = useState('');


  const playerCardArea = {
    height: "100%",
    width: "100%",
    backgroundColor: player.color,
  };

  const handleImageError = (event) => {
    event.target.src = avatar; // Set the fallback image source
  };

  const handleCardBackError = (event) => {
    event.target.src = cardBackErr; // Set the fallback image source
  };

  const handleCardFrontError = (event) => {
    event.target.src = cardFrontErr; // Set the fallback image source
  };

  //get all the cards in the players hand
  const playerCards = cards.filter((obj) => obj.inHand === playerId);

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
    <Container style={playerCardArea} className="rounded shadow" key={player.id}>
      <div>{player.name}</div>
      <img
        src={player.avatar}
        className="rounded float-end avatar"
        alt="Player's avatar"
        onError={handleImageError}
      ></img>
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
