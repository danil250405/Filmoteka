package org.glazweq.demo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.glazweq.demo.Dto.UserDto;
import org.glazweq.demo.domain.Role;
import org.glazweq.demo.domain.User;
import org.glazweq.demo.repos.UserRepo;
import org.glazweq.demo.service.AdminService;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    private AdminService adminService;
    public AdminsController(UserService userService, UserRepo userRepo, AdminService adminService) {
        this.userService = userService;
        this.userRepo = userRepo;
        this.adminService = adminService;
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
        if (dbColumn.equals("all")){
            return "redirect:/userslist";

        }
        List<UserDto> users = adminService.getUserListByFilters(dbColumn, keyword);
        model.addAttribute("oldDbColumn", dbColumn);
        model.addAttribute("oldKeyword", keyword);
        model.addAttribute("users", users);
        return "userslist";
    }
    // Обработчик GET запроса для бана пользователя
    @PostMapping("/ban-user")
    public String banUser(@RequestParam("userId") Long userId,
                          @RequestParam("banReason") String banReason) {
        System.out.println("user baned: " + userId + " " + banReason);
        adminService.banUser(userId, banReason);;
        // Здесь добавьте логику для бана пользователя с идентификатором userId
        // Например, вызов сервисного метода для установки соответствующего флага в базе данных или выполнение других действий
        return "redirect:/userslist"; // Перенаправляем пользователя обратно на главную страницу после бана
    }
    @PostMapping("/unban-user")
    public String unbanUser(@RequestParam("unban-userId") Long userId,
                          @RequestParam("unbanReason") String banReason) {
        System.out.println("user baned: " + userId + " " + banReason);
        User user = userRepo.findUserById(userId);
        System.out.println("ban taime:" + user.getUserBans().get(0).getBanReason());
//        adminService.banUser(userId, banReason);
        // Здесь добавьте логику для бана пользователя с идентификатором userId
        // Например, вызов сервисного метода для установки соответствующего флага в базе данных или выполнение других действий
        return "redirect:/userslist"; // Перенаправляем пользователя обратно на главную страницу после бана
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
