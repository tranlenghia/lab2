package dispatcher;

import business.CarManager;
import business.InsuranceManager;
import fileio.BinaryStorage;
import fileio.Storage;
import model.Car;
import model.InsuranceStatement;
import utils.DateUtils;
import utils.InputHelper;
import validation.CarValidation;
import validation.InsuranceValidation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {

    private static final String CAR_FILE = "carInfo.dat";
    private static final String INSURANCE_FILE = "insurances.dat";

    private final CarManager carManager = new CarManager();
    private final InsuranceManager insuranceManager
            = new InsuranceManager();

    private final Storage<Map<String, Car>> carStorage
            = new BinaryStorage<>();

    private final Storage<Map<String, InsuranceStatement>> insuranceStorage = new BinaryStorage<>();

    private boolean dataChanged = false;

    public static void main(String[] args) {
        Main application = new Main();
        application.run();
    }

    private void run() {
        while (true) {
            showMenu();

            int choice = InputHelper.readInt("Choose an option: ");

            switch (choice) {
                case 1:
                    addCars();
                    break;

                case 2:
                    findCar();
                    break;

                case 3:
                    updateCar();
                    break;

                case 4:
                    deleteCar();
                    break;

                case 5:
                    addInsurances();
                    break;

                case 6:
                    listInsuranceStatements();
                    break;

                case 7:
                    reportUninsuredCars();
                    break;

                case 8:
                    saveData();
                    break;

                case 9:
                    loadData();
                    break;

                case 10:
                    if (handleQuit()) {
                        return;
                    }
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please choose 1 to 10."
                    );
            }
        }
    }

    private void showMenu() {
        System.out.println();
        System.out.println("====================================");
        System.out.println("     CAR INSURANCE MANAGEMENT");
        System.out.println("====================================");
        System.out.println("1. Add car information");
        System.out.println("2. Find a car");
        System.out.println("3. Update a car");
        System.out.println("4. Delete a car");
        System.out.println("5. Add an insurance statement");
        System.out.println("6. List of insurance statements");
        System.out.println("7. Report on uninsured cars");
        System.out.println("8. Save data");
        System.out.println("9. Load data");
        System.out.println("10. Quit");
        System.out.println("====================================");
    }

    // FUNCTION 1: ADD CAR INFORMATION
    private void addCars() {
        while (true) {
            System.out.println();
            System.out.println("--- Add Car Information ---");

            Car car = readCarDetails(null);

            if (carManager.containsCar(car.getLicensePlate())) {
                System.out.println("License plate already exists.");

            } else {
                String error = CarValidation.getValidationError(car);

                if (error != null) {
                    System.out.println("Cannot add car: " + error);

                } else if (carManager.addCar(car)) {
                    dataChanged = true;
                    System.out.println("Car added successfully.");

                } else {
                    System.out.println("Cannot add car.");
                }
            }

            boolean addAnother = InputHelper.readYesNo(
                    "Add another car? (Y/N): "
            );

            if (!addAnother) {
                return;
            }
        }
    }

    private Car readCarDetails(String existingLicensePlate) {
        String plate;

        if (existingLicensePlate == null) {
            plate = InputHelper.readRequiredString("License plate: ");
        } else {
            plate = existingLicensePlate;
            System.out.println("License plate: " + plate);
        }

        String owner = InputHelper.readRequiredString("Car owner: ");
        String brand = InputHelper.readRequiredString("Car brand: ");
        long value = InputHelper.readLong("Vehicle value: ");

        LocalDate registrationDate = InputHelper.readDate(
                "Registration date"
        );

        String place = InputHelper.readRequiredString(
                "Registration place: "
        );

        int type = InputHelper.readInt("Vehicle type (5, 7, or 9): ");

        return new Car(
                plate,
                owner,
                brand,
                value,
                registrationDate,
                place,
                type
        );
    }

    // FUNCTION 2: FIND A CAR
    private void findCar() {
        System.out.println();
        System.out.println("--- Find a Car ---");

        String plate = InputHelper.readRequiredString(
                "Enter license plate: "
        );

        Car car = carManager.findCar(plate);

        if (car == null) {
            System.out.println("Unregistered vehicle");
            return;
        }

        System.out.println();
        System.out.println("----------------------------------------------------------------");
        System.out.printf("| %-22s | %-35s |%n", "Field", "Information");
        System.out.println("----------------------------------------------------------------");

        System.out.printf("| %-22s | %-35s |%n",
                "License plate", car.getLicensePlate());

        System.out.printf("| %-22s | %-35s |%n",
                "Car owner", car.getCarOwner());

        System.out.printf("| %-22s | %-35s |%n",
                "Car brand", car.getCarBrand());

        System.out.printf("| %-22s | %-35s |%n",
                "Vehicle value", car.getVehicleValue());

        System.out.printf("| %-22s | %-35s |%n",
                "Registration date",
                DateUtils.formatDate(car.getRegistrationDate()));

        System.out.printf("| %-22s | %-35s |%n",
                "Registration place", car.getRegistrationPlace());

        System.out.printf("| %-22s | %-35s |%n",
                "Vehicle type", car.getVehicleType());

        System.out.println("----------------------------------------------------------------");
    }

    // FUNCTION 3: UPDATE A CAR
    private void updateCar() {
        System.out.println();
        System.out.println("--- Update a Car ---");

        String plate = InputHelper.readRequiredString(
                "Enter license plate: "
        );

        Car existingCar = carManager.findCar(plate);

        if (existingCar == null) {
            System.out.println("Unregistered vehicle");
            return;
        }

        System.out.println("Enter the new car information.");
        System.out.println("The license plate cannot be changed.");

        while (true) {
            // Đọc thông tin mới; giữ nguyên biển số cũ.
            Car updatedCar = readCarDetails(
                    existingCar.getLicensePlate()
            );

            // Kiểm tra lại toàn bộ dữ liệu trước khi cập nhật.
            String error = CarValidation.getValidationError(updatedCar);

            if (error != null) {
                System.out.println("Cannot update car: " + error);
                System.out.println("Please enter the car information again.");
                continue;
            }

            // Chỉ thay đổi dữ liệu nếu CarManager chấp nhận.
            if (carManager.updateCar(plate, updatedCar)) {
                dataChanged = true;
                System.out.println("Car updated successfully.");
            } else {
                System.out.println(
                        "Cannot update car. Please check the information."
                );
            }

            return;
        }
    }

    // FUNCTION 4: DELETE A CAR
    private void deleteCar() {
        System.out.println();
        System.out.println("--- Delete a Car ---");

        String plate = InputHelper.readRequiredString(
                "Enter license plate: "
        );

        Car car = carManager.findCar(plate);

        if (car == null) {
            System.out.println("Unregistered vehicle");
            return;
        }

        if (insuranceManager.hasInsuranceForPlate(
                car.getLicensePlate())) {

            System.out.println(
                    "Cannot delete this car because it has "
                    + "an insurance statement."
            );
            return;
        }

        System.out.println(car);

        boolean confirmed = InputHelper.readYesNo(
                "Are you sure you want to delete this car? (Y/N): "
        );

        if (!confirmed) {
            System.out.println("Delete operation cancelled.");
            return;
        }

        if (carManager.deleteCar(plate)) {
            dataChanged = true;
            System.out.println("Car deleted successfully.");
        } else {
            System.out.println("Cannot delete car.");
        }
    }

    // FUNCTION 5: ADD AN INSURANCE STATEMENT
    private void addInsurances() {
        while (true) {
            System.out.println();
            System.out.println("--- Add an Insurance Statement ---");

            String id = InputHelper.readRequiredString(
                    "Insurance ID: "
            );

            LocalDate date = InputHelper.readDate(
                    "Established date"
            );

            String plate = InputHelper.readRequiredString(
                    "License plate: "
            );

            String customerName = InputHelper.readRequiredString(
                    "Customer name: "
            );

            int period = InputHelper.readInt(
                    "Insurance period (12, 24, or 36 months): "
            );

            InsuranceStatement statement
                    = new InsuranceStatement(
                            id,
                            date,
                            plate,
                            customerName,
                            period,
                            0
                    );

            if (insuranceManager.containsInsurance(id)) {
                System.out.println("Insurance ID already exists.");

            } else {
                String error
                        = InsuranceValidation.getInputValidationError(
                                statement
                        );

                if (error != null) {
                    System.out.println(
                            "Cannot add insurance: " + error
                    );

                } else if (carManager.findCar(plate) == null) {
                    System.out.println("Unregistered vehicle");

                } else if (insuranceManager.addInsurance(
                        statement, carManager)) {

                    dataChanged = true;
                    System.out.println(
                            "Insurance statement added successfully."
                    );

                    printInsuranceStatements(
                            java.util.Collections.singletonList(statement)
                    );

                } else {
                    System.out.println(
                            "Cannot add insurance statement."
                    );
                }
            }

            boolean addAnother = InputHelper.readYesNo(
                    "Add another insurance statement? (Y/N): "
            );

            if (!addAnother) {
                return;
            }
        }
    }

    // FUNCTION 6: LIST INSURANCE STATEMENTS
    // FUNCTION 6: LIST INSURANCE STATEMENTS
