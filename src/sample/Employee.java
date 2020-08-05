package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class Employee implements Initializable {


    public Pane view_reg_pane;
    public Pane edit_pane;
    public TextField employee_Id;
    public TextField employee_name;
    public TextField m_no;
    public TextField address;
    public TextField email_id;
    public TextField old_username;
    public TextField new_username;
    public TextField password;
    public TextField c_password;

    Connection connection = null;


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

    public void gen_reg_rep(ActionEvent actionEvent) {
        view();
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
            a.setContentText("PLEASE SELECT EMPLOYEE");
            a.showAndWait();
        }
        try {
            connection=DBConnection.getConnection();
            int e_id = Integer.parseInt(bb.get(0));
            String query = "Select * from employee where e_id='" + e_id + "'";
            ResultSet rs =connection.createStatement().executeQuery(query);
            while (rs.next())
            {
                employee_Id.setText(rs.getString(1));
                employee_name.setText(rs.getString(2));
                old_username.setText(rs.getString(3));
                password.setText(rs.getString(4));
                m_no.setText(rs.getString(5));
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void delete_employee(ActionEvent actionEvent) {
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
        alert.setContentText("ARE YOU SURE YOU WANT TO DELETE EMPLOYEE RECORD");

        Optional<ButtonType> result=alert.showAndWait();

        if((result.isPresent()) && (result.get()==ButtonType.OK)) {
            try {
                connection = DBConnection.getConnection();
                int e_id = Integer.parseInt(bb.get(0));
                String query3 = "select * from employee where e_id='" + e_id + "'";
                ResultSet rs = connection.createStatement().executeQuery(query3);
                String u_name = "";
                while (rs.next()) {
                    u_name = rs.getString("username");
                }

                String query = "delete from employee where e_id='" + e_id + "'";
                PreparedStatement ps = connection.prepareStatement(query);
                int i = ps.executeUpdate();

                String query1 = "delete from login where username='" + u_name + "'";
                PreparedStatement ps1 = connection.prepareStatement(query1);
                int j = ps1.executeUpdate();

                if (i > 0 && j > 0) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("EMPLOYEE INFORMATION DELETED SUCCESSFULLY");
                    a.showAndWait();
                    view();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("ERROR IN DELETING EMPLOYEE INFORMATION");
                    a.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save_employee(ActionEvent actionEvent) throws Exception {

        connection = DBConnection.getConnection();
        int k=0;
        String o_user="",n_user="";
        if(new_username.getText().isEmpty())
        {
            o_user=old_username.getText().trim();
            n_user=old_username.getText().trim();
        }
        else
        {
            o_user=old_username.getText().trim();
            n_user=new_username.getText().trim();
        }

        String query="select * from login where username='"+n_user+"'";
        ResultSet rs=connection.createStatement().executeQuery(query);
        while (rs.next())
        {
            k++;
        }

        if (m_no.getText().trim().isEmpty() || m_no.getText().trim().length()!= 10) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("ENTER VALID MOBILE NUMBER");
            alert1.showAndWait();
        }
        else if (k>0 && !new_username.getText().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("USERNAME ALREADY EXIST TRY WITH OTHER USER NAME");
            alert1.showAndWait();
        }
        else
        {
            try {
                PreparedStatement ps = connection.prepareStatement("update employee set " +
                        "e_name='"+employee_name.getText().trim()+"', " +
                        "username='"+n_user+"', " +
                        "password='" + password.getText().trim() + "'," +
                        "e_pnumber='" + m_no.getText().trim() + "' where e_id = " + employee_Id.getText().trim());

                int j = ps.executeUpdate();


                PreparedStatement ps1 = connection.prepareStatement("update login set username='" +n_user+"',"+
                        "password='" + password.getText().trim() + "' where username = '" +o_user+ "' and login_type='employee'");

                int i = ps1.executeUpdate();

                if (i > 0 && j > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("EMPLOYEE INFORMATION UPDATED");
                    alert.showAndWait();

                    refresh_employee();
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

    void refresh_employee()
    {
        employee_Id.clear();
        employee_name.clear();
        old_username.clear();
        new_username.clear();
       // email_id.clear();
        password.clear();
      //  c_password.clear();
     //   address.clear();
        m_no.clear();
        edit_pane.setVisible(false);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        view();
    }
}
