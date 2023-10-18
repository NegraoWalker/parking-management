package com.walker.parkingmanagement.web.controller;

import com.walker.parkingmanagement.entity.User;
import com.walker.parkingmanagement.service.UserService;
import com.walker.parkingmanagement.web.dto.CreateUserDTO;
import com.walker.parkingmanagement.web.dto.ResponseUserDTO;
import com.walker.parkingmanagement.web.dto.UpdatePasswordDTO;
import com.walker.parkingmanagement.web.dto.mapper.UserMapper;
import jakarta.validation.Valid;
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
    public ResponseEntity<ResponseUserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO){
        User userCreated = userService.save(UserMapper.toUser(createUserDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.toDTO(userCreated));
    }
    @GetMapping("/find-user/{id}")
    public ResponseEntity<ResponseUserDTO> findUserById(@PathVariable Long id){
        User userFindById = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toDTO(userFindById));
    }
    @PutMapping("/update-password/{id}")
    public ResponseEntity<ResponseUserDTO> updatePassword(@PathVariable Long id, @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User userUpdatePassword = userService.update(id,updatePasswordDTO.getCurrentPassword(),updatePasswordDTO.getNewPassword(),updatePasswordDTO.getConfirmPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(UserMapper.toDTO(userUpdatePassword));
    }
    @GetMapping("/find-user-all")
    public ResponseEntity<List<ResponseUserDTO>> findUserAll(){
        List<User> usersFindAll = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(UserMapper.toListDTO(usersFindAll));
    }
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
