package main.com.dvb.pojos.twitter;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AIA on 12/19/16.
 */

import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("media_url")
    private String media_url;

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }
}
