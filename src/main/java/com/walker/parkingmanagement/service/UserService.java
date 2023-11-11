package com.walker.parkingmanagement.service;

import com.walker.parkingmanagement.entity.User;
import com.walker.parkingmanagement.exception.EntityNotFoundException;
import com.walker.parkingmanagement.exception.PasswordInvalidException;
import com.walker.parkingmanagement.exception.UserNameUniqueViolationException;
import com.walker.parkingmanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User save(User user) {
        try {
            return userRepository.save(user);
        }catch (DataIntegrityViolationException ex){
            throw new UserNameUniqueViolationException(String.format("Usuário [%s] já cadastrado!",user.getUsername()));
        }
    }
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new EntityNotFoundException(String.format("Usuário de id = [%s] não encontrado!",id)));
    }
    @Transactional
    public User update(Long id, String currentPassword, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)){ //Testa se a nova senha é igual a confirma senha
            throw new PasswordInvalidException("Nova senha não confere com confirmação de senha.");
        }
        User user = findById(id);
        if (!user.getPassword().equals(currentPassword)){ //Testa se a senha atual é a mesma cadastrada no banco de dados
            throw new PasswordInvalidException("Sua senha não confere.");
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

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(()-> new EntityNotFoundException(String.format("Usuário com '%s' não encontrado!",username)));
    }

    @Transactional(readOnly = true)
    public User.Role findRoleByUsername(String username) {
        return userRepository.findRoleByUsername(username);
    }
}
