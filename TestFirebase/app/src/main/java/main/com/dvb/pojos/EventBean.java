package main.com.dvb.pojos;

/**
 * Created by AIA on 12/27/16.
 */

public class EventBean {
    public String title_event;
    public String message_event;
    public String date_event;
    public int id;
    public boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_event() {
        return title_event;
    }

    public void setTitle_event(String title_event) {
        this.title_event = title_event;
    }

    public String getMessage_event() {
        return message_event;
    }

    public void setMessage_event(String message_event) {
        this.message_event = message_event;
    }

    public String getDate_event() {
        return date_event;
    }

    public void setDate_event(String date_event) {
        this.date_event = date_event;
    }
}
