function Item(props) {
return(
    <div>
        Item: {props.item.name} €{props.item.price}
    </div>
);
}

export default Item;