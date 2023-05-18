package com.example.gui_project.login;

import java.io.IOException;
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

public class LoginController {

    @FXML
    public Hyperlink registerLink;

    @FXML
    private TextField emailIdField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button submitButton;

    //login button
    @FXML
    public void login(ActionEvent event) throws SQLException, IOException {

        Window owner = submitButton.getScene().getWindow();



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

        String emailId = emailIdField.getText();
        String password = passwordField.getText();

        FXMLLoader loader;
        Parent root;
        JdbcDao jdbcDao = new JdbcDao();
        if(jdbcDao.checkRecord(emailId, password)!=-1)
        {
            int id=jdbcDao.checkRecord(emailId, password);
            jdbcDao.setUser(id);
            if(jdbcDao.checkIncome(emailId)){

                root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/income_input.fxml"));
                Stage own = (Stage) submitButton.getScene().getWindow();
                own.setTitle("Income input");
                own.setScene(new Scene(root, 800, 500));
                own.show();
            }
            else{
                if(jdbcDao.checkBudgetType(id))
                {
                    Stage own = (Stage) submitButton.getScene().getWindow();

                    root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/envelope_expenses.fxml"));
                    own.setTitle("Envelope expenses");
                    own.setScene(new Scene(root, 800, 500));
                    own.show();
                }
                else{
                    Stage own = (Stage) submitButton.getScene().getWindow();

                    root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/zero_expenses.fxml"));
                    own.setTitle("Zero-based expenses");
                    own.setScene(new Scene(root, 800, 500));
                    own.show();
                }
            }

        }
        else{
            showAlert(Alert.AlertType.CONFIRMATION, owner, "Login unsuccessful!",
                    "Incorrect email or password!");
        }

    }

    //takes us to registration form
    @FXML
    public void handleLinkClick(ActionEvent event) throws Exception {
        Stage owner = (Stage) registerLink.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/registration_form.fxml"));
        owner.setTitle("User Registration");
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
