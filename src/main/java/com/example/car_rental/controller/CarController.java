package com.example.car_rental.controller;

import com.example.car_rental.dto.CarCreate;
import com.example.car_rental.dto.CarDto;
import com.example.car_rental.dto.CarEdit;
import com.example.car_rental.mappers.CarCreateMapper;
import com.example.car_rental.mappers.CarDtoMapper;
import com.example.car_rental.mappers.CarEditMapper;
import com.example.car_rental.model.Car;
import com.example.car_rental.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class CarController {

    private final CarService carService;

    @GetMapping(params = { "page" })
    public List<CarDto> readCars(@RequestParam(value = "page") Integer pageNumber) {
        Page<Car> cars = carService.readCars(pageNumber);
        List<Car> carsList = cars.getContent();
        List<CarDto> dtos = new ArrayList<>();
        for(int i = 0; i < carsList.size(); i++) {
            dtos.add(CarDtoMapper.INSTANCE.toDto(carsList.get(i)));
        }
        return dtos;
    }

    @GetMapping("/{car_id}")
    public CarDto readCar(@PathVariable("car_id") Integer id) {
        CarDto dto = CarDtoMapper.INSTANCE.toDto(carService.readCarById(id));
        if(dto == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Car doesn't exist!"
            );
        } else {
            return dto;
        }
    }

    @PostMapping(consumes = "application/json") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public void createCar(@RequestBody CarCreate carCreate) throws IOException {
        carService.createCar(CarCreateMapper.INSTANCE.toEntity(carCreate));
    }

    @DeleteMapping("/{car_id}") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public void deleteCar(@PathVariable("car_id") Integer id) {
        carService.deleteCarById(id);
        throw new ResponseStatusException(
                HttpStatus.NO_CONTENT
        );
    }

    @PatchMapping(value = "/{car_id}", consumes = "application/json") //ADMIN
    @PreAuthorize("hasRole('Admin')")
    //@RolesAllowed("Admin")
    public void editCar(@RequestBody CarEdit carEdit) throws IOException {
        carService.editCarById(CarEditMapper.INSTANCE.toEntity(carEdit));
        throw new ResponseStatusException(
                HttpStatus.NO_CONTENT
        );
    }
}
