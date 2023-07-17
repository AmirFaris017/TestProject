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

import com.heroku.java.Model.Admin;
import com.heroku.java.Model.Homestay;
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
  
        final var resultSet = statement.executeQuery("SELECT * FROM admin;");
        
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
              returnPage = "redirect:/admindashboard";
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
    @GetMapping("/admindetails")
    public String viewProfile(HttpSession session, @ModelAttribute("admindetails") Admin admin, Model model) {
       
        String username = (String) session.getAttribute("username");
        int adminid = (int) session.getAttribute("adminId");

        System.out.println("id customer details : " + adminid);

        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT adminname, adminemail, adminpassword, adminaddress FROM admin WHERE admin.adminid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, adminid);
            System.out.println("int : " + adminid);
            ResultSet resultSet = statement.executeQuery();

            
            while (resultSet.next()) {
                username = resultSet.getString("adminname");
                String password = resultSet.getString("adminpassword");
                String email = resultSet.getString("adminemail");
                String address = resultSet.getString("adminaddress");
                
                System.out.println("fullname from db: " + username);

                Admin admindetails = new Admin(username, password, email, address,null);
                // usr.add(customerdetails);
                model.addAttribute("admindetails", admindetails);
                System.out.println("fullname " + admindetails.getUsername());
                // Return the view name for displaying customer details --debug
                System.out.println("Session admindetails : " + model.getAttribute("admindetails"));
            }
            // model.addAttribute("users_db", usr);
            return "customer/admindetails";

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return "login";
    }

    

}
