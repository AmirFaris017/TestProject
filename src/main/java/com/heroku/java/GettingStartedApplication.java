package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import com.heroku.DAO.LoginDAO;

import jakarta.servlet.http.HttpSession;

import org.apache.logging.log4j.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@Controller
public class GettingStartedApplication {
    private final DataSource dataSource;

    Logger logger = LogManager.getLogger(GettingStartedApplication.class);

    @Autowired
    public GettingStartedApplication(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @GetMapping("/homestay")
    public String homestay(){
        return "homestay";
    }

    // @GetMapping("/")
    // public String index() {
    //     return "index";
    // }
    @GetMapping("/about")
    public String about(){
        return "about";
    }

    
    

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

    @GetMapping("/signUp")
    public String signUp() {
        return "signUp";
    }   

    @GetMapping("/login")
    public String login() {
        return "login";
    }   

    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }   
    @GetMapping("/cusdashboard")
    public String cusdashboard(){
        return "customer/cusdashboard";
    }

    // @GetMapping("customerDetails")
    // public String customerdetails(){
    //     return "customer/customerdetails";
    // }

    @GetMapping("admindashboard")
    public String admindashboard(){
        return "admin/admindashboard";
    }

    @GetMapping("cusabout")
    public String cusabout(){
        return "customer/cusabout";
    }

    @GetMapping("adminabout")
    public String adminabout(){
        return "admin/adminabout";
    }

    @GetMapping("addhomestay")
    public String addhomestay(){
        return "admin/addhomestay";
    }

    @GetMapping("homestayA")
    public String homestayA(){
        return "customer/homestayA";
    }

    @GetMapping("homestayB")
    public String homestayB(){
        return "customer/homestayB";
    }

    @GetMapping("homestayC")
    public String homestayC(){
        return "customer/homestayC";
    }

    @GetMapping("payment")
    public String payment(){
        return "customer/payment";
    }

    
    @GetMapping("userdetails")
    public String userdetails(){
        return "admin/userdetails";
    }
    
//     @PostMapping("/login")
// public String login(HttpSession session, @RequestParam("email") String email,
//                     @RequestParam("password") String password, Model model) {
//     System.out.println(email);
//     try {
//         LoginDAO loginDAO = new LoginDAO(dataSource);
        
//         boolean isCustomer = loginDAO.checkCustomer(email, password);
//         boolean isAdmin = loginDAO.checkAdmin(email, password);
        
//         if (isCustomer) {
//             int userid = loginDAO.getCustomerId(email); // Retrieve the userid from the database
//             session.setAttribute("email", email);
//             session.setAttribute("userid", userid);
//             return "redirect:/cusdashboard"; // Replace with the appropriate customer home page URL
//         } else if (isAdmin) {
//             session.setAttribute("email", email);
//             return "redirect:/admindashboard";
//         } else {
//             System.out.println("Invalid email or password");
//             model.addAttribute("error", true); 
//             return "login"; 
//         }
//     } catch (SQLException e) {
//         e.printStackTrace();
//         model.addAttribute("error", true); 
//         return "login";
//     }
// }

    

    @GetMapping("/database")
    String database(Map<String, Object> model) {
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)");
            statement.executeUpdate("INSERT INTO ticks VALUES (now())");

            final var resultSet = statement.executeQuery("SELECT tick FROM ticks");
            final var output = new ArrayList<>();
            while (resultSet.next()) {
                output.add("Read from DB: " + resultSet.getTimestamp("tick"));
            }

            model.put("records", output);
            return "database";

        } catch (Throwable t) {
            model.put("message", t.getMessage());
            return "error";
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(GettingStartedApplication.class, args);
    }
}
