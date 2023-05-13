import styles from "../App.css";
import UserList from "../components/UserList";
import User from "../components/User";
import jwtDecode from "jwt-decode";
import { useEffect, useState } from "react";
import axios from "axios";
import Modal from "react-modal";
import { Helmet } from "react-helmet";

function UsersPage() {
  const [users, setUsers] = useState([]);
  const [modalIsOpen, setIsOpen] = useState(false);
  const [userId, setUserId] = useState(null);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [address, setAddress] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");

  //displaying users
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
        if(jwtDecoded.roles[0] === "USER" || jwtDecoded.roles[0] === "RESTAURANT"){
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

    axios.get("http://localhost:8080/users").then((response) => {
      setUsers(response.data.users);
    });
  }, []);

  // const getUsers = async () => {
  //   const response = await axios.get("http://localhost:8080/users");
  //   setUsers(response.data.users);
  // };

  const handleRemove = (id) => {
    axios.delete(`http://localhost:8080/users/delete/${id}`);

    const newUsers = users.filter((user) => user.id !== id);
    setUsers(newUsers);
  };

  const handleUpdate = (id) => {
    setUserId(id);
    setIsOpen(true);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    const user = {
      id: userId,
      first_name: firstName,
      last_name: lastName,
      email: email,
      address: address,
      phone: phone,
      password: password
    };

    axios.put(`http://localhost:8080/users/update/${userId}`, user);
  };

  const closeModal = () => {
    setIsOpen(false);
  };

  const firstNameChanged = (e) => {
    setFirstName(e.target.value);
  };

  const lastNameChanged = (e) => {
    setLastName(e.target.value);
  };

  const emailChanged = (e) => {
    setEmail(e.target.value);
  };

  const addressChanged = (e) => {
    setAddress(e.target.value);
  };

  const phoneChanged = (e) => {
    setPhone(e.target.value);
  };

  const passwordChanged = (e) => {
    setPassword(e.target.value);
  };

  return (
    <div className="container">
      <Helmet>
        <meta charSet="utf-8" />
        <title>View users</title>
        <meta name="description" />
      </Helmet>
      <h2>Users</h2>
      <div className="inner">
        <UserList
          users={users}
          onRemove={handleRemove}
          onUpdate={handleUpdate}
        />
        <Modal
          isOpen={modalIsOpen}
          onRequestClose={closeModal}
          contentLabel="Edit user"
          className={"modal-container"}
        >
          <button onClick={closeModal}>Close</button>
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="First name"
              onChange={firstNameChanged}
            />
            <input
              type="text"
              placeholder="Last name"
              onChange={lastNameChanged}
            />
            <input type="text" placeholder="Email" onChange={emailChanged} />
            <input type="text" placeholder="Address" onChange={addressChanged} />
            <input type="text" placeholder="Phone" onChange={phoneChanged} />
            <input type="password" placeholder="Password" onChange={passwordChanged} />
            <button type="submit">Submit</button>
          </form>
        </Modal>
      </div>
    </div>
  );
}

export default UsersPage;
