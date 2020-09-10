package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Customers implements Initializable {


    public Pane view_reg_pane;
    public Pane edit_pane;
    public TextField cust_id;
    public TextField mobile_no;
    public TextField father_name;
    public TextField village;
    public TextField tehlsi;
    public DatePicker date_of_sale;
    public ComboBox model;
    public TextField engine_number;
    public TextField file_number;
    public TextField customer_name;
    public TextField model_number;
    public DatePicker service_date;
    public TextField operator_name;
    public TextField operator_number;
    public TextField job_card_number;
    public TextField chassis_number1;
    public TextField registration_number1;


    Connection connection = null;
    public  static LocalDate today = LocalDate.now(ZoneId.of("Indian/Maldives"));

    void modelload() throws SQLException {
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
    public void view()
    {
        String query="select * from customers";
        try {
            LoadingTableViewDataSelectedRowName.Welcome(query, view_reg_pane,250,950);
            edit_pane.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void gen_reg_rep(ActionEvent actionEvent) {
        view();
    }

    public void customerReprtPDF(ActionEvent actionEvent) throws Exception {

        String query = "SELECT * FROM customers";

        File dir = new File(Check.drive_name()+Configuring_Path.FOLDER_PATH);
        dir.mkdir();
        File dir2=new File(Check.drive_name()+Configuring_Path.FOLDER_PATH+"CUSTOMER_REPORT");
        dir2.mkdir();
        String path=Check.drive_name()+Configuring_Path.FOLDER_PATH+"/CUSTOMER_REPORT/Customer_report.csv";
        Controller.export_excel(query,path);
    }

    public void edit_customer(ActionEvent actionEvent) {

        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        bb.add(log1[0]);
        try {
            bb.add(log1[1]);
            edit_pane.setVisible(true);
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT CUSTOMER");
            a.showAndWait();
        }
        try {
            connection=DBConnection.getConnection();
            int e_id = Integer.parseInt(bb.get(0));
            String query = "Select * from customers where id='" + e_id + "'";
            ResultSet rs =connection.createStatement().executeQuery(query);
            while (rs.next())
            {
                cust_id.setText(rs.getString(1));
                date_of_sale.setValue(LocalDate.parse(rs.getString(2)));
                customer_name.setText(rs.getString(3));
                father_name.setText(rs.getString(4));
                service_date.setValue(LocalDate.parse(rs.getString(5)));
                village.setText(rs.getString(6));
                tehlsi.setText(rs.getString(9));
                mobile_no.setText(rs.getString(10));
                model.setValue(rs.getString(11));
                model_number.setText(rs.getString(12));
                engine_number.setText(rs.getString(13));
                file_number.setText(rs.getString(14));
                operator_name.setText(rs.getString("operator_name"));
                operator_number.setText(rs.getString("operator_number"));
                job_card_number.setText(rs.getString(15));
                chassis_number1.setText(rs.getString("chassis_number"));
                registration_number1.setText(rs.getString("registration_number"));

            }
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void delete_customers(ActionEvent actionEvent) {
        ObservableList oa = LoadingTableViewDataSelectedRowName.selectItem();

        ArrayList aa = new ArrayList();
        aa.add(oa.get(0));

        ArrayList newArray = new ArrayList();
        newArray = aa;
        String old = String.valueOf(newArray.get(0));
        ArrayList<String> bb = new ArrayList<String>();

        old = old.replace("[", "");
        old = old.replace("]", "");

        String log[] = old.split(",");
        String log1[] = old.split(",");

        bb.add(log1[0]);
        try {
            bb.add(log1[1]);
        } catch (Exception e) {

            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setContentText("PLEASE SELECT THE ITEM FROM TABLE");
            a.showAndWait();
        }

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("ARE YOU SURE YOU WANT TO DELETE CUSTOMER RECORD");

        Optional<ButtonType> result=alert.showAndWait();

        if((result.isPresent()) && (result.get()==ButtonType.OK)) {
            try {
                connection = DBConnection.getConnection();
                int e_id = Integer.parseInt(bb.get(0));
                String query3 = "select * from customers where id='" + e_id + "'";
                ResultSet rs = connection.createStatement().executeQuery(query3);


                String query = "delete from customers where id='" + e_id + "'";
                PreparedStatement ps = connection.prepareStatement(query);
                int i = ps.executeUpdate();


                if (i > 0) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("CUSTOMER INFORMATION DELETED SUCCESSFULLY");
                    a.showAndWait();
                    view();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("ERROR IN DELETING CUSTOMER INFORMATION");
                    a.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save_customer(ActionEvent actionEvent) throws Exception {
        connection = DBConnection.getConnection();


        if (mobile_no.getText().trim().isEmpty() || mobile_no.getText().trim().length()!= 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER VALID MOBILE NUMBER");
            alert1.showAndWait();
        }

        else
        {
            try {
                PreparedStatement ps = connection.prepareStatement("update customers set " +
                        "date_of_sale='"+date_of_sale.getValue()+"', " +
                        "customer_name='"+customer_name.getText()+"', " +
                        "father_name='"+father_name.getText()+"', " +
                        "village='"+village.getText()+"', " +
                        "tehlsi='"+tehlsi.getText()+"', " +
                        "mobile_number='"+mobile_no.getText()+"', " +
                        "model='"+model.getValue()+"', " +
                        "model_number='"+model_number.getText()+"', " +
                        "engine_number='"+engine_number.getText()+"', " +
                        "service_date='"+service_date.getValue()+"', " +
                        "operator_name='"+operator_name.getText()+"', " +
                        "operator_number='"+operator_number.getText()+"', " +
                        "job_card_number='"+job_card_number.getText()+"', " +
                        "chassis_number='"+chassis_number1.getText()+"', " +
                        "registration_number='"+registration_number1.getText()+"', " +
                        "file_number='" + file_number.getText().trim() + "' where id = " + cust_id.getText().trim());

                int j = ps.executeUpdate();

                if (j > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("CUSTOMER INFORMATION UPDATED");
                    alert.showAndWait();

                    refresh_customer();
                    view();

                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN UPDATING");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (connection != null)
                    connection.close();
            }
        }
    }
    void refresh_customer(){
        cust_id.clear();
        customer_name.clear();
        father_name.clear();
        village.clear();
        tehlsi.clear();
        mobile_no.clear();
        engine_number.clear();
        file_number.clear();
        model_number.clear();
        date_of_sale.setValue(today);
        service_date.setValue(today);
        job_card_number.clear();
        operator_number.clear();
        operator_name.clear();
        chassis_number1.clear();
        registration_number1.clear();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            modelload();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
