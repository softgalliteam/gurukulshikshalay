package softgalli.gurukulshikshalay.retrofit;

import softgalli.gurukulshikshalay.BuildConfig;

public class ApiUrl {

    /*#################################### FOR GURUKUL ######################################*/
    /*public static final String BASE_URL = "http://apextechies.com/gurukul/index.php/";
    public static final String IMAGE_BASE_URL = "http://www.gurukulshikshalay.com/";

    public static String PLAYSTORE_LINK = "https://play.google.com/store/apps/details?id=softgalli.gurukulshikshalay";
    public static String FACEBOOK_LINK = "https://www.facebook.com/gurukulsikshalay/";
    public static String MAIN_URL = "http://www.gurukulshikshalay.com/";
    public static String ADMISSION_API = "takeAdmission.php";*/


    /*#################################### FOR SPS NAWALSHAHI ##############################*/
    public static final String BASE_URL = "http://apextechies.com/sps/index.php/";
    public static final String IMAGE_BASE_URL = "http://spsnawalshahi.com/";

    public static String PLAYSTORE_LINK = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
    public static String FACEBOOK_LINK = "https://www.facebook.com/groups/189555905134765/";
    public static String MAIN_URL = "http://spsnawalshahi.com/";
    public static String ADMISSION_API = "takeAdmission.php";


    /*#################################### COMMON LINKS ##############################*/
    public static final String NOTIFICATION = "get_notification";
    public static final String APPLYLEAVE = "applyLeave";
    public static final String LEAVEREQUESTLIST = "leave_RequestList";
    public static final String UPDATELEAVE = "updateLeave";
    public static final String TEACHERLIST = "getTeacherList";
    public static final String ADDTEACHER = "add_teacher";
    public static final String ADDSTUDENT = "add_student";
    public static final String GALLERYLIST = "get_gallery";
    public static final String ALUMNI = "get_alumni";
    public static final String TOPPERLIST = "get_topperList";
    public static final String FEEDBACK = "add_Feedback";
    public static final String LOGIN = "login";
    public static final String UPDATETEACHER = "update_Teacher";
    public static final String UPDATESTUDENT = "update_Student";

    public static final String TIME_TABLE = "get_TimeTable";
    public static final String EXAM_SCHEDULE = "get_ExanSchedule";
    public static final String GET_ATTENDANCE = "getAttendance";
    public static final String UPLOAD_ATTENDANCE = "uploadAttendance";
    public static final String GET_CLASS_WISE_STUDENT_LIST = "getClassWiseStudentsList";
    public static final String GET_RESULT = "getResult";
    public static final String ADD_RESULT = "addResult";
}
