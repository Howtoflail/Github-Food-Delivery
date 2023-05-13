import "./App.css";
import UsersPage from "./pages/UsersPage";
import CreateUserPage from "./pages/CreateUserPage";
import HomePage from "./pages/HomePage";
import CreateRestaurantPage from "./pages/CreateRestaurantPage";
import { Route, Routes, BrowserRouter as Router, Navigate } from "react-router-dom";
import NavBar from "./components/NavBar";
import LoginPage from "./pages/LoginPage";
import LogoutPage from "./pages/LogoutPage";
import RestaurantOrdersPage from "./pages/RestaurantOrdersPage";
import UsersOrdersPage from "./pages/UsersOrdersPage";
import OrderFromRestaurant from "./pages/OrderFromRestaurant";
import CheckoutPage from "./pages/CheckoutPage";
import CreateItemPage from "./pages/CreateItemPage";
//import RestaurantImage from "../restaurant_images/restaurant_resized.jpg";
import LogoImage from "./restaurant_images/1205763res.png"

function App() {
  return (
    <div className="App">
      <div className="App-header">
        <img src={LogoImage} alt="Logo" />
        <h2>Food Delivery</h2>
      </div>
      <Router>
        <NavBar />
        <Routes>
          <Route path="/" element={<Navigate to="/home" />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/menu/:restaurantId" element={<OrderFromRestaurant />} />
          <Route path="/checkout" element={<CheckoutPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/logout" element={<LogoutPage />} />
          <Route path="/users" element={<UsersPage />} />
          <Route path="/restaurantOrders" element={<RestaurantOrdersPage />} />
          <Route path="/createItem/:userId" element={<CreateItemPage />} />
          <Route path="/orderHistory" element={<UsersOrdersPage />} />
          <Route path="/createUser" element={<CreateUserPage />} />
          <Route path="/createRestaurant" element={<CreateRestaurantPage />} />
        </Routes>
      </Router>
    </div>
  );
}

export default App;
