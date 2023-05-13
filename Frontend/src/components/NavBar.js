import jwtDecode from "jwt-decode";
import React, { useEffect, useState } from "react";
//import { NavLink } from "react-router-dom";
import { Nav, NavLink, Bars, NavMenu, NavBtn, NavBtnLink } from "./NavBarElements"; 
//import "bootstrap/dist/css/bootstrap.min.css";
//import "./NavBar.css";

function NavBar() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [role, setRole] = useState("");
  const [userId, setUserId] = useState("");

  useEffect(() => {
    var jwt = sessionStorage.getItem("jwt");
    if(jwt != null){
      setIsLoggedIn(true);
      var jwtDecoded = jwtDecode(jwt);
      setRole(jwtDecoded.roles[0]);

      if(jwtDecoded.roles[0] === "RESTAURANT"){
        setUserId(jwtDecoded.userId);
      }
    }
  }, []);
  
  const NavElementsRender = () => {
    if(role === "USER"){
    return(
      <>
        <NavLink to="/home" activestyle="true">
          Home
        </NavLink>
        {/* <NavLink to="/createUser" activestyle="true">
          Sign up
        </NavLink> */}
        <NavLink to="/orderHistory" activestyle="true">
          Order history
        </NavLink>
        <NavLink to="/createRestaurant" activestyle="true">
          Join us!
        </NavLink>
        {/* <NavLink to="/users" activestyle="true">
          Users
        </NavLink> */}
      </>
    );}
    else if(role === "RESTAURANT"){
      return(
        <>
          <NavLink to="/home" activestyle="true">
            Home
          </NavLink>
          <NavLink to="/restaurantOrders" activestyle="true">
            Orders
          </NavLink>
          <NavLink to={`/createItem/${userId}`} state={{userId: userId}}>
            Create item
          </NavLink>
        </>
      );
    }
    else if (role ==="ADMIN"){
      return(
        <>
          <NavLink to="/home" activestyle="true">
            Home
          </NavLink>
          {/* <NavLink to="/createUser" activestyle="true">
            Sign up
          </NavLink>
          <NavLink to="/createRestaurant" activestyle="true">
            Join us!
          </NavLink> */}
          <NavLink to="/users" activestyle="true">
            Users
          </NavLink>
        </>
      );
    }
    else {
      return(
        <>
          <NavLink to="/createUser" activestyle="true">
            Sign up
          </NavLink>
          <NavLink to="/createRestaurant" activestyle="true">
            Join us!
          </NavLink>
        </>
      );
    }
  };

  return (
    <>
      <Nav>
        <Bars />
        <NavMenu>
          <NavElementsRender />
        </NavMenu>
        <NavBtn>
          {isLoggedIn 
          ? <NavBtnLink to="/logout">Log out</NavBtnLink>
          : <NavBtnLink to="/login">Log in</NavBtnLink>}
        </NavBtn>
      </Nav>
    </>


    // <nav className="navbar navbar-expand-lg navbar-light bg-light">
    //   <button
    //     className="navbar-toggler"
    //     type="button"
    //     data-toggle="collapse"
    //     data-target="#navbarNavDropdown"
    //     aria-controls="navbarNavDropdown"
    //     aria-expanded="false"
    //     aria-label="Toggle navigation"
    //   >
    //     <span className="navbar-toggler-icon"></span>
    //   </button>
    //   <div className="collapse navbar-collapse" id="navbarNavDropdown">
    //     <ul className="navbar-nav mr-auto">
    //       <li className="nav-item active">
    //         <a className="nav-link fs-5" href="http://localhost:3000/">
    //           Home
    //         </a>
    //       </li>
    //       <li className="nav-item">
    //         <a
    //           className="nav-link fs-5"
    //           href="http://localhost:3000/createUser/"
    //         >
    //           Create user
    //         </a>
    //       </li>
    //     </ul>
    //     <ul className="navbar-nav">
    //       <li className="nav-item">
    //         <a
    //           className="nav-link fs-5"
    //           href="http://localhost:3000/createRestaurant/"
    //         >
    //           Join us!
    //         </a>
    //       </li>
    //       <li className="nav-item">
    //         <a className="nav-link fs-5" href="http://localhost:3000/users/">
    //           Users
    //         </a>
    //       </li>
    //     </ul>
    //   </div>
    // </nav>

    // <nav className="navBar">
    //   <ul>
    //     {links.map((link) => {
    //       return (
    //         <li key={link.id}>
    //           <NavLink to={link.path}>{link.text}</NavLink>
    //         </li>
    //       );
    //     })}
    //   </ul>
    // </nav>
  );
}

export default NavBar;
