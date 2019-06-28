package com.cibertec.amplyfm.models.Image;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageResponse implements Parcelable {

    @SerializedName("_type")
    @Expose
    private String type;

    @SerializedName("readLink")
    @Expose
    private String readLink;

    @SerializedName("webSearchUrl")
    @Expose
    private String webSearchUrl;


    @SerializedName("totalEstimatedMatches")
    @Expose
    private Integer totalEstimatedMatches;
    @SerializedName("nextOffset")
    @Expose
    private Integer nextOffset;
    @SerializedName("currentOffset")
    @Expose
    private Integer currentOffset;
    @SerializedName("value")
    @Expose
    private List<Value> value = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getReadLink() {
        return readLink;
    }

    public void setReadLink(String readLink) {
        this.readLink = readLink;
    }

    public String getWebSearchUrl() {
        return webSearchUrl;
    }

    public void setWebSearchUrl(String webSearchUrl) {
        this.webSearchUrl = webSearchUrl;
    }


    public Integer getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    public void setTotalEstimatedMatches(Integer totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }

    public Integer getNextOffset() {
        return nextOffset;
    }

    public void setNextOffset(Integer nextOffset) {
        this.nextOffset = nextOffset;
    }

    public Integer getCurrentOffset() {
        return currentOffset;
    }

    public void setCurrentOffset(Integer currentOffset) {
        this.currentOffset = currentOffset;
    }

    public List<Value> getValue() {
        return value;
    }

    public void setValue(List<Value> value) {
        this.value = value;
    }


    protected ImageResponse(Parcel in) {
        type = in.readString();
        readLink = in.readString();
        webSearchUrl = in.readString();
        totalEstimatedMatches = in.readByte() == 0x00 ? null : in.readInt();
        nextOffset = in.readByte() == 0x00 ? null : in.readInt();
        currentOffset = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            value = new ArrayList<Value>();
            in.readList(value, Value.class.getClassLoader());
        } else {
            value = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(readLink);
        dest.writeString(webSearchUrl);
        if (totalEstimatedMatches == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(totalEstimatedMatches);
        }
        if (nextOffset == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(nextOffset);
        }
        if (currentOffset == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(currentOffset);
        }
        if (value == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(value);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ImageResponse> CREATOR = new Parcelable.Creator<ImageResponse>() {
        @Override
        public ImageResponse createFromParcel(Parcel in) {
            return new ImageResponse(in);
        }

        @Override
        public ImageResponse[] newArray(int size) {
            return new ImageResponse[size];
        }
    };
}