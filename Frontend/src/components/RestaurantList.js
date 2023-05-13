import Restaurant from "./Restaurant";

function RestaurantList(props) {
  return (
    <ul>
      {props.restaurants.map((restaurant) => (
        <Restaurant key={restaurant.id} restaurant={restaurant} />
      ))}
    </ul>
  );
}

export default RestaurantList;
