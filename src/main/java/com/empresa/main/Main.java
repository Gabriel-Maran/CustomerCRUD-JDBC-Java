package com.empresa.main;

import com.empresa.service.CustomerService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int escolha;
        while (true) {
            menuCustomer();
            escolha = sc.nextInt();
            if (escolha == 0) break;
            CustomerService.menu(escolha);
        }
    }

    public static void menuCustomer() {
        System.out.println(" -------- Customer Menu --------");
        System.out.println("Type the operation you would like to perform");
        System.out.println("  1. Search for all Customers ");
        System.out.println("  2. Search for Customer by ID ");
        System.out.println("  3. Search for Customer by Email ");
        System.out.println("  4. Delete Customer by ID ");
        System.out.println("  5. Delete Customer by Email ");
        System.out.println("  6. Update Customer by ID ");
        System.out.println("  7. Save new Customer ");
        System.out.println("  0. Delete Customer by Email ");
    }
}
