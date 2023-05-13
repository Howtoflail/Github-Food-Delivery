import UserOrder from "./UserOrder";

function UserOrderList(props) {
    return(
        <ul>
            {props.orders.map((order) => (
                <UserOrder key={order.order.id} order={order.order} restaurant={order.restaurant} items={order.items} />
            ))}
        </ul>
    );
}

export default UserOrderList;