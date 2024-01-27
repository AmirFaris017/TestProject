package com.heroku.java.Controller;

import com.heroku.DAO.CustomerDAO;
import com.heroku.java.Model.Customer;
import com.heroku.java.Model.Users;


import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CustomerController {
    private final DataSource dataSource;

    @Autowired
    public CustomerController(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @PostMapping("/registerAcc")
    public String registerAccount(@ModelAttribute("register") Users user) {
        CustomerDAO customerDAO = new CustomerDAO(dataSource);
        customerDAO.registerUser(user);
        return "redirect:/login";
    }

    @GetMapping("/customerdetails")
    public String viewProfile(HttpSession session, Model model) {
        CustomerDAO customerDAO = new CustomerDAO(dataSource);
        int userId = (int) session.getAttribute("userId");
        Users user = customerDAO.getUserById(userId);
        model.addAttribute("customerdetails", user);
        return "customer/customerdetails";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully!");
        return "redirect:/"; 
    }

    @GetMapping("/deleteprofile")
    public String deleteAccount(HttpSession session) {
        CustomerDAO customerDAO = new CustomerDAO(dataSource);
        int userId = (int) session.getAttribute("userId");
        customerDAO.deleteUserById(userId);
        return "redirect:/";
    }

    @PostMapping("/updateprofile")
    public String updateAccount(HttpSession session, @ModelAttribute("customerdetails") Customer customer) {
        CustomerDAO customerDAO = new CustomerDAO(dataSource);
        int userId = (int) session.getAttribute("userId");
        customer.setCustomerid(userId);
        customerDAO.updateUser(customer);
        return "customer/customerdetails";
    }
}
