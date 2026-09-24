package com.project01.bankaccountmanagement.dal;

import com.project01.bankaccountmanagement.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    /**
     * Lấy toàn bộ danh sách khách hàng từ cơ sở dữ liệu.
     *
     * @return Danh sách khách hàng (List<Customer>)
     */
    public List<Customer> getAll() {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer ORDER BY CustomerID DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("CustomerID"),
                        rs.getString("FullName"),
                        rs.getString("IdentityCard"),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getString("Address")
                );
                list.add(customer);
            }
        } catch (SQLException e) {
            System.err.println("Lỗi truy xuất danh sách khách hàng: " + e.getMessage());
        }
        return list;
    }

    /**
     * Lấy thông tin một khách hàng theo mã CustomerID.
     *
     * @param customerId Mã khách hàng cần tìm
     * @return Đối tượng Customer nếu tìm thấy, ngược lại trả về null
     */
    public Customer getById(int customerId) {
        String sql = "SELECT * FROM Customer WHERE CustomerID = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("CustomerID"),
                            rs.getString("FullName"),
                            rs.getString("IdentityCard"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getString("Address")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm khách hàng theo ID: " + e.getMessage());
        }
        return null;
    }

    /**
     * Thêm mới một khách hàng vào cơ sở dữ liệu.
     *
     * @param customer Đối tượng khách hàng cần thêm
     * @return true nếu thêm thành công, ngược lại trả về false
     */
    public boolean insert(Customer customer) {
        String sql = "INSERT INTO Customer (FullName, IdentityCard, Phone, Email, Address) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getIdentityCard());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getAddress());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm mới khách hàng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Cập nhật thông tin khách hàng trong cơ sở dữ liệu.
     *
     * @param customer Đối tượng khách hàng chứa thông tin cập nhật
     * @return true nếu cập nhật thành công, ngược lại trả về false
     */
    public boolean update(Customer customer) {
        String sql = "UPDATE Customer SET FullName = ?, IdentityCard = ?, Phone = ?, Email = ?, Address = ? WHERE CustomerID = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, customer.getFullName());
            pstmt.setString(2, customer.getIdentityCard());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getAddress());
            pstmt.setInt(6, customer.getCustomerID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật thông tin khách hàng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Xóa một khách hàng khỏi cơ sở dữ liệu theo mã CustomerID.
     * Lưu ý: Các tài khoản ngân hàng liên kết sẽ tự động bị xóa do ràng buộc ON DELETE CASCADE.
     *
     * @param customerId Mã khách hàng cần xóa
     * @return true nếu xóa thành công, ngược lại trả về false
     */
    public boolean delete(int customerId) {
        String sql = "DELETE FROM Customer WHERE CustomerID = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, customerId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi xóa khách hàng: " + e.getMessage());
        }
        return false;
    }

    /**
     * Kiểm tra xem số CCCD đã tồn tại trong cơ sở dữ liệu hay chưa.
     * Sử dụng để ngăn ngừa trùng lặp khi thêm mới hoặc sửa thông tin.
     *
     * @param identityCard Số CCCD cần kiểm tra
     * @param excludeCustomerId Mã khách hàng cần loại trừ (dùng khi cập nhật, truyền <= 0 nếu là thêm mới)
     * @return true nếu đã tồn tại, false nếu chưa
     */
    public boolean isIdentityCardExists(String identityCard, int excludeCustomerId) {
        String sql = "SELECT COUNT(*) FROM Customer WHERE IdentityCard = ? AND CustomerID != ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, identityCard);
            pstmt.setInt(2, excludeCustomerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra trùng số CCCD: " + e.getMessage());
        }
        return false;
    }

    /**
     * Kiểm tra xem Số điện thoại đã tồn tại trong cơ sở dữ liệu hay chưa.
     * Sử dụng để ngăn ngừa trùng lặp khi thêm mới hoặc sửa thông tin.
     *
     * @param phone Số điện thoại cần kiểm tra
     * @param excludeCustomerId Mã khách hàng cần loại trừ (dùng khi cập nhật, truyền <= 0 nếu là thêm mới)
     * @return true nếu đã tồn tại, false nếu chưa
     */
    public boolean isPhoneExists(String phone, int excludeCustomerId) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM Customer WHERE Phone = ? AND CustomerID != ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, phone.trim());
            pstmt.setInt(2, excludeCustomerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra trùng Số điện thoại: " + e.getMessage());
        }
        return false;
    }

    /**
     * Kiểm tra xem Email đã tồn tại trong cơ sở dữ liệu hay chưa.
     * Sử dụng để ngăn ngừa trùng lặp khi thêm mới hoặc sửa thông tin.
     *
     * @param email Địa chỉ email cần kiểm tra
     * @param excludeCustomerId Mã khách hàng cần loại trừ (dùng khi cập nhật, truyền <= 0 nếu là thêm mới)
     * @return true nếu đã tồn tại, false nếu chưa
     */
    public boolean isEmailExists(String email, int excludeCustomerId) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM Customer WHERE Email = ? AND CustomerID != ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email.trim());
            pstmt.setInt(2, excludeCustomerId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi kiểm tra trùng Email: " + e.getMessage());
        }
        return false;
    }

    /**
     * Tìm kiếm khách hàng theo từ khóa (khớp với Họ tên, Số CCCD hoặc Số điện thoại).
     *
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách khách hàng phù hợp
     */
    public List<Customer> search(String keyword) {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM Customer WHERE FullName LIKE ? OR IdentityCard LIKE ? OR Phone LIKE ? ORDER BY CustomerID DESC";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword.trim() + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer(
                            rs.getInt("CustomerID"),
                            rs.getString("FullName"),
                            rs.getString("IdentityCard"),
                            rs.getString("Phone"),
                            rs.getString("Email"),
                            rs.getString("Address")
                    );
                    list.add(customer);
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi tìm kiếm khách hàng: " + e.getMessage());
        }
        return list;
    }
}
