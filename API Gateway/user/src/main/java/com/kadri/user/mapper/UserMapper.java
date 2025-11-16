package com.kadri.user.mapper;

import com.kadri.user.dto.UserRequest;
import com.kadri.user.dto.UserResponse;
import com.kadri.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .build();
    }

    public void updateEntity(UserRequest request, User user){
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getEmail());
    }


    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .build();
    }
}
