package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sun.security.util.Password;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Home implements Initializable {
    public ComboBox comboo;
    public AnchorPane employee_pane;
    public TextField employee_id;
    public TextField employee_name;
    public TextField employee_number;
    public TextField password;
    public Pane view_reg_pane;
    public Pane edit_pane;
    public TextField employee_Id;
    public TextField email_id;
    public TextField m_no;
    public TextField address;
    public TextField old_username;
    public TextField new_username;
    public TextField c_password;
    Connection connection = null;
    public static String idArray = "";
    public static int incidArray;

    public void perform(ActionEvent actionEvent) throws IOException {
        String string1=comboo.getValue().toString();
        switch (string1){

            case "My Profile":
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root,775,650));
                stage.show();
                break;
            //1

            case "Change Password":
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("updatelogin.fxml"));
                Parent root1 = (Parent) fxmlLoader1.load();
                Stage stage1 = new Stage();
                stage1.setScene(new Scene(root1,775,650));
                stage1.show();
                break;
            //2

            case "Logout" :
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("sample.fxml"));
                Parent root2 = (Parent) fxmlLoader2.load();
                Stage stage2 = new Stage();
                stage2.setScene(new Scene(root2,775,650));
                stage2.show();
                break;
            //3

            case "Exit" :
                Platform.exit();
                break;
            //4
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboo.getItems().removeAll(comboo.getItems());
        comboo.getItems().addAll("My Profile", "Change Password","Logout", "Exit");

    }

    public void open_employee(ActionEvent actionEvent) {

        employee_pane.setVisible(true);


    }

    public void create_id(MouseEvent mouseEvent) {
        int i = 0;
        try {
            connection = DBConnection.getConnection();
            String empIdIs = "";
            Statement stm1 = connection.createStatement();

            ResultSet rs = stm1.executeQuery("select max(e_id) as id from employee");
            while (rs.next()) {
                empIdIs = rs.getString("id");
                i++;
            }
            if (empIdIs != null && i > 0) {

                incidArray = (Integer.parseInt(empIdIs) + 1);
                employee_id.setText(String.valueOf(incidArray));

            } else {
                employee_id.setText(String.valueOf(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (connection != null) {
                    connection.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add_emp(ActionEvent actionEvent) throws Exception {
        connection = DBConnection.getConnection();
        String query = "select username from login";
        ResultSet rs = connection.createStatement().executeQuery(query);
        ArrayList<String> name = new ArrayList();
        boolean employee_found = false;
        while (rs.next()) {
            name.add(rs.getString(1));
        }

        for (int i = 0; i < name.size(); i++) {
            if (name.get(i).equalsIgnoreCase(employee_name.getText().trim())) {
                employee_found = true;
                break;
            }
        }

        if (employee_name.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER EMPLOYEE NAME");
            alert1.showAndWait();}

//        } else if (!Password.getText().trim().equals(Confpassword.getText().trim())) {
//            Alert alert1 = new Alert(Alert.AlertType.ERROR);
//            alert1.setContentText("PASSWORD AND CONFIRM PASSWORD NOT EQUAL");
//            alert1.showAndWait(); }

         else if (employee_number.getText().trim().isEmpty() || employee_number.getText().trim().length() != 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER VALID MOBILE NUMBER");
            alert1.showAndWait(); }

//         else if (Empaddress.getText().trim().isEmpty()) {
//            Alert alert1 = new Alert(Alert.AlertType.ERROR);
//            alert1.setContentText("PLEASE ENTER ADDRESS");
//            alert1.showAndWait();
//        }

         else if (employee_found == true) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("USERNAME ALREADY EXIST TRY WITH OTHER USERNAME");
            alert1.showAndWait();
        } else {

            try {

                int i = 0, j = 0;

                String query1 = "Insert into employee(e_id, e_name, username, password, " +
                        "e_pnumber) values(?,?,?,?,?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(1, employee_id.getText().trim());
                preparedStatement1.setString(2, employee_name.getText().trim());
                preparedStatement1.setString(3, employee_name.getText().trim());
                preparedStatement1.setString(4, password.getText().trim());
                preparedStatement1.setString(5, employee_number.getText().trim());


                i=preparedStatement1.executeUpdate();

                String query2 = "Insert into login(username,password,login_type) values(?,?,?)";
                PreparedStatement preparedStatement2 = connection.prepareStatement(query2);

                preparedStatement2.setString(1, employee_name.getText().trim());
                preparedStatement2.setString(2, password.getText().trim());
                preparedStatement2.setString(3, "employee");

                j = preparedStatement2.executeUpdate();

                if (i > 0 && j > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("EMPLOYEE INFORMATION ADDED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_employee(); }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING DATA");
                    alert.showAndWait(); }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close(); }

                } catch (Exception e) {
                    e.printStackTrace();
                } } } }

                void refresh_employee(){
                    employee_id.clear();
                    employee_name.clear();
                    employee_number.clear();
                    password.clear();
                }



    public void view_emp(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("employee.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        System.out.println("Employee details");
        Stage stage = new Stage();
        stage.setTitle("emp_details");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();

    }

    public void gen_reg_rep(ActionEvent actionEvent) {
        view();
    }

    public void view()
    {
        String query="select * from employee";
        try {
            LoadingTableViewDataSelectedRowName.Welcome(query, view_reg_pane,250,950);
            edit_pane.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void employeeReprtPDF(ActionEvent actionEvent) throws Exception {

        String query = "SELECT * FROM employee";

        File dir = new File(Check.drive_name()+Configuring_Path.FOLDER_PATH);
        dir.mkdir();
        File dir2=new File(Check.drive_name()+Configuring_Path.FOLDER_PATH+"EMPLOYEE_REPORT");
        dir2.mkdir();
        String path=Check.drive_name()+Configuring_Path.FOLDER_PATH+"/EMPLOYEE_REPORT/Employee_report.csv";

        Controller.export_excel(query,path);
    }

    public void edit_employee(ActionEvent actionEvent) {
    }

    public void delete_employee(ActionEvent actionEvent) {
    }

    public void save_employee(ActionEvent actionEvent) {
    }
}
