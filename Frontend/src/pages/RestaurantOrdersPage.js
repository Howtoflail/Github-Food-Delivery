import axios from "axios";
import jwtDecode from "jwt-decode";
import { useEffect, useState } from "react";
import { Helmet } from "react-helmet";
import OrderList from "../components/OrderList";

function RestaurantOrdersPage(props) {
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
        //deny users from getting to this page
        if(jwtDecoded.roles[0] === "USER"){
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
        axios.get(`http://localhost:8080/users/restaurant/${jwtDecoded.userId}`)
        .then((response) => {
            console.log(response);

            axios.get(`http://localhost:8080/orders?restaurant=${response.data}`)
            .then((orderResponse) => {
                console.log(orderResponse);
                orderResponse.data.orders.forEach((order) => {
                    tempOrders.push(order);
                });

                var tempOrderItems = [];
                axios.get(`http://localhost:8080/orderitems?restaurant=${response.data}`)
                .then((orderItemsResponse) => {
                    console.log(orderItemsResponse.data.orderItems);
                    orderItemsResponse.data.orderItems.forEach((orderItem) => {
                        tempOrderItems.push(orderItem);
                    });

                    var tempItems = [];
                    axios.get(`http://localhost:8080/items?restaurant=${response.data}`)
                    .then((itemsRepsonse) => {
                        itemsRepsonse.data.items.forEach((item) => {
                            tempItems.push(item);
                        });

                        //attaching user data to order
                        var tempUsers = [];
                        axios.get(`http://localhost:8080/users/getUsersFromOrder/${response.data}`)
                        .then((usersResponse) => {
                            usersResponse.data.users.forEach((user) => {
                                tempUsers.push(user);
                            });

                            var orders = [];
                            tempUsers.forEach((user) => {
                                tempOrders.forEach((order) => {
                                    if(user.id === order.user_id){
                                        var items = [];
                                        tempOrderItems.forEach((orderItem) => {
                                            if(order.id === orderItem.order_id){
                                                tempItems.forEach((item) => {
                                                    if(item.id === orderItem.item_id){
                                                        console.log(item);
                                                        items.push(item);
                                                    }
                                                });
                                            }
                                        });
                                        const orderWithItems = {
                                            order: order,
                                            user: user,
                                            items: items
                                        };
                                        orders.push(orderWithItems);
                                    }
                                });
                            });
                            setOrders(orders);
                        })
                        .catch((err5) => {
                            console.log(err5);
                        });

                    }).catch((err4) => {
                        console.log(err4);
                    });

                    console.log(tempItems);
                    console.log(tempOrders);
                    console.log(tempOrderItems);
                    //console.log(orders);
                    //setOrders(orderItemsWithItems);

                }).catch((err3) => {
                    console.log(err3);
                });

                //setOrders(orderResponse.data.orders);
            }).catch((err2) => {
                console.log(err2);
            });
        }).catch((err) => {
            console.log(err);
        });
    }, []);
    console.log(orders);

    return(
        <div className="container">
            <Helmet>
                <meta charSet="utf-8" />
                <title>View orders</title>
                <meta name="description" />
            </Helmet>
            <h1>All orders</h1>
            <div className="inner">
                <OrderList orders={orders} />
            </div>
        </div>
    );
}

export default RestaurantOrdersPage;