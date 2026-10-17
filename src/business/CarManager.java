package business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import model.Car;
import model.InsuranceStatement;
import validation.CarValidation;

public class CarManager {

    private final Map<String, Car> cars = new LinkedHashMap<>();

    private String normalizeKey(String licensePlate) {
        if (licensePlate == null) {
            return "";
        }

        return licensePlate.trim().toUpperCase(Locale.ROOT);
    }

    // 1. Add a car
    public boolean addCar(Car car) {
        if (car == null || !CarValidation.isValid(car)) {
            return false;
        }

        String key = normalizeKey(car.getLicensePlate());

        if (key.isEmpty() || cars.containsKey(key)) {
            return false;
        }

        cars.put(key, car);
        return true;
    }

    // 2. Find a car
    public Car findCar(String licensePlate) {
        String key = normalizeKey(licensePlate);
        return cars.get(key);
    }

    // Check whether a car exists
    public boolean containsCar(String licensePlate) {
        return cars.containsKey(normalizeKey(licensePlate));
    }

    // 3. Update a car without changing its license plate
    public boolean updateCar(String licensePlate, Car updatedCar) {
        String key = normalizeKey(licensePlate);

        if (key.isEmpty()
                || !cars.containsKey(key)
                || updatedCar == null) {
            return false;
        }

        if (!key.equals(normalizeKey(updatedCar.getLicensePlate()))) {
            return false;
        }

        if (!CarValidation.isValid(updatedCar)) {
            return false;
        }

        cars.put(key, updatedCar);
        return true;
    }

    // 4. Delete a car
    public boolean deleteCar(String licensePlate) {
        String key = normalizeKey(licensePlate);

        if (!cars.containsKey(key)) {
            return false;
        }

        cars.remove(key);
        return true;
    }

    // 7. Find and sort uninsured cars
    public List<Car> getUninsuredCars(
            InsuranceManager insuranceManager,
            String sortField,
            boolean ascending) {

        Set<String> insuredPlates = new HashSet<>();

        // Collect all license plates that have insurance.
        if (insuranceManager != null) {
            for (InsuranceStatement statement
                    : insuranceManager.getInsurances().values()) {

                if (statement != null
                        && statement.getLicensePlate() != null) {

                    insuredPlates.add(
                            normalizeKey(statement.getLicensePlate())
                    );
                }
            }
        }

        // Keep only cars without insurance.
        List<Car> result = new ArrayList<>();

        for (Car car : cars.values()) {
            if (car != null
                    && !insuredPlates.contains(
                            normalizeKey(car.getLicensePlate()))) {
                result.add(car);
            }
        }

        // Select the sorting field.
        String field = sortField == null
                ? ""
                : sortField.trim().toLowerCase(Locale.ROOT);

        Comparator<Car> comparator;

        switch (field) {
            case "owner":
                comparator = Comparator.comparing(
                        Car::getCarOwner,
                        Comparator.nullsLast(
                                String.CASE_INSENSITIVE_ORDER)
                );
                break;

            case "date":
                comparator = Comparator.comparing(
                        Car::getRegistrationDate,
                        Comparator.nullsLast(
                                Comparator.<LocalDate>naturalOrder())
                );
                break;

            case "type":
                comparator = Comparator.comparingInt(
                        Car::getVehicleType
                );
                break;

            case "plate":
            default:
                comparator = Comparator.comparing(
                        Car::getLicensePlate,
                        Comparator.nullsLast(
                                String.CASE_INSENSITIVE_ORDER)
                );
                break;
        }

        // Use license plate as a secondary sorting field.
        comparator = comparator.thenComparing(
                Car::getLicensePlate,
                Comparator.nullsLast(
                        String.CASE_INSENSITIVE_ORDER)
        );

        if (!ascending) {
            comparator = comparator.reversed();
        }

        result.sort(comparator);
        return result;
    }

    // Return a copy of the car collection.
    public Map<String, Car> getCars() {
        return new LinkedHashMap<>(cars);
    }

    // 9. Replace the car collection with loaded data.
    public void setCars(Map<String, Car> loadedCars) {
        cars.clear();

        if (loadedCars == null) {
            return;
        }

        for (Car car : loadedCars.values()) {
            if (car != null
                    && car.getLicensePlate() != null
                    && !car.getLicensePlate().trim().isEmpty()) {

                cars.put(
                        normalizeKey(car.getLicensePlate()),
                        car
                );
            }
        }
    }

    public int size() {
        return cars.size();
    }

    public boolean isEmpty() {
        return cars.isEmpty();
    }
}