package com.example.car_rental.service;

import com.example.car_rental.exceptions.NotFoundException;
import com.example.car_rental.model.User;
import com.example.car_rental.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Supplier;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private Supplier<RuntimeException> getNotFoundIdExceptionSupplier(Integer id) {
        return () -> new NotFoundException(String.format("User with id: %i not found!", id));
    }

    private Supplier<RuntimeException> getNotFoundEmailExceptionSupplier(String email) {
        return () -> new NotFoundException(String.format("User with email: %s not found!", email));
    }

    public List<User> readUsers() {
        return userRepository.findAll();
    }

    public User readUserById(Integer id) {
        return userRepository.findById(id).orElseThrow(getNotFoundIdExceptionSupplier(id));
    }

    public User readUserByEmail(String email) {
        return userRepository.readUserByEmail(email).orElseThrow(getNotFoundEmailExceptionSupplier(email));
    }

    @Transactional
    public User register(User user) {
        //String pass = passwordEncoder.encode(user.getPassword());
        //user.setPassword(pass);
        return userRepository.save(user);
    }

    @Transactional
    public User editUser(User user) {
        final User oldUser = readUserById(user.getUser_id());
        oldUser.setEmail(user.getEmail());
        oldUser.setName(user.getName());
        oldUser.setSurname(user.getSurname());
        String pass = passwordEncoder.encode(user.getPassword());
        oldUser.setPassword(pass);
        return userRepository.save(oldUser);
    }

    public void deleteUserById(Integer id) {
        userRepository.deleteUser(id);
    }
}
