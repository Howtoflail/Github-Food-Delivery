import Order from "./Order";

function OrderList(props) {
    return(
        <ul>
            {props.orders.map((order) => (
                <Order key={order.order.id} order={order.order} items={order.items} user={order.user} />
            ))}
        </ul>
    );
}

export default OrderList;