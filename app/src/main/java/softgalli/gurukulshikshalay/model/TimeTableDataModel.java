package softgalli.gurukulshikshalay.model;

public class TimeTableDataModel {
    String id, schoolName, aClass, sec, date, from_time, to_time, subject, teacher_name;

    public String getId() {
        return id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public String getaClass() {
        return aClass;
    }

    public String getSec() {
        return sec;
    }

    public String getDate() {
        return date;
    }

    public String getFrom_time() {
        return from_time;
    }

    public String getTo_time() {
        return to_time;
    }

    public String getSubject() {
        return subject;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public TimeTableDataModel(String id, String schoolName, String aClass, String sec, String date, String from_time, String to_time, String subject, String teacher_name) {
        this.id = id;
        this.schoolName = schoolName;
        this.aClass = aClass;
        this.sec = sec;
        this.date = date;
        this.from_time = from_time;
        this.to_time = to_time;
        this.subject = subject;
        this.teacher_name = teacher_name;
    }
}
