import axios from "axios";
import { useState, cloneElement } from "react";
import { Helmet } from "react-helmet";

function CreateRestaurantPage() {
  const [name, setName] = useState("");
  const [address, setAddress] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [password, setPassword] = useState("");
  const [type, setType] = useState("");
  const [selected, setSelected] = useState("Select...");

  const options = [
    { value: "african", label: "African" },
    { value: "american", label: "American" },
    { value: "asian", label: "Asian" },
    { value: "bakery", label: "Bakery" },
    { value: "burgers", label: "Burgers" },
    { value: "chinese", label: "Chinese" },
    { value: "coffee", label: "Coffee & Tea" },
    { value: "desert", label: "Desert" },
    { value: "indian", label: "Indian" },
    { value: "japanese", label: "Japanese" },
    { value: "mexican", label: "Mexican" },
    { value: "pizza", label: "Pizza" },
    { value: "steakhouse", label: "Steakhouse" },
  ];

  const handleMenuOne = () => {
    setSelected("African");
    setType("african");
  };

  const handleMenuTwo = () => {
    setSelected("American");
    setType("american");
  };

  const handleMenuThree = () => {
    setSelected("Asian");
    setType("asian");
  };

  const handleMenuFour = () => {
    setSelected("Bakery");
    setType("bakery");
  };

  const handleMenuFive = () => {
    setSelected("Burgers");
    setType("burgers");
  };

  const handleMenuSix = () => {
    setSelected("Chinese");
    setType("chinese");
  };

  const handleMenuSeven = () => {
    setSelected("Coffee & Tea");
    setType("coffee");
  };

  const handleMenuEight = () => {
    setSelected("Desert");
    setType("desert");
  };

  const handleMenuNine = () => {
    setSelected("Indian");
    setType("indian");
  };

  const handleMenuTen = () => {
    setSelected("Japanese");
    setType("japanese");
  };

  const handleMenuEleven = () => {
    setSelected("Mexican");
    setType("mexican");
  };

  const handleMenuTwelve = () => {
    setSelected("Pizza");
    setType("pizza");
  };

  const handleMenuThirteen = () => {
    setSelected("Steakhouse");
    setType("steakhouse");
  };

  const Dropdown = ({ trigger, menu }) => {
    const [open, setOpen] = useState(false);

    const handleOpen = () => {
      setOpen(!open);
    };

    return (
      <div className="dropdown">
        {cloneElement(trigger, { onClick: handleOpen })}
        {open ? (
          <ul className="menu">
            {menu.map((menuItem, index) => (
              <li key={index} className="menu-item">
                {cloneElement(menuItem, {
                  onClick: () => {
                    menuItem.props.onClick();
                    setOpen(false);
                  },
                })}
              </li>
            ))}
          </ul>
        ) : null}
      </div>
    );
  };

  const nameHandler = (e) => {
    setName(e.target.value);
  };

  const addressHandler = (e) => {
    setAddress(e.target.value);
  };

  const firstNameHandler = (e) => {
    setFirstName(e.target.value);
  };

  const lastNameHandler = (e) => {
    setLastName(e.target.value);
  };

  const emailHandler = (e) => {
    setEmail(e.target.value);
  };

  const phoneHandler = (e) => {
    setPhone(e.target.value);
  };

  const passwordHandler = (e) => {
    setPassword(e.target.value);
  };

  const submitHandler = (e) => {
    e.preventDefault();
    e.target.reset();

    const user = {
      first_name: firstName,
      last_name: lastName,
      email: email,
      address: address,
      phone: phone,
      password: password,
    };

    axios.post(`http://localhost:8080/users/saveRestaurant`, user)
    .then((userResponse) => {
      const userId = userResponse.data;

      const restaurant = {
        name: name,
        address: address,
        type: type,
        user_id: userId
      };

      axios.post(`http://localhost:8080/restaurants/save`, restaurant)
      .then((restaurantResponse) => {
        console.log(restaurantResponse.data);
        document.getElementById("submitMessage").innerHTML =
          "The restaurant has been created!";

          setTimeout(() => {
            window.location.href="/login";
          }, 3000);
      })
      .catch((err2) => {
        console.log(err2);
      });
    })
    .catch((err) => {
      console.log(err);
      document.getElementById("submitMessage").innerHTML = //error;
        "A restaurant with the same name already exists!";
    });
  };

  return (
    <div className="form-comp">
      <Helmet>
        <meta charSet="utf-8" />
        <title>Create restaurant</title>
        <meta name="description" />
      </Helmet>
      <h2>Add your restaurant</h2>
      <form
        className="create-user-form center-flexbox"
        onSubmit={submitHandler}
      >
        <label>
          Name of the restaurant:
          <input type="text" name="name" onChange={nameHandler} />
        </label>
        <br />
        <label>
          Address:
          <input type="text" onChange={addressHandler} />
        </label>
        <br />
        <Dropdown
          trigger={<button type="button">{selected}</button>}
          menu={[
            <button type="button" onClick={handleMenuOne}>
              African
            </button>,
            <button type="button" onClick={handleMenuTwo}>
              American
            </button>,
            <button type="button" onClick={handleMenuThree}>
              Asian
            </button>,
            <button type="button" onClick={handleMenuFour}>
              Bakery
            </button>,
            <button type="button" onClick={handleMenuFive}>
              Burgers
            </button>,
            <button type="button" onClick={handleMenuSix}>
              Chinese
            </button>,
            <button type="button" onClick={handleMenuSeven}>
              Coffee & Tea
            </button>,
            <button type="button" onClick={handleMenuEight}>
              Desert
            </button>,
            <button type="button" onClick={handleMenuNine}>
              Indian
            </button>,
            <button type="button" onClick={handleMenuTen}>
              Japanese
            </button>,
            <button type="button" onClick={handleMenuEleven}>
              Mexican
            </button>,
            <button type="button" onClick={handleMenuTwelve}>
              Pizza
            </button>,
            <button type="button" onClick={handleMenuThirteen}>
              Steakhouse
            </button>,
          ]}
        />
        <br />
        <h3>Details for the administrative restaurant account:</h3>
        <label>
          First name:
          <input type="text" onChange={firstNameHandler} />
        </label>
        <br />
        <label>
          Last name:
          <input type="text" onChange={lastNameHandler} />
        </label>
        <br />
        <label>
          Phone:
          <input type="text" onChange={phoneHandler} />
        </label>
        <br />
        <label>
          Email:
          <input type="text" onChange={emailHandler} />
        </label>
        <br />
        <label>
          Password:
          <input type="password" onChange={passwordHandler} />
        </label>
        <br />
        <button type="submit">Submit</button>
      </form>
      <h3 id="submitMessage"></h3>
    </div>
  );
}

export default CreateRestaurantPage;
