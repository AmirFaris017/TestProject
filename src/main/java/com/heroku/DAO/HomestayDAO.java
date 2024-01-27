package com.heroku.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import javax.sql.DataSource;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.heroku.java.Model.Homestay;

import jakarta.servlet.http.HttpSession;

public class HomestayDAO {
    private final DataSource dataSource;
    
    public HomestayDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String addHomestay(HttpSession session, MultipartFile homestayPic, Homestay homestay) {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO homestay_ds(homestayname, homestaylocation, homestayprice, homestaydetails, homestaypic) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, homestay.getHomestayname());
            statement.setString(2, homestay.getHomestaylocation());
            statement.setDouble(3, homestay.getHomestayprice());
            statement.setString(4, homestay.getHomestaydetails());
            statement.setBytes(5, homestayPic.getBytes());
            statement.executeUpdate();
            return "redirect:/adminbook";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/adminbook";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    public String viewHomestay(HttpSession session, Model model) {
        ArrayList<Homestay> homestays = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            String sql = "SELECT * FROM homestay_ds";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int homestayid = resultSet.getInt("homestayid");
                String homestayname = resultSet.getString("homestayname");
                String homestaylocation = resultSet.getString("homestaylocation");
                Double homestayprice = resultSet.getDouble("homestayprice");
                String homestaydetails = resultSet.getString("homestaydetails");
                byte[] homestaypic = resultSet.getBytes("homestaypic");
                String base64Image = Base64.getEncoder().encodeToString(homestaypic);
                String imageSrc = "data:image/jpeg;base64," + base64Image;

                Homestay homestay = new Homestay(homestayid, homestayname, homestaylocation, homestayprice, null, homestaydetails, imageSrc);
                homestays.add(homestay);
            }
            model.addAttribute("homestays", homestays);
            return "admin/adminbook";
        } catch (SQLException e) {
            e.printStackTrace();
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/";
        }
    }

    public String customerBook(HttpSession session, Model model) {
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

    public String viewHomeStay(int homestayid, Model model) {
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

    public String viewHomestayDetail(int homestayid, Model model) {
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

    public String updateHomestay(HttpSession session, Homestay homestay, Model model, int homestayid) {
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

    public String deleteHomestay(int homestayid) {
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

    

