package com.skills4it.dealership.ui;

import com.skills4it.dealership.data.ContractFileManager;
import com.skills4it.dealership.data.DealershipFileManager;
import com.skills4it.dealership.models.Dealership;
import com.skills4it.dealership.models.Vehicle;
import com.skills4it.dealership.models.Contract;
import com.skills4it.dealership.models.LeaseContract;
import com.skills4it.dealership.models.LeaseEligibilityException;
import com.skills4it.dealership.models.SalesContract;
import com.skills4it.dealership.models.enums.VehicleType;
import com.skills4it.dealership.ui.enums.MenuOption;

import java.util.List;

public class UserInterface {

    private final DealershipFileManager fileManager;
    private final ContractFileManager contractFileManager;
    private Dealership dealership;

    public UserInterface() {
        this.fileManager = new DealershipFileManager();
        this.contractFileManager = new ContractFileManager();
    }

    public void display() {
        init();

        MenuOption selectedOption;
        do {
            displayHeader();
            displayMenu();
            int choice = Helper.readInt("Choose an option: ");
            selectedOption = MenuOption.fromCode(choice).orElse(null);
            handleMenuChoice(selectedOption);
        } while (selectedOption != MenuOption.QUIT);

        System.out.println("Goodbye!");
    }

    private void init() {
        this.dealership = fileManager.getDealership();
    }

    private void displayHeader() {
        System.out.println();
        System.out.println("=============================================");
        System.out.println("Welcome to: " + dealership.getName());
        System.out.println(dealership.getAddress() + " | " + dealership.getPhone());
        System.out.println("=============================================");
    }

    private void displayMenu() {
        for (MenuOption option : MenuOption.values()) {
            System.out.printf("%-3d - %s%n", option.getCode(), option.getLabel());
        }
        System.out.println();
    }

    private void handleMenuChoice(MenuOption option) {
        if (option == null) {
            System.out.println("Invalid option. Please try again.");
            return;
        }

        switch (option) {
            case FIND_BY_PRICE       -> processGetByPriceRequest();
            case FIND_BY_MAKE_MODEL  -> processGetByMakeModelRequest();
            case FIND_BY_YEAR        -> processGetByYearRequest();
            case FIND_BY_COLOR       -> processGetByColorRequest();
            case FIND_BY_MILEAGE     -> processGetByMileageRequest();
            case FIND_BY_TYPE        -> processGetByVehicleTypeRequest();
            case LIST_ALL            -> processAllVehiclesRequest();
            case ADD_VEHICLE         -> processAddVehicleRequest();
            case REMOVE_VEHICLE      -> processRemoveVehicleRequest();
            case SELL_LEASE_VEHICLE  -> processSellLeaseVehicleRequest();
            case QUIT                -> { }
        }
    }

    private void processGetByPriceRequest() {
        double minPrice = Helper.readDouble("Minimum price: ");
        double maxPrice = Helper.readDouble("Maximum price: ");
        displayVehicles(dealership.getVehiclesByPrice(minPrice, maxPrice));
    }

    private void processGetByMakeModelRequest() {
        String make = Helper.readString("Make, leave empty for any: ");
        String model = Helper.readString("Model, leave empty for any: ");
        displayVehicles(dealership.getVehiclesByMakeModel(make, model));
    }

    private void processGetByYearRequest() {
        int minYear = Helper.readInt("Minimum year: ");
        int maxYear = Helper.readInt("Maximum year: ");
        displayVehicles(dealership.getVehiclesByYear(minYear, maxYear));
    }

    private void processGetByColorRequest() {
        String color = Helper.readString("Color: ");
        displayVehicles(dealership.getVehiclesByColor(color));
    }

    private void processGetByMileageRequest() {
        int minMileage = Helper.readInt("Minimum mileage: ");
        int maxMileage = Helper.readInt("Maximum mileage: ");
        displayVehicles(dealership.getVehiclesByMileage(minMileage, maxMileage));
    }

    private void processGetByVehicleTypeRequest() {
        VehicleType vehicleType = readVehicleType(
                "Vehicle type (" + VehicleType.getAllowedValuesText() + "): ");
        displayVehicles(dealership.getVehiclesByType(vehicleType));
    }

    private void processAllVehiclesRequest() {
        displayVehicles(dealership.getAllVehicles());
    }

