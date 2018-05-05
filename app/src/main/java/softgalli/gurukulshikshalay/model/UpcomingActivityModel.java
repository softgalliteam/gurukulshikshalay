package softgalli.gurukulshikshalay.model;

public class UpcomingActivityModel {
    String upcomingActivityTitle;
    String upcomingActivityDate;

    public String getUpcomingActivityTitle() {
        return upcomingActivityTitle;
    }

    public UpcomingActivityModel(String upcomingActivityTitle, String upcomingActivityDate) {
        this.upcomingActivityTitle = upcomingActivityTitle;
        this.upcomingActivityDate = upcomingActivityDate;
    }

    public String getUpcomingActivityDate() {
        return upcomingActivityDate;
    }

}
