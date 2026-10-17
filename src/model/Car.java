package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import utils.DateUtils;

public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String licensePlate;
    private String carOwner;
    private String carBrand;
    private long vehicleValue;
    private LocalDate registrationDate;
    private String registrationPlace;
    private int vehicleType;

    public Car(String licensePlate, String carOwner, String carBrand,
               long vehicleValue, LocalDate registrationDate,
               String registrationPlace, int vehicleType) {
        this.licensePlate = licensePlate == null
                ? null : licensePlate.trim();
        this.carOwner = carOwner == null ? null : carOwner.trim();
        this.carBrand = carBrand == null ? null : carBrand.trim();
        this.vehicleValue = vehicleValue;
        this.registrationDate = registrationDate;
        this.registrationPlace = registrationPlace == null
                ? null : registrationPlace.trim();
        this.vehicleType = vehicleType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(String carOwner) {
        this.carOwner = carOwner == null ? null : carOwner.trim();
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand == null ? null : carBrand.trim();
    }

    public long getVehicleValue() {
        return vehicleValue;
    }

    public void setVehicleValue(long vehicleValue) {
        this.vehicleValue = vehicleValue;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationPlace() {
        return registrationPlace;
    }

    public void setRegistrationPlace(String registrationPlace) {
        this.registrationPlace = registrationPlace == null
                ? null : registrationPlace.trim();
    }

    public int getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(int vehicleType) {
        this.vehicleType = vehicleType;
    }

    @Override
    public String toString() {
        return "License plate: " + licensePlate
                + "\nCar owner: " + carOwner
                + "\nCar brand: " + carBrand
                + "\nVehicle value: " + vehicleValue
                + "\nRegistration date: "
                + DateUtils.formatDate(registrationDate)
                + "\nRegistration place: " + registrationPlace
                + "\nVehicle type: " + vehicleType;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Car)) {
            return false;
        }

        Car other = (Car) object;

        return licensePlate != null
                && other.licensePlate != null
                && licensePlate.equalsIgnoreCase(other.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                licensePlate == null
                        ? null : licensePlate.toUpperCase()
        );
    }
}