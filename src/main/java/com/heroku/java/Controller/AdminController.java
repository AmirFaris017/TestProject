package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.Model.Users;

import jakarta.servlet.http.HttpSession;

public class AdminController {
     private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

     @Autowired
    private BCryptPasswordEncoder passwordEncoder;

     @PostMapping("/login")
     String homepage(HttpSession session, @ModelAttribute("user") Users user,
        @RequestParam(value = "error", defaultValue = "false") boolean loginError, Model model) {
      System.out.println("Login Error PAram : " + loginError);
      try (Connection connection = dataSource.getConnection()) {
        final var statement = connection.createStatement();
  
        final var resultSet = statement.executeQuery("SELECT * FROM users_ds;");
        
        String returnPage = "";
  
        while (resultSet.next()) {
          String pwd = resultSet.getString("password");
          String email = resultSet.getString("email");
          String name = resultSet.getString("username");
          String address = resultSet.getString("address");
          int userId = resultSet.getInt("usersid");

          System.out.println(pwd + email + userId);

          if (user.getUsername() != "" && user.getPassword() != "") {
            if (email.equals(user.getEmail()) && passwordEncoder.matches(user.getPassword(), pwd)) {
  
              session.setAttribute("username", user.getUsername());
              session.setAttribute("userId", userId);
              session.setMaxInactiveInterval(1440 * 60);
              System.out.println("user id : " + session.getAttribute("userId"));
              returnPage = "redirect:/cusdashboard";
              break;
            } else {
              returnPage = "redirect:/login?error=true";
            }
  
          } else {
            returnPage = "redirect:/login?error=true";
  
          }
        }
        connection.close();
        return returnPage;
  
      } catch (Throwable t) {
        System.out.println("message : " + t.getMessage());
        return "index";
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
                System.out.println("Session admindetails : " + model.getAttribute("customerdetails"));
            }
            // model.addAttribute("users_db", usr);
            return "customer/admindetails";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "login";
    }
}
