package com.example.car_rental.controller;

import com.example.car_rental.dto.RentalCreate;
import com.example.car_rental.dto.RentalDto;
import com.example.car_rental.dto.UserRental;
import com.example.car_rental.mappers.RentalCreateMapper;
import com.example.car_rental.mappers.RentalDtoMapper;
import com.example.car_rental.mappers.UserRentalMapper;
import com.example.car_rental.model.Rental;
import com.example.car_rental.model.User;
import com.example.car_rental.service.RentalService;
import com.example.car_rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;

    @GetMapping("/car/{car_id}/active")
    public List<RentalDto> getCarActiveRentals(@PathVariable("car_id") Integer id) {
        try{
            List<Rental> rentals = rentalService.getCarActiveRentals(id);
            List<RentalDto> dtos = new ArrayList<>();
            for(int i = 0; i < rentals.size(); i++) {
                dtos.add(RentalDtoMapper.INSTANCE.toDto(rentals.get(i)));
            }
            return dtos;
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car doesn't exist!"
            );
        }
    }

    @GetMapping("/car/{car_id}") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public List<UserRental> getCarAllRentals(@PathVariable("car_id") Integer id) {
        try{
            List<Rental> rentals = rentalService.getCarAllRentals(id);
            List<UserRental> dtos = new ArrayList<>();
            for(int i = 0; i < rentals.size(); i++) {
                dtos.add(UserRentalMapper.INSTANCE.toDto(rentals.get(i)));
            }
            return dtos;
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car doesn't exist!"
            );
        }
    }

    @GetMapping("/user/{user_id}/active") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public List<UserRental> getUserActiveRentals(@PathVariable("user_id") Integer id) {
        try{
            List<Rental> rentals = rentalService.getUserActiveRentals(id);
            List<UserRental> dtos = new ArrayList<>();
            for(int i = 0; i < rentals.size(); i++) {
                dtos.add(UserRentalMapper.INSTANCE.toDto(rentals.get(i)));
            }
            return dtos;
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User doesn't exist!"
            );
        }
    }

    @GetMapping("/user/{user_id}") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public List<UserRental> getUserAllRentals(@PathVariable("user_id") Integer id) {
        try{
            List<Rental> rentals = rentalService.getUserAllRentals(id);
            List<UserRental> dtos = new ArrayList<>();
            for(int i = 0; i < rentals.size(); i++) {
                dtos.add(UserRentalMapper.INSTANCE.toDto(rentals.get(i)));
            }
            return dtos;
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User doesn't exist!"
            );
        }
    }

    @GetMapping("/user/{user_id}/unpaid") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public List<UserRental> getUserUnpaidRentals(@PathVariable("user_id") Integer id) {
        try{
            List<Rental> rentals = rentalService.getUserUnpaidRentals(id);
            List<UserRental> dtos = new ArrayList<>();
            for(int i = 0; i < rentals.size(); i++) {
                dtos.add(UserRentalMapper.INSTANCE.toDto(rentals.get(i)));
            }
            return dtos;
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User doesn't exist!"
            );
        }
    }

    @GetMapping
    public List<RentalDto> userActiveRentals(@AuthenticationPrincipal UsernamePasswordAuthenticationToken user) {
        Integer id = userService.readUserByEmail(user.getPrincipal().toString()).getUser_id();
        List<Rental> rentals = rentalService.getUserActiveRentals(id);
        List<RentalDto> dtos = new ArrayList<>();
        for(int i = 0; i < rentals.size(); i++) {
            dtos.add(RentalDtoMapper.INSTANCE.toDto(rentals.get(i)));
        }
        return dtos;
    }

    @GetMapping("/unpaid")
    public List<RentalDto> userUnpaidRentals(@AuthenticationPrincipal UsernamePasswordAuthenticationToken user) {
        Integer id = userService.readUserByEmail(user.getPrincipal().toString()).getUser_id();
        List<Rental> rentals = rentalService.getUserUnpaidRentals(id);
        List<RentalDto> dtos = new ArrayList<>();
        for(int i = 0; i < rentals.size(); i++) {
            dtos.add(RentalDtoMapper.INSTANCE.toDto(rentals.get(i)));
        }
        return dtos;
    }

    @GetMapping("/unreturned")
    public List<RentalDto> userUnreturnedRentals(@AuthenticationPrincipal UsernamePasswordAuthenticationToken user) {
        Integer id = userService.readUserByEmail(user.getPrincipal().toString()).getUser_id();
        List<Rental> rentals = rentalService.getUserUnreturnedRentals(id);
        List<RentalDto> dtos = new ArrayList<>();
        for(int i = 0; i < rentals.size(); i++) {
            dtos.add(RentalDtoMapper.INSTANCE.toDto(rentals.get(i)));
        }
        return dtos;
    }

    @PostMapping(consumes = "application/json")
    public void userCreateRental(@RequestBody RentalCreate rentalCreate,
                                 @AuthenticationPrincipal UsernamePasswordAuthenticationToken user) {
        try {
            //Integer id = userService.readUserByEmail(user.getPrincipal().toString()).getUser_id();
            //rentalCreate.setUser_id(id);
            User userFromDB = userService.readUserByEmail(user.getPrincipal().toString());
            Rental rental = RentalCreateMapper.INSTANCE.toEntity(rentalCreate);
            rental.setUser(userFromDB);
            rentalService.createRental(rental);
        } catch (ResponseStatusException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Car doesn't exist!");
        }
    }

    @DeleteMapping("/{rental_id}")
    public void stopRental(@PathVariable("rental_id") Integer id,
                           @AuthenticationPrincipal UsernamePasswordAuthenticationToken user) {
        Integer user_id = userService.readUserByEmail(user.getPrincipal().toString()).getUser_id();
        Rental rental = rentalService.getRentalById(id);
        if (Objects.equals(rental.getUser().getUser_id(), user_id)){
            rentalService.stopRental(id);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not user's rental!");
        }
    }

    @GetMapping("/pay/{rental_id}")
    public void payRental(@PathVariable("rental_id") Integer id,
                          @AuthenticationPrincipal UsernamePasswordAuthenticationToken user) {
        Integer user_id = userService.readUserByEmail(user.getPrincipal().toString()).getUser_id();
        Rental rental = rentalService.getRentalById(id);
        if (Objects.equals(rental.getUser().getUser_id(), user_id)){
            rentalService.payRental(id);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not user's rental!");
        }
    }

    @GetMapping("/{rental_id}/return") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public void returnRental(@PathVariable("rental_id") Integer id) {
        Rental rental = rentalService.getRentalById(id);
        if (rental.isReturned()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rental is already returned!");
        }
        rentalService.returnRental(id);
    }
 }
