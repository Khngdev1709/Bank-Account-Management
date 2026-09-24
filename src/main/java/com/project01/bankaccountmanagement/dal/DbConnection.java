package com.project01.bankaccountmanagement.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=BankAccountManagement;encrypt=true;trustServerCertificate=true;";
    private static final String USERNAME = "sa"; // User của SQL Server
    private static final String PASSWORD = "sa"; // Mật khẩu của SQL Server

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Tải Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Thiết lập kết nối
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Kết nối SQL Server thành công!");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Lỗi kết nối CSDL: " + e.getMessage());
        }
        return conn;
    }

    // Hàm test nhanh
//    public static void main(String[] args) {
//        getConnection();
//    }
}
