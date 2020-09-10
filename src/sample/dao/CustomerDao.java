package sample.dao;

import sample.DBConnection;
import sample.Check;
import sample.dto.CustomerDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

 public class CustomerDao {

     public Connection connection = null;
     public String query = "";
     public final String tableName = "customers";

     public boolean insert(CustomerDto dto) {
         boolean status = false;
         try {
             connection = DBConnection.connect();
             query = "INSERT INTO " + tableName + " ( " +
                     " date_of_sale, customer_name, father_name, village, tehlsi, " +
                     " mobile_number, model, model_number, engine_number, " +
                     " file_number, service_date, customer_feedback, service_feedback, operator_name, operator_number, job_card_number, chassis_number, registration_number " +
                     " ) values (   ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?  ,?,?,? )";
             PreparedStatement stmt = connection.prepareStatement(query);
             stmt.setString(1, dto.getDate_of_sale());
             stmt.setString(2, dto.getCustomer_name());
             stmt.setString(3, dto.getFather_name());
             stmt.setString(4, dto.getVillage());
             stmt.setString(5, dto.getTehlsi());
             stmt.setString(6, dto.getMobile_number());
             stmt.setString(7, dto.getModel());
             stmt.setString(8, dto.getModel_number());
             stmt.setString(9, dto.getEngine_number());
             stmt.setString(10, dto.getFile_number());
             stmt.setString(11, dto.getServce_date());
             stmt.setString(12, dto.getFeedback());
             stmt.setString(13, dto.getService_feedback());
             stmt.setString(14, dto.getOperator_name());
             stmt.setString(15, dto.getOperator_number());
             stmt.setString(16, dto.getJob_card_number());
             stmt.setString(17, dto.getChassis_number());
             stmt.setString(18, dto.getRegistration_number());

             status = !stmt.execute();
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             try {
                 connection.close();
             } catch (SQLException e) {
                 e.printStackTrace();
             }
         }
         return status;
     }
 }