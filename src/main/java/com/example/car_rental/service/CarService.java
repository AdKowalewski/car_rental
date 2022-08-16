package com.example.car_rental.service;

import com.example.car_rental.exceptions.NotFoundException;
import com.example.car_rental.model.Car;
import com.example.car_rental.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.function.Supplier;

@Service
@Transactional
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;

    private Supplier<RuntimeException> getNotFoundExceptionSupplier(Integer id) {
        return () -> new NotFoundException(String.format("Car with id: %i not found!", id));
    }

    public Page<Car> readCars(Integer pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        return carRepository.getAllActiveCarsForPage(pageable);
    }

    public Car readCarById(Integer id) {
        return carRepository.findById(id).orElseThrow(getNotFoundExceptionSupplier(id));
    }

    @Transactional
    public Car createCar(Car car) throws IOException {
        byte[] imgBytes = imageDecoder(car.getImg());
        String[] split = car.getImg().split(".");
        String imgExt = split[split.length - 1];
        String imgPath = String.format("src\\main\\resources\\media\\car_%d.%s", car.getCar_id(), imgExt);
        saveImg(imgPath, imgBytes);
        car.setImg(imgPath);
        //car.setImg(Base64.getEncoder().encodeToString(car.getImg().getBytes()));
        return carRepository.save(car);
    }

    @Transactional
    public Car editCarById(Car car) throws IOException {
        final Car oldCar = readCarById(car.getCar_id());
        oldCar.setBrand(car.getBrand());
        oldCar.setModel(car.getModel());
        oldCar.setDescription(car.getDescription());
        byte[] imgBytes = imageDecoder(car.getImg());
        String[] split = car.getImg().split(".");
        String imgExt = split[split.length - 1];
        String imgPath = String.format("src\\main\\resources\\media\\car_%d.%s", car.getCar_id(), imgExt);
        saveImg(imgPath, imgBytes);
        car.setImg(imgPath);
        //oldCar.setImg(Base64.getEncoder().encodeToString(car.getImg().getBytes()));
        oldCar.setPrice(car.getPrice());
        return carRepository.save(oldCar);
    }

    public void deleteCarById(Integer id) {
        carRepository.deleteCar(id);
    }

//    public String imageEncoder(String image) throws IOException {
//        FileInputStream stream = new FileInputStream(image);
//        int bufLength = 2048;
//        byte[] buffer = new byte[2048];
//        byte[] data;
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int readLength;
//        while((readLength = stream.read(buffer, 0, bufLength)) != -1) {
//            out.write(buffer, 0, readLength);
//        }
//        data = out.toByteArray();
//        out.close();
//        stream.close();
//        return Base64.getEncoder().withoutPadding().encodeToString(data);
//    }

    public byte[] imageDecoder(String image) {
        return Base64.getDecoder().decode(image);
    }

    public void saveImg(String path, byte[] bytes) throws IOException {
        File file = new File(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(bytes, 0, bytes.length);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
