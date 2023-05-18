package com.example.gui_project.registration;

import java.sql.SQLException;

import com.example.gui_project.JdbcDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;

public class RegisterController {

    @FXML
    public Hyperlink loginLink;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    //register button
    @FXML
    public void register(ActionEvent event) throws SQLException, Exception{

        Stage owner = (Stage)submitButton.getScene().getWindow();

        //System.out.println(fullNameField.getText());
        //System.out.println(emailIdField.getText());
        //System.out.println(passwordField.getText());
        if (fullNameField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your name");
            return;
        }

        if (emailIdField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter your email id");
            return;
        }
        if (passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!",
                    "Please enter a password");
            return;
        }

        String fullName = fullNameField.getText();
        String emailId = emailIdField.getText();
        String password = passwordField.getText();

        JdbcDao jdbcDao = new JdbcDao();
        jdbcDao.insertRecord(fullName, emailId, password);

        showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!",
                "Welcome " + fullNameField.getText());


        //takes us to login form when user finishes registration
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/login_form.fxml"));
        owner.setTitle("User Login");
        owner.setScene(new Scene(root, 800, 500));
        owner.show();
    }

    //takes us to login form
    @FXML
    public void handleLinkClick(ActionEvent event) throws Exception {
        Stage owner = (Stage) loginLink.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/login_form.fxml"));
        owner.setTitle("User Login");
        owner.setScene(new Scene(root, 800, 500));
        owner.show();

    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
