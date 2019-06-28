package com.cibertec.amplyfm.models.Image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Value {

    @SerializedName("webSearchUrl")
    @Expose
    private String webSearchUrl;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumbnailUrl")
    @Expose
    private String thumbnailUrl;
    @SerializedName("datePublished")
    @Expose
    private String datePublished;
    @SerializedName("isFamilyFriendly")
    @Expose
    private Boolean isFamilyFriendly;
    @SerializedName("creativeCommons")
    @Expose
    private String creativeCommons;
    @SerializedName("contentUrl")
    @Expose
    private String contentUrl;
    @SerializedName("hostPageUrl")
    @Expose
    private String hostPageUrl;
    @SerializedName("contentSize")
    @Expose
    private String contentSize;
    @SerializedName("encodingFormat")
    @Expose
    private String encodingFormat;
    @SerializedName("hostPageDisplayUrl")
    @Expose
    private String hostPageDisplayUrl;
    @SerializedName("width")
    @Expose
    private Integer width;
    @SerializedName("height")
    @Expose
    private Integer height;
    @SerializedName("hostPageFavIconUrl")
    @Expose
    private String hostPageFavIconUrl;

    @SerializedName("imageInsightsToken")
    @Expose
    private String imageInsightsToken;


    @SerializedName("imageId")
    @Expose
    private String imageId;
    @SerializedName("accentColor")
    @Expose
    private String accentColor;

    public String getWebSearchUrl() {
        return webSearchUrl;
    }

    public void setWebSearchUrl(String webSearchUrl) {
        this.webSearchUrl = webSearchUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public Boolean getIsFamilyFriendly() {
        return isFamilyFriendly;
    }

    public void setIsFamilyFriendly(Boolean isFamilyFriendly) {
        this.isFamilyFriendly = isFamilyFriendly;
    }

    public String getCreativeCommons() {
        return creativeCommons;
    }

    public void setCreativeCommons(String creativeCommons) {
        this.creativeCommons = creativeCommons;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getHostPageUrl() {
        return hostPageUrl;
    }

    public void setHostPageUrl(String hostPageUrl) {
        this.hostPageUrl = hostPageUrl;
    }

    public String getContentSize() {
        return contentSize;
    }

    public void setContentSize(String contentSize) {
        this.contentSize = contentSize;
    }

    public String getEncodingFormat() {
        return encodingFormat;
    }

    public void setEncodingFormat(String encodingFormat) {
        this.encodingFormat = encodingFormat;
    }

    public String getHostPageDisplayUrl() {
        return hostPageDisplayUrl;
    }

    public void setHostPageDisplayUrl(String hostPageDisplayUrl) {
        this.hostPageDisplayUrl = hostPageDisplayUrl;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getHostPageFavIconUrl() {
        return hostPageFavIconUrl;
    }

    public void setHostPageFavIconUrl(String hostPageFavIconUrl) {
        this.hostPageFavIconUrl = hostPageFavIconUrl;
    }


    public String getImageInsightsToken() {
        return imageInsightsToken;
    }

    public void setImageInsightsToken(String imageInsightsToken) {
        this.imageInsightsToken = imageInsightsToken;
    }



    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }

}