module com.librarysystem.librarysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires org.controlsfx.controls;

    opens com.librarysystem.librarysystem to javafx.fxml;
    exports com.librarysystem.librarysystem;
}