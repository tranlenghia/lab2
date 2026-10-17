package business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Car;
import validation.CarValidation;

public class CarManager {

    private final Map<String, Car> cars = new LinkedHashMap<>();

    private String normalizeKey(String licensePlate) {
        if (licensePlate == null) {
            return "";
        }

        return licensePlate.trim().toUpperCase();
    }

    public boolean addCar(Car car) {
        if (!CarValidation.isValid(car)) {
            return false;
        }

        String key = normalizeKey(car.getLicensePlate());

        if (cars.containsKey(key)) {
            return false;
        }

        cars.put(key, car);
        return true;
    }

    public Car findCar(String licensePlate) {
        String key = normalizeKey(licensePlate);
        return cars.get(key);
    }

    public boolean containsCar(String licensePlate) {
        return cars.containsKey(normalizeKey(licensePlate));
    }

    public boolean updateCar(String licensePlate, Car updatedCar) {
        String key = normalizeKey(licensePlate);

        if (!cars.containsKey(key) || updatedCar == null) {
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

    public boolean deleteCar(String licensePlate) {
        String key = normalizeKey(licensePlate);

        if (!cars.containsKey(key)) {
            return false;
        }

        cars.remove(key);
        return true;
    }

    public List<Car> getUninsuredCars(
            InsuranceManager insuranceManager,
            String sortField,
            boolean ascending) {

        List<Car> result = new ArrayList<>();

        for (Car car : cars.values()) {
            if (insuranceManager == null
                    || !insuranceManager.hasInsuranceForPlate(
                            car.getLicensePlate())) {
                result.add(car);
            }
        }

        Comparator<Car> comparator;
        String field = sortField == null
                ? "" : sortField.toLowerCase();

        switch (field) {
            case "owner":
                comparator = Comparator.comparing(
                        Car::getCarOwner,
                        String.CASE_INSENSITIVE_ORDER
                );
                break;

            case "date":
                comparator = Comparator.comparing(
                        Car::getRegistrationDate
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
                        String.CASE_INSENSITIVE_ORDER
                );
                break;
        }

        if (!ascending) {
            comparator = comparator.reversed();
        }

        result.sort(comparator);
        return result;
    }

    public Map<String, Car> getCars() {
        return new LinkedHashMap<>(cars);
    }

    public void setCars(Map<String, Car> loadedCars) {
        cars.clear();

        if (loadedCars == null) {
            return;
        }

        for (Car car : loadedCars.values()) {
            if (car != null && car.getLicensePlate() != null
                    && !car.getLicensePlate().trim().isEmpty()) {
                cars.put(normalizeKey(car.getLicensePlate()), car);
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