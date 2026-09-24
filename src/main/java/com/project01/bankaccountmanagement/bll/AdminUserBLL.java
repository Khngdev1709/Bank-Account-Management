package com.project01.bankaccountmanagement.bll;

import com.project01.bankaccountmanagement.dal.AdminUserDAO;
import com.project01.bankaccountmanagement.model.AdminUser;

public class AdminUserBLL {
    
    private final AdminUserDAO adminUserDAO;

    public AdminUserBLL() {
        this.adminUserDAO = new AdminUserDAO();
    }

    /**
     * Xác thực thông tin đăng nhập của quản trị viên.
     * Xử lý nghiệp vụ và ném ra Exception nếu có lỗi, không hiển thị giao diện tại đây.
     *
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Đối tượng AdminUser nếu đăng nhập thành công
     * @throws Exception Các lỗi nghiệp vụ (để trống, sai thông tin, tài khoản bị khóa)
     */
    public AdminUser authenticate(String username, String password) throws Exception {
        // 1. Kiểm tra rỗng
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Tên đăng nhập không được để trống!");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new Exception("Mật khẩu không được để trống!");
        }

        // 2. Gọi xuống tầng DAO để lấy dữ liệu xác thực
        AdminUser admin = adminUserDAO.getAdminByUsernameAndPassword(username, password);

        // 3. Xử lý kết quả trả về
        if (admin == null) {
            throw new Exception("Tên đăng nhập hoặc mật khẩu không chính xác!");
        }

        // 4. Kiểm tra trạng thái hoạt động của tài khoản
        if (!admin.isActive()) {
            throw new Exception("Tài khoản quản trị viên đã bị khóa! Vui lòng liên hệ hỗ trợ.");
        }

        // 5. Trả về thông tin quản trị viên hợp lệ
        return admin;
    }
}
