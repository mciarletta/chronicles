import { Dropdown, Button, ListGroup } from "react-bootstrap";

export default function DieMenu({ box, takeOutDie }) {

    let catList = [];

    for (const obj of box) {
        if (obj.category === "die") {
          catList.push(
              <ListGroup.Item key={obj.id + "-" + obj.category}>
                {obj.name}
                <Button
                  className="px-2 mx-2"
                  size="sm"
                  variant="dark"
                onClick={() => takeOutDie(obj)}
                >
                  Take Out
                </Button>
    
              </ListGroup.Item>
          );
        } 
    }


  return (
    <Dropdown className="d-inline mx-2" autoClose="outside">
      <Dropdown.Toggle id="dropdown-autoclose-outside" variant="dark">
        Dice
      </Dropdown.Toggle>
      <Dropdown.Menu>
      <ListGroup>{catList}</ListGroup>

      </Dropdown.Menu>
    </Dropdown>
  );
}
