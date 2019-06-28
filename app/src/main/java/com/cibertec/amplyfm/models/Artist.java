package com.cibertec.amplyfm.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Artist implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("mbid")
    @Expose
    private String mbid;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image")
    @Expose
    private List<ImageItem> image = null;
    @SerializedName("streamable")
    @Expose
    private String streamable;
    @SerializedName("ontour")
    @Expose
    private String ontour;
    @SerializedName("stats")
    @Expose
    private ArtistStats stats;

    @SerializedName("similar")
    @Expose
    private SimilarArtistResponse similar;
    @SerializedName("tags")
    @Expose
    private ArtistTagResponse tags;
    @SerializedName("bio")
    @Expose
    private Bio bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMbid() {
        return mbid;
    }

    public void setMbid(String mbid) {
        this.mbid = mbid;
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

    public String getStreamable() {
        return streamable;
    }

    public void setStreamable(String streamable) {
        this.streamable = streamable;
    }

    public String getOntour() {
        return ontour;
    }

    public void setOntour(String ontour) {
        this.ontour = ontour;
    }

    public ArtistStats getStats() {
        return stats;
    }

    public void setStats(ArtistStats stats) {
        this.stats = stats;
    }

    public SimilarArtistResponse getSimilar() {
        return similar;
    }

    public void setSimilar(SimilarArtistResponse similar) {
        this.similar = similar;
    }

    public ArtistTagResponse getTags() {
        return tags;
    }

    public void setTags(ArtistTagResponse tags) {
        this.tags = tags;
    }

    public Bio getBio() {
        return bio;
    }

    public void setBio(Bio bio) {
        this.bio = bio;
    }


    protected Artist(Parcel in) {
        name = in.readString();
        mbid = in.readString();
        url = in.readString();
        if (in.readByte() == 0x01) {
            image = new ArrayList<ImageItem>();
            in.readList(image, ImageItem.class.getClassLoader());
        } else {
            image = null;
        }
        streamable = in.readString();
        ontour = in.readString();
        stats = (ArtistStats) in.readValue(ArtistStats.class.getClassLoader());
        similar = (SimilarArtistResponse) in.readValue(SimilarArtistResponse.class.getClassLoader());
        tags = (ArtistTagResponse) in.readValue(ArtistTagResponse.class.getClassLoader());
        bio = (Bio) in.readValue(Bio.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(mbid);
        dest.writeString(url);
        if (image == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(image);
        }
        dest.writeString(streamable);
        dest.writeString(ontour);
        dest.writeValue(stats);
        dest.writeValue(similar);
        dest.writeValue(tags);
        dest.writeValue(bio);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Artist> CREATOR = new Parcelable.Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };
}