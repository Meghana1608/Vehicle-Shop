package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import sample.dao.CustomerDao;
import sample.dto.CustomerDto;
import sample.sms.SMS;
import sun.security.util.Password;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import static sample.LoadingTableViewDataSelectedRowName.Welcome;

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
    public AnchorPane sms_pane;
    public DatePicker from_date;
    public DatePicker to_date;
    public Pane renewal_cust_pane;
    public TextArea text_msg;
    public AnchorPane renewal_pane;
    public TextField cust_nameee;
    public TextField cust_mobileee;
    public TextField cust_engineee;
    public TextField cust_modelll;
    public DatePicker cust_serviceee;
    public Pane view_renewal_cust;
    public TextField days;
    public TextArea feedback_box;
    public TextArea feedback;


    Connection connection = null;
    public static LocalDate today = LocalDate.now(ZoneId.of("Indian/Maldives"));
    private Object Date;


    //to choose value from combo box
    public void perform(ActionEvent actionEvent) throws IOException {
        String string1 = comboo.getValue().toString();
        switch (string1) {

            case "My Profile":
                // ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("profile.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root, 800, 650));
                stage.show();
                break;
            //1

            case "Change Password":
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("updatelogin.fxml"));
                Parent root1 = (Parent) fxmlLoader2.load();
                Stage stage1 = new Stage();
                stage1.setScene(new Scene(root1, 775, 650));
                stage1.show();
                break;
            //2

            case "Logout":
                ((Node) actionEvent.getSource()).getScene().getWindow().hide();
                fxmlLoader2 = new FXMLLoader(getClass().getResource("sample.fxml"));
                Parent root2 = (Parent) fxmlLoader2.load();
                Stage stage2 = new Stage();
                stage2.setScene(new Scene(root2, 775, 650));
                stage2.show();
                break;
            //3

            case "Exit":
                Platform.exit();
                break;
            //4
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        comboo.getItems().removeAll(comboo.getItems());
        comboo.getItems().addAll("My Account", "My Profile", "Change Password", "Logout", "Exit");
        comboo.getSelectionModel().select("My Account");
    }


    // start of employee
    public void open_employee(ActionEvent actionEvent) {

        employee_pane.setVisible(true);
        model_pane.setVisible(false);
        cust_pane.setVisible(false);
        sms_pane.setVisible(false);
        renewal_pane.setVisible(false);
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
        } finally {
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
        } else if (employee_number.getText().trim().isEmpty() || employee_number.getText().trim().length() != 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER VALID MOBILE NUMBER");
            alert1.showAndWait();
        } else if (employee_found == true) {
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


                i = preparedStatement1.executeUpdate();

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

                    refresh_employee();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING DATA");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    void refresh_employee() {
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
        sms_pane.setVisible(false);
        renewal_pane.setVisible(false);
    }

    public void hrs_to_mon(KeyEvent keyEvent) {
        int time_in_hrs, days1, months;
        time_in_hrs = Integer.parseInt(String.valueOf(hrs.getText()));
        days1 = (int) (time_in_hrs * 0.0417);
        days.setText(String.valueOf(days1));
        months = (int) (days1 * 0.032855);
        mnths.setText(String.valueOf(months));
    }


    void refresh_model() {
        sl_no.clear();
        model_name.clear();
        model_number.clear();
        hrs.clear();
        mnths.clear();
        days.clear();
    }

    public void add_models(ActionEvent actionEvent) throws Exception {
        connection = DBConnection.getConnection();

        if (model_name.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER MODEL NAME");
            alert1.showAndWait();
        } else if (model_number.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER MODEL NUMBER");
            alert1.showAndWait();
        } else {
            try {
                int i = 0;
                String query1 = "Insert into models(sl_no, model_name, model_number, service_duration_in_hrs, " +
                        "service_duration_in_months,service_duration_in_days) values(?,?,?,?,?,   ?)";
                PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
                preparedStatement1.setString(1, sl_no.getText().trim());
                preparedStatement1.setString(2, model_name.getText().trim());
                preparedStatement1.setString(3, model_number.getText().trim());
                preparedStatement1.setString(4, hrs.getText().trim());
                preparedStatement1.setString(5, mnths.getText().trim());
                preparedStatement1.setString(6, days.getText());

                i = preparedStatement1.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("MODEL INFORMATION ADDED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_model();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING DATA");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        } finally {
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
        sms_pane.setVisible(false);
        renewal_pane.setVisible(false);
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
        } finally {
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
            alert1.showAndWait();
        } else if (mobile_number.getText().trim().isEmpty() || mobile_number.getText().trim().length() != 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER VALID MOBILE NUMBER");
            alert1.showAndWait();
        } else if (father_name.getText().trim().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER FATHER NAME");
            alert1.showAndWait();
        } else {

            try {

                int i = 0;

                String query1 = "Insert into customers(id, date_of_sale, customer_name, father_name, " +
                        "village, tehlsi, mobile_number, model, engine_number, file_number, model_number, service_date, feedback) values(?,?,?,?,?   ,?,?,?,?,?,  ?,?,?)";
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
                preparedStatement1.setString(13, feedback.getText());


                i = preparedStatement1.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("CUSTOMER INFORMATION ADDED SUCCESSFULLY");
                    alert.showAndWait();

                    refresh_customer();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN SAVING DATA");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void refresh_customer() {
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
        model_number1.clear();
        feedback.clear();
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


    //send sms and to view renewal customers

    public void open_sms(ActionEvent actionEvent) {
        employee_pane.setVisible(false);
        model_pane.setVisible(false);
        cust_pane.setVisible(false);
        sms_pane.setVisible(true);
        renewal_pane.setVisible(false);
    }


    public void send_msg(ActionEvent actionEvent) {

        if (!(text_msg.getText().isEmpty())) {
            try {
                String query = "Select mobile_number from customers where service_date>='" + from_date.getValue() + "' and  service_date<='" + to_date.getValue() + "'";
                connection = DBConnection.connect();
                ResultSet rs = connection.prepareStatement(query).executeQuery();
                while (rs.next()) {
                    if (!(rs.getString(1).isEmpty()) && rs.getString(1).trim().length() >= 10) {
                        SMS.sendSms(rs.getString(1), text_msg.getText());
                    }
                }
                text_msg.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("ENTER MESSAGE CONTENT");
            alert.showAndWait();
        }

    }

    public void view_renewal_custs(ActionEvent actionEvent) throws Exception {
        try {
            String conditions = "";
            connection = DBConnection.getConnection();
            if (from_date.getValue() == null &&
                    to_date.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("PLEASE SELECT DATE TO VIEW CUSTOMER RENEWALS");
                alert.showAndWait();
            } else {
                if (!(from_date.getValue() == null)) {
                    conditions = conditions + " and service_date>='" + from_date.getValue() + "' ";
                }
                if (!(to_date.getValue() == null)) {
                    conditions = conditions + " and service_date<='" + to_date.getValue() + "' ";
                }

                String query = "select * from customers where  id>0 " + conditions;
                Welcome(query, renewal_cust_pane, 300, 430);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }

    public void excel_renewal_custs(ActionEvent actionEvent) {

        try {
            String conditions = "";
            connection = DBConnection.getConnection();

            if (from_date.getValue() == null &&
                    to_date.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("PLEASE SELECT DATE TO VIEW CUSTOMER RENEWALS");
                alert.showAndWait();
            } else {
                if (!(from_date.getValue() == null)) {
                    conditions = conditions + " and service_date>='" + from_date.getValue() + "' ";
                }
                if (!(to_date.getValue() == null)) {
                    conditions = conditions + " and service_date<='" + to_date.getValue() + "' ";
                }

                String query = "select * from customers where  id>0 " + conditions;
                new File(Check.drive_name() + Configuring_Path.FOLDER_PATH).mkdir();
                new File(Check.drive_name() + Configuring_Path.FOLDER_PATH + "Renewal_Customer_Details/").mkdir();
                String path = Check.drive_name() + Configuring_Path.FOLDER_PATH + "Renewal_Customer_Details/Renewal_Customer_Details.xlsx";
                Controller.createExcelFile(query, path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void open_renewal_pane(ActionEvent actionEvent) {
        renewal_pane.setVisible(true);
        employee_pane.setVisible(false);
        model_pane.setVisible(false);
        cust_pane.setVisible(false);
        sms_pane.setVisible(false);
    }

    public void search_renewal_cust(ActionEvent actionEvent) throws SQLException {
        try {
            String conditions = "", feedbackk = "";
            connection = DBConnection.getConnection();
            if (cust_nameee.getText().trim().isEmpty() &&
                    cust_mobileee.getText().trim().isEmpty() &&
                    cust_engineee.getText().trim().isEmpty() &&
                    cust_modelll.getText().trim().isEmpty() &&
                    cust_serviceee.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("PLEASE ENTER AT LEAST CUSTOMER NAME, MOBILE NUMBER, SERVICE DATE, MODEL NUMBER OR ENGINE NUMBER TO VIEW AND UPDATE CUSTOMER SERVICE DATE");
                alert.showAndWait();
            } else {
                if (!(cust_nameee.getText().trim().isEmpty())) {
                    conditions = conditions + " and customer_name ='" + cust_nameee.getText() + "' ";
                }
                if (!(cust_mobileee.getText().trim().isEmpty())) {
                    conditions = conditions + " and mobile_number ='" + cust_mobileee.getText() + "' ";
                }
                if (!(cust_engineee.getText().trim().isEmpty())) {
                    conditions = conditions + " and engine_number ='" + cust_engineee.getText() + "' ";
                }
                if (!(cust_modelll.getText().trim().isEmpty())) {
                    conditions = conditions + " and model_number ='" + cust_modelll.getText() + "' ";
                }
                if (!(cust_serviceee.getValue() == null)) {
                    conditions = conditions + " and service_date ='" + cust_serviceee.getValue() + "' ";
                }

                String query = "select * from customers where  id>0 " + conditions;
                Welcome(query, view_renewal_cust, 225, 540);


                Statement st = connection.createStatement();
                ResultSet resultSet = st.executeQuery("select * from customers where  id>0 " + conditions);

                while (resultSet.next()) {
                    feedbackk = resultSet.getString("feedback");
                }
                feedback_box.setText(feedbackk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }

    public void update_service_date(ActionEvent actionEvent) throws Exception {
        String conditions = " ", modelname = "", service = "";
    try {
        connection = DBConnection.getConnection();
        if (cust_nameee.getText().trim().isEmpty() &&
                cust_mobileee.getText().trim().isEmpty() &&
                cust_engineee.getText().trim().isEmpty() &&
                cust_modelll.getText().trim().isEmpty() &&
                cust_serviceee.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("PLEASE ENTER AT LEAST CUSTOMER NAME, MOBILE NUMBER, SERVICE DATE, MODEL NUMBER OR ENGINE NUMBER TO VIEW AND UPDATE CUSTOMER SERVICE DATE");
            alert.showAndWait();
        } else {

            if (!(cust_nameee.getText().trim().isEmpty())) {
                conditions = conditions + " and customer_name ='" + cust_nameee.getText() + "' ";
            }
            if (!(cust_mobileee.getText().trim().isEmpty())) {
                conditions = conditions + " and mobile_number ='" + cust_mobileee.getText() + "' ";
            }
            if (!(cust_engineee.getText().trim().isEmpty())) {
                conditions = conditions + " and engine_number ='" + cust_engineee.getText() + "' ";
            }
            if (!(cust_modelll.getText().trim().isEmpty())) {
                conditions = conditions + " and model_number ='" + cust_modelll.getText() + "' ";
            }
            if (!(cust_serviceee.getValue() == null)) {
                conditions = conditions + " and service_date ='" + cust_serviceee.getValue() + "' ";
            }

            ResultSet fetch_model_name = connection.createStatement().executeQuery("select * from customers where  id>0 " + conditions);
            while (fetch_model_name.next()) {
                modelname = fetch_model_name.getString("model");
                service = fetch_model_name.getString("service_date");
            }

            String modell = "", in_hrs = "", in_days = "", in_mnth = "", model_no = "";
            ResultSet setmodell = connection.createStatement().executeQuery("select * from models where model_name='" + modelname + "'");
            while (setmodell.next()) {
                modell = setmodell.getString("model_name");
                in_hrs = setmodell.getString("service_duration_in_hrs");
                in_days = setmodell.getString("service_duration_in_days");
                in_mnth = setmodell.getString("service_duration_in_months");
                model_no = setmodell.getString("model_number");
            }

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            Calendar c = Calendar.getInstance();
//            c.setTime(sdf.parse(service));
//            c.add(Calendar.DATE, 3);
//            //String updated_service_date =service  + in_days;  ///concatenates
//            System.out.println(c);


            // ADDINGDAYS TO SERVICE DATE LOGIC SHOULD BE ADDED HERE

            PreparedStatement ps = connection.prepareStatement("update customers set " +
                    "feedback='"+feedback_box.getText()+"' where id > 0 " + conditions);

            int j = ps.executeUpdate();

            if (j > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("CUSTOMER SERVICE DATE AND FEEDBACK UPDATED");
                alert.showAndWait();
            }
        } }

        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void update_feedback(ActionEvent actionEvent) throws Exception {
        try {
            connection = DBConnection.getConnection();
        String conditions = "";

        if (cust_nameee.getText().trim().isEmpty() &&
                    cust_mobileee.getText().trim().isEmpty() &&
                    cust_engineee.getText().trim().isEmpty() &&
                    cust_modelll.getText().trim().isEmpty() &&
                    cust_serviceee.getValue() == null )
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("PLEASE ENTER AT LEAST CUSTOMER NAME, MOBILE NUMBER, SERVICE DATE, MODEL NUMBER OR ENGINE NUMBER TO VIEW AND UPDATE CUSTOMER SERVICE DATE");
                alert.showAndWait();
            } else {

                if (!(cust_nameee.getText().trim().isEmpty())) {
                    conditions = conditions + " and customer_name ='" + cust_nameee.getText() + "' ";
                }
                if (!(cust_mobileee.getText().trim().isEmpty())) {
                    conditions = conditions + " and mobile_number ='" + cust_mobileee.getText() + "' ";
                }
                if (!(cust_engineee.getText().trim().isEmpty())) {
                    conditions = conditions + " and engine_number ='" + cust_engineee.getText() + "' ";
                }
                if (!(cust_modelll.getText().trim().isEmpty())) {
                    conditions = conditions + " and model_number ='" + cust_modelll.getText() + "' ";
                }
                if (!(cust_serviceee.getValue() == null)) {
                    conditions = conditions + " and service_date ='" + cust_serviceee.getValue() + "' ";
                }

            PreparedStatement ps = connection.prepareStatement("update customers set " +
                    "feedback='"+feedback_box.getText()+"' where id > 0 " + conditions);

            int j = ps.executeUpdate();

            if (j > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("CUSTOMER FEEDBACK UPDATED");
                alert.showAndWait();
                 }
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}