package main.com.dvb.pojos.twitter;

/**
 * Created by AIA on 12/19/16.
 */

import com.google.gson.annotations.SerializedName;

public class Entities {

    @SerializedName("media")
    private MediaList mediaList;

    public MediaList getMediaList() {
        return mediaList;
    }

    public void setMediaList(MediaList mediaList) {
        this.mediaList = mediaList;
    }
//    private Media media;
//
//    public Media getMedia() {
//        return media;
//    }
//
//    public void setMedia(Media media) {
//        this.media = media;
//    }

}
