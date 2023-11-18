import { Dropdown, DropdownButton } from "react-bootstrap";
import CategoryForm from "./CategoryForm";
import CardBoxMenu from "./CardBoxMenu";
import DiscardMenu from "./DiscardMenu";

export default function BoxMenu({ categories, takeOutOfBox, putCardInHand, returnCardToBox, returnAllDiscard }) {
  return (
    <Dropdown className="d-inline mx-2" autoClose="outside" variant="dark">
      <Dropdown.Toggle id="dropdown-autoclose-outside" variant="dark">
        Box Access
      </Dropdown.Toggle>

      <Dropdown.Menu>
        {categories.map((cat, index) => {
          return (
            <Dropdown.Item key={cat + "-" + index} className="categories">
              {cat}
              <DropdownButton variant="dark"  drop="end" title="select pieces" size="sm">
                {cat === "cards" && <CardBoxMenu putCardInHand={putCardInHand} ></CardBoxMenu>}
                {(cat === "figures" || cat === "boards") && <CategoryForm category={cat} takeOutOfBox={takeOutOfBox} ></CategoryForm>}
                {cat === "Discarded Cards" && <DiscardMenu putCardInHand={putCardInHand} returnCardToBox={returnCardToBox} returnAllDiscard={returnAllDiscard} ></DiscardMenu>}
                
              </DropdownButton>
            </Dropdown.Item>
          );
        })}
      </Dropdown.Menu>
    </Dropdown>
  );
}
