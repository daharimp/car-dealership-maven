package com.skills4it.dealership.models;

import com.skills4it.dealership.models.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Dealership {
    private String name;
    private String address;
    private String phone;
    private final List<Vehicle> inventory;

    public Dealership(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Vehicle> getAllVehicles() {
        return new ArrayList<>(inventory);
    }

    public List<Vehicle> getVehiclesByPrice(double minPrice, double maxPrice) {
        return inventory.stream()
                .filter(vehicle -> vehicle.getPrice() >= minPrice && vehicle.getPrice() <= maxPrice)
                .toList();
    }

    public List<Vehicle> getVehiclesByMakeModel(String make, String model) {
        String makeSearch = make.trim().toLowerCase();
        String modelSearch = model.trim().toLowerCase();

        return inventory.stream()
                .filter(vehicle -> makeSearch.isBlank() || vehicle.getMake().toLowerCase().contains(makeSearch))
                .filter(vehicle -> modelSearch.isBlank() || vehicle.getModel().toLowerCase().contains(modelSearch))
                .toList();
    }

    public List<Vehicle> getVehiclesByYear(int minYear, int maxYear) {
        return inventory.stream()
                .filter(vehicle -> vehicle.getYear() >= minYear && vehicle.getYear() <= maxYear)
                .toList();
    }

    public List<Vehicle> getVehiclesByColor(String color) {
        String search = color.trim().toLowerCase();

        return inventory.stream()
                .filter(vehicle -> vehicle.getColor().toLowerCase().contains(search))
                .toList();
    }

    public List<Vehicle> getVehiclesByMileage(int minMileage, int maxMileage) {
        return inventory.stream()
                .filter(vehicle -> vehicle.getOdometer() >= minMileage && vehicle.getOdometer() <= maxMileage)
                .toList();
    }

    public List<Vehicle> getVehiclesByType(VehicleType vehicleType) {
        return inventory.stream()
                .filter(vehicle -> vehicle.getVehicleType() == vehicleType)
                .toList();
    }

    public void addVehicle(Vehicle vehicle) {
        if (findVehicleByVin(vehicle.getVin()).isPresent()) {
            throw new IllegalArgumentException("A vehicle with VIN " + vehicle.getVin() + " already exists.");
        }
        inventory.add(vehicle);
    }

    public boolean removeVehicleByVin(int vin) {
        return inventory.removeIf(vehicle -> vehicle.getVin() == vin);
    }

    public Optional<Vehicle> findVehicleByVin(int vin) {
        return inventory.stream()
                .filter(vehicle -> vehicle.getVin() == vin)
                .findFirst();
    }

    public String toCsvHeaderLine() {
        return String.join("|", name, address, phone);
    }
}
