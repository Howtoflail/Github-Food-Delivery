import axios from "axios";
import { useEffect, useState } from "react";
import { Helmet } from "react-helmet";
import { Route, Router } from "react-router-dom";

function LoginPage(props) {
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const emailHandler = (e) => {
        setEmail(e.target.value);
    };

    const passwordHandler = (e) => {
        setPassword(e.target.value);
    };

    const submitHandler = (e) => {
        e.preventDefault();

        const loginDetails = {
            email: email,
            password: password,
        };

        axios.post("http://localhost:8080/login", loginDetails)
        .then((response) => {
            console.log(response.data.accessToken);
            sessionStorage.setItem("jwt", response.data.accessToken);
            window.location.href = "/home";
        })
        .catch((err) => {
            console.log(err);
        });
    }

    useEffect(() => {
        var jwt = sessionStorage.getItem("jwt");
        if(jwt != null){
            window.location.href = "/home";
        }
    }, []);

    return (
        <div className="form-comp">
            <Helmet>
                <meta charSet="utf-8" />
                <title>Login</title>
                <meta name="description" />
            </Helmet>
            <h2>Login</h2>
            <form className="create-user-form center-flexbox" onSubmit={submitHandler}>
                <label>
                    Email:
                    <input type="text" name="email" onChange={emailHandler} />
                </label>
                <label>
                    Password:
                    <input type="password" name="password" onChange={passwordHandler} />
                </label>
                <br />
                <button type="submit" name="login">Login</button>
            </form>
        </div>
    );
}

export default LoginPage;