    private void processAddVehicleRequest() {
        System.out.println("Add a new vehicle");

        int vin = Helper.readPositiveInt("VIN: ");
        if (dealership.findVehicleByVin(vin).isPresent()) {
            System.out.println("A vehicle with this VIN already exists. Vehicle was not added.");
            return;
        }

        int year = Helper.readYear("Year: ");
        String make = Helper.readRequiredString("Make: ");
        String model = Helper.readRequiredString("Model: ");
        VehicleType vehicleType = readVehicleType(
                "Vehicle type (" + VehicleType.getAllowedValuesText() + "): ");
        String color = Helper.readRequiredString("Color: ");
        int odometer = Helper.readPositiveInt("Odometer: ");
        double price = Helper.readPositiveDouble("Price: ");

        Vehicle vehicle = new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
        dealership.addVehicle(vehicle);
        fileManager.saveDealership(dealership);

        System.out.println("Vehicle added and inventory saved.");
    }

    private void processRemoveVehicleRequest() {
        int vin = Helper.readPositiveInt("Enter VIN of vehicle to remove: ");

        dealership.findVehicleByVin(vin).ifPresentOrElse(vehicle -> {
            System.out.println("Vehicle found:");
            displayVehicles(List.of(vehicle));

            boolean confirmed = Helper.readYesNo("Remove this vehicle? yes/no: ");
            if (confirmed) {
                dealership.removeVehicleByVin(vin);
                fileManager.saveDealership(dealership);
                System.out.println("Vehicle removed and inventory saved.");
            } else {
                System.out.println("Remove cancelled.");
            }
        }, () -> System.out.println("No vehicle found with VIN " + vin + "."));
    }

    private void processSellLeaseVehicleRequest() {
        System.out.println("Sell or lease a vehicle");

        int vin = Helper.readPositiveInt("VIN of vehicle: ");
        var vehicleOpt = dealership.findVehicleByVin(vin);
        if (vehicleOpt.isEmpty()) {
            System.out.println("No vehicle found with VIN " + vin + ".");
            Helper.pause();
            return;
        }
        Vehicle vehicle = vehicleOpt.get();
        System.out.println("Vehicle found:");
        displayVehicles(List.of(vehicle));

        String contractType = readContractType("Sale or lease? (sale/lease): ");
        String contractDate = Helper.readYyyyMmDdDate("Contract date (YYYYMMDD): ");
        String customerName = Helper.readRequiredString("Customer name: ");
        String customerEmail = Helper.readRequiredString("Customer email: ");

        Contract contract;
        try {
            if (contractType.equals("sale")) {
                boolean financed = Helper.readYesNo("Finance the purchase? (yes/no): ");
                contract = new SalesContract(contractDate, customerName, customerEmail, vehicle, financed);
            } else {
                contract = new LeaseContract(contractDate, customerName, customerEmail, vehicle);
            }
        } catch (LeaseEligibilityException e) {
            System.out.println(e.getMessage());
            Helper.pause();
            return;
        }

        contractFileManager.saveContract(contract);
        dealership.removeVehicleByVin(vin);
        fileManager.saveDealership(dealership);

        System.out.printf("Contract recorded. Total: $%,.2f, monthly: $%,.2f%n",
                contract.getTotalPrice(), contract.getMonthlyPayment());
        Helper.pause();
    }

    private void displayVehicles(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles found.");
            Helper.pause();
            return;
        }

        System.out.println();
        System.out.printf("%-8s %-6s %-12s %-15s %-8s %-10s %10s %11s%n",
                "VIN", "YEAR", "MAKE", "MODEL", "TYPE", "COLOR", "ODOMETER", "PRICE");
        System.out.println("---------------------------------------------------------------------------------------");

        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle);
        }

        System.out.println("---------------------------------------------------------------------------------------");
        System.out.println("Total vehicles: " + vehicles.size());
        Helper.pause();
    }

    private VehicleType readVehicleType(String prompt) {
        while (true) {
            String input = Helper.readRequiredString(prompt);
            var vehicleType = VehicleType.fromString(input);
            if (vehicleType.isPresent()) {
                return vehicleType.get();
            }
            System.out.println("Invalid vehicle type. Allowed values: "
                    + VehicleType.getAllowedValuesText());
        }
    }

    private String readContractType(String prompt) {
        while (true) {
            String value = Helper.readRequiredString(prompt).toLowerCase();
            if (value.equals("sale") || value.equals("lease")) {
                return value;
            }
            System.out.println("Please enter either 'sale' or 'lease'.");
        }
    }
}
