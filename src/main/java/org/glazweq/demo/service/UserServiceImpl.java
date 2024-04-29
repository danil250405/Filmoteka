package org.glazweq.demo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.domain.UserBan;
import org.glazweq.demo.repos.RoleRepo;
import org.glazweq.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{
    private UserRepo userRepo;
    private RoleRepo roleRepo;
    private PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepo userRepo,
                           RoleRepo roleRepo,
                           PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.passwordEncoder = passwordEncoder;
    }

    /* save user in the memory*/

    @Override

    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        System.out.println();
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepo.findByName("ROLE_USER");
        if(role == null){
            role = checkRoleExist();
        }
        user.setRoles(Arrays.asList(role));
        userRepo.save(user);

    }

    public Role getBannedRole() {
        return roleRepo.findByName("ROLE_BANNED");
    }
    public Role getUnbannedRole() {
        return roleRepo.findByName("ROLE_UNBANNED");
    }
    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }



    @Override

    public List<UserDto> findAllUsers() {
        List<User> users = (List<User>) userRepo.findAll(); //??????????????????
        return users.stream()
                .map((user) -> mapToUserDto(user))//Каждый элемент потока (то есть каждый объект пользователя) с помощью
                                                    // функции mapToUserDto() преобразуется в объект UserDto. Метод mapToUserDto()
                                                 //вероятно принимает объект типа User и возвращает объект типа UserDto,
                                                    //  преобразуя необходимые данные.
                .collect(Collectors.toList()); // Результаты преобразования (то есть объекты UserDto) собираются
                                                // обратно в список с помощью метода collect(), возвращая список UserDto.
    }
    public User getAuthUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        User authUser = findUserByEmail(currentPrincipalName);
        return authUser;
    }
    public List<UserDto> remakeListFromUserToUserDto(List<User> listUsers){
        List<UserDto> usersDtoList = new ArrayList<>();
        for (User user : listUsers){
            usersDtoList.add(mapToUserDto(user));
        }
        return usersDtoList;
    }
    private UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        userDto.setRole(String.join(", ", roleNames));
        List<String> banReasons = user.getUserBans().stream()
                .map(UserBan::getBanReason)
                .collect(Collectors.toList());
        userDto.setBanReason(String.join(", ", banReasons));
        List<String> banTimes = user.getUserBans().stream()
                .map(userBan -> userBan.getBanDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .collect(Collectors.toList());
        userDto.setBanDateTime(String.join(", ", banTimes));
        return userDto;
    }
private Role checkRoleExist(){
    Role role = new Role();
    role.setName("ROLE_USER");
    return roleRepo.save(role);
}
}

