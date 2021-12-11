package highonstudy.com.data.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedJobsDbController {

    private final SQLiteDatabase db;

    public SavedJobsDbController(Context context) {
        db = DbHelper.getInstance(context).getWritableDatabase();
    }

    public boolean insertSavedJobsData(String title, String content, String url,String lastdate) {
        ContentValues cv2 = new ContentValues();
        cv2.put(DbConstants.NOT_COLUMN_TITLE, title);
        cv2.put(DbConstants.NOT_COLUMN_CONTENT, content);
        cv2.put(DbConstants.NOT_COLUMN_URL, url);
        cv2.put(DbConstants.NOT_COLUMN_LASTDATE, lastdate);
        long result2 = db.insert(DbConstants.TABLE_NAME_SAVED,null, cv2);
        return result2 != -1;
    }

    public ArrayList<SavedJObsModel> getAllSavedJObsData() {


        String[] projection = {
                DbConstants.NOT_COLUMN_ID,
                DbConstants.NOT_COLUMN_TITLE,
                DbConstants.NOT_COLUMN_CONTENT,
                DbConstants.NOT_COLUMN_URL,
                DbConstants.NOT_COLUMN_LASTDATE,
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder = DbConstants.NOT_COLUMN_ID + " DESC";

        Cursor c = db.query(
                DbConstants.TABLE_NAME_SAVED,  // The table name to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        return fetchData(c);
    }

    private ArrayList<SavedJObsModel> fetchData(Cursor c) {
        ArrayList<SavedJObsModel> favDataArray = new ArrayList<>();

        if (c != null) {
            if (c.moveToFirst()) {
                do {
                    // get  the  data into array,or class variable
                    int itemId = c.getInt(c.getColumnIndexOrThrow(DbConstants.NOT_COLUMN_ID));
                    String postTitle = c.getString(c.getColumnIndexOrThrow(DbConstants.NOT_COLUMN_TITLE));
                    String postContent = c.getString(c.getColumnIndexOrThrow(DbConstants.NOT_COLUMN_CONTENT));
                    String postUrl = c.getString(c.getColumnIndexOrThrow(DbConstants.NOT_COLUMN_URL));
                    String postLastDate = c.getString(c.getColumnIndexOrThrow(DbConstants.NOT_COLUMN_LASTDATE));


                    // wrap up data list and return
                    favDataArray.add(new SavedJObsModel(itemId, postTitle,postContent,postUrl,postLastDate));
                } while (c.moveToNext());
            }
            c.close();
        }
        return favDataArray;
    }


    public Cursor readAllSavedJobsData(){
        String query = "SELECT * FROM " + DbConstants.TABLE_NAME_SAVED;
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void deleteSavedJobs(String url,Context context) {
        // Which row to update, based on the ID
        String selection = DbConstants.NOT_COLUMN_URL + "=?";
        String[] selectionArgs = {String.valueOf(url)};

        db.delete(
                DbConstants.TABLE_NAME_SAVED,
                selection,
                selectionArgs);
    }

    public void deleteAllSavedJobs() {
        db.execSQL("DELETE FROM " + DbConstants.TABLE_NAME_SAVED);
    }

    public void updateSavedJobsStatus(String id, String title, String url, String content,String lasdate,Context context) {

        ContentValues cv = new ContentValues();
        cv.put(DbConstants.NOT_COLUMN_TITLE, title);
        cv.put(DbConstants.NOT_COLUMN_URL, url);
        cv.put(DbConstants.NOT_COLUMN_CONTENT, content);
        cv.put(DbConstants.NOT_COLUMN_LASTDATE, lasdate);


        long result = db.update(DbConstants.TABLE_NAME_SAVED, cv, "not_id=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            //Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkOne(String url) {
        boolean valid = false;
        Cursor res = db.rawQuery("SELECT * from "+DbConstants.TABLE_NAME_SAVED+" where "+DbConstants.NOT_COLUMN_URL+"='" +url + "'", null );
        if (res.getCount()==1)
        {
            valid = true;
        }
        return valid;
    }
}
