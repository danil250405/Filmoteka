package org.glazweq.demo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.service.UserService;
import org.springframework.aop.ClassFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
@Controller
public class AdminsController {
    @Autowired
    private HttpServletRequest request;

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    public AdminsController(UserService userService) {
        this.userService = userService;
    }




//    all users page

    @GetMapping("/userslist")
    public String users(Model model, Principal principal){

        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        System.out.println(principal.getName());
        return "userslist";
    }

}
