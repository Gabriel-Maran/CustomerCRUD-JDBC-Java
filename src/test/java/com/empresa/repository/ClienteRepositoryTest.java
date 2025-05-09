package com.empresa.repository;

import com.empresa.dominio.Costumer;
import com.empresa.exception.DatabaseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class ClienteRepositoryTest {
    private Costumer existingCostumer;
    @BeforeEach
    void setUp() {
        existingCostumer = new Costumer("Gabriel", "gabriel@gmail.com", "49999999999");
        //If it's not registered
//        CostumerRepository.save(existingCostumer);
    }

    @Test
    @DisplayName("Costumer's name must be 'Gabriel' when searching by ID 1")
    void findById_ValidId__ReturnsClienteWithCorrectNome() {
        Optional<Costumer> costumer = CostumerRepository.findById(1);
        Assertions.assertTrue(costumer.isPresent(), "Costumer with id 1 must exists");
        Assertions.assertEquals("Gabriel", costumer.get().getNome(), "Costumer's name must be Gabriel");
    }

    @Test
    @DisplayName("Searching for costumer with invalid ID (-1) must return empty")
    void findById_InvalidId_ReturnsEmptyOptional() {
        Optional<Costumer> costumer = CostumerRepository.findById(-1);
        Assertions.assertFalse(costumer.isPresent(), "Costumer with id -1 must not exists");
    }


    @Test
    void save_ThrowDatabaseException_WhenEmailIsAlreadyUsed() {
        Costumer newCostumer = new Costumer("Gabriel", "gabriel@gmail.com", "49999999999");
        Assertions.assertThrows(DatabaseException.class , () -> CostumerRepository.save(newCostumer),
                "Must throw DatabaseExcetion, because there is a consumer registered with this email");
    }
}