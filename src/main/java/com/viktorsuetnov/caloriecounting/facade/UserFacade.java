package com.viktorsuetnov.caloriecounting.facade;

import com.viktorsuetnov.caloriecounting.dto.UserDTO;
import com.viktorsuetnov.caloriecounting.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserFacade {

    public UserDTO userToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }
}
