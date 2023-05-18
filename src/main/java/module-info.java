module com.example.gui_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.gui_project to javafx.fxml;
    exports com.example.gui_project;
    exports com.example.gui_project.registration;
    opens com.example.gui_project.registration to javafx.fxml;
    exports com.example.gui_project.login;
    opens com.example.gui_project.login to javafx.fxml;
    exports com.example.gui_project.budget;
    opens com.example.gui_project.budget to javafx.fxml;
}