private void listInsuranceStatements() {
    System.out.println();
    System.out.println("--- List of Insurance Statements ---");

    // Người dùng chỉ cần nhập năm
    int year = InputHelper.readIntInRange(
            "Enter year: ", 1, 9999
    );

    // Tự động xác định ngày đầu năm và cuối năm
    LocalDate fromDate = LocalDate.of(year, 1, 1);
    LocalDate toDate = LocalDate.of(year, 12, 31);

    // Chọn trường sắp xếp
    String sortField = readInsuranceSortField();

    // Chọn ASC hoặc DESC
    boolean ascending = readAscendingDirection();

    // Lấy danh sách bảo hiểm
    List<InsuranceStatement> result = new java.util.ArrayList<>(
            insuranceManager.getInsurances().values()
    );

    // Lọc bảo hiểm theo năm đã nhập
    result.removeIf(statement -> {
        LocalDate date = statement.getEstablishedDate();

        return date == null
                || date.isBefore(fromDate)
                || date.isAfter(toDate);
    });

    // Sắp xếp dữ liệu
    java.util.Comparator<InsuranceStatement> comparator;

    switch (sortField) {
        case "id":
            comparator = java.util.Comparator.comparing(
                    InsuranceStatement::getInsuranceId,
                    String.CASE_INSENSITIVE_ORDER
            );
            break;

        case "date":
            comparator = java.util.Comparator.comparing(
                    InsuranceStatement::getEstablishedDate
            );
            break;

        case "plate":
            comparator = java.util.Comparator.comparing(
                    InsuranceStatement::getLicensePlate,
                    String.CASE_INSENSITIVE_ORDER
            );
            break;

        default:
            comparator = java.util.Comparator.comparingInt(
                    InsuranceStatement::getInsurancePeriod
            );
            break;
    }

    if (!ascending) {
        comparator = comparator.reversed();
    }

    result.sort(comparator);

    // In báo cáo với khoảng ngày tự động
    printInsuranceReport(
            result,
            fromDate,
            toDate,
            sortField,
            ascending
    );
}
private void printInsuranceReport(
        List<InsuranceStatement> statements,
        LocalDate fromDate,
        LocalDate toDate,
        String sortField,
        boolean ascending) {

    System.out.println();
    System.out.println("Report : INSURANCE STATEMENTS");

    System.out.println(
            "From : " + DateUtils.formatDate(fromDate)
            + " To: " + DateUtils.formatDate(toDate)
    );

    System.out.println();

    String sortLabel;

    switch (sortField) {
        case "id":
            sortLabel = "Insurance ID";
            break;

        case "date":
            sortLabel = "Established Date";
            break;

        case "plate":
            sortLabel = "License plate";
            break;

        default:
            sortLabel = "Insurance period";
            break;
    }

    System.out.println("Sorted by: " + sortLabel);
    System.out.println("Sort type: " + (ascending ? "ASC" : "DESC"));
    System.out.println();

    String border =
            "--------------------------------------------------------------"
          + "---------------------------------------------------------------";

    System.out.println(border);

    System.out.printf(
            "| %-4s | %-12s | %-17s | %-16s | %-24s | %16s | %14s |%n",
            "No.",
            "Insurance Id",
            "Established Date",
            "License plate",
            "Customer",
            "Insurance period",
            "Insurance fees"
    );

    System.out.println(border);

    if (statements.isEmpty()) {
        System.out.printf(
                "| %-97s |%n",
                "No insurance statements found in this date range."
        );
        System.out.println(border);
        return;
    }

    int number = 1;

    for (InsuranceStatement statement : statements) {
        System.out.printf(
                "| %4d | %-12s | %-17s | %-16s | %-24s | %16d | %14s |%n",
                number++,
                statement.getInsuranceId(),
                DateUtils.formatDate(
                        statement.getEstablishedDate()
                ),
                statement.getLicensePlate(),
                statement.getCustomerName(),
                statement.getInsurancePeriod(),
                String.format(
                        java.util.Locale.US,
                        "$ %,d",
                        statement.getInsuranceFees()
                )
        );
    }

    System.out.println(border);
}

    private String readInsuranceSortField() {
        System.out.println();
        System.out.println("Sort insurance statements by:");
        System.out.println("1. Insurance ID");
        System.out.println("2. Established date");
        System.out.println("3. License plate");
        System.out.println("4. Insurance period");

        int choice = InputHelper.readIntInRange(
                "Choose sort field: ", 1, 4
        );

        switch (choice) {
            case 1:
                return "id";
            case 2:
                return "date";
            case 3:
                return "plate";
            default:
                return "period";
        }
    }

    private void printInsuranceStatements(
            List<InsuranceStatement> statements) {

        if (statements.isEmpty()) {
            System.out.println(
                    "No insurance statements found for this year."
            );
            return;
        }

        String border
                = "-----------------------------------------------------------------------------------------------------------------------";

        System.out.println();
        System.out.println(border);

        System.out.printf(
                "| %-13s | %-12s | %-15s | %-25s | %8s | %15s |%n",
                "Insurance ID",
                "Date",
                "License Plate",
                "Customer Name",
                "Period",
                "Fees"
        );

        System.out.println(border);

        for (InsuranceStatement statement : statements) {
            System.out.printf(
                    "| %-13s | %-12s | %-15s | %-25s | %8d | %15s |%n",
                    statement.getInsuranceId(),
                    DateUtils.formatDate(
                            statement.getEstablishedDate()
                    ),
                    statement.getLicensePlate(),
                    statement.getCustomerName(),
                    statement.getInsurancePeriod(),
                    String.format(
                            java.util.Locale.US,
                            "$ %,d",
                            statement.getInsuranceFees()
                    )
            );
        }

        System.out.println(border);
    }

  // FUNCTION 7: REPORT ON UNINSURED CARS
