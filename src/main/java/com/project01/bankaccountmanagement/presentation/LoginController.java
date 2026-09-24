package com.project01.bankaccountmanagement.presentation;

import com.project01.bankaccountmanagement.bll.AdminUserBLL;
import com.project01.bankaccountmanagement.model.AdminUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;
    
    @FXML
    private Label lblMessage; // Nhãn hiển thị thông báo dưới form (nếu cần)

    private final AdminUserBLL adminUserBLL;

    public LoginController() {
        // Khởi tạo BLL
        this.adminUserBLL = new AdminUserBLL();
    }

    /**
     * Hàm xử lý sự kiện khi nhấn nút "Đăng nhập".
     * Ở tầng này, chúng ta chỉ lấy dữ liệu từ giao diện, gọi hàm authenticate từ BLL
     * và xử lý các Exception thông qua JavaFX Alert.
     */
    @FXML
    void handleLoginAction(ActionEvent event) {
        // 1. Thu thập dữ liệu từ giao diện
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {
            // 2. Gọi hàm authenticate của BLL bên trong khối try-catch
            AdminUser admin = adminUserBLL.authenticate(username, password);

            // 3. Nếu không có Exception nào được ném ra, đăng nhập thành công
            showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đăng nhập thành công!", 
                      "Xin chào " + admin.getFullName());
            
            // Xóa thông báo lỗi trên nhãn nếu có
            if (lblMessage != null) {
                lblMessage.setText(""); 
            }

            // Mở giao diện Admin
            openAdminView(admin);

        } catch (Exception e) {
            // 4. Sử dụng JavaFX Alert để hiển thị thông báo lỗi từ BLL
            showAlert(Alert.AlertType.ERROR, "Lỗi Đăng nhập", "Đăng nhập thất bại", e.getMessage());
        }
    }

    private void openAdminView(AdminUser admin) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/project01/bankaccountmanagement/view/AdminView.fxml"));
            javafx.scene.Parent root = loader.load();

            // Lấy controller của AdminView để truyền dữ liệu
            AdminController adminController = loader.getController();
            adminController.setAdminInfo(admin);

            // Hiển thị Stage mới
            javafx.scene.Scene scene = new javafx.scene.Scene(root);
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Trang Quản Trị Viên");
            stage.setScene(scene);
            stage.show();

            // Đóng cửa sổ Login hiện tại
            javafx.stage.Stage loginStage = (javafx.stage.Stage) txtUsername.getScene().getWindow();
            loginStage.close();
            
        } catch (java.io.IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi Hệ Thống", "Không thể tải giao diện Admin", e.getMessage());
        }
    }

    /**
     * Hàm tiện ích để hiển thị hộp thoại Alert.
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
