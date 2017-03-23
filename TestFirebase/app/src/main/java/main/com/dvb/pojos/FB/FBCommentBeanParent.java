package main.com.dvb.pojos.FB;

import java.util.ArrayList;

/**
 * Created by AIA on 11/18/16.
 */

public class FBCommentBeanParent {
    public ArrayList<FBCommentBean> commentsList;
    public String before;
    public String after;
    public String next;
    public String order;
    public int totalCount;
    public boolean canComment;

    public ArrayList<FBCommentBean> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(ArrayList<FBCommentBean> commentsList) {
        this.commentsList = commentsList;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isCanComment() {
        return canComment;
    }

    public void setCanComment(boolean canComment) {
        this.canComment = canComment;
    }
}
