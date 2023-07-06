package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.Model.Admin;
import com.heroku.java.Model.Users;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
    private final DataSource dataSource;

    @Autowired
    public LoginController(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Autowired 
    private BCryptPasswordEncoder passwordEncoder;

  
    // @GetMapping("/")
    // public String index (HttpSession session){
    //     if (session.getAttribute("username") != null) {
    //         return "redirect:/dashboard";
    //       } else {
    //         System.out.println("Session expired or invalid...");
    //         return "index";
    //       }
    // }

    // @GetMapping("/login")
    //  public String displayLogin(HttpSession session,
    //   @RequestParam(value = "error", defaultValue = "false") boolean loginError) {
    //     if (session.getAttribute("username") != null) {
    //       return "redirect:/login";
    // }   else {
    //       System.out.println("Session expired or invalid...");
    //       return "index";
    // }
    //   }

    @PostMapping("/login")
    String homepage(HttpSession session, @ModelAttribute("login") Users user,Admin admin,
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
}