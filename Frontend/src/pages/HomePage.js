import styles from "../App.css";
import axios from "axios";
import { useEffect, useState } from "react";
import RestaurantList from "../components/RestaurantList";
import jwtDecode from "jwt-decode";
import { Helmet } from "react-helmet";

function HomePage() {
  const [restaurants, setRestaurants] = useState([]);
  const [user, setUser] = useState("");
  let restaurantsLength = 0;

  useEffect(() => {
    var jwt = sessionStorage.getItem("jwt");
    if(jwt == null){
      delete axios.defaults.headers.common["Authorization"];
      window.location.href = "/login";
    }
    else{
      console.log(jwt);
      var jwtDecoded = jwtDecode(jwt);
      console.log(jwtDecoded);
      // var role = jwtDecoded.roles;
      // console.log(role[0]);

      const expiryTime = jwtDecoded.exp * 1000;
      const expiryDate = new Date(expiryTime);
      const currentDate = new Date();

      if(currentDate >= expiryDate){
        delete axios.defaults.headers.common["Authorization"];
        sessionStorage.removeItem("jwt");
        window.location.href = "/login";
      }

      axios.defaults.headers.common["Authorization"] = `Bearer ${jwt}`;

      axios.get(`http://localhost:8080/users/${jwtDecoded.userId}`)
      .then((response) => {
        console.log(response);
        setUser(response.data);
      })
      .catch((err) => {
        console.log(err);
      });
    }

    axios.get("http://localhost:8080/restaurants").then((response) => {
      setRestaurants(response.data.restaurants);
      //restaurantsLength = restaurants.length;
    });
  }, []);
  restaurantsLength = restaurants.length;

  return (
    <div className="container">
      <Helmet>
        <meta charSet="utf-8" />
        <title>Home</title>
        <meta name="description" />
      </Helmet>
      <h1>Order from {restaurantsLength} restaurants</h1>
      <p className="userFirstName">Hi, {user.first_name}</p>
      <div className="inner">
        <RestaurantList restaurants={restaurants} />
      </div>
    </div>
  );
}

export default HomePage;
