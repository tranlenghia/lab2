package validation;

import model.InsuranceStatement;

public class InsuranceValidation {

    public static String getInputValidationError(
            InsuranceStatement statement) {

        if (statement == null) {
            return "Insurance information cannot be null.";
        }

        if (statement.getInsuranceId() == null
                || statement.getInsuranceId().trim().isEmpty()) {
            return "Insurance ID cannot be empty.";
        }

        if (statement.getEstablishedDate() == null) {
            return "Established date is invalid.";
        }

        if (statement.getLicensePlate() == null
                || statement.getLicensePlate().trim().isEmpty()) {
            return "License plate cannot be empty.";
        }

        if (statement.getCustomerName() == null
                || statement.getCustomerName().trim().isEmpty()) {
            return "Customer name cannot be empty.";
        }

        int period = statement.getInsurancePeriod();

        if (period != 12 && period != 24 && period != 36) {
            return "Insurance period must be 12, 24, or 36 months.";
        }

        return null;
    }

    public static String getValidationError(
            InsuranceStatement statement) {

        String inputError = getInputValidationError(statement);

        if (inputError != null) {
            return inputError;
        }

        if (statement.getInsuranceFees() <= 0) {
            return "Insurance fees must be a positive integer.";
        }

        return null;
    }

    public static boolean isValid(InsuranceStatement statement) {
        return getValidationError(statement) == null;
    }
}