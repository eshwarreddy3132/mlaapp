package main.com.dvb.pojos.FB;

import java.util.ArrayList;

/**
 * Created by AIA on 11/16/16.
 */

public class FacebookBeanParent {
    public String previous;
    public String next;
    public ArrayList<FacebookBean> facebookBeanArrayList;

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public ArrayList<FacebookBean> getFacebookBeanArrayList() {
        return facebookBeanArrayList;
    }

    public void setFacebookBeanArrayList(ArrayList<FacebookBean> facebookBeanArrayList) {
        this.facebookBeanArrayList = facebookBeanArrayList;
    }
}
