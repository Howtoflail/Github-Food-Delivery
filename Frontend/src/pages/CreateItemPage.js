import { useEffect, useState } from "react";
import jwtDecode from "jwt-decode";
import axios from "axios";
import { useLocation } from "react-router-dom";
import { Helmet } from "react-helmet";

function CreateItemPage(props) {
    const [userId, setUserId] = useState();
    const [restaurantId, setRestaurantId] = useState();
    const [name, setName] = useState();
    const [price, setPrice] = useState();

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

        const userId = location.state.userId;
        setUserId(userId);
        console.log(userId);

        axios.get(`http://localhost:8080/users/restaurant/${userId}`)
        .then((response) => {
            console.log(response.data);
            setRestaurantId(response.data);
        })
        .catch((err) => {
            console.log(err);
        });
    }, []);

    const nameHandler = (e) => {
        setName(e.target.value);
    };

    const priceHandler = (e) => {
        setPrice(e.target.value);
    };

    const submitHandler = (e) => {
        e.preventDefault();
        e.target.reset();

        const item = {
            name: name,
            price: price,
            restaurant_id: restaurantId
        };

        axios.post(`http://localhost:8080/items/save`, item)
        .then((response) => {
            console.log(response.data);

            document.getElementById("submitMessage").innerHTML =
          "Item has been created!";
        }).catch((err) => {
            console.log(err);

            document.getElementById("submitMessage").innerHTML =
          "Make sure all the fields have correct data!";
        });
    };

    return(
        <div className="container">
            <Helmet>
                <meta charSet="utf-8" />
                <title>Create item</title>
                <meta name="description" />
            </Helmet>
            <h2>Create item</h2>
                <form className="create-user-form center-flexbox" onSubmit={submitHandler}>
                    <label>
                        Name of the item:
                        <input type="text"  onChange={nameHandler}/>
                    </label>
                    <br />
                    <label>
                        Price:
                        <input type="text"  onChange={priceHandler}/>
                    </label>
                    <br />
                    <button type="submit">Submit</button>
                </form>
                <h3 id="submitMessage"></h3>
        </div>
    );
}

export default CreateItemPage;