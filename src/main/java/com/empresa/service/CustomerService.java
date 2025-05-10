package com.empresa.service;

import com.empresa.dominio.Customer;
import com.empresa.repository.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.Scanner;

public class CustomerService {
    private static final Logger log = LogManager.getLogger(com.empresa.service.CustomerService.class);

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final String REGEXNUMBERS = "^[0-9]+$";
    private static final String REGEXEMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    public void menu(int escolha) {
        switch (escolha) {
            case 1: findById();
            case 2: findByEmail();
            case 3: deleteById();
            case 4: deleteByEmail();

        }
    }

    private static void findById() {

        String id = "0";
        do {
            System.out.println("Type the id of the customer you want to search: ");
            id = SCANNER.nextLine();
        } while (id.matches(REGEXNUMBERS));
        Optional<Customer> customer = CustomerRepository.findById(Integer.parseInt(id));
        customer.ifPresent(cstmr -> {
            System.out.printf("[%d] - name: %s; email: %s; telefone: %s\n"
                    , cstmr.getId(), cstmr.getNome(), cstmr.getEmail(), cstmr.getTelefone());
        });
    }

    private static void findByEmail() {
        String email = "";
        do {
            System.out.println("Type the email of the customer you wanto to search: ");
            email = SCANNER.nextLine();
        } while (email.matches(REGEXEMAIL));
        Optional<Customer> customer = CustomerRepository.findByEmail(email);
        customer.ifPresent(cstmr -> {
            System.out.printf("[%d] - name: %s; email: %s; telefone: %s\n"
                    , cstmr.getId(), cstmr.getNome(), cstmr.getEmail(), cstmr.getTelefone());
        });
    }

    private static void deleteById() {
        String id = "0";
        do {
            System.out.println("Type the id of the customer you want to search: ");
            id = SCANNER.nextLine();
        } while (id.matches(REGEXNUMBERS));
            CustomerRepository.deleteById(Integer.parseInt(id));
    }

    private static void deleteByEmail() {
        String email = "";
        do {
            System.out.println("Type the email of the customer you wanto to search: ");
            email = SCANNER.nextLine();
        } while (email.matches(REGEXEMAIL));
        CustomerRepository.deleteByEmail(email);
    }
}
