import { useState } from "react";
import "./User.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus, faMinus } from "@fortawesome/free-solid-svg-icons";

function ItemForOrder(props) {
    const [number, setNumber] = useState(0);

    const handleAddProduct = () => {
        props.number(`${props.item.name}: ${number + 1}`);
        setNumber(number + 1);
    };

    const handleDeductProduct = () => {
        if(number > 0){
            props.number(`${props.item.name}: ${number - 1}`);
            setNumber(number - 1);
        }
    };

    return(
        <li className="menuItem">
            <p className="menuItemText"> {props.item.name}  € {props.item.price} </p>
            <FontAwesomeIcon icon={faPlus} onClick={handleAddProduct} className="itemButtons" />
            <FontAwesomeIcon icon={faMinus} onClick={handleDeductProduct} className="itemButtons" />
        </li>
    );
}

export default ItemForOrder;