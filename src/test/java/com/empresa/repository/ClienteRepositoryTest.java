package com.empresa.repository;

import com.empresa.dominio.Costumer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class ClienteRepositoryTest {
    private Costumer cliente;
    @BeforeEach
    void setUp() {
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
    void save() {

    }
}