package softgalli.gurukulshikshalay.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shankar on 4/6/2018.
 */

public class GalleryImages implements Parcelable {


    protected GalleryImages(Parcel in) {
        id = in.readString();
        image = in.readString();
        description = in.readString();
        album_id = in.readString();
        status = in.readString();
        precedence = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(description);
        dest.writeString(album_id);
        dest.writeString(status);
        dest.writeString(precedence);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GalleryImages> CREATOR = new Creator<GalleryImages>() {
        @Override
        public GalleryImages createFromParcel(Parcel in) {
            return new GalleryImages(in);
        }

        @Override
        public GalleryImages[] newArray(int size) {
            return new GalleryImages[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getStatus() {
        return status;
    }

    public String getPrecedence() {
        return precedence;
    }

    @SerializedName("id")
    String id;
    @SerializedName("image")
    String image;
    @SerializedName("description")
    String description;
    @SerializedName("album_id")
    String album_id;
    @SerializedName("status")
    String status;
    @SerializedName("precedence")
    String precedence;


}
