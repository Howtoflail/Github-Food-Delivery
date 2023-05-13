package food.delivery.business.impl;

import food.delivery.domain.User;
import food.delivery.persistence.entity.UserEntity;

final class UserConverter {
    private UserConverter(){}

    public static User convert(UserEntity user){
        return User.builder()
                .id(user.getId())
                .first_name(user.getFirst_name())
                .last_name(user.getLast_name())
                .email(user.getEmail()).address(user.getAddress())
                .phone(user.getPhone()).password(user.getPassword())
                .build();
    }
}
