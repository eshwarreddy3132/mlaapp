package main.com.dvb.pojos;

/**
 * Created by AIA on 11/2/16.
 */
public class User {
    public String department;
    public long mobile_number;
    public String name;
    public String place;
    public String problem;
    public String subject;
    public String date;
    public String FCM_RegisterID;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String otp;


    public String getFCM_RegisterID() {
        return FCM_RegisterID;
    }

    public void setFCM_RegisterID(String FCM_RegisterID) {
        this.FCM_RegisterID = FCM_RegisterID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public long getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(long mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
