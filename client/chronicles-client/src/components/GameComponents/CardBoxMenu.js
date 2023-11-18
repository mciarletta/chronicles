import { useContext } from "react";
import BoxContext from "../contexts/BoxContext";
import { Button, ListGroup, DropdownButton } from "react-bootstrap";
import PlayersContext from "../contexts/PlayersContext";
import BoardsContext from "../contexts/BoardsContext";

export default function CardBoxMenu({ putCardInHand }) {
  //grab our box contenxt and players
  const box = useContext(BoxContext);
  const players = useContext(PlayersContext);
  const boards = useContext(BoardsContext);

  //filter out the cards
  const cards = box.filter((obj) => obj.category === "cards");

  //get the types
  const cardTypes = new Set();

  for (const c of cards) {
    const type = c.type;
    cardTypes.add(type);
  }

  function sendRandomCard(player, cardType) {
    //get a random card from the type
    const cardsOfType = box.filter(
      (obj) => obj.category === "cards" && obj.type === cardType
    );

    //see how many there are of this card type
    const amountOfType = cardsOfType.length;

    //get a random one
    const randomIndex = Math.floor(Math.random() * amountOfType);
    const chosenCard = cardsOfType[randomIndex];

    //put in the players hand
    putCardInHand(chosenCard, player);
  }

  // Convert the Set back to an array if needed
  const uniqueCardTypes = Array.from(cardTypes);

  return (
    <>
      {uniqueCardTypes.map((ct) => {
        return (
          <div key={ct}>
            <ListGroup>
              <ListGroup.Item>
                {ct}
                {players.map((p) => {
                  return (
                    <Button
                      key={p.id}
                      size="sm"
                      variant="dark"
                      onClick={() => sendRandomCard(p, ct)}
                    >
                      {p.name}
                    </Button>
                  );
                })}
                <Button
                  size="sm"
                  variant="dark"
                  onClick={() => sendRandomCard(-999, ct)}
                >
                  Shared Space
                </Button>

                {boards.map((b) => {
                  return (
                    <Button
                      key={b.id}
                      size="sm"
                      variant="dark"
                      onClick={() => sendRandomCard(-(b.boxId + 1), ct)}
                    >
                      {b.name}
                    </Button>
                  );
                })}

                <DropdownButton
                  variant="dark"
                  drop="end"
                  title="select cards"
                  size="sm"
                >
                  <ListGroup style={{ maxHeight: "50vh", overflowY: "scroll" }}>
                    {cards.map((card) => {
                      return (
                        card.type === ct && (
                          <ListGroup.Item key={card.boxId}>
                            {card.name}
                            {players.map((p) => {
                              return (
                                <Button
                                  key={p.id}
                                  onClick={() => putCardInHand(card, p)}
                                  size="sm"
                                  variant="dark"
                                >
                                  {p.name}
                                </Button>
                              );
                            })}
                            <Button
                              onClick={() => putCardInHand(card, -999)}
                              size="sm"
                              variant="dark"
                            >
                              Shared Space
                            </Button>

                            {boards.map((b) => {
                              return (
                                <Button
                                  key={b.id}
                                  size="sm"
                                  variant="dark"
                                  onClick={() =>
                                    putCardInHand(card, -(b.boxId + 1))
                                  }
                                >
                                  {b.name}
                                </Button>
                              );
                            })}
                          </ListGroup.Item>
                        )
                      );
                    })}
                  </ListGroup>
                </DropdownButton>
              </ListGroup.Item>
            </ListGroup>
          </div>
        );
      })}
    </>
  );
}
