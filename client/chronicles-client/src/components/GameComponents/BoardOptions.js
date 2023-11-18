import { Dropdown, Button } from "react-bootstrap";

export default function BoardOptions({
  boards,
  returnBoardToBox,
  rotateBoard,
  swapBoard,
}) {

  return (
    <Dropdown className="d-inline mx-2" autoClose="outside">
      <Dropdown.Toggle id="dropdown-autoclose-outside" variant="dark">
        Board Options
      </Dropdown.Toggle>
      <Dropdown.Menu>
        {boards &&
          boards.map((b, index) => {
            return (
              <Dropdown.Item key={b.id + "-" + index}>
                {b.name}
                <Button
                  onClick={() => returnBoardToBox(b)}
                  className="mx-2 px-2"
                  size="sm"
                  variant="dark"
                >
                  Return to Box
                </Button>
                <Button
                  onClick={() => rotateBoard(b.col, b.row, -90, b.name)}
                  className="mx-2 px-2"
                  size="sm"
                  variant="dark"
                >
                  Rotate &#x2190;
                </Button>
                <Button
                  onClick={() => rotateBoard(b.col, b.row, 90, b.name)}
                  className="mx-2 px-2"
                  size="sm"
                  variant="dark"
                >
                  Rotate &#x2192;
                </Button>
                <Button
                  onClick={() => rotateBoard(b.col, b.row, 180, b.name)}
                  className="mx-2 px-2"
                  size="sm"
                  variant="dark"
                >
                  Rotate &#x2193;
                </Button>
                <Dropdown className="d-inline mx-2" autoClose="outside">
                  <Dropdown.Toggle
                    id="dropdown-autoclose-outside"
                    variant="dark"
                  >
                    Swap with:
                  </Dropdown.Toggle>
                  <Dropdown.Menu>
                    {boards &&
                      boards.map((bs, index) => {
                        if (bs === b ){
                          return null;
                        } else {
                        return (
                      
                          <Dropdown.Item key={bs.id + "-" + index}>
                            <Button
                              onClick={() => swapBoard(b, bs)}
                              className="mx-2 px-2"
                              size="sm"
                              variant="dark"
                            >
                              {bs.name}
                            </Button>
                          </Dropdown.Item>
                        );
                      }
                      })}
                  </Dropdown.Menu>
                </Dropdown>
              </Dropdown.Item>
            );
          })}
      </Dropdown.Menu>
    </Dropdown>
  );
}
