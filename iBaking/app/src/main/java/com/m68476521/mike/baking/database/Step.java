package com.m68476521.mike.baking.database;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Bean Step used for recipes. It contains
 * id
 * short description
 * description
 * videoUrl
 * thumbnailUrl
 */

public class Step implements Parcelable {

    public static final Parcelable.Creator<Step> CREATOR = new Parcelable.Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel parcel) {
            return new Step(parcel);
        }

        @Override
        public Step[] newArray(int i) {
            return new Step[i];
        }
    };

    private int mId;
    private String sortDescription;
    private String description;
    private String video;
    private String image;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getSortDescription() {
        return sortDescription;
    }

    public void setSortDescription(String sortDescription) {
        this.sortDescription = sortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Step() {
    }

    private Step(Parcel in) {
        mId = in.readInt();
        sortDescription = in.readString();
        description = in.readString();
        video = in.readString();
        image = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(sortDescription);
        parcel.writeString(description);
        parcel.writeString(video);
        parcel.writeString(image);
    }
}
