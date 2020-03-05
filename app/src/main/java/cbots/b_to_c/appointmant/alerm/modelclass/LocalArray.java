package cbots.b_to_c.appointmant.alerm.modelclass;

public class LocalArray {


    private String name,time,ordernumber,id,value,mobilenumber,address;
    public LocalArray(){

    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getOrdernumber() {
        return ordernumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrdernumber(String ordernumber) {
        this.ordernumber = ordernumber;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
