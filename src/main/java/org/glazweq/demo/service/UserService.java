package org.glazweq.demo.service;

import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findUserByEmail(String email);
    User findUserByUsername(String username);
    List<UserDto> findAllUsers();
}
