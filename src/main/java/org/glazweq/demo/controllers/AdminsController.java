package org.glazweq.demo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.repos.UserRepo;
import org.glazweq.demo.service.UserService;
import org.springframework.aop.ClassFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@Controller
public class AdminsController {
    @Autowired
    private HttpServletRequest request;

    private PasswordEncoder passwordEncoder;
    private UserService userService;
    private UserRepo userRepo;
    public AdminsController(UserService userService, UserRepo userRepo) {
        this.userService = userService;
        this.userRepo = userRepo;
    }
    public void getAndSetUserRole(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String roleAuthUser;
        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            roleAuthUser = "admin";
        } else if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            roleAuthUser = "user";
        } else {
            roleAuthUser = "guest";
        }
        model.addAttribute("userRole", roleAuthUser);
    }



//    all users page
    @GetMapping("/admin-home")
    public String adminHome(Model model){
        getAndSetUserRole(model);
        return "admin-home";
    }
    @PostMapping("/searchUsersByFilters")
    public String searchUsersByFilters(Model model,
                                       @RequestParam("dbColumn") String dbColumn,
                                       @RequestParam("keyword") String keyword) {
        String columnName;
        List<User> users = new ArrayList<>();
        switch (dbColumn) {
            case "name":
                columnName = "name";
                users = userRepo.findUsersByName(keyword);
                break;
            case "id":
                columnName = "id";
                break;
            case "username":
                columnName = "username";
                break;
            case "mail":
                columnName = "email";
                break;
            case "roles":
//                columnName = "roles.name";
//                List<Role> roles = new ArrayList<>();
//                roles.add(keyword);
//                users = userRepo.findUsersByRoles(keyword);
//                break;
            default:
                columnName = "name";

        }


        model.addAttribute("users", users);
        return "userslist";
    }
    @GetMapping("/userslist")
    public String users(Model model, Principal principal){
        getAndSetUserRole(model);
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        System.out.println(principal.getName());
        return "userslist";
    }

}
