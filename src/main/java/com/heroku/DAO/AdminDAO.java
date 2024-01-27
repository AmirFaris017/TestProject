package com.heroku.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.heroku.java.Model.Admin;

public class AdminDAO {
     private DataSource dataSource;


    public AdminDAO (DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Admin getAdminById(int adminId) {
        Admin admin = null;
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT adminname, adminemail, adminpassword, adminaddress FROM admin WHERE admin.adminid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, adminId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("adminname");
                String password = resultSet.getString("adminpassword");
                String email = resultSet.getString("adminemail");
                String address = resultSet.getString("adminaddress");
                admin = new Admin(name, password, email, address, null);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    public void updateAdmin(Admin admin) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "UPDATE admin SET adminname=?, adminpassword=?, adminemail=?, adminaddress=? WHERE adminid=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, admin.getName());
            statement.setString(2, admin.getPassword());
            statement.setString(3, admin.getEmail());
            statement.setString(4, admin.getAddress());
            statement.setInt(5, admin.getAdminid());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
