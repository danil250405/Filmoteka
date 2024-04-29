package org.glazweq.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.domain.UserBan;
import org.glazweq.demo.repos.UserBanRepo;
import org.glazweq.demo.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminService {
    private UserRepo userRepo;
    private UserService userService;
    private UserServiceImpl userServiceImpl;
    private UserBanRepo userBanRepo;

    public AdminService(UserRepo userRepo, UserService userService, UserServiceImpl userServiceImpl, UserBanRepo userBanRepo) {
        this.userRepo = userRepo;
        this.userService = userService;
        this.userServiceImpl = userServiceImpl;
        this.userBanRepo = userBanRepo;
    }

    public void banUser(Long userId, String reason){
        if (reason == null || reason.isEmpty()) {
            reason = "We decided to ban you without explaining the reason for the ban, if you think that you received this ban undeservedly, then write to us";
        }
            User user = userRepo.findUserById(userId);

        UserBan userBan = new UserBan();
        userBan.setUser(user);
        userBan.setBanDateTime(getCurrentDateTime());
        userBan.setBanReason(reason);
        userBan.setAdmin(userServiceImpl.getAuthUser());
        Role bannedRole = userServiceImpl.getBannedRole();
        Role unbannedRole = userServiceImpl.getUnbannedRole();
        System.out.println("BAN ROLE:  " + bannedRole);
        user.getRoles().add(bannedRole);
        for (Role role : user.getRoles()){
            if (role == unbannedRole) user.getRoles().remove(unbannedRole);
        }

        user.getRoles().remove(unbannedRole);
        userRepo.save(user);
        userBanRepo.save(userBan);
    }
    public void unbanUser(Long userId, String unbanReason){
        User user = userRepo.findUserById(userId);
        UserBan userBan = userBanRepo.findUserBanByUser(user);
        if (unbanReason == null || unbanReason.isEmpty()) unbanReason = "Our team decided that you still deserve to watch films on our website ☻";
        userBan.setUnbanReason(unbanReason);
        userBan.setUnbanDateTime(getCurrentDateTime());
        Role bannedRole = userServiceImpl.getBannedRole();
        user.getRoles().remove(bannedRole);
        Role unbannedRole = userServiceImpl.getUnbannedRole();
        user.getRoles().add(unbannedRole);
        userRepo.save(user);
        userBanRepo.save(userBan);
    }






    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
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
        usersWithTargetId.add(userRepo.findUserById(id));
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


