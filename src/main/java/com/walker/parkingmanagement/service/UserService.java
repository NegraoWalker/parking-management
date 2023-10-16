package com.walker.parkingmanagement.service;

import com.walker.parkingmanagement.entity.User;
import com.walker.parkingmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("Usuário não encontrado!"));
    }
    @Transactional
    public User update(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)){ //Testa se a nova senha é igual a confirma senha
            throw new RuntimeException("Campo newPassword não é igual ao campo confirmPassword!");
        }
        User user = findById(id);
        if (!user.getPassword().equals(currentPassword)){ //Testa se a senha atual é a mesma cadastrada no banco de dados
            throw new RuntimeException("currentPassword não confere!");
        }
        user.setPassword(newPassword);
        return user;
    }
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Transactional
    public void delete(Long id) {
       userRepository.deleteById(id);
    }
}
