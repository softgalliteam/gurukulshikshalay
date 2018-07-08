package softgalli.gurukulshikshalay.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpcomingActivityModel implements Serializable {
    @SerializedName("title")
    String upcomingEvetTitle;
    @SerializedName("message")
    String upcomingEvetDescription;
    @SerializedName("date")
    String upcomingEventDate;

    public void setIsDescShown(boolean isDescShown) {
        this.isDescShown = isDescShown;
    }

    @SerializedName("posted_by")

    String upcomingEventPostedBy;
    @SerializedName("status")
    String upcomingEventStatus;

    public String getUpcomingEvetTitle() {
        return upcomingEvetTitle;
    }

    public String getUpcomingEvetDescription() {
        return upcomingEvetDescription;
    }

    public String getUpcomingEventDate() {
        return upcomingEventDate;
    }

    public String getUpcomingEventPostedBy() {
        return upcomingEventPostedBy;
    }

    public String getUpcomingEventStatus() {
        return upcomingEventStatus;
    }

    public boolean isDescShown() {
        return isDescShown;
    }

    boolean isDescShown;
}
