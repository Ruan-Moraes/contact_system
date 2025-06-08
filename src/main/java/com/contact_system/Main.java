package com.contact_system;

import com.contact_system.db.DbException;
import com.contact_system.db.H2Connection;
import com.contact_system.model.dao.user.UserDaoImpl;
import com.contact_system.model.entities.telephone.Telephone;
import com.contact_system.model.entities.user.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
                System.out.println("4 - Get telephone by number");
                System.out.println("5 - Delete phone number by number");
                System.out.println("6 - View all my phone numbers");
                System.out.println("7 - Log out");
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

                        user = new User(name, password);

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

                        userDaoImpl.addTelephone(phoneNumber, authenticatedUser.getName());

                        break;
                    case 4:
                        userDaoImpl = new UserDaoImpl(connection);

                        scanner.nextLine();

                        System.out.println("Enter the phone number to search: ");
                        phoneNumber = scanner.nextLine();

                        Telephone telephoneByNumber = userDaoImpl.getTelephoneByNumber(phoneNumber);
                        System.out.println(telephoneByNumber);

                        break;
                    case 5:
                        userDaoImpl = new UserDaoImpl(connection);

                        scanner.nextLine();

                        System.out.println("Enter the phone number to delete: ");
                        phoneNumber = scanner.nextLine();

                        userDaoImpl.deleteByNumber(phoneNumber);

                        System.out.println("Phone number deleted successfully!");

                        break;
                    case 6:
                        if (authenticatedUser == null) {
                            System.out.println("You must authenticate first!");
                            break;
                        }

                        userDaoImpl = new UserDaoImpl(connection);

                        try {
                            List<Telephone> telephones = userDaoImpl.getAllTelephonesByUser(authenticatedUser.getName());

                            System.out.println("\nYour phone numbers:");

                            for (Telephone tel : telephones) {
                                System.out.println("- " + tel.getNumber());
                            }
                        } catch (DbException e) {
                            System.out.println(e.getMessage());
                        }

                        break;
                    case 7:
                        if (authenticatedUser == null) {
                            System.out.println("You are not logged in!");
                        }

                        if (authenticatedUser != null) {
                            authenticatedUser = null;
                            System.out.println("Logged out successfully!");
                        }
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
