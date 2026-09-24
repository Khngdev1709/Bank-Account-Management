package com.project01.bankaccountmanagement.presentation;

import com.project01.bankaccountmanagement.model.AdminUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class AdminController {

    @FXML
    private Label lblAdminName;

    @FXML
    private BorderPane mainPane;
    
    private AdminUser loggedInAdmin;

    /**
     * Phương thức này được gọi khi truyền dữ liệu từ LoginController sang.
     */
    public void setAdminInfo(AdminUser admin) {
        this.loggedInAdmin = admin;
        lblAdminName.setText("Xin chào: " + admin.getFullName());
    }

    @FXML
    void handleLogout(ActionEvent event) {
        try {
            // Load lại màn hình Login
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/project01/bankaccountmanagement/view/LoginView.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Đăng nhập Hệ thống");
            stage.setScene(scene);
            stage.show();

            // Đóng cửa sổ Admin hiện tại
            javafx.stage.Stage adminStage = (javafx.stage.Stage) mainPane.getScene().getWindow();
            adminStage.close();
            
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void showCustomerManagement(ActionEvent event) {
        // Tải giao diện quản lý khách hàng vào giữa mainPane
    }

    @FXML
    void showAccountManagement(ActionEvent event) {
        // Tải giao diện quản lý tài khoản vào giữa mainPane
    }
}