private void reportUninsuredCars() {
    System.out.println();

    // Ask the user how the report should be sorted.
    String sortField = readCarSortField();
    boolean ascending = readAscendingDirection();

    // Get cars that do not have an insurance statement.
    List<Car> result = carManager.getUninsuredCars(
            insuranceManager,
            sortField,
            ascending
    );

    // Print the report.
    printCars(result, sortField, ascending);
}

// Choose the field used to sort the report.
private String readCarSortField() {
    System.out.println();
    System.out.println("Sort uninsured cars by:");
    System.out.println("1. License plate");
    System.out.println("2. Car owner");
    System.out.println("3. Registration date");
    System.out.println("4. Vehicle type");

    int choice = InputHelper.readIntInRange(
            "Choose sort field: ", 1, 4
    );

    switch (choice) {
        case 1:
            return "plate";
        case 2:
            return "owner";
        case 3:
            return "date";
        default:
            return "type";
    }
}

// Choose ASC or DESC.
private boolean readAscendingDirection() {
    System.out.println();
    System.out.println("Sort direction:");
    System.out.println("1. Ascending (ASC)");
    System.out.println("2. Descending (DESC)");

    int choice = InputHelper.readIntInRange(
            "Choose direction: ", 1, 2
    );

    return choice == 1;
}

