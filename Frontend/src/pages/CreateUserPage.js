import axios from "axios";
import React, { useEffect, useState } from "react";
import { Helmet } from "react-helmet";

function CreateUserPage(props) {
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [address, setAddress] = useState("");
  const [phone, setPhone] = useState("");
  const [isNotValid, setIsNotValid] = useState(true);

  useEffect(() => {
    if(firstName !== "" && lastName !== "" && email !== "" && password !== "" && address !== "" && phone !== ""){
      setIsNotValid(false);
    }
    else{
      setIsNotValid(true);
    }
  }, [firstName, lastName, email, password, address, phone]);

  const handleSubmit = (e) => {
    e.preventDefault();
    e.target.reset();

    const user = {
      first_name: firstName,
      last_name: lastName,
      email: email,
      password: password,
      address: address,
      phone: phone,
      role: "USER",
    };

    axios
      .post("http://localhost:8080/users/save", user)
      .then(() => {
        document.getElementById("submitMessage").innerHTML = "User created!";

        setTimeout(() => {
          window.location.href="/login";
        }, 3000);
      })
      .catch((error) => {
        document.getElementById("submitMessage").innerHTML =
          "This email address is already in use!";
      });
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

  const passwordChanged = (e) => {
    setPassword(e.target.value);
  };

  const addressChanged = (e) => {
    setAddress(e.target.value);
  };

  const phoneChanged = (e) => {
    setPhone(e.target.value);
  };

  return (
    <div className="form-comp">
      <Helmet>
        <meta charSet="utf-8" />
        <title>Create user</title>
        <meta name="description" />
      </Helmet>
      <h2>Create user</h2>
      <form className="create-user-form center-flexbox" onSubmit={handleSubmit}>
        <label>
          First name:
          <input type="text" name="first_name" onChange={firstNameChanged} />
        </label>
        <br />
        <label>
          Last name:
          <input type="text" name="last_name" onChange={lastNameChanged} />
        </label>
        <br />
        <label>
          Email:
          <input type="text" name="email" onChange={emailChanged} />
        </label>
        <br />
        <label>
          Password:
          <input type="password" name="password" onChange={passwordChanged} />
        </label>
        <br />
        <label>
          Address:
          <input type="text" name="address" onChange={addressChanged} />
        </label>
        <br />
        <label>
          Phone:
          <input type="text" name="phone" onChange={phoneChanged} />
        </label>
        <br />
        <button type="submit" disabled={isNotValid}>Submit</button>
      </form>
      <h3 id="submitMessage"></h3>
    </div>
  );
}

export default CreateUserPage;
