package com.project01.bankaccountmanagement.presentation;

import com.project01.bankaccountmanagement.bll.CustomerBLL;
import com.project01.bankaccountmanagement.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    // ─── TableView ───────────────────────────────
    @FXML private TableView<Customer>              tblCustomers;
    @FXML private TableColumn<Customer, Integer>   colId;
    @FXML private TableColumn<Customer, String>    colFullName;
    @FXML private TableColumn<Customer, String>    colIdentityCard;
    @FXML private TableColumn<Customer, String>    colPhone;
    @FXML private TableColumn<Customer, String>    colEmail;
    @FXML private TableColumn<Customer, String>    colAddress;

    // ─── Form fields ─────────────────────────────
    @FXML private VBox      rowCustomerId;
    @FXML private TextField txtCustomerId;
    @FXML private TextField txtFullName;
    @FXML private TextField txtIdentityCard;
    @FXML private TextField txtPhone;
    @FXML private TextField txtEmail;
    @FXML private TextField txtAddress;

    // ─── Toolbar ─────────────────────────────────
    @FXML private TextField txtSearch;
    @FXML private Button    btnAdd;

    // ─── Nút cố định ─────────────────────────────
    // btnPrimary: luôn visible, text đổi "Lưu" ↔ "Cập nhật"
    // btnDelete : managed=true luôn (giữ space), chỉ toggle visible
    @FXML private Button btnPrimary;
    @FXML private Button btnDelete;

    // ─── Labels ──────────────────────────────────
    @FXML private Label lblFormTitle;
    @FXML private Label lblTotalRecords;

    private final CustomerBLL              customerBLL;
    private final ObservableList<Customer> customerList;
    private boolean                        isEditMode = false;

    public CustomerFormController() {
        this.customerBLL  = new CustomerBLL();
        this.customerList = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ánh xạ cột
        colId.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        colFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        colIdentityCard.setCellValueFactory(new PropertyValueFactory<>("identityCard"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tblCustomers.setItems(customerList);

        // Click dòng → chế độ chỉnh sửa
        tblCustomers.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        fillForm(newVal);
                        switchToEditMode();
                    }
                }
        );

        // Lọc tức thì khi gõ tìm kiếm
        txtSearch.textProperty().addListener(
                (obs, old, newText) -> filterCustomers(newText)
        );

        // Khởi động ở chế độ Thêm mới
        switchToAddMode();
        loadData();
    }

    // ─────────────────────────────────────────────
    //  CHUYỂN CHẾ ĐỘ
    // ─────────────────────────────────────────────

    /**
     * Add mode:
     *   - rowCustomerId : visible=false, managed=true → layout KHÔNG dịch chuyển
     *   - btnPrimary    : text "Lưu", luôn visible
     *   - btnDelete     : visible=false, managed=true → giữ space cố định
     */
    private void switchToAddMode() {
        isEditMode = false;
        clearForm();
        txtCustomerId.setPromptText("(Tự động tạo)");
        rowCustomerId.setVisible(true);
        btnPrimary.setText("Lưu");
        btnDelete.setVisible(false);       // managed=true → giữ nguyên khoảng trống, nút Lưu ở cùng vị trí
        lblFormTitle.setText("Thêm Khách Hàng");
        tblCustomers.getSelectionModel().clearSelection();
    }

    /**
     * Edit mode:
     *   - txtCustomerId : hiển thị mã ID
     *   - btnPrimary    : cùng vị trí, đổi text thành "Cập nhật"
     *   - btnDelete     : hiện ra ngay phía dưới
     */
    private void switchToEditMode() {
        isEditMode = true;
        txtCustomerId.setPromptText("");
        rowCustomerId.setVisible(true);
        btnPrimary.setText("Cập nhật");
        btnDelete.setVisible(true);
        lblFormTitle.setText("Chỉnh Sửa Khách Hàng");
    }

    // ─────────────────────────────────────────────
    //  DỮ LIỆU
    // ─────────────────────────────────────────────

    private void loadData() {
        try {
            List<Customer> list = customerBLL.getAllCustomers();
            customerList.setAll(list);
            lblTotalRecords.setText(list.size() + " bản ghi");
        } catch (Exception e) {
            showError("Lỗi tải dữ liệu", e.getMessage());
        }
    }

    private void filterCustomers(String keyword) {
        try {
            List<Customer> list = customerBLL.searchCustomers(keyword);
            customerList.setAll(list);
            lblTotalRecords.setText(list.size() + " bản ghi");
        } catch (Exception e) {
            System.err.println("Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    private void fillForm(Customer c) {
        txtCustomerId.setText(String.valueOf(c.getCustomerID()));
        txtFullName.setText(c.getFullName());
        txtIdentityCard.setText(c.getIdentityCard());
        txtPhone.setText(c.getPhone());
        txtEmail.setText(c.getEmail() != null ? c.getEmail() : "");
        txtAddress.setText(c.getAddress() != null ? c.getAddress() : "");
    }

    private void clearForm() {
        txtCustomerId.clear();
        txtFullName.clear();
        txtIdentityCard.clear();
        txtPhone.clear();
        txtEmail.clear();
        txtAddress.clear();
    }

    // ─────────────────────────────────────────────
    //  SỰ KIỆN
    // ─────────────────────────────────────────────

    /** Nút "Thêm mới" góc trên: luôn về add mode */
    @FXML
    void handleAdd(ActionEvent event) {
        switchToAddMode();
    }

    /** Nút chính (btnPrimary): "Lưu" hoặc "Cập nhật" tùy chế độ */
    @FXML
    void handlePrimary(ActionEvent event) {
        if (isEditMode) {
            // Cập nhật
            try {
                int id = Integer.parseInt(txtCustomerId.getText().trim());
                Customer c = new Customer(id,
                        txtFullName.getText(),
                        txtIdentityCard.getText(),
                        txtPhone.getText(),
                        txtEmail.getText(),
                        txtAddress.getText()
                );
                customerBLL.updateCustomer(c);
                showInfo("Đã cập nhật thông tin khách hàng mã " + id + ".");
                loadData();
                switchToAddMode();
            } catch (Exception e) {
                showError("Không thể cập nhật", e.getMessage());
            }
        } else {
            // Lưu mới
            try {
                Customer c = new Customer(
                        txtFullName.getText(),
                        txtIdentityCard.getText(),
                        txtPhone.getText(),
                        txtEmail.getText(),
                        txtAddress.getText()
                );
                customerBLL.addCustomer(c);
                showInfo("Đã thêm khách hàng: " + c.getFullName());
                loadData();
                switchToAddMode();
            } catch (Exception e) {
                showError("Không thể thêm khách hàng", e.getMessage());
            }
        }
    }

    /** Nút "Xóa" (chỉ hiện ở edit mode) */
    @FXML
    void handleDelete(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtCustomerId.getText().trim());
            String name = txtFullName.getText();

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Xác nhận xóa");
            confirm.setHeaderText("Xóa khách hàng: " + name);
            confirm.setContentText("Thao tác này không thể hoàn tác. Bạn có chắc chắn không?");
            Optional<ButtonType> result = confirm.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                customerBLL.deleteCustomer(id);
                showInfo("Đã xóa khách hàng: " + name);
                loadData();
                switchToAddMode();
            }
        } catch (Exception e) {
            showError("Không thể xóa", e.getMessage());
        }
    }

    /** Nút "Tìm kiếm" */
    @FXML
    void handleSearch(ActionEvent event) {
        filterCustomers(txtSearch.getText());
    }

    /** Nút "Làm mới" */
    @FXML
    void handleRefresh(ActionEvent event) {
        txtSearch.clear();
        loadData();
        switchToAddMode();
    }

    // ─────────────────────────────────────────────
    //  TIỆN ÍCH
    // ─────────────────────────────────────────────

    private void showInfo(String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thành công");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Lỗi");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