// Print the uninsured-car report.
private void printCars(
        List<Car> cars, String sortField, boolean ascending) {

    // Convert the sorting key into a readable label.
    String sortLabel;

    switch (sortField) {
        case "plate":
            sortLabel = "License plate";
            break;
        case "owner":
            sortLabel = "Car owner";
            break;
        case "date":
            sortLabel = "Registration date";
            break;
        default:
            sortLabel = "Vehicle type";
            break;
    }

    // Report heading.
    System.out.println();
    System.out.println("Report: UNINSURED CARS");
    System.out.println();
    System.out.println("Sorted by : " + sortLabel);
    System.out.println("Sort type : " + (ascending ? "ASC" : "DESC"));
    System.out.println();

    // Handle the case where no uninsured cars exist.
    if (cars.isEmpty()) {
        System.out.println("No uninsured cars found.");
        return;
    }

    // Table border.
    String border =
            "+-----+-----------------+--------------------+"
          + "-------------------------------------+"
          + "----------------------+--------------+-----------------+";

    System.out.println(border);

    // Table headings.
    System.out.printf(
            "| %3s | %-15s | %-18s | %-35s | %-20s | %12s | %15s |%n",
            "No.",
            "License plate",
            "Registration Date",
            "Vehicle Owner",
            "Brand",
            "Vehicle type",
            "Value"
    );

    System.out.println(border);

    // Print each car with its row number.
    int number = 1;

    for (Car car : cars) {
        System.out.printf(
                "| %3d | %-15s | %-18s | %-35s | %-20s | %12d | %15s |%n",
                number++,
                car.getLicensePlate(),
                DateUtils.formatDate(car.getRegistrationDate()),
                car.getCarOwner(),
                car.getCarBrand(),
                car.getVehicleType(),
                String.format(
                        java.util.Locale.US,
                        "$ %,d",
                        car.getVehicleValue()
                )
        );
    }

    System.out.println(border);
}

    // FUNCTION 8: SAVE DATA
    private boolean saveData() {
        System.out.println();
        System.out.println("--- Save Data ---");

        boolean carsSaved = carStorage.save(
                CAR_FILE, carManager.getCars()
        );

        boolean insurancesSaved = insuranceStorage.save(
                INSURANCE_FILE, insuranceManager.getInsurances()
        );

        if (carsSaved && insurancesSaved) {
            dataChanged = false;
            System.out.println("Data saved successfully.");
            System.out.println("Car data file: " + CAR_FILE);
            System.out.println(
                    "Insurance data file: " + INSURANCE_FILE
            );
            return true;
        }

        System.out.println(
                "Some data could not be saved. Please try again."
        );
        return false;
    }

    // FUNCTION 9: LOAD DATA
    private void loadData() {
        System.out.println();
        System.out.println("--- Load Data ---");

        if (dataChanged) {
            boolean continueLoading = InputHelper.readYesNo(
                    "Unsaved changes may be lost. Continue loading? (Y/N): "
            );

            if (!continueLoading) {
                System.out.println("Load operation cancelled.");
                return;
            }
        }

        try {
            Map<String, Car> loadedCars
                    = carStorage.load(CAR_FILE);

            Map<String, InsuranceStatement> loadedInsurances
                    = insuranceStorage.load(INSURANCE_FILE);

            if (loadedCars == null || loadedInsurances == null) {
                System.out.println(
                        "Cannot load data. Check whether both data "
                        + "files exist and are valid."
                );
                return;
            }

            carManager.setCars(loadedCars);
            insuranceManager.setInsurances(loadedInsurances);

            dataChanged = false;

            System.out.println("Data loaded successfully.");
            System.out.println(
                    "Number of cars: " + carManager.size()
            );
            System.out.println(
                    "Number of insurance statements: "
                    + insuranceManager.size()
            );

        } catch (ClassCastException e) {
            System.out.println(
                    "The data files have an unexpected format."
            );
        }
    }

    // FUNCTION 10: QUIT
    private boolean handleQuit() {
        System.out.println();

        boolean confirmed = InputHelper.readYesNo(
                "Are you sure you want to quit? (Y/N): "
        );

        if (!confirmed) {
            System.out.println("Returning to the main menu.");
            return false;
        }

        if (dataChanged) {
            System.out.println(
                    "There are unsaved changes. Saving data..."
            );

            boolean saved = saveData();

            if (!saved) {
                boolean quitAnyway = InputHelper.readYesNo(
                        "Saving failed. Quit anyway? (Y/N): "
                );

                if (!quitAnyway) {
                    return false;
                }
            }
        }

        System.out.println("Program terminated.");
        return true;
    }
}
