import ItemForOrder from "./ItemForOrder";

function ItemList(props) {
    const numberOfItems = (data) => {
        props.number(data);
    };

    return(
        <ul className="menuItemList">
            {props.items.map((item) => (
                <ItemForOrder key={item.id} item={item} number={numberOfItems} />
            ))}
        </ul>
    );
}

export default ItemList;