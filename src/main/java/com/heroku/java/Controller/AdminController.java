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

import com.heroku.DAO.AdminDAO;
import com.heroku.java.Model.Admin;
import com.heroku.java.Model.Homestay;
import com.heroku.java.Model.Users;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
  private final DataSource dataSource;

    @Autowired
    public AdminController (DataSource dataSource){
      this.dataSource = dataSource;
    }

    @GetMapping("/admindetails")
    public String viewAdminProfile(HttpSession session, Model model) {
        AdminDAO adminDAO = new AdminDAO(dataSource);
        int adminId = (int) session.getAttribute("adminId");
        Admin admin = adminDAO.getAdminById(adminId);
        model.addAttribute("admin", admin);
        return "admin/admindetails";
    }

    @PostMapping("/updateprof")
    public String updateProfile(HttpSession session, @ModelAttribute("admin") Admin admin, Model model) {
        AdminDAO adminDAO = new AdminDAO(dataSource);
        int adminId = (int) session.getAttribute("adminId");
        admin.setAdminid(adminId);
        adminDAO.updateAdmin(admin);
        return "admin/admindetails";
    }
}

