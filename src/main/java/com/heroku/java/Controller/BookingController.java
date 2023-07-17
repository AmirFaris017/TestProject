package com.heroku.java.Controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.heroku.java.Model.Payment;
import com.heroku.java.Model.Users;

import jakarta.servlet.http.HttpSession;

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

                Homestay homestay = new Homestay(homestayid, homestayname, homestaylocation, homestayprice, null,
                        homestaydetails, imageSrc);
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
    public String addBooking(HttpSession session, @RequestParam("homestayid") int homestayid, Booking booking,
            Model model) {
        String email = (String) session.getAttribute("email");
        int userId = (int) session.getAttribute("userId");
        Date startDate = booking.getStartDate();
        Date endDate = booking.getEndDate();
        Double totalamount = booking.getTotalamount();
        System.out.println("total amount: " + totalamount);
        if (startDate == null || endDate == null) {
            return "redirect:/error";
        }

        try {
            Connection connection = dataSource.getConnection();

            // Fetch homestay price using homestay ID
            String homestayPriceQuery = "SELECT homestayprice FROM homestay_ds WHERE homestayid = ?";
            PreparedStatement priceStatement = connection.prepareStatement(homestayPriceQuery);
            priceStatement.setInt(1, homestayid);
            ResultSet priceResult = priceStatement.executeQuery();

            double homestayPrice = 0.0;
            if (priceResult.next()) {
                homestayPrice = priceResult.getDouble("homestayprice");
            } else {
                throw new SQLException("Failed to retrieve homestay price.");
            }

            // Calculate the total payment
            long timeDiff = endDate.getTime() - startDate.getTime();
            int daysDiff = (int) (timeDiff / (1000 * 3600 * 24));
            double totalPayment = daysDiff * homestayPrice;

            String sql = "INSERT INTO book(adminid, bookdate, startdate, enddate, homestayid, userid, status, totalamount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setInt(1, 1);
            statement.setDate(2, booking.getBookDate());
            statement.setDate(3, startDate);
            statement.setDate(4, endDate);
            statement.setInt(5, homestayid);
            statement.setInt(6, userId);
            statement.setString(7, "Pending");
            statement.setDouble(8, booking.getTotalamount()); // Set the total payment value
            statement.executeUpdate();
            System.out.println("Total payment below DAO: " + totalPayment);
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int bookId;
            if (generatedKeys.next()) {
                bookId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve the generated book ID.");
            }

            connection.close();

            // Pass the necessary data to the payment page
            model.addAttribute("bookId", bookId);
            model.addAttribute("totalamount", totalamount);
            session.setAttribute("bookId", bookId);

            return "customer/payment";
        } catch (Exception e) {
            System.out.println("E message: " + e.getMessage());
            return "redirect:/";
        }
    }

    @GetMapping("/viewbooking")
    public String viewBooking(HttpSession session, Model model) {
        int userId = (int) session.getAttribute("userId");
        ArrayList<Booking> bookings = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT b.bookid, b.adminid, b.bookdate, b.startdate, b.enddate, b.homestayid, b.userid, b.status, b.totalamount, " +
                         "hs.homestayname, hs.homestaylocation, hs.homestaypic " +
                         "FROM book b " +
                         "JOIN homestay_ds hs ON b.homestayid = hs.homestayid " +
                         "WHERE b.userid = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                int bookingId = resultSet.getInt("bookid");
                int adminId = resultSet.getInt("adminid");
                Date bookDate = resultSet.getDate("bookdate");
                Date startDate = resultSet.getDate("startdate");
                Date endDate = resultSet.getDate("enddate");
                int homestayId = resultSet.getInt("homestayid");
                String status = resultSet.getString("status");
                Double totalAmount = resultSet.getDouble("totalamount");
    
                String homestayName = resultSet.getString("homestayname");
                String homestayLocation = resultSet.getString("homestaylocation");
                byte[] homestayPicBytes = resultSet.getBytes("homestaypic");
                String base64Image = Base64.getEncoder().encodeToString(homestayPicBytes);
                String imageSrc = "data:image/jpeg;base64," + base64Image;
                
                Booking booking = new Booking(bookingId, adminId, bookDate, startDate, endDate, homestayId, userId, status, totalAmount, null,null);
                Homestay homestay = new Homestay(homestayId, homestayName, homestayLocation, null, null, null, imageSrc);
                booking.setHomestay(homestay);
    
                bookings.add(booking);
            }
    
            model.addAttribute("bookings", bookings);
            session.setAttribute("userId", userId);
            return "customer/viewbooking";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/listbooking")
