package com.empresa.service;

import com.empresa.dominio.Customer;
import com.empresa.repository.CustomerRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.InputMismatchException;
import java.util.Optional;
import java.util.Scanner;

public class CustomerService {
    private static final Logger log = LogManager.getLogger(CustomerService.class);

    private static final Scanner SCANNER = new Scanner(System.in);

    private static final String REGEXNUMBERS = "^[0-9]+$";
    private static final String REGEXEMAIL = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String REGEXNOME = "^[a-zA-Z ]+$";
    private static final String REGEXTELEFONE = "^\\(?[1-9]{2}\\)?\\s?(?:9\\d{4}|\\d{4})[-\\s]?\\d{4}$";


    public static void menu(int escolha) {
        switch (escolha) {
            case 1:
                findAll();
                break;
            case 2:
                findById();
                break;
            case 3:
                findByEmail();
                break;
            case 4:
                deleteById();
                break;
            case 5:
                deleteByEmail();
                break;
            case 6:
                updateById();
                break;
            case 7:
                saveNewCustomer();
                break;
            case 0:
                System.out.println("Ending program...");
                break;
            default:
                throw new InputMismatchException();
        }
    }

    private static void findAll() {
        CustomerRepository.findAll().forEach(System.out::println);
    }

    private static void findById() {
        String id = "0";
        do {
            System.out.println("Type the id of the customer you want to search: ");
            id = SCANNER.nextLine();
        } while (!id.matches(REGEXNUMBERS));
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
        } while (!email.matches(REGEXEMAIL));
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
        } while (!id.matches(REGEXNUMBERS));
        CustomerRepository.deleteById(Integer.parseInt(id));
    }

    private static void deleteByEmail() {
        String email = "";
        do {
            System.out.println("Type the email of the customer you wanto to search: ");
            email = SCANNER.nextLine();
        } while (!email.matches(REGEXEMAIL));
        CustomerRepository.deleteByEmail(email);
    }

    private static void updateById() {
        String id = "0";
        int choose = 0;
        do {
            System.out.println("Type the id of the customer you want to update: ");
            id = SCANNER.nextLine();
        } while (!id.matches(REGEXNUMBERS));
        Optional<Customer> existingCustomer = CustomerRepository.findById(Integer.parseInt(id));
        if (existingCustomer.isEmpty()) {
            System.out.println("Customer not found!");
            return;
        }
        Customer customer = getNewCustomerToUpdate(existingCustomer.get());
        CustomerRepository.updateById(Integer.parseInt(id), customer);
    }

    private static Customer getNewCustomerToUpdate(Customer customer) {
        int choose = 0;
        do {
            choose = getUpdateChoose();
            switch (choose) {
                case 1:
                    getNewName(customer);
                    break;
                case 2:
                    getNewEmail(customer);
                    break;
                case 3:
                    getNewTelephone(customer);
                    break;
            }
        } while (choose != 0);
        return customer;
    }

    private static Customer getNewCustomer() {
        Customer customer = new Customer("", "", "");
        getNewName(customer);
        getNewEmail(customer);
        getNewTelephone(customer);
        return customer;
    }

    private static int getUpdateChoose() { //Get choose to update some element of Customer
        int choose = 0;
        do {
            try {
                System.out.println("What do you want to update?");
                System.out.println("1 - Name");
                System.out.println("2 - Email");
                System.out.println("3 - Telephone");
                System.out.println("0 - End update");
                choose = SCANNER.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number");
            } finally {
                SCANNER.nextLine();
            }
        } while (choose < 0 || choose > 3);
        return choose;
    }

    private static void getNewName(Customer customer) {
        String nome;
        do {
            System.out.print("New name: ");
            nome = SCANNER.nextLine();
        } while (!nome.matches(REGEXNOME)); // Validate format
        customer.setNome(nome);
    }

    private static void getNewEmail(Customer customer) {
        String email;
        do {
            System.out.println("Type the new email of the customer");
            email = SCANNER.nextLine();
        } while (!email.matches(REGEXEMAIL));
        customer.setEmail(email);
    }

    private static void getNewTelephone(Customer customer) {
        String telefone;
        System.out.println("Type the new telephone of the customer:");
        telefone = SCANNER.nextLine();
        customer.setTelefone(telefone);
    }

    private static void saveNewCustomer() {
        Customer newCustomer = getNewCustomer();
        CustomerRepository.save(newCustomer);
        System.out.println("Customer saved!");
    }
}
