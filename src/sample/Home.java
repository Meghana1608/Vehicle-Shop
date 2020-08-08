package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Home implements Initializable {
    public ComboBox comboo;
    public AnchorPane employee_pane;
    public TextField employee_id;
    public TextField employee_name;
    public TextField employee_number;
    public TextField password;
    public AnchorPane model_pane;
    public TextField sl_no;
    public TextField model_name;
    public TextField model_number;
    public TextField hrs;
    public TextField mnths;
    public AnchorPane cust_pane;
    public TextField cust_id;
    public DatePicker date_of_sale;
    public TextField cust_name;
    public TextField father_name;
    public TextField village;
    public TextField tehlsi;
    public TextField mobile_number;
    public ComboBox model;
    public TextField engine_number;
    public TextField file_number;
    public TextField model_number1;
    public DatePicker service_date;

    public static int incidArray;
    Connection connection = null;
    public  static LocalDate today = LocalDate.now(ZoneId.of("Indian/Maldives"));


   //to choose value from combo box
    public void perform(ActionEvent actionEvent) throws IOException {
        String string1=comboo.getValue().toString();
        switch (string1){

            case "My Profile":
               // ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root,800,650));
                stage.show();
                break;
            //1

            case "Change Password":
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("updatelogin.fxml"));
                Parent root1 = (Parent) fxmlLoader2.load();
                Stage stage1 = new Stage();
                stage1.setScene(new Scene(root1,775,650));
                stage1.show();
                break;
            //2

            case "Logout" :
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                fxmlLoader2 = new FXMLLoader(getClass().getResource("sample.fxml"));
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
        comboo.getItems().addAll("My Account","My Profile", "Change Password","Logout", "Exit");
        comboo.getSelectionModel().select("My Account");
    }


    // start of employee
    public void open_employee(ActionEvent actionEvent) {

        employee_pane.setVisible(true);
        model_pane.setVisible(false);
        cust_pane.setVisible(false);
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
            alert1.showAndWait();
        }


         else if (employee_number.getText().trim().isEmpty() || employee_number.getText().trim().length() != 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER VALID MOBILE NUMBER");
            alert1.showAndWait();
         }

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


    // start of model
    public void open_model(ActionEvent actionEvent) {
        model_pane.setVisible(true);
        employee_pane.setVisible(false);
        cust_pane.setVisible(false);
    }

    public void hrs_to_mon(KeyEvent keyEvent) {
        int time_in_hrs, days , months;
        time_in_hrs = Integer.parseInt(String.valueOf(hrs.getText()));
        days = (int) (time_in_hrs * 0.0417);
        months = (int) (days * 0.032855);
        mnths.setText(String.valueOf(months));
    }


    void refresh_model(){
        sl_no.clear();
        model_name.clear();
        model_number.clear();
        hrs.clear();
        mnths.clear();
    }

    public void add_models(ActionEvent actionEvent) throws Exception {
        connection = DBConnection.getConnection();
        if (model_name.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER MODEL NAME");
            alert1.showAndWait();
        }

        else if (model_number.getText().trim().isEmpty() ) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER MODEL NUMBER");
            alert1.showAndWait(); 
        }

       else {
            try {
                int i = 0;
                String query1 = "Insert into models(sl_no, model_name, model_number, service_duration_in_hrs, " +
                        "service_duration_in_months) values(?,?,?,?,?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(1, sl_no.getText().trim());
                preparedStatement1.setString(2, model_name.getText().trim());
                preparedStatement1.setString(3, model_number.getText().trim());
                preparedStatement1.setString(4, hrs.getText().trim());
                preparedStatement1.setString(5, mnths.getText().trim());

                i=preparedStatement1.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("MODEL INFORMATION ADDED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_model(); }
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
                } } }
    }



    public void view_models(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("models.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        System.out.println("Model Details");
        Stage stage = new Stage();
        stage.setTitle("Model Details");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }


    public void generate_sl_no(MouseEvent mouseEvent) {
        int i = 0;
        try {
            connection = DBConnection.getConnection();
            String empIdIs = "";
            Statement stm1 = connection.createStatement();

            ResultSet rs = stm1.executeQuery("select max(sl_no) as id from models");
            while (rs.next()) {
                empIdIs = rs.getString("id");
                i++;
            }

            if (empIdIs != null && i > 0) {
                incidArray = (Integer.parseInt(empIdIs) + 1);
                sl_no.setText(String.valueOf(incidArray));

            } else {
                sl_no.setText(String.valueOf(1));
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


    // start of customer
    public void open_cust(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        employee_pane.setVisible(false);
        model_pane.setVisible(false);
        cust_pane.setVisible(true);
        modelload();
    }

    void modelload() throws SQLException, ClassNotFoundException {
        try {
            connection = DBConnection.getConnection();
            model.getItems().removeAll(model.getItems());
            String query1 = "select model_name from models group by model_name order by model_name ASC";
            ArrayList<String> gstList = new ArrayList<String>();
            ResultSet set = connection.createStatement().executeQuery(query1);
            while (set.next()) {
                gstList.add(set.getString(1));
            }

            for (int a = 0; a < gstList.size(); a++) {
                String a1 = gstList.get(a);
                for (int b = a + 1; b < gstList.size(); b++) {
                    String b1 = gstList.get(b);
                    if (a1.equals(b1)) {
                        String temp = a1;
                        a1 = b1;
                        b1 = temp;
                        gstList.set(a, a1);
                        gstList.set(b, b1);
                    }
                }
            }

            for (int i = 0; i < gstList.size(); i++) {
                model.getItems().add(gstList.get(i));
            }
            if (gstList.size() > 0) {
                model.setValue(gstList.get(0));
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    public void gen_cust_id(MouseEvent mouseEvent) {
        int i = 0;
        try {
            connection = DBConnection.getConnection();
            String empIdIs = "";
            Statement stm1 = connection.createStatement();

            ResultSet rs = stm1.executeQuery("select max(id) as id from customers");
            while (rs.next()) {
                empIdIs = rs.getString("id");
                i++;
            }

            if (empIdIs != null && i > 0) {
                incidArray = (Integer.parseInt(empIdIs) + 1);
                cust_id.setText(String.valueOf(incidArray));

            } else {
                cust_id.setText(String.valueOf(1));
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

    public void add_customers(ActionEvent actionEvent) throws Exception {

        connection = DBConnection.getConnection();

        if (cust_name.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER CUSTOMER NAME");
            alert1.showAndWait();}

        else if (mobile_number.getText().trim().isEmpty() || mobile_number.getText().trim().length() != 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER VALID MOBILE NUMBER");
            alert1.showAndWait(); }

         else if (father_name.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER FATHER NAME");
            alert1.showAndWait();
        }
         else {

            try {

                int i = 0;

                String query1 = "Insert into customers(id, date_of_sale, customer_name, father_name, " +
                        "village, tehlsi, mobile_number, model, engine_number, file_number, model_number, service_date) values(?,?,?,?,?   ,?,?,?,?,?,  ?,?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(1, cust_id.getText().trim());
                preparedStatement1.setString(2, String.valueOf(date_of_sale.getValue()));
                preparedStatement1.setString(3, cust_name.getText().trim());
                preparedStatement1.setString(4, father_name.getText().trim());
                preparedStatement1.setString(5, village.getText().trim());
                preparedStatement1.setString(6, tehlsi.getText().trim());
                preparedStatement1.setString(7, mobile_number.getText().trim());
                preparedStatement1.setString(8, String.valueOf(model.getValue()));
                preparedStatement1.setString(9, engine_number.getText().trim());
                preparedStatement1.setString(10, file_number.getText().trim());
                preparedStatement1.setString(11, model_number1.getText().trim());
                preparedStatement1.setString(12, String.valueOf(service_date.getValue()));


                i=preparedStatement1.executeUpdate();

                if (i > 0 ) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("CUSTOMER INFORMATION ADDED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_customer(); }
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

    void refresh_customer(){
        cust_id.clear();
        cust_name.clear();
        father_name.clear();
        village.clear();
        tehlsi.clear();
        mobile_number.clear();
        engine_number.clear();
        file_number.clear();
        date_of_sale.setValue(today);
        service_date.setValue(today);
        model_number.clear();
    }


    public void view_customers(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customers.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        System.out.println("Customer details");
        Stage stage = new Stage();
        stage.setTitle("customer_details");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }

    public void open_import_excel_file(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("import_from_excel.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        System.out.println("Customer details");
        Stage stage = new Stage();
        stage.setTitle("customer_details");
        stage.setScene(new Scene(root1, 900, 700));
        stage.show();
    }
}
