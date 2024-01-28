package com.heroku.DAO;
import java.io.IOException;
import java.sql.Connection;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;

import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;


public class PaymentDAO {
    private final DataSource dataSource;

    
    public PaymentDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void addPayment(int bookId, MultipartFile receipt) throws SQLException, IOException {
        try (Connection connection = dataSource.getConnection()) {
            String sql = "INSERT INTO payment(bookid,receipt) VALUES(?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, bookId);
            statement.setBytes(2, receipt.getBytes());
            statement.executeUpdate();
        }
    }
}

