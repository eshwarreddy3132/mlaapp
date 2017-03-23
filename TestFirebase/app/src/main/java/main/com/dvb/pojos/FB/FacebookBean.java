package main.com.dvb.pojos.FB;

/**
 * Created by AIA on 11/15/16.
 */

public class FacebookBean {
    public String userName;
    public String userId;
    public String userMessage;
    public String userCreated_time;
    public String userStory;
    public String picture;
    public FBLikesBeanParent fbLikesBeanParent;
    public FBCommentBeanParent fbCommentBeanParent;

    public FBCommentBeanParent getFbCommentBeanParent() {
        return fbCommentBeanParent;
    }

    public void setFbCommentBeanParent(FBCommentBeanParent fbCommentBeanParent) {
        this.fbCommentBeanParent = fbCommentBeanParent;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public FBLikesBeanParent getFbLikesBeanParent() {
        return fbLikesBeanParent;
    }

    public void setFbLikesBeanParent(FBLikesBeanParent fbLikesBeanParent) {
        this.fbLikesBeanParent = fbLikesBeanParent;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getUserCreated_time() {
        return userCreated_time;
    }

    public void setUserCreated_time(String userCreated_time) {
        this.userCreated_time = userCreated_time;
    }

    public String getUserStory() {
        return userStory;
    }

    public void setUserStory(String userStory) {
        this.userStory = userStory;
    }
}
