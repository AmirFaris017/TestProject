package com.heroku.DAO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.heroku.java.Model.Customer;
import com.heroku.java.Model.Users;

public class CustomerDAO {
    private DataSource dataSource;

    public CustomerDAO (DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void registerUser(Users user) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO customer(username, email, password, address) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getAddress());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Users getUserById(int userId) {
        Users user = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT username, password, email, address FROM customer WHERE customer.userid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                user = new Users(username, password, email, address, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public void deleteUserById(int userId) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "DELETE FROM customer WHERE userid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUser(Customer customer) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE customer SET username=?, password=?, email=?, address=? WHERE userid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, customer.getUsername());
            statement.setString(2, customer.getPassword());
            statement.setString(3, customer.getEmail());
            statement.setString(4, customer.getAddress());
            statement.setInt(5, customer.getCustomerid());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
