package com.example.car_rental.controller;

import com.example.car_rental.auth.JwtTokenUtil;
//import com.example.car_rental.auth.MyUserDetails;
import com.example.car_rental.dto.TokenDto;
import com.example.car_rental.dto.UserCreate;
import com.example.car_rental.dto.UserDto;
import com.example.car_rental.dto.UserEdit;
import com.example.car_rental.dto.UserLogin;
import com.example.car_rental.mappers.UserCreateMapper;
import com.example.car_rental.mappers.UserDtoMapper;
import com.example.car_rental.mappers.UserEditMapper;
import com.example.car_rental.mappers.UserLoginMapper;
import com.example.car_rental.model.User;
import com.example.car_rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "/register", consumes = "application/json")
    public void createUser(@RequestBody UserCreate userCreate) {
        try {
            User user = UserCreateMapper.INSTANCE.toEntity(userCreate);
            userService.register(user);
            //User userFromDB = userService.readUserByEmail(user.getEmail());
            authenticate(user.getEmail(), user.getPassword());
            //final String token = jwtTokenUtil.generateToken(new MyUserDetails(userFromDB));
            //return ResponseEntity.ok(new TokenDto(token));
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Email already registered!"
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody UserLogin userLogin) throws Exception {

        User user = UserLoginMapper.INSTANCE.toEntity(userLogin);
        User userFromDB;
        try {
            userFromDB = userService.readUserByEmail(user.getEmail());
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User doesn't exist!"
            );
        }
        if (user.getPassword().equals(userFromDB.getPassword())) {
            authenticate(user.getEmail(), user.getPassword());
            final String token = jwtTokenUtil.generateToken(user.getEmail());
            return ResponseEntity.ok(new TokenDto(token));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, "Invalid email or password!"
            );
        }
    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException ex) {
            throw new Exception("User disabled!", ex);
        } catch (BadCredentialsException ex) {
            throw new Exception("Invalid credentials!", ex);
        }
    }

    @GetMapping //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public List<UserDto> readUsers() {
        List<User> cars = userService.readUsers();
        List<UserDto> dtos = new ArrayList<>();
        for(int i = 0; i < cars.size(); i++) {
            dtos.add(UserDtoMapper.INSTANCE.toDto(cars.get(i)));
        }
        return dtos;
    }

    @GetMapping("/{user_id}")
    public UserDto readUser(@PathVariable("user_id") Integer id) {
        UserDto dto = UserDtoMapper.INSTANCE.toDto(userService.readUserById(id));
        if(dto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User doesn't exist!"
            );
        } else {
            return dto;
        }
    }

    @DeleteMapping("/{user_id}") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public void deleteUser(@PathVariable("user_id") Integer id) {
        userService.deleteUserById(id);
        throw new ResponseStatusException(
                HttpStatus.NO_CONTENT
        );
    }

    @PatchMapping(value = "/{user_id}", consumes = "application/json") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public void editUser(@RequestBody UserEdit userEdit) {
        userService.editUser(UserEditMapper.INSTANCE.toEntity(userEdit));
        throw new ResponseStatusException(
                HttpStatus.NO_CONTENT
        );
    }
}
