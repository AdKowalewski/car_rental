package com.example.car_rental.repository;

import com.example.car_rental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "SELECT u FROM User u WHERE u.email = :email AND u.active = True")
    Optional<User> readUserByEmail(@Param("email") String email);

    @Transactional
    @Query(value = "UPDATE User u SET u.active = False WHERE u.user_id = :id")
    void deleteUser(@Param("id") Integer id);
}
