# Kiến Trúc Hệ Thống (System Architecture)

Dự án **Quản lý Tài khoản Ngân hàng (Bank Account Management)** được xây dựng dựa trên nguyên tắc phân tách các mối quan tâm (Separation of Concerns), kết hợp giữa mô hình kiến trúc **3-Tier** (3 lớp) truyền thống và mẫu thiết kế **MVC** (Model-View-Controller) tại tầng giao diện.

Dưới đây là sơ đồ cấu trúc chi tiết của toàn bộ hệ thống:

## Mô Hình Tổng Quan (3-Tier & MVC)

```text
[ USER (Admin) ]
       │
       ▼
┌─────────────────────────────────────────────────────────────┐
│  TIER 1: PRESENTATION LAYER (Lớp Giao Diện)                 │
│  (Áp dụng mô hình MVC)                                      │
│                                                             │
│  [VIEW - UI] (JavaFX & Scene Builder)                       │
│   ├── LoginView.fxml (Giao diện đăng nhập)                  │
│   ├── MainDashboardView.fxml (Giao diện chính/Tab quản lý)  │
│   ├── CustomerFormView.fxml (Giao diện Thêm/Sửa Khách hàng) │
│   └── AccountFormView.fxml (Giao diện Thêm/Sửa Tài khoản)   │
│         │                                                   │
│         ▼ Tương tác người dùng (Events)                     │
│                                                             │
│  [CONTROLLER] (Java)                                        │
│   ├── LoginController.java                                  │
│   ├── MainDashboardController.java                          │
│   ├── CustomerFormController.java                           │
│   └── AccountFormController.java                            │
│         │                                                   │
│         ▼ Yêu cầu xử lý / Lấy dữ liệu hiển thị (Model)      │
│                                                             │
│  [MODEL] (Data Entities)                                    │
│   ├── AdminUser (AdminID, Username, PasswordHash,...)       │
│   ├── Customer (CustomerID, FullName, IdentityCard,...)     │
│   └── BankAccount (AccountID, CustomerID, AccountNumber,...)│
└─────────│───────────────────────────────────────────────────┘
          │ (Gọi phương thức nghiệp vụ)
          ▼
┌─────────────────────────────────────────────────────────────┐
│  TIER 2: BUSINESS LOGIC LAYER (BLL - Lớp Nghiệp Vụ)         │
│  (Xử lý quy tắc nghiệp vụ, tính toán, kiểm duyệt dữ liệu)   │
│                                                             │
│   ├── AdminUserBLL.java (Xác thực thông tin Login)          │
│   ├── CustomerBLL.java (Validate CCCD, Số điện thoại,...)   │
│   └── BankAccountBLL.java (Validate số dư, trạng thái thẻ)  │
└─────────│───────────────────────────────────────────────────┘
          │ (Đóng gói dữ liệu hợp lệ, yêu cầu truy xuất CSDL)
          ▼
┌─────────────────────────────────────────────────────────────┐
│  TIER 3: DATA ACCESS LAYER (DAL - Lớp Truy Cập Dữ Liệu)     │
│  (Giao tiếp trực tiếp với cơ sở dữ liệu)                    │
│                                                             │
│   ├── DBConnection.java (Cấu hình Driver JDBC kết nối)      │
│   ├── AdminUserDAO.java (Thực thi SQL SELECT login)         │
│   ├── CustomerDAO.java (Thực thi SQL CRUD Customer)         │
│   └── BankAccountDAO.java (Thực thi SQL CRUD BankAccount)   │
└─────────│───────────────────────────────────────────────────┘
          │ (Truyền các câu lệnh T-SQL)
          ▼
[ DATABASE SERVER: Microsoft SQL Server ]
   (Lưu trữ 3 bảng: AdminUser, Customer, BankAccount)
