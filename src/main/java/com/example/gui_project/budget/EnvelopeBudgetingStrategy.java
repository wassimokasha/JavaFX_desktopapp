package com.example.gui_project.budget;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EnvelopeBudgetingStrategy implements BudgetingStrategy{
    @Override
    public void showScene(Stage owner) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/gui_project/envelope_expenses.fxml"));
        owner.setTitle("Envelope expenses");
        owner.setScene(new Scene(root, 800, 500));
        owner.show();
    }
}
