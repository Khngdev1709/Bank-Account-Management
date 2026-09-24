package com.project01.bankaccountmanagement.bll;

import com.project01.bankaccountmanagement.dal.CustomerDAO;
import com.project01.bankaccountmanagement.model.Customer;

import java.util.List;

public class CustomerBLL {

    private final CustomerDAO customerDAO;

    public CustomerBLL() {
        this.customerDAO = new CustomerDAO();
    }

    /**
     * Lấy toàn bộ danh sách khách hàng.
     *
     * @return Danh sách khách hàng
     */
    public List<Customer> getAllCustomers() {
        return customerDAO.getAll();
    }

    /**
     * Lấy thông tin khách hàng theo ID.
     *
     * @param customerId Mã khách hàng
     * @return Đối tượng Customer hoặc null
     */
    public Customer getCustomerById(int customerId) {
        return customerDAO.getById(customerId);
    }

    /**
     * Thêm mới một khách hàng sau khi đã kiểm tra hợp lệ toàn bộ quy tắc nghiệp vụ.
     *
     * @param customer Đối tượng khách hàng cần thêm
     * @throws Exception Thông báo lỗi nghiệp vụ cụ thể nếu không hợp lệ
     */
    public void addCustomer(Customer customer) throws Exception {
        if (customer == null) {
            throw new Exception("Dữ liệu khách hàng không hợp lệ!");
        }

        // Validate nghiệp vụ (chế độ thêm mới: isInsert = true)
        validateCustomer(customer, true);

        // Lưu vào CSDL qua DAO
        boolean success = customerDAO.insert(customer);
        if (!success) {
            throw new Exception("Không thể thêm khách hàng vào hệ thống! Vui lòng thử lại sau.");
        }
    }

    /**
     * Cập nhật thông tin khách hàng sau khi đã kiểm tra hợp lệ dữ liệu.
     *
     * @param customer Đối tượng khách hàng chứa thông tin cập nhật
     * @throws Exception Thông báo lỗi nghiệp vụ nếu không hợp lệ
     */
    public void updateCustomer(Customer customer) throws Exception {
        if (customer == null || customer.getCustomerID() <= 0) {
            throw new Exception("Vui lòng chọn khách hàng cần cập nhật!");
        }

        // Kiểm tra xem khách hàng có tồn tại không
        Customer existing = customerDAO.getById(customer.getCustomerID());
        if (existing == null) {
            throw new Exception("Khách hàng không tồn tại trên hệ thống!");
        }

        // Validate nghiệp vụ (chế độ cập nhật: isInsert = false)
        validateCustomer(customer, false);

        // Cập nhật CSDL qua DAO
        boolean success = customerDAO.update(customer);
        if (!success) {
            throw new Exception("Không thể cập nhật thông tin khách hàng! Vui lòng thử lại sau.");
        }
    }

    /**
     * Xóa khách hàng theo mã ID.
     *
     * @param customerId Mã khách hàng cần xóa
     * @throws Exception Thông báo lỗi nếu ID không hợp lệ hoặc xóa thất bại
     */
    public void deleteCustomer(int customerId) throws Exception {
        if (customerId <= 0) {
            throw new Exception("Mã khách hàng không hợp lệ!");
        }

        Customer existing = customerDAO.getById(customerId);
        if (existing == null) {
            throw new Exception("Khách hàng không tồn tại trên hệ thống!");
        }

        boolean success = customerDAO.delete(customerId);
        if (!success) {
            throw new Exception("Không thể xóa khách hàng! Vui lòng kiểm tra lại liên kết dữ liệu.");
        }
    }

