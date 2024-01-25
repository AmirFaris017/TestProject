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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            String sql = "INSERT INTO users_ds(username, email, password, address,phoneno) VALUES (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getAddress());
            statement.setString(5, user.getPhoneNo());

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
    public String viewprofile(HttpSession session, @ModelAttribute("customerdetails") Users user, Model model) {
        int userid = (int) session.getAttribute("userId");
        String email = (String) session.getAttribute("email");
        
        try {
            Connection connection = dataSource.getConnection();
            String sql = "SELECT userid,username, password, email, address FROM users_ds WHERE users_ds.userid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userid);
            System.out.println("int : " + userid);
            ResultSet resultSet = statement.executeQuery();

            
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                email = resultSet.getString("email");
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

    // Update Profile Customer
    @PostMapping("/updateprofile")
    public String updateProfile(HttpSession session, @ModelAttribute("customerdetails") Users user, Model model) {
        int userid = (int) session.getAttribute("userId");
        String email = (String) session.getAttribute("email");
        System.out.println("id customer update : " + userid);

        String username = user.getUsername();
        String password = user.getPassword();
        email = user.getEmail();
        String address = user.getAddress();
        
        try {
            Connection connection = dataSource.getConnection();
            String sql = "UPDATE users_ds SET username=?, password=?, email=?, address=? WHERE userid=?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, username);
            statement.setString(2, password);
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

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        // Invalidate the session
        session.invalidate();

        // Redirect to the login page or any other desired page
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully!");
        return "redirect:/"; // Update with your login page URL
    }
    @GetMapping("/deleteprofile")
    public String deleteProfile(HttpSession session,Model model) {
    int userid = (int) session.getAttribute("userId");
    String email = (String) session.getAttribute("email");

    
        try (Connection connection = dataSource.getConnection()) {
            final var statement = connection.prepareStatement("DELETE FROM users_ds WHERE userid=?");
            statement.setInt(1, userid);
            
            // Execute the delete statement
            statement.executeUpdate();
            return "redirect:/";


        } catch (SQLException e) {
            // Handle any potential exceptions (e.g., log the error, display an error page)
            e.printStackTrace();
            return "redirect:/deleteError";
        }
}
}
