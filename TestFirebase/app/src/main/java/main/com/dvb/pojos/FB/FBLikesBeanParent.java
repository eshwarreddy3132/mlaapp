package main.com.dvb.pojos.FB;

import java.util.ArrayList;

/**
 * Created by AIA on 11/18/16.
 */

public class FBLikesBeanParent {
    public ArrayList<FBLikeBean> fbLikeBeen;
    public String beforeCursor;
    public String afterCursor;
    public String likeNext;
    public int totalCount;
    public boolean canLike;
    public boolean hasLiked;


    public ArrayList<FBLikeBean> getFbLikeBeen() {
        return fbLikeBeen;
    }

    public void setFbLikeBeen(ArrayList<FBLikeBean> fbLikeBeen) {
        this.fbLikeBeen = fbLikeBeen;
    }

    public String getBeforeCursor() {
        return beforeCursor;
    }

    public void setBeforeCursor(String beforeCursor) {
        this.beforeCursor = beforeCursor;
    }

    public String getAfterCursor() {
        return afterCursor;
    }

    public void setAfterCursor(String afterCursor) {
        this.afterCursor = afterCursor;
    }

    public String getLikeNext() {
        return likeNext;
    }

    public void setLikeNext(String likeNext) {
        this.likeNext = likeNext;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isCanLike() {
        return canLike;
    }

    public void setCanLike(boolean canLike) {
        this.canLike = canLike;
    }

    public boolean isHasLiked() {
        return hasLiked;
    }

    public void setHasLiked(boolean hasLiked) {
        this.hasLiked = hasLiked;
    }
}
