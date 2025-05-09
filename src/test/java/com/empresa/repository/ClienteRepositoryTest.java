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
    void findById_ReturnsCostumer_WhenIDIs1() {
        Optional<Costumer> costumer = CostumerRepository.findById(1);
        Assertions.assertTrue(costumer.isPresent(), "Costumer with id 1 must exists");
        Assertions.assertEquals("Gabriel", costumer.get().getNome(), "Costumer's name must be Gabriel");
    }

    @Test
    @DisplayName("Searching for costumer with invalid ID (-1) must return empty")
    void findById_ReturnsEmptyOptional_WhenIdIsInvalid() {
        Optional<Costumer> costumer = CostumerRepository.findById(-1);
        Assertions.assertFalse(costumer.isPresent(), "Costumer with id -1 must not exists");
    }

    @Test
    @DisplayName("Costumer's name must be 'Gabriel' when searching by email 'gabriel@gmail.com'")
    void findByEmail_ReturnCostumer_WhenEmailIsValid() {
        Optional<Costumer> costumer = CostumerRepository.findByEmail("gabriel@gmail.com");
        Assertions.assertTrue(costumer.isPresent(), "Costumer with email 'gabriel@gmail.com' must exists");
    }

    @Test
    @DisplayName("Searching for costumer with invalid email (abc@notAnEmail.com) must return empty")
    void findByEmail_ReturnsEmptyOptional_WhenEmailIsInvalid() {
        Optional<Costumer> costumer = CostumerRepository.findByEmail("abc@notAnEmail.com");
        Assertions.assertFalse(costumer.isPresent(), "Costumer with email 'abc@notAnEmail.com' must not exists");
    }


    @Test
    @DisplayName("Should throw DatabaseException when creating customer with existing email")
    void save_ThrowDatabaseException_WhenEmailIsAlreadyUsed() {
        Costumer newCostumer = new Costumer("Gabriel", "gabriel@gmail.com", "49999999999");
        Assertions.assertThrows(DatabaseException.class , () -> CostumerRepository.save(newCostumer),
                "Must throw DatabaseExcetion, because there is a consumer registered with this email");
    }


}