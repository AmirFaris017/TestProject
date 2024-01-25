package com.heroku.java.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.ui.Model;
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
    private DataSource dataSource;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostMapping("/addhomestay")
    public String addHomestay(HttpSession session, @RequestParam("homestaypic") MultipartFile homestayPic,
            @ModelAttribute("addhomestay") Homestay homestay) {
        try {
            Connection connection = dataSource.getConnection();
            String sql = "INSERT INTO homestay_ds(homestayname,homestaylocation, homestayprice, homestaydetails, homestaypic) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, homestay.getHomestayname());
            statement.setString(2, homestay.getHomestaylocation());
            statement.setDouble(3, homestay.getHomestayprice());
            statement.setString(4, homestay.getHomestaydetails());
            statement.setBytes(5, homestayPic.getBytes());
            statement.executeUpdate();
            connection.close();

            return "redirect:/adminbook";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    @GetMapping("/adminbook")
    public String viewhomestay(HttpSession session, Model model) {
        ArrayList<Homestay> homestays = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            final var statement = con.prepareStatement("SELECT * FROM homestay_ds");
            final var rs = statement.executeQuery();
            while (rs.next()) {
                int homestayid = rs.getInt("homestayid");
                String homestayname = rs.getString("homestayname");
                String homestaylocation = rs.getString("homestaylocation");
                Double homestayprice = rs.getDouble("homestayprice");
                String homestaydetails = rs.getString("homestaydetails");
                byte[] homestaypic = rs.getBytes("homestaypic");
                String base64Image = Base64.getEncoder().encodeToString(homestaypic);
                String imageSrc = "data:image/jpeg;base64," + base64Image;

                Homestay homestay = new Homestay(homestayid, homestayname, homestaylocation, homestayprice, null, homestaydetails, imageSrc);
                homestays.add(homestay);
            }
            model.addAttribute("homestays", homestays);
            return "admin/adminbook";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("E message : " + e.getMessage());

            return "redirect:/";
        }
    } 
    @GetMapping("/cusbook")
    public String customerhomestay(HttpSession session, Model model) {
        ArrayList<Homestay> homestays = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            final var statement = con.prepareStatement("SELECT * FROM homestay_ds");
            final var rs = statement.executeQuery();
            while (rs.next()) {
                int homestayid = rs.getInt("homestayid");
                String homestayname = rs.getString("homestayname");
                String homestaylocation = rs.getString("homestaylocation");
                Double homestayprice = rs.getDouble("homestayprice");
                String homestaydetails = rs.getString("homestaydetails");
                byte[] homestaypic = rs.getBytes("homestaypic");
                String base64Image = Base64.getEncoder().encodeToString(homestaypic);
                String imageSrc = "data:image/jpeg;base64," + base64Image;

                Homestay homestay = new Homestay(homestayid, homestayname, homestaylocation, homestayprice, null, homestaydetails, imageSrc);
                homestays.add(homestay);
            }
            model.addAttribute("homestays", homestays);
            return "customer/cusbook";
        } catch (SQLException sqe) {
            System.out.println("Error Code = " + sqe.getErrorCode());
            System.out.println("SQL state = " + sqe.getSQLState());
            System.out.println("Message = " + sqe.getMessage());
            System.out.println("printTrace /n");
            sqe.printStackTrace();

            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("E message : " + e.getMessage());

            return "redirect:/";
        }
    } 

     @GetMapping("/viewhome")
    public String viewhome (@RequestParam("homestayid") int homestayid, Model model){
        try{
            Connection connection = dataSource.getConnection();
            String sql = "SELECT homestayname,homestaylocation,homestayprice,homestaydetails,homestaypic FROM homestay_ds WHERE homestayid = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, homestayid);
            final var resultSet = statement.executeQuery();
    
            if(resultSet.next()){
                // int homestayid2 = resultSet.getInt("homestayid");
                String homestayname = resultSet.getString("homestayname");
                String homestaylocation = resultSet.getString("homestaylocation");
                Double homestayprice = resultSet.getDouble("homestayprice");
                String homestaydetails = resultSet.getString("homestaydetails");
                byte[] homestaybytes = resultSet.getBytes("homestaypic");
                String base64Image = Base64.getEncoder().encodeToString(homestaybytes);
                String imageSrc = "data:image/jpeg;base64," + base64Image;
    
                Homestay homestay = new Homestay(homestayid, homestayname, homestaylocation, homestayprice, null, homestaydetails, imageSrc);
                model.addAttribute("homestay", homestay);
                
            }
            return "customer/viewhome";
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Error: " + t.getMessage());
            return "index";
        }
    }
    @GetMapping("/viewhomestay")
    public String viewhomestaydetail (@RequestParam("homestayid") int homestayid, Model model){
        try{
            Connection connection = dataSource.getConnection();
            String sql = "SELECT homestayname,homestaylocation,homestayprice,homestaydetails,homestaypic FROM homestay_ds WHERE homestayid = ?";
            final var statement = connection.prepareStatement(sql);
            statement.setInt(1, homestayid);
            final var resultSet = statement.executeQuery();
    
            if(resultSet.next()){
                String homestayname = resultSet.getString("homestayname");
                String homestaylocation = resultSet.getString("homestaylocation");
                Double homestayprice = resultSet.getDouble("homestayprice");
                String homestaydetails = resultSet.getString("homestaydetails");
                byte[] homestaybytes = resultSet.getBytes("homestaypic");
                String base64Image = Base64.getEncoder().encodeToString(homestaybytes);
                String imageSrc = "data:image/jpeg;base64," + base64Image;
    
                Homestay homestay = new Homestay(homestayid, homestayname, homestaylocation, homestayprice, null, homestaydetails, imageSrc);
                model.addAttribute("homestay", homestay);
                
            }
            return "admin/viewhomestay";
        } catch (Throwable t) {
            t.printStackTrace();
            System.out.println("Error: " + t.getMessage());
            return "index";
        }
    }

    @PostMapping("/updatehomestay/{homestayid}")
    public String updatehomestay(HttpSession session,@ModelAttribute("homestay") Homestay homestay,Model model,@PathVariable("homestayid") int homestayid) {
    String homestayname = homestay.getHomestayname();
    String homestaylocation = homestay.getHomestaylocation();
    Double homestayprice = homestay.getHomestayprice();
    String homestaydetails = homestay.getHomestaydetails();
    
    try {
        Connection con = dataSource.getConnection();
        String sql = "UPDATE homestay_ds SET homestayname=?,homestaylocation=?,homestayprice=?,homestaydetails=? WHERE homestayid=?";
        final var statement = con.prepareStatement(sql);

        statement.setString(1, homestayname);
        statement.setString(2, homestaylocation);
        statement.setDouble(3, homestayprice);
        statement.setString(4, homestaydetails);
        statement.setInt(5, homestayid);

        statement.executeUpdate();
        statement.close();
        con.close();

        return "redirect:/viewhomestay?homestayid=" + homestayid;
    } catch (Throwable t) {
        t.printStackTrace();
        System.out.println("Error: " + t.getMessage());
        return "redirect:/login";
    }
}
    @GetMapping("/deletehomestay")
    public String deletehomestay(@RequestParam("homestayid") int homestayid) {
    try (Connection connection = dataSource.getConnection()) {
        String sql = "DELETE FROM homestay_ds WHERE homestayid = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, homestayid);
        statement.executeUpdate();
        return "redirect:/adminbook";
    } catch (Throwable t) {
        System.out.println("message: " + t.getMessage());
        return "redirect:/login";
    }
    }

    
}
