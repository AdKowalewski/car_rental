package com.example.car_rental.service;

import com.example.car_rental.model.Rental;
import com.example.car_rental.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;

    public Rental getRentalById(Integer id) {
        return rentalRepository.findById(id).orElseThrow();
    }

    public List<Rental> getCarActiveRentals(Integer id) {
        return rentalRepository.getCarActiveRentals(id);
    }

    public List<Rental> getCarAllRentals(Integer id) {
        return rentalRepository.getCarAllRentals(id);
    }

    public List<Rental> getUserActiveRentals(Integer id) {
        return rentalRepository.getUserActiveRentals(id);
    }

    public List<Rental> getUserAllRentals(Integer id) {
        return rentalRepository.getUserAllRentals(id);
    }

    public List<Rental> getUserUnpaidRentals(Integer id) {
        return rentalRepository.getUserUnpaidRentals(id);
    }

    public List<Rental> getUserUnreturnedRentals(Integer id) {
        return rentalRepository.getUserUnreturnedRentals(id);
    }

    public void payRental(Integer id) {
        rentalRepository.payRental(id);
    }

    public void returnRental(Integer id) {
        rentalRepository.returnRental(id);
    }

    @Transactional
    public Rental createRental(Rental rental) {

        List<Rental> rentals = getCarActiveRentals(rental.getCar().getCar_id());
        LocalDate start = rental.getRental_start();
        LocalDate end = rental.getRental_end();

        for(int i = 0; i < rentals.size(); i++) {
            if(((start.isAfter(rentals.get(i).getRental_start()) || start.equals(rentals.get(i).getRental_start())) && (start.isBefore(rentals.get(i).getRental_end()) || start.equals(rentals.get(i).getRental_end()))) ||
                    ((end.isAfter(rentals.get(i).getRental_start()) || end.equals(rentals.get(i).getRental_start())) && (end.isBefore(rentals.get(i).getRental_end()) || end.equals(rentals.get(i).getRental_end()))) ||
                    (start.isBefore(rentals.get(i).getRental_start()) && end.isAfter(rentals.get(i).getRental_end()))) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Car is taken for given period!");
            }
        }

        rental.setTotal_price(countTotalPrice(rental.getRental_start(), rental.getRental_end(), rental.getCar().getPrice()));

        return rentalRepository.save(rental);
    }

    public void stopRental(Integer id) {
        Rental rental = getRentalById(id);
        LocalDate today = LocalDate.now();
        if(rental.isPaid()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Rental has been paid for whole period!");
        }
        if(rental.getRental_start().isAfter(today)) {
            rentalRepository.deleteById(id);
        }
        rental.setRental_end(today);
        rental.setTotal_price(countTotalPrice(rental.getRental_start(), rental.getRental_end(), rental.getCar().getPrice()));
    }

    public Float countTotalPrice(LocalDate rental_start, LocalDate rental_end, Float priceForOneDay) {
        return Period.between(rental_start, rental_end).getDays() * priceForOneDay;
    }
}
