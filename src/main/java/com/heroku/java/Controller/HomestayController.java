package com.heroku.java.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;

import com.heroku.DAO.HomestayDAO;
import com.heroku.java.Model.Homestay;
import jakarta.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

@Controller
public class HomestayController {

    private final DataSource dataSource;

    @Autowired
    public HomestayController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/addhomestay")
    public String addHomestay(HttpSession session, @RequestParam("homestaypic") MultipartFile homestayPic,
                               @ModelAttribute("addhomestay") Homestay homestay) {
        HomestayDAO homestayDAO = new HomestayDAO(dataSource);
        return homestayDAO.addHomestay(session, homestayPic, homestay);
    }

    @GetMapping("/adminbook")
    public String viewhomestay(HttpSession session, Model model) {
        HomestayDAO homestayDAO = new HomestayDAO(dataSource);
        return homestayDAO.viewHomestay(session, model);
    }

    @GetMapping("/cusbook")
    public String customerBook(HttpSession session, Model model) {
        HomestayDAO homestayDAO = new HomestayDAO(dataSource);
        return homestayDAO.customerBook(session, model);
    }

    @GetMapping("/viewhome")
    public String viewHomestay(@RequestParam("homestayid") int homestayid, Model model) {
        HomestayDAO homestayDAO = new HomestayDAO(dataSource);
        return homestayDAO.viewHomeStay(homestayid, model);
    }

    @GetMapping("/viewhomestay")
    public String viewHomestayDetail(@RequestParam("homestayid") int homestayid, Model model) {
        HomestayDAO homestayDAO = new HomestayDAO(dataSource);
        return homestayDAO.viewHomestayDetail(homestayid, model);
    }

    @PostMapping("/updatehomestay/{homestayid}")
    public String updateHomestay(HttpSession session, @ModelAttribute("homestay") Homestay homestay, Model model, @PathVariable("homestayid") int homestayid) {
        HomestayDAO homestayDAO = new HomestayDAO(dataSource);
        return homestayDAO.updateHomestay(session, homestay, model, homestayid);
    }

    @GetMapping("/deletehomestay")
    public String deleteHomestay(@RequestParam("homestayid") int homestayid) {
        HomestayDAO homestayDAO = new HomestayDAO(dataSource);
        return homestayDAO.deleteHomestay(homestayid);
    }
}
