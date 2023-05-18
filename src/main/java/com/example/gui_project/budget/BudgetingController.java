package com.example.gui_project.budget;

import com.example.gui_project.JdbcDao;
import com.example.gui_project.budget.BudgetingStrategy;
import com.example.gui_project.budget.EnvelopeBudgetingStrategy;
import com.example.gui_project.budget.ZeroBasedBudgetingStrategy;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.SQLException;

public class BudgetingController {



    @FXML
    TextField main_salary, other_salary, household, living, travel, insurance,zero_expense;

    @FXML
    Button next,income_button,submitButton_e,submitButton_z,analytics;

    @FXML
    RadioButton envelope, zero;



    //navigate to analytics
    @FXML
    public void getAnalytics(ActionEvent event) throws Exception {


        Stage owner = (Stage) analytics.getScene().getWindow();
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/analytics.fxml"));

        owner.setTitle("Analytics");
        owner.setScene(new Scene(root, 800, 500));
        owner.show();

    }

    //to insert zero expenses into user's database
    @FXML
    public void zero_submit(ActionEvent event) throws Exception {
        JdbcDao jdbcDao = new JdbcDao();
        Stage owner = (Stage) submitButton_z.getScene().getWindow();

        double income=jdbcDao.getIncome(jdbcDao.getUser());
        double expenses=Double.parseDouble(zero_expense.getText())+
                jdbcDao.getExpenses(jdbcDao.getUser());

        if(income<expenses)
        {
            showAlert(Alert.AlertType.CONFIRMATION, owner, "FAILED!",
                    "Expenses exceed your income!");
        }
        else {
            jdbcDao.insertExpense(jdbcDao.getUser(), "", Double.parseDouble(zero_expense.getText()));

            showAlert(Alert.AlertType.CONFIRMATION, owner, "DONE!",
                    "Expenses added successfully");
        }

    }

    //to insert envelope expenses into user's database
    @FXML
    public void envelope_submit(ActionEvent event) throws Exception {
        JdbcDao jdbcDao = new JdbcDao();
        Stage owner = (Stage) submitButton_e.getScene().getWindow();

        double income=jdbcDao.getIncome(jdbcDao.getUser());
        double expenses=Double.parseDouble(household.getText())+Double.parseDouble(living.getText())+
                Double.parseDouble(travel.getText())+Double.parseDouble(insurance.getText())+
                jdbcDao.getExpenses(jdbcDao.getUser());

        if(income<expenses)
        {
            showAlert(Alert.AlertType.CONFIRMATION, owner, "FAILED!",
                    "Expenses exceed your income!");
        }
        else {
            jdbcDao.insertExpense(jdbcDao.getUser(), "household bills", Double.parseDouble(household.getText()));
            jdbcDao.insertExpense(jdbcDao.getUser(), "living expenses", Double.parseDouble(living.getText()));
            jdbcDao.insertExpense(jdbcDao.getUser(), "travel", Double.parseDouble(travel.getText()));
            jdbcDao.insertExpense(jdbcDao.getUser(), "insurance", Double.parseDouble(insurance.getText()));

            showAlert(Alert.AlertType.CONFIRMATION, owner, "DONE!",
                    "Expenses added successfully");
        }

    }

    //to insert income into user's database
    @FXML
    public void income_next(ActionEvent event) throws Exception {
        JdbcDao jdbcDao = new JdbcDao();

        jdbcDao.insertIncome(jdbcDao.getUser(),Double.parseDouble(main_salary.getText()) ,Double.parseDouble(other_salary.getText()));

        Stage owner = (Stage) income_button.getScene().getWindow();
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/strategyPicker.fxml"));
        owner.setTitle("Strategy input");
        owner.setScene(new Scene(root, 800, 500));
        owner.show();
    }


    //for strategy radio buttons
    @FXML
    public void next(ActionEvent event) throws Exception
    {
        Stage owner = (Stage) next.getScene().getWindow();
        BudgetingStrategy strategy;

        if(envelope.isSelected())
        {
            strategy=new EnvelopeBudgetingStrategy();
            strategy.showScene(owner);
        }
        else if(zero.isSelected())
        {
            strategy=new ZeroBasedBudgetingStrategy();
            strategy.showScene(owner);
        }

    }



    //for input to be only numeric
    @FXML
    public void numericOnly(KeyEvent event) {
        if(main_salary!=null) {
            main_salary.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        main_salary.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
        }
        if(other_salary!=null) {
            other_salary.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        other_salary.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
        }
        if(household!=null) {
            household.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        household.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
        }
        if(living!=null) {
            living.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        living.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
        }
        if(travel!=null) {
            travel.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        travel.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
        }
        if(insurance!=null) {
            insurance.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        insurance.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
        }
        if(zero_expense!=null) {
            zero_expense.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(
                        ObservableValue<? extends String> observable,
                        String oldValue, String newValue) {
                    if (!newValue.matches("\\d*")) {
                        zero_expense.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
        }
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
