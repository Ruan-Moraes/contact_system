package com.contact_system.model.dao.user;

import com.contact_system.db.DbException;
import com.contact_system.db.H2Connection;
import com.contact_system.model.entities.telephone.Telephone;
import com.contact_system.model.entities.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    Connection connection;

    public UserDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(User user) {
        String getUserQuery = "INSERT INTO tb_user (name, password) VALUES (?, ?)";

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(getUserQuery);

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPassword());

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

            User authenticatedUser = new User(
                    resultSet.getString("name"),
                    resultSet.getString("password")
            );

            System.out.println("Successful authentication");
            return authenticatedUser;
        } catch (SQLException e) {
            throw new DbException("Error authenticating user: " + e.getMessage());
        } finally {
            H2Connection.closeStatement(preparedStatement);
            H2Connection.closeResultSet(resultSet);
        }
    }

    @Override
    public void addTelephone(String phoneNumber, String userName) {
        System.out.println("Adding telephone " + phoneNumber + " for user " + userName);

        String getUserQuery = "SELECT * FROM tb_user WHERE name = ?";
        String addUserQuery = "INSERT INTO tb_telephone (user_id, number) VALUES (?, ?)";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(getUserQuery);
            preparedStatement.setString(1, userName);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DbException("Error adding telephone: user not found!");
            }

            int userId = resultSet.getInt("id");

            preparedStatement = connection.prepareStatement(addUserQuery);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, phoneNumber);

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
    public void deleteByNumber(String id) {
        String deleteQuery = "DELETE FROM tb_telephone WHERE number = ?";

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setString(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                throw new DbException("Error deleting telephone: no rows affected!");
            }

            System.out.println("Telephone deleted successfully!");
        } catch (SQLException e) {
            throw new DbException("Error deleting telephone: " + e.getMessage());
        } finally {
            H2Connection.closeStatement(preparedStatement);
        }
    }

    @Override
    public Telephone getTelephoneByNumber(String number) {
        String query = "SELECT u.name, u.password, t.number " +
                "FROM tb_user u " +
                "JOIN tb_telephone t ON u.id = t.user_id " +
                "WHERE t.number = ?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, number);

            resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new DbException("No telephone found with number: " + number);
            }

            User user = new User(
                    resultSet.getString("name"),
                    resultSet.getString("password")
            );

            Telephone telephone = new Telephone(user, resultSet.getString("number"));

            System.out.println("Telephone found successfully!");
            return telephone;
        } catch (SQLException e) {
            throw new DbException("Error finding telephone: " + e.getMessage());
        } finally {
            H2Connection.closeStatement(preparedStatement);
            H2Connection.closeResultSet(resultSet);
        }
    }

    @Override
    public List<Telephone> getAllTelephonesByUser(String userName) {
        String query = "SELECT u.name, u.password, t.number " +
                "FROM tb_user u " +
                "JOIN tb_telephone t ON u.id = t.user_id " +
                "WHERE u.name = ?";

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Telephone> telephones = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("password")
                );

                Telephone telephone = new Telephone(user, resultSet.getString("number"));
                telephones.add(telephone);
            }

            if (telephones.isEmpty()) {
                throw new DbException("No telephones found for user: " + userName);
            }

            System.out.println("Telephones found successfully!");
            return telephones;
        } catch (SQLException e) {
            throw new DbException("Error finding telephones: " + e.getMessage());
        } finally {
            H2Connection.closeStatement(preparedStatement);
            H2Connection.closeResultSet(resultSet);
        }
    }
}
