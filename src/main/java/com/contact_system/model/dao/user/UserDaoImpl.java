package com.contact_system.model.dao.user;

import com.contact_system.db.DbException;
import com.contact_system.db.H2Connection;
import com.contact_system.model.entities.telephone.Telephone;
import com.contact_system.model.entities.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {
    Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(User user) {
        String getUserQuery = "INSERT INTO tb_user (name, password, phone_number) VALUES (?, ?, ?)";

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(getUserQuery);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getPhoneNumber());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Error inserting user: no rows affected!");
            }

            System.out.println("User inserted successfully!");
        } catch (SQLException e) {
            throw new DbException("Error inserting user: " + e.getMessage());
        } finally {
            H2Connection.closeStatement(preparedStatement);
        }
    }


    @Override
    public User authenticate(String user, String password) {
        String query = "SELECT * FROM tb_user WHERE name = ? AND password = ?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user);
            preparedStatement.setString(2, password);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DbException("User does not exist!");
            }

            if (resultSet.next()) {
                return new User(resultSet.getString("name"), resultSet.getString("password"), resultSet.getString("phone_number"));
            }

            System.out.println("Successful authentication");
        } catch (SQLException e) {
            throw new DbException("Error authenticating user: " + e.getMessage());
        } finally {
            H2Connection.closeStatement(preparedStatement);
        }

        return null;
    }

    @Override
    public void addTelephone(String phoneNumber) {
        System.out.println(phoneNumber);

        String getUserQuery = "SELECT * FROM tb_user WHERE phone_number = ?";
        String addUserQuery = "INSERT INTO tb_telephone (user_id, number) VALUES (?, ?)";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(getUserQuery);
            preparedStatement.setString(1, phoneNumber);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DbException("Error adding telephone: user not found!");
            }

            int userId = resultSet.getInt("id");
            String phone = resultSet.getString("phone_number");

            preparedStatement = connection.prepareStatement(addUserQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, phone);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Error adding telephone: no rows affected!");
            }

            System.out.println("Telephone added successfully!");
        } catch (SQLException e) {
            throw new DbException("Error adding telephone: " + e.getMessage());
        } finally {
            H2Connection.closeStatement(preparedStatement);
            H2Connection.closeResultSet(resultSet);
        }
    }

    @Override
    public void updateByUser(User user) {

    }

    @Override
    public void updateByNumber(String phoneNumber, String newUserName, String newPhoneNumber) {
        String updateUserQuery = "UPDATE tb_user SET name = ?, phone_number = ? WHERE id = ?";

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(updateUserQuery);
            preparedStatement.setString(1, newPhoneNumber);
            preparedStatement.setString(2, newUserName);
            preparedStatement.setString(3, phoneNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Error editing phone number.");
            }

            System.out.println("Successful phone number update!");
        } catch (SQLException e) {
            throw new DbException("Error editing phone number: " + e.getMessage());
        } finally {
            H2Connection.closeStatement(preparedStatement);
        }
    }

    @Override
    public void deleteByUser(String user) {

    }

    @Override
    public void deleteByNumber(String number) {

    }

    @Override
    public Telephone getTelephoneByUser(String user) {
        return null;
    }

    @Override
    public Telephone getTelephoneByNumber(String number) {
        return null;
    }
}
