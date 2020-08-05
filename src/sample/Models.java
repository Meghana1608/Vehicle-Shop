package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class Models {

    public Pane view_reg_pane;
    public Pane edit_pane;
    public TextField sl_no;
    public TextField model_name;
    public TextField model_no;
    public TextField hrs;
    public TextField mnths;
    Connection connection = null;

    public void view()
    {
        String query="select * from models";
        try {
            LoadingTableViewDataSelectedRowName.Welcome(query, view_reg_pane,250,600);
            edit_pane.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gen_model_rep(ActionEvent actionEvent) {
        view();
    }

    public void modelReprtPDF(ActionEvent actionEvent) throws Exception {
        String query = "SELECT * FROM models";

        File dir = new File(Check.drive_name()+Configuring_Path.FOLDER_PATH);
        dir.mkdir();
        File dir2=new File(Check.drive_name()+Configuring_Path.FOLDER_PATH+"MODELS_REPORT");
        dir2.mkdir();
        String path=Check.drive_name()+Configuring_Path.FOLDER_PATH+"/MODELS_REPORT/Models_report.csv";
        Controller.export_excel(query,path);
    }


    public void hrs_to_mon(KeyEvent keyEvent) {
        int time_in_hrs, days , months;
        time_in_hrs = Integer.parseInt(String.valueOf(hrs.getText()));
        days = (int) (time_in_hrs * 0.0417);
        months = (int) (days * 0.032855);
        mnths.setText(String.valueOf(months));
    }


    public void edit_model(ActionEvent actionEvent) {
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
            String query = "Select * from models where sl_no ='" + e_id + "'";
            ResultSet rs =connection.createStatement().executeQuery(query);
            while (rs.next())
            {
                sl_no.setText(rs.getString(1));
                model_name.setText(rs.getString(2));
                model_no.setText(rs.getString(3));
                hrs.setText(rs.getString(4));
                mnths.setText(rs.getString(5));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void delete_model(ActionEvent actionEvent) {
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
        alert.setContentText("ARE YOU SURE YOU WANT TO DELETE THIS MODEL");

        Optional<ButtonType> result=alert.showAndWait();

        if((result.isPresent()) && (result.get()==ButtonType.OK)) {
            try {
                connection = DBConnection.getConnection();
                int e_id = Integer.parseInt(bb.get(0));
                String query3 = "select * from models where sl_no='" + e_id + "'";
                ResultSet rs = connection.createStatement().executeQuery(query3);

                String query = "delete from models where sl_no='" + e_id + "'";
                PreparedStatement ps = connection.prepareStatement(query);
                int i = ps.executeUpdate();

                if (i > 0 ) {
                    Alert a = new Alert(Alert.AlertType.INFORMATION);
                    a.setContentText("MODEL INFORMATION DELETED SUCCESSFULLY");
                    a.showAndWait();
                    view();
                } else {
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("ERROR IN DELETING MODEL INFORMATION");
                    a.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void save_model(ActionEvent actionEvent) throws Exception {
        connection = DBConnection.getConnection();
            try {
                PreparedStatement ps = connection.prepareStatement("update models set " +
                        "model_name='"+model_name.getText().trim()+"', " +
                        "model_number='"+model_no.getText()+"', " +
                        "service_duration_in_hrs='" + hrs.getText().trim() + "'," +
                        "service_duration_in_months='" + mnths.getText().trim() + "' where sl_no = " + sl_no.getText().trim());

                int j = ps.executeUpdate();


                if (j > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("MODEL INFORMATION UPDATED");
                    alert.showAndWait();

                    refresh_model();
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

    void refresh_model(){
        sl_no.clear();
        model_name.clear();
        model_no.clear();
        hrs.clear();
        mnths.clear();
    }
}
