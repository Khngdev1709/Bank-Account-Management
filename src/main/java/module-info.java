module com.project01.bankaccountmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.project01.bankaccountmanagement to javafx.fxml;
    exports com.project01.bankaccountmanagement;
}