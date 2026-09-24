package com.project01.bankaccountmanagement.dal;

import com.project01.bankaccountmanagement.model.BankAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAO {

    /**
     * Lấy toàn bộ danh sách tài khoản ngân hàng.
     */
    public List<BankAccount> getAllAccounts() throws SQLException {

        List<BankAccount> accounts = new ArrayList<>();

        String sql = "SELECT AccountID, CustomerID, AccountNumber, "
                + "Balance, Status, CreatedAt "
                + "FROM BankAccount "
                + "ORDER BY AccountID";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                BankAccount account = new BankAccount();

                account.setAccountID(rs.getInt("AccountID"));
                account.setCustomerID(rs.getInt("CustomerID"));
                account.setAccountNumber(rs.getString("AccountNumber"));
                account.setBalance(rs.getDouble("Balance"));
                account.setStatus(rs.getString("Status"));

                account.setCreatedAt(rs.getTimestamp("CreatedAt"));

                accounts.add(account);
            }
        }

        return accounts;
    }

    /**
     * Kiểm tra AccountNumber đã tồn tại trong Database hay chưa.
     *
     * @return true nếu đã tồn tại, false nếu chưa tồn tại.
     */
    public boolean existsAccountNumber(String accountNumber) throws SQLException {

        String sql = "SELECT COUNT(*) "
                + "FROM BankAccount "
                + "WHERE AccountNumber = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, accountNumber);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Cấp tài khoản ngân hàng mới.
     *
     * Balance, Status và CreatedAt sử dụng giá trị DEFAULT
     * được khai báo trong Database.
     *
     * Balance = 0.00
     * Status = Active
     * CreatedAt = GETDATE()
     */
    public boolean insertAccount(BankAccount account) throws SQLException {

        String sql = "INSERT INTO BankAccount (CustomerID, AccountNumber) "
                + "VALUES (?, ?)";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, account.getCustomerID());
            ps.setString(2, account.getAccountNumber());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật trạng thái tài khoản.
     *
     * @param accountID ID tài khoản cần cập nhật.
     * @param status Trạng thái mới: Active hoặc Locked.
     * @return true nếu cập nhật thành công.
     */
    public boolean updateStatus(int accountID, String status) throws SQLException {

        String sql = "UPDATE BankAccount "
                + "SET Status = ? "
                + "WHERE AccountID = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, accountID);

            return ps.executeUpdate() > 0;
        }
    }
}