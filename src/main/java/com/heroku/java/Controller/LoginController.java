package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.Model.Admin;
import com.heroku.java.Model.Customer;
import com.heroku.java.Model.Users;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
  private final DataSource dataSource;

  @Autowired
  public LoginController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;

  @PostMapping("/login")
  String homepage(HttpSession session, @ModelAttribute("login") Users user, @ModelAttribute("admin") Admin admin,
       Model model) {
    // System.out.println("Login Error Param: " + loginError);
    try (Connection connection = dataSource.getConnection()) {
      final var statement = connection.createStatement();
      System.out.println("Connected");
  
      final var userQuery = "SELECT * FROM customer WHERE email = ?";
      final var adminQuery = "SELECT * FROM admin WHERE adminemail = ?";
      
      PreparedStatement userStatement = connection.prepareStatement(userQuery);
      userStatement.setString(1, user.getEmail());
      
      PreparedStatement adminStatement = connection.prepareStatement(adminQuery);
      adminStatement.setString(1, admin.getEmail());
  
      ResultSet userResultSet = userStatement.executeQuery();
      ResultSet adminResultSet = adminStatement.executeQuery();
  
      String returnPage = "";
      
      if (userResultSet.next()) {
        String pwd = userResultSet.getString("password");
        int userId = userResultSet.getInt("userid");
  
        if (pwd.matches(user.getPassword())) {
          // Passwords match
          session.setAttribute("username", user.getUsername());
          session.setAttribute("userId", userId);
          session.setMaxInactiveInterval(1440 * 60);
          System.out.println("User ID: " + session.getAttribute("userId"));
          model.addAttribute("loginSuccess", true);
          returnPage = "customer/cusdashboard";
      } else {
          // Passwords don't match
          System.out.println("invalid password");
          model.addAttribute("error", true);//tukar
          returnPage = "index";
      }
      
      } else if (adminResultSet.next()) {
        String pwd = adminResultSet.getString("adminpassword");
        int adminId = adminResultSet.getInt("adminId");
  
        if (pwd.matches(admin.getPassword())) {
          session.setAttribute("username", admin.getName());
          session.setAttribute("adminId", adminId);
          session.setMaxInactiveInterval(1440 * 60);
          System.out.println("Admin ID: " + session.getAttribute("adminId"));
          model.addAttribute("loginSuccess", true);
          returnPage = "admin/admindashboard";
        } else {
          System.out.println("Invalid email or password");
          model.addAttribute("error", true);//tukar
          returnPage = "index"; 
        }
      } else {
        model.addAttribute("invalidEmail", true);
        model.addAttribute("error", true);
        returnPage = "redirect:/";
      }
      
      connection.close();
      return returnPage;
  
    } catch (Throwable t) {
      System.out.println("message: " + t.getMessage());
      return "index";
    }
  }
  
  }
