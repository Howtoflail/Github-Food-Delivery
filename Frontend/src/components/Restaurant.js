import "./User.css";
import RestaurantImage from "../restaurant_images/restaurant_resized.jpg";
import { Link } from "react-router-dom";

function Restaurant(props) {

  return (
    <Link to={`/menu/${props.restaurant.id}`} state={{restaurantId: props.restaurant.id}} style={{color: "black", textDecoration: "none"}}>
      <li className="restaurant">
        <img src={RestaurantImage} alt="Restaurant" />
        <div className="restaurantInfo">
          <h3 className="restaurantName">{props.restaurant.name}</h3>
          <p className="restaurantAddress">{props.restaurant.address}</p>
        </div>
      </li>
    </Link>
  );
}

export default Restaurant;
