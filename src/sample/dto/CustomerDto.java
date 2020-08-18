package sample.dto;

public class CustomerDto {

    private String date_of_sale;
    private String customer_name;
    private String father_name;
    private String village;
    private String tehlsi;
    private String mobile_number;
    private String model;
    private String model_number;
    private String engine_number;
    private String file_number;
    private String service_date;
    private String feedback;
    private int id;

    public CustomerDto() {
        date_of_sale = "";
        customer_name = "";
        father_name = "";
        village = "";
        tehlsi = "";
        mobile_number = "";
        model = "";
        model_number = "";
        engine_number = "";
        file_number = "";
        service_date = "";
        feedback = "";
        id = 0;
    }

    public String getDate_of_sale() {
        return date_of_sale;
    }

    public void setDate_of_sale(String date_of_sale) {
        this.date_of_sale = date_of_sale;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getTehlsi() {
        return tehlsi;
    }

    public void setTehlsi(String tehlsi) {
        this.tehlsi = tehlsi;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel_number() {
        return model_number;
    }

    public void setModel_number(String model_number) {
        this.model_number = model_number;
    }

    public String getEngine_number() {
        return engine_number;
    }

    public void setEngine_number(String engine_number) {
        this.engine_number = engine_number;
    }

    public String getFile_number() {
        return file_number;
    }

    public void setFile_number(String file_number) {
        this.file_number = file_number;
    }

    public String getServce_date(){ return  service_date;}

    public void setService_date(String service_date){ this.service_date = service_date; }

    public String getFeedback(){ return  feedback;}

    public void setFeedback(String feedback){ this.feedback = feedback; }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString() {
        return "CustomerDto{" +
                "date_of_sale='" + date_of_sale + '\'' +
                ", customer_name='" + customer_name + '\'' +
                ", father_name='" + father_name + '\'' +
                ", service_date='" + service_date + '\'' +
                ", village='" + village + '\'' +
                ", tehlsi=" + tehlsi +
                ", mobile_number=" + mobile_number +
                ", model=" + model +
                ", model_number=" + model_number +
                ", engine_number=" + engine_number +
                ", file_number='" + file_number + '\'' +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}


