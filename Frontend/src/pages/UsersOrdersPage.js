import axios from "axios";
import jwtDecode from "jwt-decode";
import { useEffect, useState } from "react";
import { Helmet } from "react-helmet";
import UserOrderList from "../components/UserOrderList";


function UsersOrdersPage(props) {
    const [orders, setOrders] = useState([]);

    useEffect(() => {
        var jwt = sessionStorage.getItem("jwt");
        if(jwt == null){
            window.location.href = "/login";
        }
        console.log(jwt);

        axios.defaults.headers.common["Authorization"] = `Bearer ${jwt}`;
        var jwtDecoded = jwtDecode(jwt);
        console.log(jwtDecoded);
        //deny restaurant users from getting to this page
        if(jwtDecoded.roles[0] === "RESTAURANT"){
            window.location.href = "/home";
        }

        //log out if jwt expired
        const expiryTime = jwtDecoded.exp * 1000;
        const expiryDate = new Date(expiryTime);
        const currentDate = new Date();

        if(currentDate >= expiryDate){
            delete axios.defaults.headers.common["Authorization"];
            sessionStorage.removeItem("jwt");
            window.location.href = "/login";
        }

        var tempOrders = [];
        axios.get(`http://localhost:8080/orders/userOrders?user=${jwtDecoded.userId}`)
        .then((ordersResponse) => {
            ordersResponse.data.orders.forEach((order) => {
                tempOrders.push(order);
            });
            console.log(tempOrders);

                //attaching restaurant data to order
                var tempRestaurants = [];
                axios.get(`http://localhost:8080/restaurants/getRestaurantsFromUser?user=${jwtDecoded.userId}`)
                .then((restaurantsResponse) => {
                    restaurantsResponse.data.restaurants.forEach((restaurant) => {
                        tempRestaurants.push(restaurant);
                    });
                    console.log(tempRestaurants);

                    var tempItems = [];
                    axios.get(`http://localhost:8080/items/getItemsFromUser?user=${jwtDecoded.userId}`)
                    .then((itemsResponse) => {
                        itemsResponse.data.items.forEach((item) => {
                            tempItems.push(item);
                        });
                        console.log(tempItems);

                        var tempOrderItems = [];
                        axios.get(`http://localhost:8080/orderitems/getOrderItemsFromUser?user=${jwtDecoded.userId}`)
                        .then((orderItemsResponse) => {
                            orderItemsResponse.data.orderItems.forEach((orderItem) => {
                                tempOrderItems.push(orderItem);
                            });
                            console.log(tempOrderItems);

                            var orders = [];
                            tempRestaurants.forEach((restaurant) => {
                                tempOrders.forEach((order) => {
                                    if(order.restaurant_id === restaurant.id){
                                        var items = [];
                                        tempOrderItems.forEach((orderItem) => {
                                            if(order.id === orderItem.order_id){
                                                tempItems.forEach((item) => {
                                                    if(item.id === orderItem.item_id){
                                                        items.push(item);
                                                    }
                                                });
                                            }
                                        });
                                        const orderWithItems = {
                                            order: order,
                                            restaurant: restaurant,
                                            items: items
                                        };
                                        orders.push(orderWithItems);
                                    }
                                });
                            });
                            setOrders(orders);
                            console.log(orders);
                        }).catch((err4) => {
                            console.log(err4);
                        });
                    }).catch((err3) => {
                        console.log(err3);
                    });
                }).catch((err2) => {
                    console.log(err2);
                }); 
        }).catch((err) => {
            console.log(err);
        });
    }, []);

    return(
        <div className="container">
            <Helmet>
                <meta charSet="utf-8" />
                <title>Order history</title>
                <meta name="description" />
            </Helmet>
            <h1>Order history</h1>
            <div className="inner">
                <UserOrderList orders={orders} />
            </div>
        </div>
    );
}

export default UsersOrdersPage;