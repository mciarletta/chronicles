import { ListGroup, Button } from "react-bootstrap";
import DiscardContext from "../contexts/DiscardContext";
import PlayersContext from "../contexts/PlayersContext";
import { useContext } from "react";

export default function DiscardMenu({putCardInHand, returnCardToBox, returnAllDiscard}) {
  //grab our dsicard contenxt
  const discardPile = useContext(DiscardContext);
  const players = useContext(PlayersContext);

  return (
    <>
    <ListGroup style={{ maxHeight: "50vh", overflowY: "scroll"}}>
    <ListGroup.Item>
        <Button onClick={returnAllDiscard} size="sm" variant="info" >Return All</Button>
    </ListGroup.Item>
      {discardPile.map((card) => {
        return (
          <ListGroup.Item key={card.id}>
            {card.name}
            <Button onClick={() => returnCardToBox(card)} size="sm" variant="info" >Return</Button>

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
          </ListGroup.Item>
        );
      })}


    </ListGroup>
        </>
  );
}
