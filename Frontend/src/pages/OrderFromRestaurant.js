import { useEffect, useState } from "react";
import jwtDecode from "jwt-decode";
import axios from "axios";
import { useLocation } from "react-router-dom";
import ItemList from "../components/ItemList"
import Basket from "../components/Basket";
import { Helmet } from "react-helmet";

function OrderFromRestaurant(props){
    const [restaurant, setRestaurant] = useState("");
    const [items, setItems] = useState([]);
    const [amountOfProducts, setAmountOfProducts] = useState("");
    const [itemsWithZeroAmount, setItemsWithZeroAmount] = useState([]);
    const [itemsWithNumber, setItemsWithNumber] = useState([]);

    const location = useLocation();
    useEffect(() => {
        var jwt = sessionStorage.getItem("jwt");
        if(jwt == null){
            window.location.href = "/login";
        }
        console.log(jwt);

        axios.defaults.headers.common["Authorization"] = `Bearer ${jwt}`;
        var jwtDecoded = jwtDecode(jwt);
        console.log(jwtDecoded);

        //log out if jwt expired
        const expiryTime = jwtDecoded.exp * 1000;
        const expiryDate = new Date(expiryTime);
        const currentDate = new Date();

        if(currentDate >= expiryDate){
            delete axios.defaults.headers.common["Authorization"];
            sessionStorage.removeItem("jwt");
            window.location.href = "/login";
        }
        
        const restaurantId = location.state.restaurantId;
        console.log(restaurantId);

        axios.get(`http://localhost:8080/restaurants/${restaurantId}`)
        .then((restaurantResponse) => {
            console.log(restaurantResponse);
            setRestaurant(restaurantResponse.data);
        })
        .catch((restaurantError) => {
            console.log(restaurantError);
        });

        var tempItems = [];
        axios.get(`http://localhost:8080/items?restaurant=${restaurantId}`)
        .then((itemsResponse) => {
            itemsResponse.data.items.forEach((item) => {
                tempItems.push(item);
            });

            console.log(tempItems);
            setItems(tempItems);

            var tempItemsWithZeroAmount = [];
            tempItems.forEach((item) => {
                const itemWithZeroAmount = {
                    id: item.id,
                    name: item.name,
                    amount: 0,
                    price: item.price,
                    totalPrice: 0
            };

            tempItemsWithZeroAmount.push(itemWithZeroAmount);
            });

            console.log(tempItemsWithZeroAmount);
            setItemsWithZeroAmount(tempItemsWithZeroAmount);
        })
        .catch((itemsError) => {
            console.log(itemsError);
        });
    }, []);

    //var tempAmountOfProducts = [];
    const handleAmountOfProducts = (data) => {
        //console.log(data);
        // tempAmountOfProducts.push(data);
        // console.log(tempAmountOfProducts);

        setAmountOfProducts(data);
    };

    useEffect(() => {
        console.log(amountOfProducts);
        
        var nameOfItem = amountOfProducts.split(": ");
        console.log(nameOfItem);
        console.log(nameOfItem[1]);

        var tempItemsWithNumbers = [];
        itemsWithZeroAmount.forEach((item) => {
            var amount = +nameOfItem[1];
            //amount = (Math.round(amount * 100) / 100).toFixed(2);
            console.log(amount);

            if(item.name === nameOfItem[0]){
                if(amount > item.amount){
                    item.amount = amount;
                    item.totalPrice += item.price;
                }
                else if(amount < item.amount){
                    item.amount = amount;
                    item.totalPrice -= item.price;
                }
            }

            tempItemsWithNumbers.push(item);
        });
        console.log(tempItemsWithNumbers);
        setItemsWithNumber(tempItemsWithNumbers);
    }, [amountOfProducts]);
    console.log(itemsWithNumber);

    return(
        <div className="bigContainer">
            <Helmet>
                <meta charSet="utf-8" />
                <title>Order</title>
                <meta name="description" />
            </Helmet>
            <div className="container">
                <h2>{restaurant.name}</h2>
                <div className="inner">
                    <ItemList items={items} number={handleAmountOfProducts} />
                </div>
            </div>
            <div className="basket">
                <Basket items={itemsWithNumber} restaurant={restaurant} />
            </div>
        </div>
    );
}

export default OrderFromRestaurant;