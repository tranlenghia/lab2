package validation;

import model.Car;

public class CarValidation {

    public static String getValidationError(Car car) {
        if (car == null) {
            return "Car information cannot be null.";
        }

        if (car.getLicensePlate() == null
                || car.getLicensePlate().trim().isEmpty()) {
            return "License plate cannot be empty.";
        }

        String owner = car.getCarOwner();

        if (owner == null || owner.trim().length() < 2
                || owner.trim().length() > 35) {
            return "Car owner must contain 2 to 35 characters.";
        }

        if (car.getCarBrand() == null
                || car.getCarBrand().trim().isEmpty()) {
            return "Car brand cannot be empty.";
        }

        if (car.getVehicleValue() <= 999) {
            return "Vehicle value must be greater than 999.";
        }

        if (car.getRegistrationDate() == null) {
            return "Registration date is invalid.";
        }

        if (car.getRegistrationPlace() == null
                || car.getRegistrationPlace().trim().isEmpty()) {
            return "Registration place cannot be empty.";
        }

        if (!isValidVehicleType(car.getVehicleType())) {
            return "Vehicle type must be 5, 7, or 9.";
        }

        return null;
    }

    public static boolean isValid(Car car) {
        return getValidationError(car) == null;
    }

    public static boolean isValidVehicleType(int vehicleType) {
        return vehicleType == 5
                || vehicleType == 7
                || vehicleType == 9;
    }
}