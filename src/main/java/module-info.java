module com.project01.bankaccountmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    requires java.sql;

    opens com.project01.bankaccountmanagement.presentation to javafx.fxml;
    opens com.project01.bankaccountmanagement.model to javafx.base;

    exports com.project01.bankaccountmanagement.presentation;
    exports com.project01.bankaccountmanagement.model;
    exports com.project01.bankaccountmanagement.bll;
}