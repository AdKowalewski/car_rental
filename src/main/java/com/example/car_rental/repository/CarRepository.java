package com.example.car_rental.repository;

import com.example.car_rental.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {

//    @Transactional
//    @Query(value = "SELECT c FROM Car c WHERE c.active = True OFFSET :offset LIMIT :limit",
//            countQuery = "SELECT COUNT(c) FROM Car c")
//    Page<Car> getAllActiveCarsForPage(Pageable pageable, @Param("offset") Integer offset, @Param("limit") Integer limit);

    @Transactional
    @Query(value = "SELECT c FROM Car c WHERE c.active = True",
            countQuery = "SELECT COUNT(c) FROM Car c")
    Page<Car> getAllActiveCarsForPage(Pageable pageable);

    @Transactional
    @Query(value = "UPDATE Car c SET c.active = False WHERE c.car_id = :id")
    void deleteCar(@Param("id") Integer id);
}
