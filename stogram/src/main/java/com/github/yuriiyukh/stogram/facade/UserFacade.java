package com.github.yuriiyukh.stogram.facade;

import com.github.yuriiyukh.stogram.annotation.Facade;
import com.github.yuriiyukh.stogram.dto.UserDTO;
import com.github.yuriiyukh.stogram.entity.UserEntity;

@Facade
public class UserFacade {

    public UserDTO userToUserDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setUserName(user.getUsername());
        userDTO.setBio(user.getBio());

        return userDTO;
    }
}
