package org.glazweq.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminService {
    private UserRepo userRepo;
    private UserService userService;
    private UserServiceImpl userServiceImpl;

    public AdminService(UserRepo userRepo, UserService userService, UserServiceImpl userServiceImpl) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
    }

    public void banUser(String reason){

    }









//    filter section
    public List<UserDto> getUserListByFilters(String columnName, String keyword){
        if (columnName.equals("roles")){
            return getUsersListByRole(keyword);
        }
        if (columnName.equals("name")){
            return  getUsersListByName(keyword);
        }
        if (columnName.equals("email")){
            return  getUsersListByMail(keyword);
        }
        if (columnName.equals("id")){
            return  getUsersListById(Long.valueOf(keyword));
        }
        if (columnName.equals("username")){
            return  getUsersListByUsername(keyword);
        }
        return null;
    }
    // Фильтруем пользователей по наличию нужного id
    public List<UserDto> getUsersListById(Long id){
        List<User> usersWithTargetId = new ArrayList<>();
        usersWithTargetId.add(userRepo.findUsersById(id));
        return userServiceImpl.remakeListFromUserToUserDto(usersWithTargetId);
    }
    // Фильтруем пользователей по наличию нужной почты
    public List<UserDto> getUsersListByMail(String targetMail){
        List<User> usersWithTargetMail = userRepo.findByPartialMail(targetMail);
        return userServiceImpl.remakeListFromUserToUserDto(usersWithTargetMail);
    }
    // Фильтруем пользователей по наличию нужному username
    public List<UserDto> getUsersListByUsername(String targetUsername){
        List<User> usersWithTargetUsername = userRepo.findByPartialUsername(targetUsername);
        return userServiceImpl.remakeListFromUserToUserDto(usersWithTargetUsername);
    }
    // Фильтруем пользователей по наличию нужного имени
    public List<UserDto> getUsersListByName(String targetUserName){
        List<User> usersWithTargetName = userRepo.findByPartialName(targetUserName);
        return userServiceImpl.remakeListFromUserToUserDto(usersWithTargetName);
    }
    // Фильтруем пользователей по наличию нужной роли
    public List<UserDto> getUsersListByRole(String targetRoleName){
    List<User> usersWithTargetRole = userRepo.findByRoleName(targetRoleName);
    return userServiceImpl.remakeListFromUserToUserDto(usersWithTargetRole);
    }
}


