package highonstudy.com.data.sqlite;

import android.provider.BaseColumns;

public class DbConstants implements BaseColumns {

    public static final String TABLE_NAME_NOTIFY = "highonstudy_notify";
    public static final String TABLE_NAME_SAVED = "highonstudy_savedjobs";



    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_READ_STATUS = "read_status";
    public static final String NOT_COLUMN_ID = "not_id";
    public static final String NOT_COLUMN_TITLE = "not_title";
    public static final String NOT_COLUMN_CONTENT = "not_content";
    public static final String NOT_COLUMN_URL = "not_url";
    public static final String NOT_COLUMN_LASTDATE = "not_last_date";



    public static final String SQL_CREATE_NOTIFICATION_ENTRIES =
            "CREATE TABLE " + TABLE_NAME_NOTIFY +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_URL + " TEXT, " +
                    COLUMN_READ_STATUS + " TEXT" + ");";

    public static final String SQL_DELETE_NOTIFICATION_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME_NOTIFY;

    public static final String SQL_CREATE_SAVED_JOBS_ENTRIES =
            "CREATE TABLE " + TABLE_NAME_SAVED +
                    " (" + NOT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    NOT_COLUMN_TITLE + " TEXT, " +
                    NOT_COLUMN_CONTENT + " TEXT, " +
                    NOT_COLUMN_URL + " TEXT, " +
                    NOT_COLUMN_LASTDATE + " TEXT" + ");";
    public static final String SQL_DELETE_SAVED_JOBS_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME_SAVED;

}
