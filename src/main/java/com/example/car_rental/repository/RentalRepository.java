package com.example.car_rental.repository;

import com.example.car_rental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Integer> {

//    @Query(value = "SELECT r FROM Rental r INNER JOIN Car c ON r.car.car_id = u.car_id WHERE c.car_id = :id AND c.active = True AND r.rental_end >= NOW()")
//    public List<Rental> getCarActiveRentals(@Param("id") Integer id);

    @Query(value = "SELECT r FROM Rental r WHERE r.car.car_id = :id AND r.car.active = True AND r.rental_end >= NOW()")
    public List<Rental> getCarActiveRentals(@Param("id") Integer id);

    @Query(value = "SELECT r FROM Rental r WHERE r.car.car_id = :id AND r.car.active = True")
    public List<Rental> getCarAllRentals(@Param("id") Integer id);

    @Query(value = "SELECT r FROM Rental r WHERE r.user.user_id = :id AND r.user.active = True AND r.rental_end >= NOW()")
    public List<Rental> getUserActiveRentals(@Param("id") Integer id);

    @Query(value = "SELECT r FROM Rental r WHERE r.user.user_id = :id AND r.user.active = True")
    public List<Rental> getUserAllRentals(@Param("id") Integer id);

    @Query(value = "SELECT r FROM Rental r WHERE r.user.user_id = :id AND r.user.active = True AND r.paid = False")
    public List<Rental> getUserUnpaidRentals(@Param("id") Integer id);

    @Query(value = "SELECT r FROM Rental r WHERE r.user.user_id = :id AND r.user.active = True AND r.rental_end >= NOW() AND r.returned = False")
    public List<Rental> getUserUnreturnedRentals(@Param("id") Integer id);

    @Query(value = "UPDATE Rental r SET r.paid = True WHERE r.rental_id = :id")
    public void payRental(@Param("id") Integer id);

    @Query(value = "UPDATE Rental r SET r.returned = True WHERE r.rental_id = :id")
    public void returnRental(@Param("id") Integer id);
}
