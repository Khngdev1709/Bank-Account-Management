package com.project01.bankaccountmanagement.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Đường dẫn trỏ tới file thiết kế giao diện FXML
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/com/project01/bankaccountmanagement/view/LoginView.fxml"));

        // Khởi tạo cửa sổ với kích thước 600x400
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Hệ thống Quản lý Tài khoản Ngân hàng - Admin");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}