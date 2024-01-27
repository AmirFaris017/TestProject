package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heroku.DAO.BookingDAO;
import com.heroku.java.Model.Booking;
import com.heroku.java.Model.Homestay;
import com.heroku.java.Model.Payment;
import com.heroku.java.Model.Users;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;

@Controller
public class BookingController {

    private final DataSource dataSource;

    @Autowired
    public BookingController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/checkavailability")
    public String checkAvailability(
            @RequestParam("homestayid") int homestayId,
            @RequestParam("startDate") Date startDate,
            @RequestParam("endDate") Date endDate) {
        BookingDAO bookingDAO = new BookingDAO(dataSource);
        return bookingDAO.checkAvailability(homestayId, startDate, endDate);
    }

    @GetMapping("/booking/{homestayid}")
    public String getBooking(@PathVariable("homestayid") int homestayid, Model model,
                             @RequestParam("startDate") Date startDate,
                             @RequestParam("endDate") Date endDate) {
        BookingDAO bookingDAO = new BookingDAO(dataSource);
        return bookingDAO.getBooking(homestayid, model, startDate, endDate);
    }

    @PostMapping("/addbooking")
    public String addBooking(HttpSession session, @RequestParam("homestayid") int homestayid,
                             @RequestParam("startDate1") Date startDate,
                             @RequestParam("endDate1") Date endDate,
                             Model model) {
        BookingDAO bookingDAO = new BookingDAO(dataSource);
        return bookingDAO.addBooking(session, homestayid, startDate, endDate, model);
    }

    @GetMapping("/viewbooking")
    public String viewBooking(HttpSession session, Model model) {
        BookingDAO bookingDAO = new BookingDAO(dataSource);
        return bookingDAO.viewBooking(session, model);
    }

    @GetMapping("/listbooking")
    public String listBooking(Model model) {
        BookingDAO bookingDAO = new BookingDAO(dataSource);
        return bookingDAO.listBooking(model);
    }

    @GetMapping("/approvebooking")
    public String approveBooking(@RequestParam("bookingid") int bookingId, Model model) {
        BookingDAO bookingDAO = new BookingDAO(dataSource);
        return bookingDAO.approveBooking(bookingId, model);
    }

    @GetMapping("/rejectbooking")
    public String rejectBooking(@RequestParam("bookingid") int bookingId, Model model) {
        BookingDAO bookingDAO = new BookingDAO(dataSource);
        return bookingDAO.rejectBooking(bookingId, model);
    }

    @GetMapping("/viewreceipt")
    public String viewReceipt(@RequestParam("bookingid") int bookingId, Model model) {
        BookingDAO bookingDAO = new BookingDAO(dataSource);
        return bookingDAO.viewReceipt(bookingId, model);
    }
}
