import { useEffect } from "react";


function LogoutPage(props) {
useEffect(() => {
    var jwt = sessionStorage.getItem("jwt");
    if(jwt == null){
        window.location.href = "/login";
    }
    else{
        sessionStorage.removeItem("jwt");
        window.location.href = "/login";
    }
}, []);

}

export default LogoutPage;