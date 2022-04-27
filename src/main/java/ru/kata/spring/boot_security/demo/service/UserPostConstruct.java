package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class UserPostConstruct {

    private final UserService userService;
    private final RoleService roleService;

    private UserPostConstruct(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

//    public User(String username, String password, Set<Role> roles, String firstName,
//    String lastName, String email)

    @PostConstruct
    private void postConstruct() {
        roleService.saveRole(new Role(1L, "ADMIN"));
        roleService.saveRole(new Role(2L, "USER"));
        userService.saveUser(new User("admin@mail.ru", "$2a$12$piJQL/Gjt/H0IybvwiNcMeHb9kwz6XxuGtQrpSbHvSOpUAJPDhHS2",
                new ArrayList<>(roleService.findAll()),
                "Vasya", "Pupkin", 35));
        userService.saveUser(new User("user@mail.ru", "$2a$12$IKsa6sqRgxdZMbtuVmyHtOZvU0p8nfbuUaWKs8nfCKZw6vSDVGKWO",
                roleService.findAll().stream().filter(e -> e.getRoleName()
                        .contains("USER")).collect(Collectors.toList()),
                "Merlin", "Monroe", 28));
        userService.saveUser(new User("test@mail.ru", "$2a$12$CXU5aVl1S00bW8LfJ30WMujLybJcjiDIJZD11lnqi6DGYCXV3auyy",
                roleService.findAll().stream().filter(e -> e.getRoleName()
                        .contains("USER")).collect(Collectors.toList()),
                "Test", "Testov", 99));
    }
}
