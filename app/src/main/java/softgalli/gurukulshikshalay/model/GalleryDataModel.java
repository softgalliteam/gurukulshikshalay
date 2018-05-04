package softgalli.gurukulshikshalay.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Shankar on 3/18/2018.
 */

public class GalleryDataModel implements Parcelable {


    protected GalleryDataModel(Parcel in) {
        id = in.readString();
        album_id = in.readString();
        album_name = in.readString();
        status = in.readString();
        precedence = in.readString();
        images = in.createTypedArrayList(GalleryImages.CREATOR);
    }

    public static final Creator<GalleryDataModel> CREATOR = new Creator<GalleryDataModel>() {
        @Override
        public GalleryDataModel createFromParcel(Parcel in) {
            return new GalleryDataModel(in);
        }

        @Override
        public GalleryDataModel[] newArray(int size) {
            return new GalleryDataModel[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public String getStatus() {
        return status;
    }

    public String getPrecedence() {
        return precedence;
    }

    public ArrayList<GalleryImages> getImages() {
        return images;
    }

    @SerializedName("id")
    String id;
    @SerializedName("album_id")
    String album_id;
    @SerializedName("album_name")
    String album_name;
    @SerializedName("status")
    String status;
    @SerializedName("precedence")
    String precedence;
    @SerializedName("images")
    private ArrayList<GalleryImages> images;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(album_id);
        dest.writeString(album_name);
        dest.writeString(status);
        dest.writeString(precedence);
        dest.writeTypedList(images);
    }
}
