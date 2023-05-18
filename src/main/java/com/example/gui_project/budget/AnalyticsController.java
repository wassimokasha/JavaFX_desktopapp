package com.example.gui_project.budget;

import com.example.gui_project.JdbcDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;

public class AnalyticsController {
    @FXML
    Text income,spending,spare,household,living,travel,insurance;

    @FXML
    Button back;


    @FXML
    public void logout_button(ActionEvent event) throws Exception {
        Stage own = (Stage) back.getScene().getWindow();

        Parent root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/login_form.fxml"));
        own.setTitle("Login form.");
        own.setScene(new Scene(root, 800, 500));
        own.show();
    }

    //navigate to expenses from analytics
    @FXML
    public void back_button(ActionEvent event) throws Exception{
        JdbcDao jdbcDao=new JdbcDao();
        if(jdbcDao.checkBudgetType(jdbcDao.getUser()))
        {
            Stage own = (Stage) back.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/envelope_expenses.fxml"));
            own.setTitle("Envelope expenses");
            own.setScene(new Scene(root, 800, 500));
            own.show();
        }
        else{
            Stage own = (Stage) back.getScene().getWindow();

            Parent root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/zero_expenses.fxml"));
            own.setTitle("Zero-based expenses");
            own.setScene(new Scene(root, 800, 500));
            own.show();
        }
    }

    public void initialize()
    {
        JdbcDao jdbcDao = new JdbcDao();

        double i=jdbcDao.getIncome(jdbcDao.getUser());
        double s=jdbcDao.getExpenses(jdbcDao.getUser());
        double sp=i-s;

        income.setText(String.valueOf(i));
        spending.setText(String.valueOf(s));
        spare.setText(String.valueOf(sp));
        DecimalFormat df=new DecimalFormat("#.##");

        if(jdbcDao.checkBudgetType(jdbcDao.getUser()))
        {
            household.setText(Double.parseDouble(df.format((jdbcDao.getSpecificExpense(jdbcDao.getUser(),"household bills")/i)*100)) + "% on household bills.");
            living.setText(Double.parseDouble(df.format((jdbcDao.getSpecificExpense(jdbcDao.getUser(),"living expenses")/i)*100)) + "% on living expenses.");
            travel.setText(Double.parseDouble(df.format((jdbcDao.getSpecificExpense(jdbcDao.getUser(),"travel")/i)*100)) + "% on travel.");
            insurance.setText(Double.parseDouble(df.format((jdbcDao.getSpecificExpense(jdbcDao.getUser(),"insurance")/i)*100)) + "% on insurance.");

        }
        else {
            household.setText(Double.parseDouble(df.format((jdbcDao.getSpecificExpense(jdbcDao.getUser(),"")/i)*100)) + "% of your total salary.");
        }
    }
}
