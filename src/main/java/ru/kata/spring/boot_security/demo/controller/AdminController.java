package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "")
    public String showUsers(@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("currentuser", user);
        model.addAttribute("roles", roleService.findAll());
        return "admin";
    }

    @GetMapping(value = "/new")
    public String newUser (@AuthenticationPrincipal UserDetails user, Model model) {
        model.addAttribute("currentuser", user);
        model.addAttribute("roles", roleService.findAll());
        return "/new";
    }

    @PostMapping(value = "/new")
    public String saveNewUser (@ModelAttribute("user") User user, BindingResult result) {
        getUserRoles(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/admin";
    }

//    @GetMapping(value = "/edit/{id}")
//    public String findUser(@PathVariable("id") Long id, ModelMap model) {
//        User user = userService.getById(id);
//        model.addAttribute("user", user);
//        model.addAttribute("roles", roleService.findAll());
//        return "/edit";
//    }

    @PutMapping(value = "/edit/{id}")
//    public String updateUser(@PathVariable("id") Long id, User user, BindingResult result) {
        public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") User user) {
//        if (result.hasErrors()) {
//            return "redirect:/edit/{id}";
//        }
        getUserRoles(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.err.println(user);
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    private void getUserRoles(User user) {
        user.setRoles(user.getRoles().stream()
                .map(role -> roleService.getById(role.getId())).collect(Collectors.toList()));
    }
}
