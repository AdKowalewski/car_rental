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
public class RentalCreate {

    private LocalDate rental_start;
    private LocalDate rental_end;
    private Integer car_id;
    //private Integer user_id; //?
}
