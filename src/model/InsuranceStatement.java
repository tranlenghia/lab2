package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import utils.DateUtils;

public class InsuranceStatement implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String insuranceId;
    private final LocalDate establishedDate;
    private final String licensePlate;
    private final String customerName;
    private final int insurancePeriod;
    private long insuranceFees;

    public InsuranceStatement(String insuranceId,
                              LocalDate establishedDate,
                              String licensePlate,
                              String customerName,
                              int insurancePeriod,
                              long insuranceFees) {
        this.insuranceId = insuranceId == null
                ? null : insuranceId.trim();
        this.establishedDate = establishedDate;
        this.licensePlate = licensePlate == null
                ? null : licensePlate.trim();
        this.customerName = customerName == null
                ? null : customerName.trim();
        this.insurancePeriod = insurancePeriod;
        this.insuranceFees = insuranceFees;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public LocalDate getEstablishedDate() {
        return establishedDate;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getInsurancePeriod() {
        return insurancePeriod;
    }

    public long getInsuranceFees() {
        return insuranceFees;
    }

    public void setInsuranceFees(long insuranceFees) {
        this.insuranceFees = insuranceFees;
    }

    @Override
    public String toString() {
        return "Insurance ID: " + insuranceId
                + "\nEstablished date: "
                + DateUtils.formatDate(establishedDate)
                + "\nLicense plate: " + licensePlate
                + "\nCustomer name: " + customerName
                + "\nInsurance period: " + insurancePeriod + " months"
                + "\nInsurance fees: " + insuranceFees;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof InsuranceStatement)) {
            return false;
        }

        InsuranceStatement other = (InsuranceStatement) object;

        return insuranceId != null
                && other.insuranceId != null
                && insuranceId.equalsIgnoreCase(other.insuranceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                insuranceId == null
                        ? null : insuranceId.toUpperCase()
        );
    }
}