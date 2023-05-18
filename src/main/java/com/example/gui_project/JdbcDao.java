package com.example.gui_project;

import javafx.scene.control.Alert;

import java.sql.*;

public class JdbcDao {

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/registration?useSSL=false";
    private static final String DATABASE_USERNAME = "root";
    private static final String DATABASE_PASSWORD = "wassimokasha1";
    private static final String INSERT_QUERY = "INSERT INTO registration (full_name, email_id, password) VALUES (?, ?, ?)";
    private static final String INSERT_INCOME = "INSERT INTO incomes (user_id, main, other) VALUES (?, ?, ?)";
    private static final String DELETE_USER = "DELETE FROM user";
    private static final String INSERT_USER = "INSERT INTO user (user_id) VALUES (?)";
    private static final String INSERT_ENVELOPE = "INSERT INTO expenses (user_id, category, expense) VALUES (?, ?, ?)";

    //get specific expense
    public double getSpecificExpense(int user_id,String category)
    {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            String query = "SELECT expense FROM expenses WHERE user_id=? and category=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setString(2, category);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                return rs.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    //get expenses
    public double getExpenses(int user_id)
    {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            String query = "SELECT sum(expense) FROM expenses WHERE user_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                return rs.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    //get income
    public double getIncome(int user_id)
    {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            String query = "SELECT main,other FROM incomes WHERE user_id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);

            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
                return rs.getInt(1)+rs.getInt(2);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    //to insert expenses into user's database
    public void insertExpense(int user_id, String category, double expense)
    {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            String query = "SELECT * FROM expenses WHERE user_id=? and category=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);
            pstmt.setString(2, category);


            ResultSet rs = pstmt.executeQuery();
            if(rs.next())
            {
                try (Connection connn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
                    query = "UPDATE expenses SET expense=expense+? WHERE user_id=? and category=?";
                    pstmt = connn.prepareStatement(query);
                    pstmt.setDouble(1, expense);
                    pstmt.setInt(2, user_id);
                    pstmt.setString(3, category);

                    pstmt.executeUpdate();
                    return;


                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ENVELOPE)) {
            preparedStatement.setInt(1, user_id);
            preparedStatement.setString(2, category);
            preparedStatement.setDouble(3, expense);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    //sets active user
    public void setUser(int user_id)
    {
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            printSQLException(e);
        }
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            preparedStatement.setInt(1, user_id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    //gets active user
    public int getUser()
    {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            String query = "SELECT user_id FROM user";
            PreparedStatement pstmt = conn.prepareStatement(query);

            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    //check which strategy is associated with the user
    public boolean checkBudgetType(int user_id)
    {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            String query = "SELECT category FROM expenses WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user_id);

            ResultSet rs = pstmt.executeQuery();
            rs.next();

            if (!rs.getString(1).equals("")) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    //insert income into income table
    public void insertIncome(int user_id, double main, double other) throws SQLException
    {
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INCOME)) {
            preparedStatement.setInt(1, user_id);
            preparedStatement.setDouble(2, main);
            preparedStatement.setDouble(3, other);


            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    //check if income is null to take us to income page
    public boolean checkIncome(String emailId) throws SQLException{
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            String query = "SELECT * FROM incomes WHERE user_id = (SELECT id FROM registration WHERE email_id=?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, emailId);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return true;
    }

    //checks if login info is correct
    public int checkRecord(String emailId, String password) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD)) {
            String query = "SELECT * FROM registration WHERE email_id = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, emailId);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();


            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return -1;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    //inserts new record when user registers
    public void insertRecord(String fullName, String emailId, String password) throws SQLException {

        // Step 1: Establishing a Connection and
        // try-with-resource statement will auto close the connection.
        try (Connection connection = DriverManager
                .getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);

             // Step 2:Create a statement using connection object
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            preparedStatement.setString(1, fullName);
            preparedStatement.setString(2, emailId);
            preparedStatement.setString(3, password);

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // print SQL exception information
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}