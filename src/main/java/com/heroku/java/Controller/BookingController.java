package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.Model.Booking;
import com.heroku.java.Model.Homestay;


@Controller
public class BookingController {
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/booking/{homestayid}")
    public String getBooking(@PathVariable("homestayid") int homestayid, Model model) {
    try {
        Connection connection = dataSource.getConnection();
        String sql = "SELECT homestayname, homestaylocation, homestayprice, homestaydetails, homestaypic FROM homestay_ds WHERE homestayid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, homestayid);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String homestayname = resultSet.getString("homestayname");
            String homestaylocation = resultSet.getString("homestaylocation");
            double homestayprice = resultSet.getDouble("homestayprice");
            String homestaydetails = resultSet.getString("homestaydetails");
            byte[] homestayPicBytes = resultSet.getBytes("homestaypic");
            String base64Image = Base64.getEncoder().encodeToString(homestayPicBytes);
            String imageSrc = "data:image/jpeg;base64," + base64Image;

            Homestay homestay = new Homestay(homestayid, homestayname, homestaylocation, homestayprice, null, homestaydetails, imageSrc);
            model.addAttribute("homestay", homestay);
        }

        connection.close();
        return "customer/booking";
    } catch (Throwable t) {
        System.out.println("Error: " + t.getMessage());
        return "index";
    }
}

@PostMapping("/addbooking")
public String addBooking(
    @RequestParam("homestayid") int homestayid, Booking booking, Model model) {

    Date startdate = booking.getStartDate();
    Date enddate = booking.getEndDate();
    
    if (startdate == null || enddate == null) {
        // Handle the case when start date or end date is null
        // You can display an error message or redirect the user to an error page
        return "redirect:/error";
    }
    
    try {
        Connection connection = dataSource.getConnection();
        String sql = "INSERT INTO book(adminId, startDate, endDate, homestayid, userid, status) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setInt(1, 1);
        statement.setDate(2,booking.getStartDate());
        statement.setDate(3, booking.getEndDate());
        statement.setInt(4, homestayid);
        statement.setInt(5, booking.getUserid());
        statement.setString(6, "Pending");
        statement.executeUpdate();
        connection.close();
        return "redirect:/cusdashboard";
    } catch (Exception e) {
        System.out.println("E message: " + e.getMessage());
        return "redirect:/";
    }
}


}