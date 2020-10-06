package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sample.dao.CustomerDao;
import sample.dto.CustomerDto;

import java.awt.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC;
import static org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING;

public class ImportFromExcel {

    public Label path_label;

    public void createTemplteExcel(ActionEvent actionEvent) throws Exception {
        File file2 = new File(Check.drive_name() + Configuring_Path.FOLDER_PATH + "/TEMPLATE");
        boolean aa = file2.mkdir();
        File file1 = new File(Check.drive_name() + Configuring_Path.FOLDER_PATH + "/TEMPLATE");
        boolean bb = file1.mkdir();
        String FILE = file1 + "/Import_template.xlsx";
        path_label.setText("Path : ->" + FILE);
        createExcelTEMPLEFile(FILE);
    }


    void createExcelTEMPLEFile( String path) throws Exception {
        Connection con = null;
        try {

            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("Items details");

            XSSFCellStyle style = wb.createCellStyle();
            style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            XSSFRow header = sheet.createRow(0);

            ArrayList<String> headerList=new ArrayList<String>();


            headerList.add("DATE OF SALE(YYYY)");
            headerList.add("DATE OF SALE(MM)");
            headerList.add("DATE OF SALE(DD)");
            headerList.add("CUSTOMER NAME");
            headerList.add("FATHER NAME");
            headerList.add("VILLAGE");
            headerList.add("TEHLSI");
            headerList.add("MOBILE NUMBER");
            headerList.add("MODEL");
            headerList.add("MODEL NUMBER");
            headerList.add("ENGINE NUMBER");
            headerList.add("FILE NUMBER");
            headerList.add("SERVICE DATE(YYYY)");
            headerList.add("SERVICE DATE(MM)");
            headerList.add("SERVICE DATE(DD)");
            headerList.add("CUSTOMER FEEDBACK");
            headerList.add("SERVICE FEEDBACK");
            headerList.add("OPERATOR NAME");
            headerList.add("OPERATOR NUMBER");
            headerList.add("JOB CARD NUMBER");
            headerList.add("CHASSIS NUMBER");
            headerList.add("REGISTRATION NUMBER");


            for(int i=0; i<headerList.size();i++)
            {
                String colname=headerList.get(i);
                header.createCell(i).setCellValue(colname.toUpperCase());
                header.getCell(i).setCellStyle(style);
            }


            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            fileOut.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("IMPORT EXCEL TEMPLATE SHEET IS CREATE IN PATH : "+path);
            alert.showAndWait();

            Desktop dt = Desktop.getDesktop();
            dt.open(new File(path));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String getStringCelldata(Row row, int index) throws Exception {
        String data = "";
        try {
            if (row.getCell(index).getCellType() == CELL_TYPE_STRING) {
                data = row.getCell(index).getStringCellValue();
            } else if (row.getCell(index).getCellType() == CELL_TYPE_NUMERIC) {
                int bb = (int) row.getCell(index).getNumericCellValue();
                data = bb + "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    public void updateCustomer(ActionEvent actionEvent) throws Exception {
        CustomerDto dto = new CustomerDto();
        Connection connection = null;
        try {
            connection = DBConnection.connect();

            FileChooser fChooser = new FileChooser();
            FileChooser.ExtensionFilter extentionFilter = new FileChooser.ExtensionFilter("Xlsx (.xlsx, .csv)", "*.xlsx", "*.csv");
            fChooser.getExtensionFilters().add(extentionFilter);
            //Set to user directory or go to default if cannot access
            String userDirectoryString = System.getProperty("user.home");
            File userDirectory = new File(userDirectoryString);

            if (!userDirectory.canRead()) {
                userDirectory = new File("c:/");
            }
            fChooser.setInitialDirectory(userDirectory);

            //Choose the file
            File chosenFile = fChooser.showOpenDialog(null);

            String path = "";
            if (chosenFile != null) {
                path = chosenFile.getPath();
                File file = new File(path);

                System.out.println("File Path = " + file);
                FileInputStream fis = new FileInputStream(file);
            }

            FileInputStream fis = new FileInputStream(new File(path));


            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Row row;

            XSSFCellStyle ErrorCellstyle = wb.createCellStyle();
            ErrorCellstyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            ErrorCellstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int totalitemInXl = sheet.getLastRowNum();
            int totalItemAddedtoDatabase = 0;
            boolean dataIsCorrect = true;
            //For excel sheet checking purpose
            ArrayList<CustomerDto> stockDtos = new ArrayList<CustomerDto>();
            ArrayList<String> erroeLog = new ArrayList<String>();

            for (int i = 0; i < totalitemInXl; i++) {

                int b = 1 + i;
                dataIsCorrect = true;
                row = sheet.getRow(b);
                String date;
                String date1;

                // 0 date of sale yyyy
                date = "";
                try {
                    String dateyyy = getStringCelldata(row, 0);
                    int py = Integer.parseInt(dateyyy);

                    if ((py + "").trim().length() != 4) {
                        String error = "Mistake in row : " + (b) + " and column 1 : cell should contain Year in 'YYYY' pattern\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 1 : cell should contain Year in 'YYYY' pattern\n";
                    erroeLog.add(error);
                }


                //  1   date of sale MM
                try {
                    int pm = Integer.parseInt((getStringCelldata(row, 1)));
                    date = date + "-" + pm;
                    if (pm <= 0 || pm > 12) {
                        String error = "Mistake in row : " + b + " and column 2 : cell should contain month in 'MM' pattern\n";
                        row.getCell(1).setCellStyle(ErrorCellstyle);
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + b + " and column 2 : cell should contain month in 'MM' pattern\n";
                    row.getCell(1).setCellStyle(ErrorCellstyle);
                    erroeLog.add(error);
                }


                //  2   date of sale dd
                try {
                    int pd = Integer.parseInt((getStringCelldata(row, 2)));
                    date = date + "-" + pd;


                    if (pd <= 0 || pd > 31) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + b + " and column 3 : cell should contain day in 'DD' pattern\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + b + " and column 3 : cell should contain day in 'DD' pattern\n";
                    erroeLog.add(error);
                }


                //3  Customer Name
                try {
                    String aa = (getStringCelldata(row, 3));
                    if (aa.trim().length() < 1) {
                        String error = "Mistake in row : " + (b) + " and column 4 : cell should contain Customer Name\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 4 : cell should contain Customer Name\n";
                    erroeLog.add(error);
                }


                //4  Father Name
                try {
                    String aa = (getStringCelldata(row, 4));
//                 //   if (aa.trim().length() < 1) {
//                        String error = "Mistake in row : " + (b) + " and column 5 : cell should contain Father Name\n";
//                        erroeLog.add(error);
//                 //   }
                } catch (Exception e) {
//                    dataIsCorrect = false;
//                    String error = "Mistake in row : " + (b) + " and column 5 : cell should contain Father Name\n";
//                    erroeLog.add(error);
                }


                //5 Village
                try {
                    String aa = (getStringCelldata(row, 5));
                    if (aa.trim().length() < 1) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + (b) + " and column 6 : cell should contain Village name\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 6 : cell should contain Village name\n";
                    erroeLog.add(error);
                }


                //6 Tehlsi
                try {
                    String aa = (getStringCelldata(row, 6));
                    if (aa.trim().length() < 1) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + (b) + " and column 7 : cell should contain Tehlsi\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 7 : cell should contain Tehlsi\n";
                    erroeLog.add(error);
                }


                //7 Mobile number
                try {
                    String aa = (getStringCelldata(row, 7));
                    if (aa.trim().length() < 1) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + (b) + " and column 8 : cell should contain Mobile number\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 8 : cell should contain Mobile number\n";
                    erroeLog.add(error);
                }


                //  8  Model name
                try {
                    String model = (getStringCelldata(row, 8));
                    String query1 = "select * from models where model_name='" + model + "'";
                    boolean modelfound = false;

                    ResultSet set1 = connection.createStatement().executeQuery(query1);
                    while (set1.next()) {
                        modelfound = true;
                    }
                    if (!modelfound) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + b + " and column 9 : Model not found\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 9 : cell should contain Model name\n";
                    erroeLog.add(error);
                }


                //9 Model number
                try {
                    String aa = (getStringCelldata(row, 9));
                    if (aa.trim().length() < 1) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + (b) + " and column 10 : cell should contain Model number\n";
                        erroeLog.add(error);
                    }
                } catch (Exception e) {
                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 10 : cell should contain Model number\n";
                    erroeLog.add(error);
                }


                //10 Engine number
                try {
                    String aa = (getStringCelldata(row, 10));
                    /*if (aa.trim().length() < 1) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + (b) + " and column 11 : cell should contain Engine number\n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                    /*dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 11 : cell should contain Engine number\n";
                    erroeLog.add(error);*/
                }


                //11 File number
                try {
                    String aa = (getStringCelldata(row, 11));
                   /* if (aa.trim().length() < 1) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + (b) + " and column 12 : cell should contain File number\n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                   /* dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 12 : cell should contain File number\n";
                    erroeLog.add(error);*/
                }

                // 12 service date yyyy
                date = "";
                try {
                    String dateyyy1 = getStringCelldata(row, 12);
                    int py1 = Integer.parseInt(dateyyy1);

                   /* if ((py1 + "").trim().length() != 4) {
                        String error = "Mistake in row : " + (b) + " and column 13 : cell should contain Year in 'YYYY' pattern\n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
/*                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 13 : cell should contain Year in 'YYYY' pattern\n";
                    erroeLog.add(error);
              */
                }


                //  13   date of service date MM
                try {
                    int pm = Integer.parseInt((getStringCelldata(row, 13)));
                    date = date + "-" + pm;
                  /*  if (pm <= 0 || pm > 12) {
                        String error = "Mistake in row : " + b + " and column 14 : cell should contain month in 'MM' pattern\n";
                        row.getCell(1).setCellStyle(ErrorCellstyle);
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                   /* dataIsCorrect = false;
                    String error = "Mistake in row : " + b + " and column 14 : cell should contain month in 'MM' pattern\n";
                    row.getCell(1).setCellStyle(ErrorCellstyle);
                    erroeLog.add(error);*/
                }


                //  14   date of service dd
                try {
                    int pd = Integer.parseInt((getStringCelldata(row, 14)));
                    date = date + "-" + pd;


                   /* if (pd <= 0 || pd > 31) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + b + " and column 15 : cell should contain day in 'DD' pattern\n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                   /* dataIsCorrect = false;
                    String error = "Mistake in row : " + b + " and column 15 : cell should contain day in 'DD' pattern\n";
                    erroeLog.add(error);*/
                }

                //15 Customer Feedback
                try {
                    String aa = (getStringCelldata(row, 15));
                   /* if (aa.trim().length() < 1) {
                        dataIsCorrect = false;
                        String error = "Mistake in row : " + (b) + " and column 16 : cell should contain Customer Feedback\n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                  /*  dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 16 : cell should contain Customer Feedback\n";
                    erroeLog.add(error);*/
                }

                //16  Service Feedback
                try {
                    String aa = (getStringCelldata(row, 16));
                    /*if (aa.trim().length() < 1) {
                        String error = "Mistake in row : " + (b) + " and column 16 : cell should contain Service Feedback \n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                   /* dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 4 : cell should contain Service Feedback\n";
                    erroeLog.add(error);*/
                }

                //17  Operator Name
                try {
                    String aa = (getStringCelldata(row, 17));
                   /* if (aa.trim().length() < 1) {
                        String error = "Mistake in row : " + (b) + " and column 17 : cell should contain Operator name \n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                    /*dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 17 : cell should contain Operator name\n";
                    erroeLog.add(error);
                */
                }


                //18  Operator number
                try {
                    String aa = (getStringCelldata(row, 18));
/*                    if (aa.trim().length() < 1) {
                        String error = "Mistake in row : " + (b) + " and column 18 : cell should contain Operator number \n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
/*                    dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 18 : cell should contain Operator number\n";
                    erroeLog.add(error);*/
                }

                //19  Job card number
                try {
                    String aa = (getStringCelldata(row, 19));
                    /*if (aa.trim().length() < 1) {
                        String error = "Mistake in row : " + (b) + " and column 19 : cell should contain Job card number \n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                    /*dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 19 : cell should contain Job card number\n";
                    erroeLog.add(error);*/
                }

                //20  Chassis number
                try {
                    String aa = (getStringCelldata(row, 20));
                    /*if (aa.trim().length() < 1) {
                        String error = "Mistake in row : " + (b) + " and column 19 : cell should contain Job card number \n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                    /*dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 19 : cell should contain Job card number\n";
                    erroeLog.add(error);*/
                }

                //21  Registration number
                try {
                    String aa = (getStringCelldata(row, 21));
                    /*if (aa.trim().length() < 1) {
                        String error = "Mistake in row : " + (b) + " and column 19 : cell should contain Job card number \n";
                        erroeLog.add(error);
                    }*/
                } catch (Exception e) {
                    /*dataIsCorrect = false;
                    String error = "Mistake in row : " + (b) + " and column 19 : cell should contain Job card number\n";
                    erroeLog.add(error);*/
                }

            }


            if (erroeLog.size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            String error = "";
            for (int ii = 0; ii < erroeLog.size(); ii++) {
                error = error + erroeLog.get(ii);
            }
            alert.setContentText(error);
            alert.showAndWait();
        } else {

            CustomerDao d = new CustomerDao();

            boolean b = false;

            for (int i = 0; i < totalitemInXl; i++) {
                row = sheet.getRow(i + 1);

                String date;
                String date1;

                // 0  date of sale yyyy
                date = "";
                String dateyyy = getStringCelldata(row, 0);

                int py = Integer.parseInt(dateyyy);
                date = date + py;


                //  1  date of sale MM

                int pm = Integer.parseInt((getStringCelldata(row, 1)));
                date = date + "-" + pm;


                //  2   date of sale dd

                int pd = Integer.parseInt((getStringCelldata(row, 2)));
                date = date + "-" + pd;
                dto.setDate_of_sale(date);
                System.out.println("Date" + pd);


                //3 Customer name

                String cust_name = (getStringCelldata(row, 3));
                System.out.println("Customer Name" + cust_name);
                dto.setCustomer_name(cust_name);


                //  4  father name

                String father_name = (getStringCelldata(row, 4));
                System.out.println("father name" + father_name);
                dto.setFather_name(father_name);


                //5 Village

                String village = (getStringCelldata(row, 5));
                System.out.println("Village" + village);
                dto.setVillage(village);


                //6 tehlsi

                String tehlsi = (getStringCelldata(row, 6));
                System.out.println("tehlsi" + tehlsi);
                dto.setTehlsi(tehlsi);


                //7 Mobile number

                String mobile_no = (getStringCelldata(row, 7));
                System.out.println("mobile_no" + mobile_no);
                dto.setMobile_number(mobile_no);


                //8 model

                String model = (getStringCelldata(row, 8));
                System.out.println("model" + model);
                dto.setModel(model);


                //9 model_number

                String model_number = (getStringCelldata(row, 9));
                System.out.println("model_number" + model_number);
                dto.setModel_number(model_number);


                //10 engine_number

                String engine_number = (getStringCelldata(row, 10));
                System.out.println("engine_number" + engine_number);
                dto.setEngine_number(engine_number);


                //11 file_number

                String file_number = (getStringCelldata(row, 11));
                System.out.println("file_number" + file_number);
                dto.setFile_number(file_number);


                //12 service date year
                date1 = "";
                String dateyyy1 = getStringCelldata(row, 12);

                int py1 = Integer.parseInt(dateyyy1);
                date1 = date1 + py1;


                //  13 service date month

                int pm1 = Integer.parseInt((getStringCelldata(row, 13)));
                date1 = date1 + "-" + pm1;


                //  14  service date day

                int pd1 = Integer.parseInt((getStringCelldata(row, 14)));
                date1 = date1 + "-" + pd1;
                dto.setService_date(date1);
                System.out.println("Date" + pd1);


                //15 feedback

                String feedback = (getStringCelldata(row, 15));
                System.out.println("feedback" + feedback);
                dto.setFeedback(feedback);


                //16 service feedback

                String service_feedback = (getStringCelldata(row, 16));
                System.out.println("service_feedback" + service_feedback);
                dto.setService_feedback(service_feedback);


                // 17 operator name

                String operator_name = (getStringCelldata(row, 17));
                System.out.println("operator name" + operator_name);
                dto.setOperator_name(operator_name);


                // 18 operator number

                String operator_number = (getStringCelldata(row, 18));
                System.out.println("operator_number" + operator_number);
                dto.setOperator_number(operator_number);


                // 19 job card number

                String job_card_number = (getStringCelldata(row, 19));
                System.out.println("job_card" + job_card_number);
                dto.setJob_card_number(job_card_number);

                // 20 job card number

                String chassis_number = (getStringCelldata(row, 20));
                System.out.println("chassis" + chassis_number);
                dto.setChassis_number(chassis_number);


                // 21 job card number

                String registrtion_number = (getStringCelldata(row, 21));
                System.out.println("registration" + registrtion_number);
                dto.setRegistration_number(registrtion_number);


                b = d.insert(dto);
            }

            if (b == true) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setContentText("Details Imported to Database");
                a.showAndWait();
                Customers cs = new Customers();
               // cs.view();
            }
        }
    }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
