package com.heroku.DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;

import javax.sql.DataSource;
import org.springframework.ui.Model;

import com.heroku.java.Model.Booking;
import com.heroku.java.Model.Homestay;
import com.heroku.java.Model.Users;

import jakarta.servlet.http.HttpSession;


public class BookingDAO {
    private DataSource dataSource;

    public BookingDAO (DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String checkAvailability(int homestayId, Date startDate, Date endDate) {
        
        String book = null;
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("startDate" + startDate);
            System.out.println("endDate" + endDate);
            // @DateTimeFormat(pattern = "yyyy-MM-dd")
            // @DateTimeFormat(pattern = "yyyy-MM-dd")
            // java.sql.Date sD = Date.valueOf(startDate);
            // java.sql.Date eD = Date.valueOf(endDate);
            // Query to check if there are any bookings for the given homestay and date
            // range
            String checkAvailabilitySql = "SELECT COUNT(*) FROM book WHERE homestayid = ? " +
                    "AND ((startdate <= ? AND enddate >= ?) OR (startdate <= ? AND enddate >= ?) OR (startdate >= ? AND enddate <= ?))";

            try (PreparedStatement statement = connection.prepareStatement(checkAvailabilitySql)) {
                statement.setInt(1, homestayId);
                statement.setDate(2, startDate);
                statement.setDate(3, startDate);
                statement.setDate(4, endDate);
                statement.setDate(5, endDate);
                statement.setDate(6, startDate);
                statement.setDate(7, endDate);

                ResultSet resultSet = statement.executeQuery();

                // If count is greater than 0, there is a booking for the given date range
                boolean status = resultSet.next() && resultSet.getInt(1) == 0;
                System.out.println("success status : " + status);

                if (status) {
                    return "redirect:/booking/" + homestayId + "?startDate=" + startDate + "&endDate=" + endDate;

                } else {
                    return "redirect:/viewhome?homestayid=" + homestayId + "&status="+ String.valueOf(status);
                }

            }
            // return true; //dummy

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception appropriately (e.g., log the error, return false)
            System.out.println(false);

        }
        
        return  book;// Return appropriate view name or redirect URL
    }

    public String getBooking(int homestayid, Model model, Date startDate, Date endDate) {
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
                // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                // java.util.Date parsedDate = dateFormat.parse(startDate);
                // java.util.Date parsedDate2 = dateFormat.parse(endDate);
                // java.sql.Date sDate = new java.sql.Date(parsedDate.getTime());
                // java.sql.Date eDate =new java.sql.Date(parsedDate2.getTime());

                System.out.println("sDate : " + startDate);

                long timeDiff = endDate.getTime() - startDate.getTime();
                int day = (int) (timeDiff / (1000 * 3600 * 24));

                Booking booking = new Booking(startDate, endDate, day);

                model.addAttribute("bookingDate", booking);
            }
            connection.close();
            return "customer/booking";
        } catch (Throwable t) {
            System.out.println("Error: " + t.getMessage());
            return "index";
        }
    }

    public String addBooking(HttpSession session, int homestayid, Date startDate, Date endDate, Model model) {
       
        String email = (String) session.getAttribute("email");
        int userId = (int) session.getAttribute("userId");

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
            LocalDate currentDate = LocalDate.now();
            statement.setInt(1, 1);
            statement.setDate(2, Date.valueOf(currentDate));
            statement.setDate(3, startDate);
            statement.setDate(4, endDate);
            statement.setInt(5, homestayid);
            statement.setInt(6, userId);
            statement.setString(7, "Pending");
            statement.setDouble(8, totalPayment); // Set the total payment value
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
            model.addAttribute("totalamount", totalPayment);
            session.setAttribute("bookId", bookId);

            return "customer/payment";
        } catch (Exception e) {
            System.out.println("E message: " + e.getMessage());
            return "redirect:/";
        }
        
    }

    public String viewBooking(HttpSession session, Model model) {
        int userId = (int) session.getAttribute("userId");
        ArrayList<Booking> bookings = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT b.bookid, b.adminid, b.bookdate, b.startdate, b.enddate, b.homestayid, b.userid, b.status, b.totalamount, "
                    +
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

                Booking booking = new Booking(bookingId, adminId, bookDate, startDate, endDate, homestayId, userId,
                        status, totalAmount, null, null);
                Homestay homestay = new Homestay(homestayId, homestayName, homestayLocation, null, null, null,
                        imageSrc);
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

    public String listBooking(Model model) {
        ArrayList<Booking> bookings = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT b.bookid, b.adminid, b.bookdate, b.startdate, b.enddate, b.homestayid, b.userid, b.status, b.totalamount, "
                    +
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

                Users user = new Users(username, null, null, null, null);
                user.setPhoneNo(phoneNo);

                Booking booking = new Booking(bookingId, adminId, bookDate, startDate, endDate, homestayId, userId,
                        status, totalAmount, null, user);
                Homestay homestay = new Homestay(homestayId, homestayName, homestayLocation, null, null, null,
                        imageSrc);
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

    public String approveBooking(int bookingId, Model model) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "UPDATE book SET status =? WHERE bookid =?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, "Approve");
            statement.setInt(2, bookingId);
            statement.executeUpdate();

            return "redirect:/listbooking";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    public String rejectBooking(int bookingId, Model model) {
        try (Connection con = dataSource.getConnection()) {
            String sql = "UPDATE book SET status =? WHERE bookid =?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setString(1, "Rejected");
            statement.setInt(2, bookingId);
            statement.executeUpdate();
            model.addAttribute("successMessage", "Booking approved successfully");
            return "redirect:/listbooking";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    public String viewReceipt(int bookingId, Model model) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT receipt FROM payment WHERE bookid = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, bookingId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    byte[] receiptBytes = resultSet.getBytes("receipt");
                    String base64Receipt = Base64.getEncoder().encodeToString(receiptBytes);
                    String receiptImageSrc = "data:image/jpeg;base64," + base64Receipt;
                    model.addAttribute("receiptImageSrc", receiptImageSrc);
                    return "admin/viewreceipt"; // Assuming you have a Thymeleaf template for viewing the receipt
                } else {
                    // Handle the case when receipt is not found
                    return "redirect:/listbooking";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception
            return "redirect:/listbooking";
        }
    }

}
