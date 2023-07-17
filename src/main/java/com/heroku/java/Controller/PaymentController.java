package com.heroku.java.Controller;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.heroku.java.Model.Homestay;
import com.heroku.java.Model.Payment;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentController{
    private DataSource dataSource;
    

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/payment")
    public String payment(HttpSession session,@RequestParam("receipt")MultipartFile receipt,@ModelAttribute("payment")Payment payment){
        
        int bookId = (int) session.getAttribute("bookId");
        // session.setAttribute("bookId", bookId);
        System.out.println("bookId: "+bookId);

        try{
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO payment(bookid,receipt) VALUES(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookId);
            statement.setBytes(2, receipt.getBytes());
            statement.executeUpdate();
            connection.close();

            return "redirect:/cusbook";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    
    }