public String listBooking(Model model) {
    ArrayList<Booking> bookings = new ArrayList<>();
    try (Connection con = dataSource.getConnection()) {
        String sql = "SELECT b.bookid, b.adminid, b.bookdate, b.startdate, b.enddate, b.homestayid, b.userid, b.status, b.totalamount, " +
                     "hs.homestayname, hs.homestaylocation, hs.homestaypic, " +
                     "u.username, u.phoneNo " +
                     "FROM book b " +
                     "JOIN homestay_ds hs ON b.homestayid = hs.homestayid " +
                     "JOIN users_ds u ON b.userid = u.userid";

        PreparedStatement statement = con.prepareStatement(sql);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int bookingId = resultSet.getInt("bookid");
            int adminId = resultSet.getInt("adminid");
            Date bookDate = resultSet.getDate("bookdate");
            Date startDate = resultSet.getDate("startdate");
            Date endDate = resultSet.getDate("enddate");
            int homestayId = resultSet.getInt("homestayid");
            int userId = resultSet.getInt("userid");
            String status = resultSet.getString("status");
            Double totalAmount = resultSet.getDouble("totalamount");

            String homestayName = resultSet.getString("homestayname");
            String homestayLocation = resultSet.getString("homestaylocation");
            byte[] homestayPicBytes = resultSet.getBytes("homestaypic");
            String base64Image = Base64.getEncoder().encodeToString(homestayPicBytes);
            String imageSrc = "data:image/jpeg;base64," + base64Image;

            String username = resultSet.getString("username");
            String phoneNo = resultSet.getString("phoneNo");

            Users user = new Users(username, null, null, null,null);
            user.setPhoneNo(phoneNo);

            Booking booking = new Booking(bookingId, adminId, bookDate, startDate, endDate, homestayId, userId, status, totalAmount, null, user);
            Homestay homestay = new Homestay(homestayId, homestayName, homestayLocation, null, null, null, imageSrc);
            booking.setHomestay(homestay);

            bookings.add(booking);
        }


        model.addAttribute("bookings", bookings);
        return "admin/listbooking";
    } catch (SQLException sqe) {
        sqe.printStackTrace();
        return "redirect:/";
    } catch (Exception e) {
        e.printStackTrace();
        return "redirect:/";
    }
}

@GetMapping("/approvebooking")
public String updateStatusBook(@RequestParam("bookingid") int bookingId) throws SQLException {
    try(Connection con = dataSource.getConnection()){
        String sql = "UPDATE book SET status =? WHERE bookid =?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1, "Approve");
        statement.setInt(2, bookingId);
        statement.executeUpdate();

        return "redirect:/listbooking";
    }catch (SQLException e) {
         e.printStackTrace();
         return "redirect:/";
        }
    
}
@GetMapping("/rejectbooking")
public String rejectbooking(@RequestParam("bookingid") int bookingId) throws SQLException {
    try(Connection con = dataSource.getConnection()){
        String sql = "UPDATE book SET status =? WHERE bookid =?";
        PreparedStatement statement = con.prepareStatement(sql);
        statement.setString(1, "Rejected");
        statement.setInt(2, bookingId);
        statement.executeUpdate();

        return "redirect:/listbooking";
    }catch (SQLException e) {
         e.printStackTrace();
         return "redirect:/";
        }
    
}
}


    
