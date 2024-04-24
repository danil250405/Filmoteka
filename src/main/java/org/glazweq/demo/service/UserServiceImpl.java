package org.glazweq.demo.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.repos.RoleRepo;
import org.glazweq.demo.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

//    @Override
//    public User findUserByUsername(String inputUsername) {
//
//        if (userRepo.findByUsername(inputUsername) != null){
//            System.out.println("111111111111111111111");
//            // Получение имени пользователя из контекста безопасности
//            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            System.out.println("22222222222222222222222222");
//            String username = userDetails.getUsername();
//            System.out.println("333333333333333333333333");
//            // сохранение информации о пользователе в сессии
//            session.setAttribute("username", username);
//            System.out.println(session.getAttribute("username")+"======================================");
//            System.out.println("4444444444444444444444444");
//            return userRepo.findByUsername(inputUsername);
//        }
//        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//        return null;
//    }

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
        return userDto;
    }
private Role checkRoleExist(){
    Role role = new Role();
    role.setName("ROLE_USER");
    return roleRepo.save(role);
}
}

