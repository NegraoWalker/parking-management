package com.walker.parkingmanagement.web.controller;

import com.walker.parkingmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "api/v1/users")
public class UserController {
    private final UserService userService;
}
