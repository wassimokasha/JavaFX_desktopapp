package com.example.gui_project.budget;

import javafx.stage.Stage;

import java.io.IOException;

public interface BudgetingStrategy {
    public void showScene(Stage owner) throws IOException;
}
