import "./User.css";
import Item from "./Item";
import { useEffect, useState } from "react";

function Order(props) {
    const [totalPrice, setTotalPrice] = useState(0);
    const [date, setDate] = useState("");

    useEffect(() => {
        var sum = 0;
        props.items.forEach((item) => {
            sum += item.price;
        });
        setTotalPrice(sum);

        var dateTime = props.order.dateTime;
        var date = dateTime.substring(0, 9);
        var time = dateTime.substring(11, 19);
        dateTime = date + " " + time;
        setDate(dateTime);
    }, []);

    return(
        <li className="item">
            <div>
            {"Order #" + props.order.id}
            </div>
            <div>
                <h3>User details:</h3>
                <p>name: {props.user.first_name + " " + props.user.last_name}</p>
                <p>email: {props.user.email}</p>
                <p>address: {props.user.address}</p>
                <p>phone: {props.user.phone}</p>
            </div>
            <h3>Order details:</h3>
            <p>Date: {date}</p>
            {props.items.map((item) => (
                <Item key={item.id} item={item} />
            ))}
            <p>Total sum: €{totalPrice}</p>
        </li>
    );
}

export default Order;