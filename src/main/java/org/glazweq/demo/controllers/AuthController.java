package org.glazweq.demo.controllers;

import ch.qos.logback.core.net.SyslogOutputStream;
import jakarta.validation.Valid;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.repos.UserRepo;
import org.glazweq.demo.service.UserService;
import org.glazweq.demo.service.UserServiceImpl;
import org.springframework.aop.ClassFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller

public class AuthController {
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle home page request
    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // create model object to store form data
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        System.out.println("qwe-----------------------------------------------------------------------");
        return "register";
    }
@PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model) {
    System.out.println("qwe-----------------------------------------------------------------------");

        User existingUserByEmail = userService.findUserByEmail(userDto.getEmail());

    if(result.hasErrors()){
        model.addAttribute("user", userDto);
        return "register";
    }
        if (existingUserByEmail != null && existingUserByEmail.getEmail() != null && !existingUserByEmail.getEmail().isEmpty()) {
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
            return "register";
        }
        User existingUserByUsername = userService.findUserByUsername(userDto.getUsername());

        if (existingUserByUsername != null && existingUserByUsername.getUsername() != null && !existingUserByUsername.getUsername().isEmpty()) {

            result.rejectValue("username", "username",
                    "There is already an account registered with the same username");
            System.out.println("----------123-----------");

        }

         model.addAttribute("success", true);
        userService.saveUser(userDto);
        return "redirect:/register/save?success";
}
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @PostMapping("/login/check")
    public String loginCheck(@RequestParam("username") String username, @RequestParam("password") String password){
        User logUser = userService.findUserByUsername(username);

        if (logUser != null && passwordEncoder.matches(password, logUser.getPassword())){
            System.out.println("good job");
            return "redirect:/home";
        }else
        return "redirect:/login?error";
    }

    // handler method to handle list of users
    @GetMapping("/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }
}
