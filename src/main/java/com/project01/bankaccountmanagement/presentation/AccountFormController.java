package com.project01.bankaccountmanagement.presentation;

import com.project01.bankaccountmanagement.bll.BankAccountBLL;
import com.project01.bankaccountmanagement.model.BankAccount;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class AccountFormController {

    // =========================
    // FORM
    // =========================

    @FXML
    private ComboBox<Integer> cbCustomer;

    @FXML
    private TextField txtAccountNumber;

    // =========================
    // TABLE
    // =========================

    @FXML
    private TableView<BankAccount> tblAccounts;

    @FXML
    private TableColumn<BankAccount, Integer> colAccountID;

    @FXML
    private TableColumn<BankAccount, Integer> colCustomerID;

    @FXML
    private TableColumn<BankAccount, String> colAccountNumber;

    @FXML
    private TableColumn<BankAccount, Double> colBalance;

    @FXML
    private TableColumn<BankAccount, String> colStatus;

    @FXML
    private TableColumn<BankAccount, Timestamp> colCreatedAt;

    // =========================
    // STATUS
    // =========================

    @FXML
    private ComboBox<String> cbStatus;

    // =========================
    // BLL
    // =========================

    private BankAccountBLL bankAccountBLL;

    // =========================
    // INITIALIZE
    // =========================

    @FXML
    public void initialize() {

        bankAccountBLL = new BankAccountBLL();

        setupTableColumns();

        loadStatus();

        loadAccounts();

        loadCustomers();
    }

    // =========================
    // SETUP TABLE
    // =========================

    private void setupTableColumns() {

        colAccountID.setCellValueFactory(
                new PropertyValueFactory<>("accountID")
        );

        colCustomerID.setCellValueFactory(
                new PropertyValueFactory<>("customerID")
        );

        colAccountNumber.setCellValueFactory(
                new PropertyValueFactory<>("accountNumber")
        );

        colBalance.setCellValueFactory(
                new PropertyValueFactory<>("balance")
        );

        colStatus.setCellValueFactory(
                new PropertyValueFactory<>("status")
        );

        colCreatedAt.setCellValueFactory(
                new PropertyValueFactory<>("createdAt")
        );
    }

    // =========================
    // LOAD STATUS
    // =========================

    private void loadStatus() {

        cbStatus.setItems(
                FXCollections.observableArrayList(
                        "Active",
                        "Locked"
                )
        );

        cbStatus.setValue("Active");
    }

    // =========================
    // LOAD ACCOUNT LIST
    // =========================

    private void loadAccounts() {

        try {

            List<BankAccount> accounts =
                    bankAccountBLL.getAllAccounts();

            ObservableList<BankAccount> data =
                    FXCollections.observableArrayList(accounts);

            tblAccounts.setItems(data);

        } catch (SQLException e) {

            showError(
                    "Database Error",
                    "Không thể tải danh sách tài khoản.",
                    e.getMessage()
            );
        }
    }

    // =========================
    // LOAD CUSTOMER
    // =========================

    private void loadCustomers() {

        /*
         * Tạm thời chưa lấy Customer từ CustomerBLL.
         *
         * Vì module Customer của thành viên khác đang phụ trách.
         *
         * Khi CustomerBLL hoàn thiện, phần này sẽ được
         * kết nối với CustomerBLL để lấy CustomerID thực tế.
         */
    }

    // =========================
    // CREATE ACCOUNT
    // =========================

    @FXML
    private void handleCreateAccount() {

        try {

            // 1. Kiểm tra Customer
            Integer customerID = cbCustomer.getValue();

            if (customerID == null) {

                showWarning(
                        "Thiếu thông tin",
                        "Vui lòng chọn Customer."
                );

                return;
            }

            // 2. Lấy Account Number
            String accountNumber =
                    txtAccountNumber.getText();

            // 3. Tạo object
            BankAccount account = new BankAccount();

            account.setCustomerID(customerID);
            account.setAccountNumber(accountNumber);

            // 4. Gọi BLL
            bankAccountBLL.createAccount(account);

            // 5. Thông báo
            showInformation(
                    "Thành công",
                    "Cấp tài khoản ngân hàng thành công."
            );

            // 6. Refresh TableView
            loadAccounts();

            // 7. Clear form
            clearForm();

        } catch (IllegalArgumentException e) {

            showWarning(
                    "Dữ liệu không hợp lệ",
                    e.getMessage()
            );

        } catch (SQLException e) {

            showError(
                    "Database Error",
                    "Không thể tạo tài khoản.",
                    e.getMessage()
            );
        }
    }

    // =========================
    // UPDATE STATUS
    // =========================

    @FXML
    private void handleUpdateStatus() {

        BankAccount selectedAccount =
                tblAccounts.getSelectionModel()
                        .getSelectedItem();

        // Không chọn tài khoản
        if (selectedAccount == null) {

            showWarning(
                    "Chưa chọn tài khoản",
                    "Vui lòng chọn một tài khoản trong danh sách."
            );

            return;
        }

        String status = cbStatus.getValue();

        if (status == null) {

            showWarning(
                    "Thiếu trạng thái",
                    "Vui lòng chọn trạng thái."
            );

            return;
        }

        try {

            bankAccountBLL.updateStatus(
                    selectedAccount.getAccountID(),
                    status
            );

            showInformation(
                    "Thành công",
                    "Cập nhật trạng thái tài khoản thành công."
            );

            loadAccounts();

        } catch (IllegalArgumentException e) {

            showWarning(
                    "Dữ liệu không hợp lệ",
                    e.getMessage()
            );

        } catch (SQLException e) {

            showError(
                    "Database Error",
                    "Không thể cập nhật trạng thái.",
                    e.getMessage()
            );
        }
    }

    // =========================
    // CLEAR FORM
    // =========================

    private void clearForm() {

        txtAccountNumber.clear();

        cbCustomer.setValue(null);

        cbStatus.setValue("Active");
    }

    // =========================
    // ALERT - WARNING
    // =========================

    private void showWarning(
            String title,
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.WARNING);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================
    // ALERT - INFORMATION
    // =========================

    private void showInformation(
            String title,
            String message) {

        Alert alert =
                new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================
    // ALERT - ERROR
    // =========================

    private void showError(
            String title,
            String message,
            String detail) {

        Alert alert =
                new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.setContentText(detail);

        alert.showAndWait();
    }
}