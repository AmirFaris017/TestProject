package com.heroku.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.apache.logging.log4j.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import javax.sql.DataSource;
import java.sql.Connection;
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

    @GetMapping("cusbook")
    public String cusbook(){
        return "customer/cusbook";
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