    /**
     * Tìm kiếm khách hàng theo từ khóa (Tên, CCCD hoặc SĐT).
     *
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách khách hàng phù hợp
     */
    public List<Customer> searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return customerDAO.getAll();
        }
        return customerDAO.search(keyword);
    }

    /**
     * Kiểm tra tính hợp lệ của dữ liệu khách hàng theo đúng chuẩn nghiệp vụ:
     * - Họ tên: Không rỗng, <= 100 ký tự.
     * - CCCD: Bắt buộc, là ký số, 9 hoặc 12 số, không được trùng với khách hàng khác.
     * - Số điện thoại: Bắt buộc, đúng 10 số, bắt đầu bằng 0.
     * - Email: Định dạng chuẩn RFC, <= 50 ký tự (nếu có nhập).
     * - Địa chỉ: <= 200 ký tự.
     *
     * @param c Đối tượng khách hàng
     * @param isInsert true nếu là thao tác thêm mới, false nếu là cập nhật
     * @throws Exception Ném ngoại lệ chứa thông báo lỗi tiếng Việt nếu không hợp lệ
     */
    private void validateCustomer(Customer c, boolean isInsert) throws Exception {
        // 1. Validate Họ và tên
        String fullName = c.getFullName() != null ? c.getFullName().trim() : "";
        if (fullName.isEmpty()) {
            throw new Exception("Họ và tên khách hàng không được để trống!");
        }
        if (fullName.length() > 100) {
            throw new Exception("Họ và tên khách hàng không được vượt quá 100 ký tự!");
        }
        if (fullName.matches(".*\\d.*")) {
            throw new Exception("Họ và tên khách hàng không được chứa chữ số!");
        }
        c.setFullName(fullName);

        // 2. Validate CCCD / CMND
        String idCard = c.getIdentityCard() != null ? c.getIdentityCard().trim() : "";
        if (idCard.isEmpty()) {
            throw new Exception("Số CCCD/CMND không được để trống!");
        }
        if (!idCard.matches("^\\d+$")) {
            throw new Exception("Số CCCD/CMND chỉ được chứa các ký số (không chứa chữ hoặc ký tự đặc biệt)!");
        }
        if (idCard.length() != 12 && idCard.length() != 9) {
            throw new Exception("Số CCCD phải có đúng 12 chữ số (hoặc CMND 9 số)!");
        }

        // Kiểm tra chống trùng lặp CCCD trong CSDL (loại trừ chính mình nếu đang cập nhật)
        int excludeId = isInsert ? 0 : c.getCustomerID();
        if (customerDAO.isIdentityCardExists(idCard, excludeId)) {
            throw new Exception("Số CCCD/CMND [" + idCard + "] đã tồn tại trên hệ thống! Vui lòng kiểm tra lại.");
        }
        c.setIdentityCard(idCard);

        // 3. Validate Số điện thoại
        String phone = c.getPhone() != null ? c.getPhone().trim() : "";
        if (phone.isEmpty()) {
            throw new Exception("Số điện thoại không được để trống!");
        }
        if (!phone.matches("^0\\d{9}$")) {
            throw new Exception("Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng số 0 (Ví dụ: 0912345678)!");
        }

        // Kiểm tra chống trùng lặp Số điện thoại trong CSDL
        if (customerDAO.isPhoneExists(phone, excludeId)) {
            throw new Exception("Số điện thoại [" + phone + "] đã tồn tại trên hệ thống! Vui lòng kiểm tra lại.");
        }
        c.setPhone(phone);

        // 4. Validate Email (không bắt buộc nhưng nếu nhập thì phải chuẩn và không trùng)
        String email = c.getEmail() != null ? c.getEmail().trim() : "";
        if (!email.isEmpty()) {
            if (email.length() > 50) {
                throw new Exception("Địa chỉ Email không được vượt quá 50 ký tự!");
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                throw new Exception("Địa chỉ Email không đúng định dạng (Ví dụ: khachhang@gmail.com)!");
            }

            // Kiểm tra chống trùng lặp Email trong CSDL (cả khi thêm mới và cập nhật)
            if (customerDAO.isEmailExists(email, excludeId)) {
                throw new Exception("Địa chỉ Email [" + email + "] đã tồn tại trên hệ thống! Vui lòng sử dụng email khác.");
            }
            c.setEmail(email);
        } else {
            c.setEmail(null);
        }

        // 5. Validate Địa chỉ
        String address = c.getAddress() != null ? c.getAddress().trim() : "";
        if (!address.isEmpty()) {
            if (address.length() > 200) {
                throw new Exception("Địa chỉ không được vượt quá 200 ký tự!");
            }
            c.setAddress(address);
        } else {
            c.setAddress(null);
        }
    }
}
