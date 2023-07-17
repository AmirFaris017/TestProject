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

import com.heroku.java.Model.Admin;
import com.heroku.java.Model.Users;

import jakarta.servlet.http.HttpSession;

public class CustomerController {
    private DataSource dataSource;
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

     @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/registerAcc")
    public String registerAcc(HttpSession session, @ModelAttribute("register") Users user) {
        try {
            Connection connection = dataSource.getConnection();
            String sql1 = "INSERT INTO users_ds(username, email, password, address) VALUES (?,?,?,?)";
            PreparedStatement statement1 = connection.prepareStatement(sql1);
            statement1.setString(1, user.getUsername());
            statement1.setString(2, user.getEmail());
            statement1.setString(3, passwordEncoder.encode(user.getPassword()));
            statement1.setString(4, user.getAddress());
            statement1.setString(5, user.getPhoneNo());

            statement1.executeUpdate();
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

            Users customerdetails = new Users( username, password, email, address,null);
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

}
