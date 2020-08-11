package sample;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import static sample.LoadingDataToTableView.Welcome;
import static sample.digital_clock.backupDataWithDatabase;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public JFXTextField username;
    public JFXPasswordField password;

    public static String userNameArray;
    public static String passwordArray;
    public static String loginUserName = "";
    public  static LocalDate today = LocalDate.now(ZoneId.of("Indian/Maldives"));
    public static String userEmpName;
    public JFXTextField new_user;
    public JFXPasswordField new_pass;

    public String profileImageStringpdf;
    public String mailAddressPdf1;
    public String mailAddressPdf2;
    public String emailidPdf3;
    public String contactpdf;
    public String gstinpdf;
    public String acc_namepdf;
    public String acc_numpdf;
    public String acc_typepdf;
    public String ifscpdf;
    public String termspdf;
    public String declarationpdf;
    public String jurisidictionpdf;
    public String statee;
    public String districtt;
    public String pan_number;
    public JFXTextField buname;
    public JFXTextField mail;
    public JFXTextField buemailid;
    public JFXTextField state;
    public JFXTextField district;
    public JFXTextField contactnum;
    public JFXTextField gstin;
    public TextArea terms;
    public TextArea declaration;
    public JFXTextField bank_aa_name;
    public JFXTextField acc_num;
    public JFXTextField acc_type;
    public JFXTextField ifsc;
    public JFXTextField pancard_no;
    public AnchorPane rootPane;


    public static String getUserEmpName() {
        return userEmpName;
    }

    public static void setUserEmpName(String userEmpName) {
        userEmpName = userEmpName;
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


    public static void export_excel(String query, String path) throws Exception {

        Connection con = null;
        try {
            con = DBConnection.connect();
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Items details");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header = sheet.createRow(0);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                String colname = rs.getMetaData().getColumnName(i + 1);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }


            int index = 1;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(index);
                for (int j = 0; j < rs.getMetaData().getColumnCount(); j++) {
                    row.createCell(j).setCellValue(rs.getString(j + 1));
                }

                index++;
            }

            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Details Exported to excel sheet and stored in " + path);
            alert.showAndWait();

            ps.close();
            rs.close();
            con.close();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatelogin(ActionEvent actionEvent) throws SQLException {
        if (new_user.getText().isEmpty() || new_pass.getText().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.ERROR);
            alert1.setContentText("PLEASE ENTER USERNAME AND PASSWORD");
            alert1.showAndWait();
        } else {
            try {
                connection = DBConnection.getConnection();
                String username = new_user.getText();
                String password = new_pass.getText();

                PreparedStatement ps = connection.prepareStatement("update login set username='" + username + "' ,  password='" + password + "' where login_type='admin'");

                int i = ps.executeUpdate();

                if (i > 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("USERNAME AND PASSWORD UPDATED SUCCESSFULLY");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("ERROR IN UPDATING ENTER CORRECT INFORMATION");
                    alert.showAndWait();
                }

                final Node source = (Node) actionEvent.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                Stage stage1 = new Stage();
                stage1.setTitle("Login");
                stage1.setScene(new Scene(root1, 775, 650));
                stage1.show();

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                if (connection != null) {
                    connection.close();
                }
            }
        }

    }

    public void update_profile(ActionEvent actionEvent) throws SQLException {

        try {
            String updateProfileQuery = "";
            connection = DBConnection.connect();
           // System.out.println("image is=========" + newName);
          //  if (newName == null) {
                updateProfileQuery = "UPDATE profile SET name='" + buname.getText() + "',mail_addres='" + mail.getText() + "' ,comemailid='" + buemailid.getText() + "'," +
                        "contact='" + contactnum.getText() + "',gstin='" + gstin.getText() + "',state='"+state.getText()+"', district='"+district.getText()+"'," +
                        "acc_name='" + bank_aa_name.getText() + "',acc_num='" + acc_num.getText() + "',acc_type='" + acc_type.getText() + "'," +
                        "ifsc='" + ifsc.getText() + "',pan_no='"+pancard_no.getText()+"',terms='" + terms.getText() + "'," +
                        "declaration='" + declaration.getText() + "'";
//            } else {
//                newName = newName.replace("\\", "\\\\");
//                updateProfileQuery = "UPDATE profile SET name='" + buname.getText() + "',mail_addres='" + mail.getText() + "'," +
//                        "logo='" + newName + "',comemailid='" + buemailid.getText() + "',contact='" + contactnum.getText() + "'," +
//                        "gstin='" + gstin.getText() + "',licence_no='"+licence.getText()+"',acc_name='" + bank_aa_name.getText() + "'," +
//                        "acc_num='" + acc_num.getText() + "',acc_type='" + acc_type.getText() + "',ifsc='" + ifsc.getText() + "'," +
//                        "pan_no='"+pancard_no.getText()+"',terms='" + terms.getText() + "',declaration='" + declaration.getText() + "'," +
//                        "jurisidiction='" + jurisid.getText() + "'";
//            }

            PreparedStatement ps = connection.prepareStatement(updateProfileQuery);
            int i = ps.executeUpdate();

            if (i > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("PROFILE INFORMATION UPDATED");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("ERROR IN UPDATING PROFILE DATA");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }


    public static void createExcelFile(String query, String path)  {
        Connection con = null;
        try {
            con = DBConnection.connect();

            ResultSet rs = con.createStatement().executeQuery(query);

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Items details");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header = sheet.createRow(0);

            for(int i=0; i<rs.getMetaData().getColumnCount();i++)
            {
                String colname=rs.getMetaData().getColumnName(i+1);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }


            int index = 1;
            while (rs.next()) {
                XSSFRow row = sheet.createRow(index);
                for (int j=0;j<rs.getMetaData().getColumnCount();j++)
                {
                    row.createCell(j).setCellValue(rs.getString(j+1));
                }

                index++;
            }


            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Details Exported to excel sheet and stored in "+path);
            alert.showAndWait();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(con.isClosed()) { con.close(); }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = DBConnection.connect();
            Statement st = connection.createStatement();
            ResultSet resultSet = st.executeQuery("select * from profile ");


            while (resultSet.next()) {

                profileImageStringpdf = resultSet.getString("logo");
                mailAddressPdf1 = resultSet.getString("name");
                mailAddressPdf2 = resultSet.getString("mail_addres");
                emailidPdf3 = resultSet.getString("comemailid");
                contactpdf = resultSet.getString("contact");
                gstinpdf = resultSet.getString("gstin");
                statee = resultSet.getString("state");
                districtt = resultSet.getString("district");
                acc_namepdf = resultSet.getString("acc_name");
                acc_numpdf = resultSet.getString("acc_num");
                acc_typepdf = resultSet.getString("acc_type");
                ifscpdf = resultSet.getString("ifsc");
                pan_number = resultSet.getString("pan_no");
                termspdf = resultSet.getString("terms");
                declarationpdf = resultSet.getString("declaration");

            }

            javafx.scene.image.Image image1 = new Image(profileImageStringpdf, 1000, 1500, true, true);

            buname.setText(mailAddressPdf1);
            mail.setText(mailAddressPdf2);
         //   imageView.setImage(image1);
            buemailid.setText(emailidPdf3);
            contactnum.setText(contactpdf);
            gstin.setText(gstinpdf);
            bank_aa_name.setText(acc_namepdf);
            acc_num.setText(acc_numpdf);
            acc_type.setText(acc_typepdf);
            ifsc.setText(ifscpdf);
            pancard_no.setText(pan_number);
            state.setText(statee);
            district.setText(districtt);
            terms.setText(termspdf);
            declaration.setText(declarationpdf);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
