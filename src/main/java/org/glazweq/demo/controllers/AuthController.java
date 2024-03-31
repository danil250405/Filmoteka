package org.glazweq.demo.controllers;

import jakarta.validation.Valid;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller

public class AuthController {
    private PasswordEncoder passwordEncoder;
    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    // handler method to handle home page request

    @GetMapping("/index")
    public String home() {
        return "index";
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


        User existingUserByEmail = userService.findUserByEmail(userDto.getEmail());

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            System.out.println("qwe-----------------------------------------------------------------------");
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
            model.addAttribute("errorusername", "There is already an account registered with the same username");
            System.out.println("----------123-----------");
            return "register";
        }

        model.addAttribute("success", true);
        userService.saveUser(userDto);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login-success")
    public String loginCheck(@AuthenticationPrincipal UserDto userDto, @RequestParam("username") String username, @RequestParam("password") String password) {
        System.out.println("mamamaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
       // User logUser = userService.findUserByUsername(username);
        System.out.println("qwe---------------------------aaaaaaa--------------------------------------------");
//
//        if (logUser != null && passwordEncoder.matches(password, logUser.getPassword())){
//            System.out.println("good job");
//            return "redirect:/home";
//        }else
//            return "redirect:/login?error";
//    }
        return "redirect:/home-page";
        // handler method to handle list of users

    }
}