package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.heroku.java.Model.Homestay;


import jakarta.servlet.http.HttpSession;

public class HomestayController {
    private DataSource dataSource;

    /**
     * @param dataSource
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping
    public String registerAcc(HttpSession session, @ModelAttribute("addhomestay") Homestay homestay, Model model){
         try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO homestay(homestayName,homestayLocation,homestayPrice,homestayPic) VALUES (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, homestay.getHomestayName());
            statement.setString(2, homestay.getHomestayLocation());
            statement.setDouble(3, homestay.getHomestayPrice());
            statement.setString(4, homestay.getHomestayPic());

            statement.executeUpdate();
            connection.close();

            return "addhomestay";
    }catch (SQLException sqe) {
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
}