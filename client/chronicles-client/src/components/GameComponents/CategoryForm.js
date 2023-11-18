import { useContext } from "react";
import BoxContext from "../contexts/BoxContext";
import { Button, ListGroup } from "react-bootstrap";

export default function CategoryForm({ category, takeOutOfBox }) {
  //grab our box contenxt
  const box = useContext(BoxContext);

  const catList = [];

  for (const obj of box) {
    if (obj.category === category && category === "boards") {
      catList.push(
          <ListGroup.Item className="mx-2" key={obj.id + "-" + obj.category}>
            {obj.name}
            <Button
              className="px-2 mx-2"
              size="sm"
              variant="dark"
              onClick={() => takeOutOfBox(obj)}
            >
              Take Out
            </Button>

          </ListGroup.Item>
      );
    } else if (obj.category === category && category === "figures") {
      catList.push(
          <ListGroup.Item className="mx-2" key={obj.id + "-" + obj.category}>
            {obj.name}
            <Button
              className="px-2 mx-2"
              size="sm"
              variant="dark"
              onClick={() => takeOutOfBox(obj)}
            >
              Take Out
            </Button>
          </ListGroup.Item>
      );
    } 
  }

  return (
    <>
    <ListGroup style={{ maxHeight: "50vh", overflowY: "scroll"}}>
    {catList}

    </ListGroup>
    </>
  );
}

