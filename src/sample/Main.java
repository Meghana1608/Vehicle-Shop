package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //creating json file in c drive

        try {
            boolean json_1 = new File(Configuring_Path.JSON_PATH_1).exists();

            if (json_1 == false) {
                new File(Configuring_Path.JSON_PATH_1).mkdir();
                new File(Configuring_Path.JSON_PATH_2).mkdir();
                String FILE = Configuring_Path.JSON_PATH_2 + "/database.txt";
                PrintWriter writer = new PrintWriter(FILE);
                writer.println("{");
                writer.print("\n\"DRIVE\": \"E:/\",");
                writer.println("\n");
                writer.print("\n\"USERNAME\": \"root\",");
                writer.println("\n");
                writer.print("\n\"PASSWORD\": \"\",");
                writer.println("\n");
                writer.print("\n\"DBNAME\": \"vehicle_shop\",");
                writer.println("\n");
                writer.println("\n\"IP_ADDRESS\": \"localhost\"");
                writer.println("}");
                writer.close();
            }

            new File(Check.drive_name() + Configuring_Path.FOLDER_PATH).mkdir();
            String FILE = Check.drive_name() + Configuring_Path.FOLDER_PATH + "/output.txt";
            PrintWriter writer = new PrintWriter(FILE);
            writer.print("");
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();

        }
        //end of writing json file


        //checking of file
        boolean test_file = true;
        try {
            String a = Check.ipaddress_name();
            String b = Check.drive_name();
            if (a.trim().isEmpty() || b.trim().isEmpty()) {
                test_file = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!test_file) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("IP_DRIVE_CONFIG FILE IS NOT FOUND");
            alert.showAndWait();
        }
        //end of checking file
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
       // primaryStage.setTitle("Express Billing");
        primaryStage.setScene(new Scene(root, 775, 650));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
