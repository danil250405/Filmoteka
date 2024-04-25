package org.glazweq.demo.service;

import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
    User findUserByUsername(String username);
    List<UserDto> findAllUsers();
//    List<UserDto> findAllUsersByRole(String role);

}
