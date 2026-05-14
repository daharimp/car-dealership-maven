# Object-Oriented Car Dealership

A Maven-based Java console application for managing a used car dealership inventory.

## Features

- Find vehicles by price range
- Find vehicles by make/model
- Find vehicles by year range
- Find vehicles by color
- Find vehicles by mileage range
- Find vehicles by type using the `VehicleType` enum
- List all vehicles
- Add a vehicle
- Remove a vehicle by VIN
- Save inventory changes immediately to `src/main/resources/inventory.csv`
- Automatically creates timestamped backups in `src/main/resources/backups`

## Enums Added

This version includes two enums:

```text
VehicleType
CAR, TRUCK, SUV, VAN
```

Used in the `Vehicle` model instead of a plain `String`. This makes vehicle type input safer and prevents invalid values.

```text
MenuOption
FIND_BY_PRICE, FIND_BY_MAKE_MODEL, FIND_BY_YEAR, FIND_BY_COLOR,
FIND_BY_MILEAGE, FIND_BY_TYPE, LIST_ALL, ADD_VEHICLE,
REMOVE_VEHICLE, QUIT
```

Used by the user interface to make the menu logic cleaner and easier to maintain.

## Project Structure

```text
src/main/java/com/skills4it/dealership
├── Program.java
├── data
│   └── DealershipFileManager.java
├── models
│   ├── Dealership.java
│   ├── Vehicle.java
│   └── enums
│       └── VehicleType.java
└── ui
    ├── UserInterface.java
    └── enums
        └── MenuOption.java

src/main/resources
└── inventory.csv
```

## Data Format

The first line contains dealership information:

```text
name|address|phone
```

Each following line contains one vehicle:

```text
vin|year|make|model|vehicleType|color|odometer|price
```

Example:

```text
D & B Used Cars|111 Old Benbrook Rd|817-555-5555
10112|1993|Ford|Explorer|SUV|Red|525123|995.00
```

Allowed vehicle types are:

```text
Car, Truck, SUV, Van
```

## Run the Application

From the project root:

```bash
mvn clean compile exec:java
```

## Notes

This project uses Java 17 and the Maven exec plugin.
