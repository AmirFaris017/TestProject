package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.Model.Admin;
import com.heroku.java.Model.Homestay;
import com.heroku.java.Model.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
  private DataSource dataSource;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Autowired
  private BCryptPasswordEncoder passwordEncoder;


  @GetMapping("/admindetails")
  public String viewAdminProfile(HttpSession session, Model model,Admin admin) {
    int adminId = (int) session.getAttribute("adminId");
    System.out.println("masuk admindetails ");
    try (Connection connection = dataSource.getConnection()) {
      String sql = "SELECT adminname, adminemail, adminpassword, adminaddress FROM admin WHERE admin.adminid = ?";
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setInt(1, adminId);
      ResultSet resultSet = statement.executeQuery();

      if (resultSet.next()) {
        String name = resultSet.getString("adminname");
        String password = resultSet.getString("adminpassword");
        String email = resultSet.getString("adminemail");
        String address = resultSet.getString("adminaddress");

        admin = new Admin(name, password, email, address, null);
        model.addAttribute("admin", admin);
        System.out.println(name + address + password + email);
      }

      connection.close();

      return "admin/admindetails";
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return "login";
  }

}
