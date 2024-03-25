package org.glazweq.demo.service;

import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.repos.RoleRepo;
import org.glazweq.demo.repos.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
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


    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setName(userDto.getFirstName() + " " + userDto.getLastName());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepo.findByName("ROLE_ADMIN");
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
    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        String[] str = user.getName().split(" ");
        userDto.setFirstName(str[0]);
        userDto.setLastName(str[1]);
        userDto.setEmail(user.getEmail());
        return userDto;
    }
private Role checkRoleExist(){
    Role role = new Role();
    role.setName("ROLE_ADMIN");
    return roleRepo.save(role);
}
}

