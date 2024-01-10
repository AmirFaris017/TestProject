// package com.heroku.DAO;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// import javax.sql.DataSource;

// public class LoginDAO {
    
//     private DataSource dataSource;

//     public LoginDAO(DataSource dataSource) {
//         this.dataSource = dataSource;
//     }

//     public boolean checkAdmin(String email, String password) throws SQLException {
//         try (Connection connection = dataSource.getConnection()) {
//             String sql = "SELECT COUNT(*) FROM admin WHERE adminemail = ? AND adminpassword = ?";
//             PreparedStatement statement = connection.prepareStatement(sql);
//             statement.setString(1, email);
//             statement.setString(2, password);
//             ResultSet resultSet = statement.executeQuery();
//             if (resultSet.next()) {
//                 int rowCount = resultSet.getInt(1);
//                 return rowCount > 0;
//             }
//             connection.close();
//         } catch (SQLException e) {
//             throw e;
//         }
//         return false;
//     }

//     public boolean checkCustomer(String email, String password) throws SQLException {
//         try (Connection connection = dataSource.getConnection()) {
//             String sql = "SELECT COUNT(*) FROM users_ds WHERE email = ? AND password = ?";
//             PreparedStatement statement = connection.prepareStatement(sql);
//             statement.setString(1, email);
//             statement.setString(2, password);
//             ResultSet resultSet = statement.executeQuery();
//             if (resultSet.next()) {
//                 int rowCount = resultSet.getInt(1);
//                 return rowCount > 0;
//             }
//             connection.close();
//         } catch (SQLException e) {
//             throw e;
//         }
//         return false;
//     }
//         public int getCustomerId(String email) throws SQLException {
//         try (Connection connection = dataSource.getConnection()) {
//             String sql = "SELECT userid FROM users_ds WHERE email = ?";
//             PreparedStatement statement = connection.prepareStatement(sql);
//             statement.setString(1, email);
//             ResultSet resultSet = statement.executeQuery();
//             if (resultSet.next()) {
//                 return resultSet.getInt("userid");
//             }
//             connection.close();
//         } catch (SQLException e) {
//             throw e;
//         }
//         return 0;
//     }
    
// }
