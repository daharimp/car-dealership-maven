package com.skills4it.dealership.data;

import com.skills4it.dealership.models.Contract;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class ContractFileManager {

    private static final Path CONTRACTS_PATH =
            Path.of("src", "main", "resources", "contracts.csv");

    public void saveContract(Contract contract) {
        Objects.requireNonNull(contract, "contract");
        try {
            Files.createDirectories(CONTRACTS_PATH.getParent());
            try(BufferedWriter writer = Files.newBufferedWriter(
                    CONTRACTS_PATH,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND)) {
                writer.write(contract.toCsvLine());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not append contract to " + CONTRACTS_PATH + ": " + e.getMessage(), e);
        }
    }
}
