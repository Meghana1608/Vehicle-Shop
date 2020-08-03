package sample;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
//import static sample.LoadingDataToTableView.Welcome;
import static sample.digital_clock.backupDataWithDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;

public class Controller {

    public JFXTextField username;
    public JFXPasswordField password;

    public static String userNameArray;
    public static String passwordArray;
    public static String loginUserName = "";
    public  static LocalDate today = LocalDate.now(ZoneId.of("Indian/Maldives"));
    public static String userEmpName;

    public static String getUserEmpName() {
        return userEmpName;
    }

    public static void setUserEmpName(String userEmpName) {
        Controller.userEmpName = userEmpName;
    }

    Connection connection = null;

    public void login(ActionEvent actionEvent) throws SQLException {
        try {
            //to clear output text file(logger txt file)

            connection = DBConnection.getConnection();

            Statement statement = connection.createStatement();

            String username1 = username.getText().trim().toString();
            String password1 = password.getText().trim().toString();

            loginUserName = username1;
            setUserEmpName(loginUserName);
            String user_type = "";
            int i = 0, j = 0, k = 0;
            ResultSet r = statement.executeQuery("select * from durability");
            while (r.next()) {
                j++;
            }
            LocalDate date1 = LocalDate.now();
            LocalDate date2 = LocalDate.now().plusDays(7);

            if (j == 0) {
                connection = DBConnection.getConnection();
                String check_login = "Insert into durability (a_date,e_date) values(?,?)";
                PreparedStatement preparedStatement_1 = connection.prepareStatement(check_login);
                preparedStatement_1.setString(1, String.valueOf(date1));
                preparedStatement_1.setString(2, String.valueOf(date2));
                preparedStatement_1.execute();
            } else {
                Statement s = connection.createStatement();
                ResultSet rs = s.executeQuery("select * from durability where e_date<='" + date1 + "'");
                while (rs.next()) {
                    k++;
                }
            }

            Statement statement2 = connection.createStatement();
            ResultSet res = statement2.executeQuery("Select * from login where username='" + username1 + "' and password='" + password1 + "'");

            while (res.next()) {
                userNameArray = res.getString("username");
                passwordArray = res.getString("password");
                user_type = res.getString("login_type");
                i++;
            }
            if (i == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("Username and Password Doesn't Match, please confirm");
                alert.showAndWait();
            } else if (k > 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setContentText("YOURS TRAIL VERSION COMPLETED");
                alert.showAndWait();
            } else {
                if (userNameArray.equals(username1) && passwordArray.equals(password1) && k == 0 && user_type.equalsIgnoreCase("admin")) {
                    backupDataWithDatabase();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Login");
                    alert.setContentText("Login Successful");
                    alert.showAndWait();

                    ((Node) actionEvent.getSource()).getScene().getWindow().hide();

                    Stage stage = new Stage();
                    stage.setTitle("Admin");
                    stage.setScene(new Scene(root1, 1100, 700));
                    stage.show();

                } else if (user_type.equals("employee")) {
                    if (userNameArray.equals(username) && passwordArray.equals(password) && k == 0) {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employee.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Login");
                        alert.setContentText("Login Successful");
                        alert.showAndWait();


                        ((Node) actionEvent.getSource()).getScene().getWindow().hide();

                        Stage stage = new Stage();
                        stage.setTitle("Employee");
                        stage.setScene(new Scene(root1, 1100, 700));
                        stage.show();
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("INVALID USER");
                    alert.showAndWait();
                    System.out.println("Meghana");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }


    }
}
