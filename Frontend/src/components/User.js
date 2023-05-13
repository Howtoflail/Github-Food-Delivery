import "./User.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { faPenToSquare } from "@fortawesome/free-solid-svg-icons";

function User(props) {
  return (
    <li className="item">
      {props.user.first_name +
        " " +
        props.user.last_name +
        " " +
        props.user.email}
      {/* <button onClick={() => props.onRemove(props.user.id)}>Delete</button> */}
      <FontAwesomeIcon
        icon={faTrash}
        className="buttons"
        onClick={() => props.onRemove(props.user.id)}
      />
      {/* <button onClick={() => props.onUpdate(props.user.id)}>Update</button> */}
      <FontAwesomeIcon
        icon={faPenToSquare}
        className="buttons"
        onClick={() => props.onUpdate(props.user.id)}
      />
    </li>
  );
}

export default User;
