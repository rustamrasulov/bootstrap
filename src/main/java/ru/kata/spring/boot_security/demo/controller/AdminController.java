package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping(value = "/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "")
    public String showUsers(Principal principal, Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentuser", userService.loadUserByUsername(principal.getName()));
        model.addAttribute("principal", principal);
//        model.addAttribute("currentauth", ((Authentication) principal).getAuthorities());
        model.addAttribute("roles", roleService.findAll());
        return "admin";
    }

    @GetMapping(value = "new")
    public String newUser (User user) {
        return "/new";
    }

    @PostMapping(value = "/new")
    public String saveNewUser (User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/new";
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping(value = "/edit/{id}")
    public String findUser(@PathVariable("id") Long id, ModelMap model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "/edit";
    }

    @PutMapping(value = "/edit/{id}")
    public String updateUser(@PathVariable("id") Long id, User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/edit/{id}";
        }

        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
