package com.cibertec.amplyfm.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistTagResponse implements Parcelable {

    @SerializedName("tag")
    @Expose
    private List<ArtistTag> tag = null;

    public List<ArtistTag> getTag() {
        return tag;
    }

    public void setTag(List<ArtistTag> tag) {
        this.tag = tag;
    }


    protected ArtistTagResponse(Parcel in) {
        if (in.readByte() == 0x01) {
            tag = new ArrayList<ArtistTag>();
            in.readList(tag, ArtistTag.class.getClassLoader());
        } else {
            tag = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (tag == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tag);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ArtistTagResponse> CREATOR = new Parcelable.Creator<ArtistTagResponse>() {
        @Override
        public ArtistTagResponse createFromParcel(Parcel in) {
            return new ArtistTagResponse(in);
        }

        @Override
        public ArtistTagResponse[] newArray(int size) {
            return new ArtistTagResponse[size];
        }
    };
}