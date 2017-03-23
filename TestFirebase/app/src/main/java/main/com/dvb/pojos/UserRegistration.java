package main.com.dvb.pojos;

/**
 * Created by artre on 2/17/2017.
 */

public class UserRegistration {

    public String mobile;
    public String emailid;
    public String place;
    public String gender;
    public String dateofBorth;
    public String fcmid;
    public String getFcmid() {
        return fcmid;
    }

    public void setFcmid(String fcmid) {
        this.fcmid = fcmid;
    }



    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String fullname;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateofBorth() {
        return dateofBorth;
    }

    public void setDateofBorth(String dateofBorth) {
        this.dateofBorth = dateofBorth;
    }


}

