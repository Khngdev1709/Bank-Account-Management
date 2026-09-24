package com.project01.bankaccountmanagement.bll;

import com.project01.bankaccountmanagement.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Kiểm thử nghiệp vụ (Unit Test) cho CustomerBLL theo chuẩn Superpowers.
 * Kiểm tra đầy đủ các quy tắc nghiệp vụ trong tài liệu Bank Account Management.docx.
 */
public class CustomerBLLTest {

    private CustomerBLL customerBLL;

    @BeforeEach
    void setUp() {
        customerBLL = new CustomerBLL();
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi họ tên khách hàng bị bỏ trống")
    void testEmptyFullNameThrowsException() {
        Customer c = new Customer("", "001202012345", "0912345678", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("Họ và tên khách hàng không được để trống"));
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi họ tên dài hơn 100 ký tự")
    void testFullNameTooLongThrowsException() {
        String longName = "A".repeat(101);
        Customer c = new Customer(longName, "001202012345", "0912345678", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("không được vượt quá 100 ký tự"));
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi số CCCD/CMND bị bỏ trống")
    void testEmptyIdentityCardThrowsException() {
        Customer c = new Customer("Nguyễn Văn An", "", "0912345678", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("Số CCCD/CMND không được để trống"));
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi số CCCD chứa ký tự chữ cái")
    void testIdentityCardWithLettersThrowsException() {
        Customer c = new Customer("Nguyễn Văn An", "00120201234A", "0912345678", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("chỉ được chứa các ký số"));
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi số CCCD không đủ 9 hoặc 12 số (ví dụ 10 số)")
    void testIdentityCardInvalidLengthThrowsException() {
        Customer c = new Customer("Nguyễn Văn An", "0012020123", "0912345678", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("phải có đúng 12 chữ số (hoặc CMND 9 số)"));
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi số điện thoại bị bỏ trống")
    void testEmptyPhoneThrowsException() {
        Customer c = new Customer("Nguyễn Văn An", "001202012345", "", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("Số điện thoại không được để trống"));
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi số điện thoại không bắt đầu bằng số 0")
    void testPhoneNotStartingWithZeroThrowsException() {
        Customer c = new Customer("Nguyễn Văn An", "001202012345", "1912345678", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("bắt đầu bằng số 0"));
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi số điện thoại không đủ 10 số (ví dụ 9 số hoặc 11 số)")
    void testPhoneInvalidLengthThrowsException() {
        Customer c = new Customer("Nguyễn Văn An", "001202012345", "091234567", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("phải gồm đúng 10 chữ số"));
    }

    @Test
    @DisplayName("Kiểm tra lỗi khi định dạng email không hợp lệ")
    void testInvalidEmailFormatThrowsException() {
        Customer c = new Customer("Nguyễn Văn An", "001202012345", "0912345678", "email-sai-dinh-dang", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.addCustomer(c));
        assertTrue(exception.getMessage().contains("Email không đúng định dạng"));
    }

    @Test
    @DisplayName("Kiểm tra trường hợp cập nhật khi chưa chọn khách hàng (ID <= 0)")
    void testUpdateCustomerWithoutSelectionThrowsException() {
        Customer c = new Customer(0, "Nguyễn Văn An", "001202012345", "0912345678", "test@gmail.com", "Hà Nội");
        Exception exception = assertThrows(Exception.class, () -> customerBLL.updateCustomer(c));
        assertTrue(exception.getMessage().contains("Vui lòng chọn khách hàng cần cập nhật"));
    }
}
