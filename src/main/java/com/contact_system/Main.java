package com.contact_system;

import com.contact_system.db.H2Connection;
import com.contact_system.model.dao.user.UserDaoImpl;
import com.contact_system.model.entities.user.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        UserDaoImpl userDaoImpl = null;
        User authenticatedUser = null;
        User user = null;

        String name = null;
        String password = null;
        String phoneNumber = null;

        try (Connection connection = H2Connection.getConnection()) {
            do {
                System.out.println("\n===================================");
                System.out.println("\nOptions:");
                System.out.println("1 - Register");
                System.out.println("2 - Authenticate");
                System.out.println("3 - Add telephone");
                System.out.println("4 - Update by number");
                System.out.println("5 - Update by user");
                System.out.println("6 - Get telephone by number");
                System.out.println("7 - Get telephone by user");
                System.out.println("8 - Delete by number");
                System.out.println("9 - Delete by user");
                System.out.println("0 - Exit");

                System.out.println("\nSelect an option: ");

                switch (scanner.nextInt()) {
                    case 1:
                        userDaoImpl = new UserDaoImpl(connection);

                        scanner.nextLine();

                        System.out.println("Enter your name: ");
                        name = scanner.nextLine();

                        System.out.println("Enter your password: ");
                        password = scanner.nextLine();

                        System.out.println("Enter your phone number: ");
                        phoneNumber = scanner.nextLine();

                        user = new User(name, password, phoneNumber);

                        userDaoImpl.insert(user);

                        break;
                    case 2:
                        userDaoImpl = new UserDaoImpl(connection);

                        scanner.nextLine();

                        System.out.println("Enter your name: ");
                        name = scanner.nextLine();

                        System.out.println("Enter your password: ");
                        password = scanner.nextLine();

                        authenticatedUser = userDaoImpl.authenticate(name, password);

                        break;
                    case 3:
                        if (authenticatedUser == null) {
                            System.out.println("You must authenticate first!");

                            break;
                        }

                        userDaoImpl = new UserDaoImpl(connection);

                        scanner.nextLine();

                        System.out.println("Enter the telephone number: ");
                        phoneNumber = scanner.nextLine();

                        userDaoImpl.addTelephone(phoneNumber);

                        break;
                    case 4:
                        userDaoImpl = new UserDaoImpl(connection);

                        scanner.nextLine();

                        System.out.println("Enter the phone number: ");
                        phoneNumber = scanner.nextLine();

                        System.out.println("Enter the new user: ");
                        String newUserName = scanner.nextLine();

                        System.out.println("Enter the new phone number");
                        String newPhoneNumber = scanner.nextLine();

                        userDaoImpl.updateByNumber(phoneNumber, newUserName, newPhoneNumber);

                        break;
                    case 5:
                        System.out.println("Updating by user...");
                        break;
                    case 6:
                        System.out.println("Getting telephone by number...");
                        break;
                    case 7:
                        System.out.println("Getting telephone by user...");
                        break;
                    case 8:
                        System.out.println("Getting all telephone...");
                        break;
                    case 9:
                        System.out.println("Deleting by number...");
                        break;
                    case 10:
                        System.out.println("Deleting by user...");
                        break;
                    case 11:
                        System.out.println("Log out of account...");
                        break;
                    case 0:
                        H2Connection.closeConnection();

                        System.out.println("Exiting...");

                        return;
                    default:
                        System.out.println("Invalid option");
                }
            } while (true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}