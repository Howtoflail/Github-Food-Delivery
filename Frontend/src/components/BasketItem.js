

function BasketItem(props){

return(
    <li className="basketItem">
        <p> {props.item.amount} </p>
        <p className="basketItemName"> {props.item.name} </p>
        <p className="basketItemPrice"> € {props.item.totalPrice} </p>
    </li>
);
}

export default BasketItem;