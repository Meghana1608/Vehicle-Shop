package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Home implements Initializable {
    public ComboBox comboo;
    public AnchorPane employee_pane;

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
           // System.out.println("bg");
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
}
