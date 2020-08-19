package sample.sms;

import javafx.scene.control.Alert;
import sample.Check;
import sample.DBConnection;



import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SMS {
    static Connection con=null;
    static String URL_OpenWeatherMap_weather_London_uk = "";
    public static void sendSms(String phone,String message){

        String sender="MITSOF";
        message=message.replace(" ","%20");
        try {
            Check.p("SMS>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

            InetAddress ipAddr = InetAddress.getLocalHost();
            System.out.println("IP address = " + ipAddr.getHostAddress());
            Check.p(">>>>>>>>>>>>>>>>>>>>>>>>>>"+message);
            URL_OpenWeatherMap_weather_London_uk = "http://alerts.solutionsinfini.com/api/web2sms.php?workingkey=A77acf1b7bc5b079a849e6b141f236ca2&sender="+sender+"&to="+phone+"&message="+message;

            Check.p("  "+URL_OpenWeatherMap_weather_London_uk);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }
        //jsonTest();
    }

    public static void jsonTest() {
        String result = "";
        try {
            URL url_weather = new URL(URL_OpenWeatherMap_weather_London_uk);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                httpURLConnection.disconnect();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("NOT SENT");
                alert.showAndWait();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

//    public static void sendSMSBillNotPaid(String month,String year) throws Exception {
//        try{
//            con= DBConnection.connect();
//            String qurey="SELECT c.phoneno FROM billing AS b,customer c WHERE (b.cardnumber=c.cardnumber) AND b.payment='0' AND b.month='"+month+"'AND b.year='"+year+"'";
//            ResultSet rs=con.createStatement().executeQuery(qurey);
//            Check.p(qurey);
//            while(rs.next()){
//                Check.p(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+rs.getString(1));
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            if(con!=null){
//                con.close();
//            }
//        }
//    }
//

//    public static void insert(String month,String year,String phoneNumbers) throws SQLException {
//        try{
//            con= DBConnect.connect();
//            String query="INSERT INTO sms (phone,month,year) VALUES (?,?,?)";
//            PreparedStatement stmt=con.prepareStatement(query);
//            stmt.setString(1,phoneNumbers);
//            stmt.setString(2,month);
//            stmt.setString(3,year);
//
//            if(stmt.execute()){
//                AlertPrint.error("ERROR DATA NOT INSERTED");
//            }else{
//                AlertPrint.information("OK");
//            }
//
//        }catch (Exception e){
//
//        }finally {
//            if(con!=null){
//                con.close();
//            }
//        }
//    }
//
//    public static boolean check(String month,String year) throws SQLException {
//        boolean flag=false;
//        try{
//            con=DBConnection.connect();
//            String query="SELECT * FROM sms WHERE month='"+month+"' AND year='"+year+"'";
//            ResultSet rs=con.createStatement().executeQuery(query);
//            if(rs.next()){
//                flag=false;
//            }else{
//                flag=true;
//            }
//        }catch (Exception e){
//
//        }finally {
//            if(con!=null){
//                con.close();
//            }
//            return flag;
//        }
//    }
}
