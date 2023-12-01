package com.hungtran.youtubeclone.controller;

import com.hungtran.youtubeclone.dto.UserInfoDTO;
import com.hungtran.youtubeclone.model.User;
import com.hungtran.youtubeclone.service.UserRegistrationService;
import com.hungtran.youtubeclone.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRegistrationService userRegistrationService;
    private final UserService userService;
    @GetMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public String register(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal(); //đây là json web token

        userRegistrationService.registerUser(jwt.getTokenValue());
        return userRegistrationService.registerUser(jwt.getTokenValue());
    }

    @PostMapping("/subscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean subscribeUser(@PathVariable String userId) {
        userService.subscribeUser(userId);
        return true;
    }

    @PostMapping("/unSubscribe/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public boolean unSubscribeUser(@PathVariable String userId) {
        userService.unSubscribeUser(userId);
        return true;
    }

    @GetMapping("/{userId}/history")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> userHistory(@PathVariable String userId) {
        return userService.userHistory(userId);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable String userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/current")
    @ResponseStatus(HttpStatus.OK)
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @GetMapping("/byIds")
    public List<User> getUsersByIds(@RequestParam List<String> ids) {
        return userService.getUsersByUserIds(ids);
    }

}















