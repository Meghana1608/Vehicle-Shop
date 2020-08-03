package sample;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Check {

    public static String drive_name() throws Exception {
        return getDataFromIPtext("DRIVE");
    }

    public static String dname() throws Exception {
        return getDataFromIPtext("DBNAME");
    }

    public static String duser() throws Exception {
        return getDataFromIPtext("USERNAME");
    }

    public static String dpword() throws Exception {
        return getDataFromIPtext("PASSWORD");
    }

    public static String ipaddress_name() throws Exception {
        return getDataFromIPtext("IP_ADDRESS");
    }

    public static String getDataFromIPtext(String key) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        String s = new String(Files.readAllBytes(Paths.get(Configuring_Path.JSON_PATH_2+"database.txt")));
        Object obj = parser.parse(s);
        String d= String.valueOf(((JSONObject) obj).get(key));
        System.out.println(d);
        return d;
    }

}

