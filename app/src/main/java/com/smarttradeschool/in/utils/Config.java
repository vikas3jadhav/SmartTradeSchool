package com.smarttradeschool.in.utils;

/**
 * Created by Acer on 12/11/2016.
 */
public class Config {
    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String DISCUSSION_NOTIFICATION = "discussionNotification";
    public static final String SECURITY_NOTIFICATION = "securityNotification";
    public static final String Dashboard_Refresh = "refreshDashboard";
    public static final String StaffAttendance_Refresh1 = "staff_attendance_date1";
    public static final String StaffAttendance_Refresh2 = "staff_attendance_date2";
    public static final String StudentAttendance_Refresh1 = "student_attendance_date1";
    public static final String StudentAttendance_Refresh2 = "student_attendance_date2";
    public static final String MyAttendanceStaff_Refresh1 = "my_attendance_staff_date1";
    public static final String MyAttendanceStaff_Refresh2 = "my_attendance_staff_date2";
    public static final String MyAttendanceStudent_Refresh1 = "my_attendance_student_date1";
    public static final String MyAttendanceStudent_Refresh2 = "my_attendance_student_date2";
    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";
}
