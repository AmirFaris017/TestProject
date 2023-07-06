package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.heroku.java.Model.Users;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsersController {
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/registerAcc")
    public String registerAcc(HttpSession session, @ModelAttribute("register") Users user, Model model) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO users_ds(username, email, password, address) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, passwordEncoder.encode(user.getPassword()));
            statement.setString(4, user.getAddress());

            statement.executeUpdate();
            connection.close();

            return "redirect:/login";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();

            return "redirect:/";
        } catch (Exception e) {
            System.out.println("E message : " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/customerdetails")
    public String viewProfile(HttpSession session, @ModelAttribute("customerdetails") Users user, Model model) {
       
        String username = (String) session.getAttribute("username");
        int userid = (int) session.getAttribute("userId");

        System.out.println("id customer details : " + userid);

        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT username, password, email, address FROM users_ds WHERE users_ds.usersid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userid);
            System.out.println("int : " + userid);
            ResultSet resultSet = statement.executeQuery();

            
            while (resultSet.next()) {
                username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                System.out.println("fullname from db: " + username);

                Users customerdetails = new Users(username, password, email, address);
                // usr.add(customerdetails);
                model.addAttribute("customerdetails", customerdetails);
                System.out.println("fullname " + customerdetails.getUsername());
                // Return the view name for displaying customer details --debug
                System.out.println("Session customerdetails : " + model.getAttribute("customerdetails"));
            }
            // model.addAttribute("users_db", usr);
            return "customer/customerdetails";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "login";
    }

    // Update Profile Customer
    @PostMapping("/updateprofile")
    public String updateProfile(HttpSession session, @ModelAttribute("customerdetails") Users user, Model model) {
        int userid = (int) session.getAttribute("userId");
        System.out.println("id customer update : " + userid);

        String username = user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();
        String address = user.getAddress();
        
        try {
            Connection connection = dataSource.getConnection();
            String sql = "UPDATE users_ds SET username=?, password=?, email=?, address=? WHERE usersid=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, passwordEncoder.encode(password));
            statement.setString(3, email);
            statement.setString(4, address);
            statement.setInt(5, userid);
            statement.executeUpdate();
            

            System.out.println("debug= " + user.getUsername() + " " + user.getEmail() + " " + user.getPassword());
            
            String returnPage = "customer/customerdetails";
            return returnPage;

        } catch (Throwable t) {
            System.out.println("message : " + t.getMessage());
            System.out.println("error");
            return "redirect:/login";
        }
    }

    @PostMapping("/deleteprofile")
    public String deleteProfile(HttpSession session,Model model) {
    String username = (String) session.getAttribute("username");
     int userid = (int) session.getAttribute("userId");

    
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.prepareStatement("DELETE FROM users_ds WHERE users_ds.usersid=?");
            statement.setInt(1, userid);
            
            // Execute the delete statement
            statement.executeUpdate();


        } catch (SQLException e) {
            // Handle any potential exceptions (e.g., log the error, display an error page)
            e.printStackTrace();
            return "redirect:/deleteError";
        }
    

    // Username is null, handle accordingly (e.g., redirect to an error page)
    return "redirect/deleteError";
}
}
