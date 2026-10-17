package business;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Car;
import model.InsuranceStatement;
import validation.InsuranceValidation;

public class InsuranceManager {

    private final Map<String, InsuranceStatement> insurances
            = new LinkedHashMap<>();

    private String normalizeKey(String insuranceId) {
        if (insuranceId == null) {
            return "";
        }

        return insuranceId.trim().toUpperCase();
    }

    public boolean addInsurance(
            InsuranceStatement statement,
            CarManager carManager) {

        if (statement == null || carManager == null) {
            return false;
        }

        String key = normalizeKey(statement.getInsuranceId());

        if (key.isEmpty() || insurances.containsKey(key)) {
            return false;
        }

        String error =
                InsuranceValidation.getInputValidationError(statement);

        if (error != null) {
            return false;
        }

        Car car = carManager.findCar(statement.getLicensePlate());

        if (car == null) {
            return false;
        }

        long fees = calculateInsuranceFees(
                car.getVehicleValue(),
                statement.getInsurancePeriod()
        );

        if (fees <= 0) {
            return false;
        }

        statement.setInsuranceFees(fees);

        if (!InsuranceValidation.isValid(statement)) {
            return false;
        }

        insurances.put(key, statement);
        return true;
    }

    public InsuranceStatement findInsurance(String insuranceId) {
        return insurances.get(normalizeKey(insuranceId));
    }

    public boolean containsInsurance(String insuranceId) {
        return insurances.containsKey(normalizeKey(insuranceId));
    }

    public boolean hasInsuranceForPlate(String licensePlate) {
        if (licensePlate == null) {
            return false;
        }

        for (InsuranceStatement statement : insurances.values()) {
            if (statement.getLicensePlate() != null
                    && statement.getLicensePlate()
                            .equalsIgnoreCase(licensePlate.trim())) {
                return true;
            }
        }

        return false;
    }

    public List<InsuranceStatement> getInsuranceByYear(
            int year,
            String sortField,
            boolean ascending) {

        List<InsuranceStatement> result = new ArrayList<>();

        for (InsuranceStatement statement : insurances.values()) {
            if (statement.getEstablishedDate() != null
                    && statement.getEstablishedDate().getYear() == year) {
                result.add(statement);
            }
        }

        Comparator<InsuranceStatement> comparator;

        String field = sortField == null
                ? "" : sortField.toLowerCase();

        switch (field) {
            case "date":
                comparator = Comparator.comparing(
                        InsuranceStatement::getEstablishedDate
                );
                break;

            case "plate":
                comparator = Comparator.comparing(
                        InsuranceStatement::getLicensePlate,
                        String.CASE_INSENSITIVE_ORDER
                );
                break;

            case "period":
                comparator = Comparator.comparingInt(
                        InsuranceStatement::getInsurancePeriod
                );
                break;

            case "id":
            default:
                comparator = Comparator.comparing(
                        InsuranceStatement::getInsuranceId,
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

    public static long calculateInsuranceFees(
            long vehicleValue,
            int insurancePeriod) {

        if (vehicleValue <= 0) {
            return 0;
        }

        BigDecimal rate;
        int numberOfYears;

        switch (insurancePeriod) {
            case 12:
                rate = new BigDecimal("0.25");
                numberOfYears = 1;
                break;

            case 24:
                rate = new BigDecimal("0.20");
                numberOfYears = 2;
                break;

            case 36:
                rate = new BigDecimal("0.15");
                numberOfYears = 3;
                break;

            default:
                return 0;
        }

        BigDecimal fees = BigDecimal.valueOf(vehicleValue)
                .multiply(rate)
                .multiply(BigDecimal.valueOf(numberOfYears));

        return fees.setScale(0, RoundingMode.HALF_UP).longValue();
    }

    public Map<String, InsuranceStatement> getInsurances() {
        return new LinkedHashMap<>(insurances);
    }

    public void setInsurances(
            Map<String, InsuranceStatement> loadedInsurances) {

        insurances.clear();

        if (loadedInsurances == null) {
            return;
        }

        for (InsuranceStatement statement : loadedInsurances.values()) {
            if (statement != null && statement.getInsuranceId() != null
                    && !statement.getInsuranceId().trim().isEmpty()) {
                insurances.put(
                        normalizeKey(statement.getInsuranceId()),
                        statement
                );
            }
        }
    }

    public int size() {
        return insurances.size();
    }

    public boolean isEmpty() {
        return insurances.isEmpty();
    }

   
}