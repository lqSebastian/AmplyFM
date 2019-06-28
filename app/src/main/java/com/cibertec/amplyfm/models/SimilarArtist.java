package com.cibertec.amplyfm.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SimilarArtist implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image")
    @Expose
    private List<ImageItem> image = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ImageItem> getImage() {
        return image;
    }

    public void setImage(List<ImageItem> image) {
        this.image = image;
    }


    protected SimilarArtist(Parcel in) {
        name = in.readString();
        url = in.readString();
        if (in.readByte() == 0x01) {
            image = new ArrayList<ImageItem>();
            in.readList(image, ImageItem.class.getClassLoader());
        } else {
            image = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(url);
        if (image == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(image);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<SimilarArtist> CREATOR = new Parcelable.Creator<SimilarArtist>() {
        @Override
        public SimilarArtist createFromParcel(Parcel in) {
            return new SimilarArtist(in);
        }

        @Override
        public SimilarArtist[] newArray(int size) {
            return new SimilarArtist[size];
        }
    };
}