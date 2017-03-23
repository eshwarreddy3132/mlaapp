package main.com.dvb.pojos.FB;

/**
 * Created by AIA on 11/18/16.
 */

public class FBCommentBean {
    public String createdTime;
    public String userName;
    public String userNameId;
    public String message;
    public String commentId;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNameId() {
        return userNameId;
    }

    public void setUserNameId(String userNameId) {
        this.userNameId = userNameId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
}
