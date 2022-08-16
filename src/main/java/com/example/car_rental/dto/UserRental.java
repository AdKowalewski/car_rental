package com.example.car_rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserRental {

    private Integer id;
    private LocalDate rental_start;
    private LocalDate rental_end;
    private CarDto car;
    private UserDto user;
    private Float total_price;
    private boolean returned;
}
