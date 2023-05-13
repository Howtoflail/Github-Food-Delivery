import { useEffect, useState } from "react";
import jwtDecode from "jwt-decode";
import axios from "axios";
import { useLocation } from "react-router-dom";
import CheckoutBasket from "../components/CheckoutBasket";
import { Helmet } from "react-helmet";

function CheckoutPage(props) {
    const [jwtDecoded, setJwtDecoded] = useState({});
    const [items, setItems] = useState([]);
    const [user, setUser] = useState({});
    const [restaurant, setRestaurant] = useState("");

    const location = useLocation();
    useEffect(() => {
        var jwt = sessionStorage.getItem("jwt");
        if(jwt === null){
            window.location.href = "/login";
        }
        console.log(jwt);

        axios.defaults.headers.common["Authorization"] = `Bearer ${jwt}`;
        var jwtDecoded = jwtDecode(jwt);
        console.log(jwtDecoded);
        setJwtDecoded(jwtDecoded);

        //log out if jwt expired
        const expiryTime = jwtDecoded.exp * 1000;
        const expiryDate = new Date(expiryTime);
        const currentDate = new Date();

        if(currentDate >= expiryDate){
            delete axios.defaults.headers.common["Authorization"];
            sessionStorage.removeItem("jwt");
            window.location.href = "/login";
        }

        var tempItems;
        var tempRestaurant;
        try {
            tempItems = location.state.items;
            tempRestaurant = location.state.restaurant;
            console.log(tempItems);
            console.log(tempRestaurant);
        } catch (error) {
            window.location.href = "/home";
        }
        setItems(tempItems);
        setRestaurant(tempRestaurant);

        axios.get(`http://localhost:8080/users/${jwtDecoded.userId}`)
        .then((userResponse) => {
            //console.log(userResponse);
            setUser(userResponse.data);
        })
        .catch((err) => {
            console.log(err);
        });


    }, []);
    console.log(user);

    const handleOrder = (e) => {
        e.preventDefault();

        const order = {
            user_id: jwtDecoded.userId,
            restaurant_id: restaurant.id
        };

        axios.post(`http://localhost:8080/orders/save`, order)
        .then((orderResponse) => {
            console.log(orderResponse.data);

            var orderItems = [];
            items.forEach((item) => {
                var amount = item.amount;
                for(var i = 1; i <= amount; i++){
                    const orderItem = {
                        order_id: orderResponse.data.orderId,
                        item_id: item.id
                    };

                    orderItems.push(orderItem);
                }
            });
            console.log(orderItems);

            orderItems.forEach((orderItem) => {
                axios.post(`http://localhost:8080/orderitems/save`, orderItem)
                .then((orderItemResponse) => {
                    console.log(orderItemResponse.data);
                })
                .catch((err) => {
                    console.log(err);
                });
            });
        })
        .catch((err) => {
            console.log(err);
        });
    };

    return(
        <div className="bigContainer">
            <Helmet>
                <meta charSet="utf-8" />
                <title>Checkout</title>
                <meta name="description" />
            </Helmet>
            <div className="checkoutContainer">
                <h2 className="checkoutHeader">Checkout</h2>
                <div className="checkoutInner">
                    <p className="checkoutRestaurantName"> {restaurant.name} </p>
                    <ul>
                        <li className="checkoutUserDetail">
                            <p className="checkoutRestaurantName"> Address & personal details </p>
                            <p className="checkoutUserDetailText"> {user.address} </p>
                            <p className="checkoutUserDetailText"> {user.first_name} {user.last_name} </p>
                            <p className="checkoutUserDetailText"> {user.phone} </p>
                        </li>
                    </ul>
                    <button className="checkoutButton" onClick={handleOrder}>Order</button>
                </div>
            </div>
            <div className="basket">
                <CheckoutBasket items={items} />
            </div>
        </div>
    );
}

export default CheckoutPage;