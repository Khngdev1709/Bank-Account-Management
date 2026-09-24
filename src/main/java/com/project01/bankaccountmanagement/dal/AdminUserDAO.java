package com.project01.bankaccountmanagement.dal;

import com.project01.bankaccountmanagement.model.AdminUser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminUserDAO {

    /**
     * Lấy thông tin quản trị viên dựa trên tên đăng nhập và mật khẩu.
     * Sử dụng PreparedStatement để ngăn chặn SQL Injection.
     * 
     * @param username Tên đăng nhập của quản trị viên
     * @param password Mật khẩu của quản trị viên
     * @return Đối tượng AdminUser nếu tìm thấy, ngược lại trả về null
     */
    public AdminUser getAdminByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM AdminUser WHERE Username = ? AND PasswordHash = ?";
        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Gán giá trị cho các tham số
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new AdminUser(
                            rs.getInt("AdminID"),
                            rs.getString("Username"),
                            rs.getString("PasswordHash"),
                            rs.getString("FullName"),
                            rs.getBoolean("IsActive")
                    );
                }
            }
        } catch (SQLException e) {
            // Log lỗi, tuyệt đối không dùng Alert hay System.out.println để hiển thị cho người dùng ở tầng này
            System.err.println("Lỗi truy xuất dữ liệu: " + e.getMessage());
        }
        return null;
    }
}
