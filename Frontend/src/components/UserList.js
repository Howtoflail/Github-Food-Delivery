import User from "./User";
import React from "react";

function UserList(props) {
  return (
    <ul>
      {props.users.map((user) => (
        <User
          key={user.id}
          user={user}
          onRemove={props.onRemove}
          onUpdate={props.onUpdate}
        />
      ))}
    </ul>
  );
}

export default UserList;
