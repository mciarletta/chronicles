import useClickAway from "use-click-away/useClickAway";
import AuthContext from "../contexts/AuthContext";
import { useRef, useState, useContext } from "react";
import { Button, Card, Container, Row, Col, Form } from "react-bootstrap";

export default function ViewCard({
  players,
  card,
  closeViewCard,
  putCardInHand,
  updateCard,
  returnCardToBox,
  discardCard,
  updateStatsOnCard,
  boards
}) {
  const clickRef = useRef("");
  const [imageError, setImageError] = useState(false);
  const auth = useContext(AuthContext);

  useClickAway(clickRef, () => {
    closeViewCard();
  });

  const handleClickShow = () => {
    if (card.inHand === auth.user.app_user_id || card.inHand < 0) {
      updateCard(card, true);
      closeViewCard();
    }
  };

  const handleClickHide = () => {
    if (card.inHand === auth.user.app_user_id || card.inHand < 0) {
      updateCard(card, false);
      closeViewCard();
    }
  };

  const handleClickDiscard = () => {
    if (card.inHand === auth.user.app_user_id || card.inHand < 0) {
      discardCard(card);
      closeViewCard();
    }
  };

  const handleClickGive = (p) => {
    if (card.inHand === auth.user.app_user_id || card.inHand < 0) {
      putCardInHand(card, p);
      closeViewCard();
    }
  };

  const handleClickBox = () => {
    if (card.inHand === auth.user.app_user_id || card.inHand < 0) {
      returnCardToBox(card);
      closeViewCard();
    }
  };

  const noImageCard = (
    <Card style={{ width: "100%" }}>
      <Card.Body>
        <Card.Title>{card.name}</Card.Title>
        <Card.Text>{card.text}</Card.Text>
      </Card.Body>
    </Card>
  );

  const noImageCardUnknown = (
    <Card style={{ width: "100%" }}>
      <Card.Body>
        <Card.Title>{card.type}</Card.Title>
        <Card.Text>Click show to reveal this card.</Card.Text>
      </Card.Body>
    </Card>
  );

  //to rerender with the text from the card if there is an image error
  const handleImageError = () => {
    setImageError(true);
  };

  //some cards may have adjustable variables, deal with them here
  let varialbesList = [];

  if (card.variables) {
    //get the splits
    const splitVariables = card.variables.split(",");

    //card variables are split by name and then value and must be an even number
    for (let i = 0; i < splitVariables.length; i = i + 2) {
      let obj = { name: splitVariables[i], value: splitVariables[i + 1] };
      varialbesList.push(obj);
    }
  }

  const handleSubmit = (e) => {
    if (card.inHand === auth.user.app_user_id || card.inHand < 0) {
      e.preventDefault();
      let updatedVarialbes = "";
      for (let i = 0; i < varialbesList.length; i++) {
        let item = "";
        //check if the value was untouched
        if (e.target[i].value === "") {
          //reconstruct the variables string
          item = varialbesList[i].name + "," + varialbesList[i].value;
        } else {
          //reconstruct the variables string
          item = varialbesList[i].name + "," + e.target[i].value;
        }

        if (i === 0) {
          updatedVarialbes = item;
        } else {
          updatedVarialbes = updatedVarialbes + "," + item;
        }
      }
      //call the update
      updateStatsOnCard(card, updatedVarialbes);

      //close the card ---since I can't get it to refresh otherwise : (
      closeViewCard();
    }
  };

  return (
    <>
      <Container ref={clickRef} className="viewcard m-2 p-2">
        {/* if in the shared hand */}
        {((card.inHand < 0 && card.show) || card.inHand > 0) && <Row>
          {(imageError || !card.cardFront) && noImageCard}
          {card.cardFront && (
            <img
              src={card.cardFront}
              className="rounded frontView"
              alt={card.name}
              onError={handleImageError}
            ></img>
          )}
        </Row>}

        {card.inHand < 0 && !card.show && <Row>
          {(imageError || !card.cardBack) && noImageCardUnknown}
          {card.cardFront && (
            <img
              src={card.cardBack}
              className="rounded frontView"
              alt={card.type}
              onError={handleImageError}
            ></img>
          )}
        </Row>}
        

        {(auth.user.app_user_id === card.inHand || card.inHand < 0) && (card.inHand !== -1999) && (
          <Row className="d-flex align-content-start flex-wrap">
            {card.show ? (
              <Col>
                <Button onClick={handleClickHide} size="sm" variant="info">
                  Hide
                </Button>
              </Col>
            ) : (
              <Col>
                <Button onClick={handleClickShow} size="sm" variant="success">
                  Show
                </Button>
              </Col>
            )}
            <Col>
              <Button onClick={handleClickDiscard} size="sm" variant="danger">
                Discard
              </Button>
            </Col>
            <Col>
              <Button size="sm" variant="danger" onClick={handleClickBox}>
                Return to Box
              </Button>
            </Col>
            {players.map((p) => {
              return (
                <Col key={p.id}>
                  <Button
                    onClick={() => handleClickGive(p)}
                    size="sm"
                    variant="dark"
                  >
                    {p.name}
                  </Button>
                </Col>
              );
            })}
            <Col>
              <Button
                onClick={() => handleClickGive(-999)}
                size="sm"
                variant="dark"
              >
                Shared Space
              </Button>

              {boards.map((b) => {
              return (
                <Col key={b.id}>
                  <Button
                    onClick={() => handleClickGive(-(b.boxId + 1))}
                    size="sm"
                    variant="dark"
                  >
                    {b.name}
                  </Button>
                </Col>
              );
            })}
            </Col>
          </Row>
        )}

        {card.variables && ((card.inHand < 0 && card.show) || card.inHand > 0) && (
          <Row>
            <Form onSubmit={handleSubmit}>
              {varialbesList.map((obj, index) => {
                return (
                  <Col key={obj.name + index}>
                    <Form.Label htmlFor={obj.name}>
                      {obj.name}: {obj.value}
                    </Form.Label>
                    <Form.Control
                      id={obj.name}
                      name={obj.name}
                      placeholder={obj.value}
                    />
                  </Col>
                );
              })}

              {(auth.user.app_user_id === card.inHand ||
                card.inHand < 0) && (card.inHand !== -1999) && (
                <Button
                  className="my-2"
                  type="submit"
                  size="sm"
                  variant="success"
                >
                  Update
                </Button>
              )}
            </Form>
          </Row>
        )}
      </Container>
    </>
  );
}
