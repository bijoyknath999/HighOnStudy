package highonstudy.com.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class NotificationDbController {

    private final SQLiteDatabase db;

    private static final String READ = "read", UNREAD = "unread";

    public NotificationDbController(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
    }

    public boolean insertNotifyData(String title, String content, String url) {
        ContentValues cv = new ContentValues();

        cv.put(DbConstants.COLUMN_TITLE, title);
        cv.put(DbConstants.COLUMN_CONTENT, content);
        cv.put(DbConstants.COLUMN_URL, url);
        cv.put(DbConstants.COLUMN_READ_STATUS, UNREAD);
        long result = db.insert(DbConstants.TABLE_NAME_NOTIFY,null, cv);
        return result != -1;
    }

    public Cursor readAllNotifyData(){
        String query = "SELECT * FROM " + DbConstants.TABLE_NAME_NOTIFY;

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public ArrayList<notifymodel> getAllData() {


        String[] projection = {
                DbConstants.COLUMN_ID,
                DbConstants.COLUMN_TITLE,
                DbConstants.COLUMN_CONTENT,
                DbConstants.COLUMN_URL,
                DbConstants.COLUMN_READ_STATUS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DbConstants.COLUMN_ID + " DESC";

        Cursor c = db.query(
                DbConstants.TABLE_NAME_NOTIFY,  // The table name to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return fetchData(c);
    }

    public ArrayList<notifymodel> getUnreadData() {


        String[] projection = {
                DbConstants.COLUMN_ID,
                DbConstants.COLUMN_TITLE,
                DbConstants.COLUMN_CONTENT,
                DbConstants.COLUMN_URL,
                DbConstants.COLUMN_READ_STATUS
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DbConstants.COLUMN_ID + " DESC";
        String selection = DbConstants.COLUMN_READ_STATUS + "=?";
        String[] selectionArgs = {UNREAD};

        Cursor c = db.query(
                DbConstants.TABLE_NAME_NOTIFY,  // The table name to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return fetchData(c);
    }

    private ArrayList<notifymodel> fetchData(Cursor c) {
        ArrayList<notifymodel> ntyDataArray = new ArrayList<>();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    // get  the  data into array,or class variable
                    int itemId = c.getInt(c.getColumnIndexOrThrow(DbConstants.COLUMN_ID));
                    String title = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_TITLE));
                    String message = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_CONTENT));
                    String contentUrl = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_URL));
                    String status = c.getString(c.getColumnIndexOrThrow(DbConstants.COLUMN_READ_STATUS));

                    boolean isUnread = !status.equals(READ);

                    // wrap up data list and return
                    ntyDataArray.add(new notifymodel(itemId, title, message, contentUrl,isUnread));
                } while (c.moveToNext());
            }
            c.close();
        }
        return ntyDataArray;
    }

    public void updateStatus(int itemId, boolean read) {

        String readStatus = UNREAD;
        if (read) {
            readStatus = READ;
        }

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(DbConstants.COLUMN_READ_STATUS, readStatus);

        // Which row to update, based on the ID
        String selection = DbConstants.COLUMN_ID + "=?";
        String[] selectionArgs = {String.valueOf(itemId)};

        db.update(
                DbConstants.TABLE_NAME_NOTIFY,
                values,
                selection,
                selectionArgs);
    }

    public void deleteNotification(String itemId,Context context) {
        long result = db.delete(DbConstants.TABLE_NAME_NOTIFY, "_id=?", new String[]{itemId});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAllNotification() {
        db.execSQL("DELETE FROM " + DbConstants.TABLE_NAME_NOTIFY);
    }

    public void updateStatus(String id, String title, String url, String content,Context context) {

        ContentValues cv = new ContentValues();
        cv.put(DbConstants.COLUMN_TITLE, title);
        cv.put(DbConstants.COLUMN_URL, url);
        cv.put(DbConstants.COLUMN_CONTENT, content);

        long result = db.update(DbConstants.TABLE_NAME_NOTIFY, cv, "_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }
}
