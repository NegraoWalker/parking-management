package com.walker.parkingmanagement.web.controller;

import com.walker.parkingmanagement.entity.User;
import com.walker.parkingmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User userCreated = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
    @GetMapping("/find-user/{id}")
    public ResponseEntity<User> findUserById(@PathVariable Long id){
        User userFindById = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userFindById);
    }
    @PutMapping("/update-password/{id}")
    public ResponseEntity<User> updatePassword(@PathVariable Long id, @RequestBody User user) {
        User userUpdatePassword = userService.update(id,user.getPassword());
        return ResponseEntity.status(HttpStatus.OK).body(userUpdatePassword);
    }
    @GetMapping("/find-user-all")
    public ResponseEntity<List<User>> findUserAll(){
        List<User> usersFindAll = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(usersFindAll);
    }
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
