package com.project01.bankaccountmanagement.bll;

import com.project01.bankaccountmanagement.dal.BankAccountDAO;
import com.project01.bankaccountmanagement.model.BankAccount;

import java.sql.SQLException;
import java.util.List;

public class BankAccountBLL {

    private final BankAccountDAO bankAccountDAO;


    public BankAccountBLL() {
        bankAccountDAO = new BankAccountDAO();
    }

    /**
     * Lấy toàn bộ danh sách tài khoản.
     */
    public List<BankAccount> getAllAccounts() throws SQLException {
        return bankAccountDAO.getAllAccounts();
    }

    /**
     * Tạo tài khoản ngân hàng mới.
     */
    public void createAccount(BankAccount account)
            throws SQLException {

        // 1. Kiểm tra object
        if (account == null) {
            throw new IllegalArgumentException(
                    "Thông tin tài khoản không được để trống."
            );
        }

        // 2. Kiểm tra CustomerID
        if (account.getCustomerID() <= 0) {
            throw new IllegalArgumentException(
                    "Customer không hợp lệ."
            );
        }

        // 3. Kiểm tra AccountNumber
        if (account.getAccountNumber() == null
                || account.getAccountNumber().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Số tài khoản không được để trống."
            );
        }

        String accountNumber = account.getAccountNumber().trim();

        // 4. Kiểm tra độ dài
        if (accountNumber.length() > 20) {
            throw new IllegalArgumentException(
                    "Số tài khoản không được vượt quá 20 ký tự."
            );
        }

        // 5. Kiểm tra số tài khoản có bị trùng không
        if (bankAccountDAO.existsAccountNumber(accountNumber)) {
            throw new IllegalArgumentException(
                    "Số tài khoản đã tồn tại."
            );
        }

        // 6. Gán lại dữ liệu đã trim
        account.setAccountNumber(accountNumber);

        // 7. Tạo tài khoản
        bankAccountDAO.insertAccount(account);
    }

    /**
     * Kiểm tra số tài khoản đã tồn tại hay chưa.
     */
    public boolean isAccountNumberExists(String accountNumber)
            throws SQLException {

        if (accountNumber == null
                || accountNumber.trim().isEmpty()) {

            return false;
        }

        return bankAccountDAO.existsAccountNumber(
                accountNumber.trim()
        );
    }

    /**
     * Cập nhật trạng thái tài khoản.
     */
    public void updateStatus(int accountID, String status)
            throws SQLException {

        // 1. Kiểm tra AccountID
        if (accountID <= 0) {
            throw new IllegalArgumentException(
                    "AccountID không hợp lệ."
            );
        }

        // 2. Kiểm tra Status
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Status không được để trống."
            );
        }

        status = status.trim();

        // 3. Chỉ cho phép 2 trạng thái theo Database
        if (!status.equals("Active")
                && !status.equals("Locked")) {

            throw new IllegalArgumentException(
                    "Status chỉ được là Active hoặc Locked."
            );
        }

        // 4. Gọi DAO cập nhật
        bankAccountDAO.updateStatus(accountID, status);
    }
}