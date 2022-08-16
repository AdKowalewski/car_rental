package com.example.car_rental.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Id")
    private Integer rental_id;

    @Column(name = "RentalStart")
    private LocalDate rental_start;

    @Column(name = "RentalEnd")
    private LocalDate rental_end;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "CarId", foreignKey = @ForeignKey(name = "car_id"))
//    private Integer car_id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "UserId", foreignKey = @ForeignKey(name = "user_id"))
//    private Integer user_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Car")
    private Car car;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "User")
    private User user;

    @Column(name = "TotalPrice")
    private Float total_price;

    @Column(name = "Returned")
    private boolean returned = false;

    @Column(name = "Paid")
    private boolean paid = false;
}
