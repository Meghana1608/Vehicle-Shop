package sample;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection conn;
    private static String url = "jdbc:mysql://localhost:3306/vehicle_shop";

    private static String user = "root";
    private static String pass = "";

    public static Connection connect() throws Exception {
        try{
            System.out.println("Here i called "+Check.ipaddress_name().toString());
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        }
        catch(ClassNotFoundException cnfe){
            System.err.println("Error: "+cnfe.getMessage());
        }
        catch(InstantiationException ie){
            System.err.println("Error: "+ie.getMessage());
        }
        catch(IllegalAccessException iae){
            System.err.println("Error: "+iae.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        String databaseName=Check.dname();
        String dUser=Check.duser();
        String dPword=Check.dpword();
        if(Check.ipaddress_name() != null){
            url = "jdbc:mysql://"+Check.ipaddress_name()+"/"+databaseName;
       //   Check.p("  url    :  "+url);
        }

        System.out.println("this is the url of the final "+url);
        try {
            conn = DriverManager.getConnection(url, dUser, dPword);
           // Check.p("connection--->" + conn);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("ERROR IN DATABASE CONNECTION '"+url+"'");
            alert.showAndWait();
        }
        return conn;
    }

    public static Connection getConnection() throws Exception {
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;
    }

    public static void close() throws Exception {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
