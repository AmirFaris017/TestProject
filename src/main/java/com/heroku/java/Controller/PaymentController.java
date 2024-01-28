package com.heroku.java.Controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.heroku.DAO.PaymentDAO;
import com.heroku.java.Model.Homestay;
import com.heroku.java.Model.Payment;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController {
    private final DataSource dataSource;

    @Autowired
    public PaymentController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/payment")
    public String addPayment(HttpSession session, @RequestParam("receipt") MultipartFile receipt,
            @ModelAttribute("payment") Payment payment) throws IOException {
        int bookId = (int) session.getAttribute("bookId");
        PaymentDAO paymentDAO = new PaymentDAO(dataSource);
        try {
            paymentDAO.addPayment(bookId, receipt);
            return "redirect:/viewbooking";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }
}



