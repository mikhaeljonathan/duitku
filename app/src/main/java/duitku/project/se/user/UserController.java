package duitku.project.se.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import duitku.project.se.database.DuitkuContract.UserEntry;
import duitku.project.se.firebase.FirebaseWriter;

import java.util.HashMap;

public class UserController {

    private final Context context;

    public UserController(Context context) {
        this.context = context;
    }

    public void addUser(User user) {
        ContentValues values = convertUserToContentValues(user);
        context.getContentResolver().insert(UserEntry.CONTENT_URI, values);
    }

    public void updateUser(User user) {
        ContentValues values = convertUserToContentValues(user);
        context.getContentResolver().update(UserEntry.CONTENT_URI, values, null, null);
        new FirebaseWriter(context).writeUser();
    }

    public void deleteUser() {
        context.getContentResolver().delete(UserEntry.CONTENT_URI, null, null);
    }

    public User getUser() {
        User ret = null;

        Cursor data = context.getContentResolver().query(UserEntry.CONTENT_URI, getFullProjection(), null, null, null);
        if (data.moveToFirst()) {
            ret = convertCursorToUser(data);
        }

        return ret;
    }

    public String[] getFullProjection() {
        return new String[]{UserEntry.COLUMN_ID,
                UserEntry.COLUMN_USER_NAME,
                UserEntry.COLUMN_USER_EMAIL,
                UserEntry.COLUMN_USER_STATUS,
                UserEntry.COLUMN_USER_FIRST_TIME,
                UserEntry.COLUMN_USER_PASSCODE};
    }

    public boolean isPremium() {
        User user = getUser();
        if (user == null) return false;
        return user.getUser_status().equals(UserEntry.TYPE_PREMIUM);
    }

    private User convertCursorToUser(Cursor data) {
        int userIdColumnIndex = data.getColumnIndex(UserEntry.COLUMN_ID);
        int userNameColumnIndex = data.getColumnIndex(UserEntry.COLUMN_USER_NAME);
        int userEmailColumnIndex = data.getColumnIndex(UserEntry.COLUMN_USER_EMAIL);
        int userStatusColumnIndex = data.getColumnIndex(UserEntry.COLUMN_USER_STATUS);
        int userFirstTimeColumnIndex = data.getColumnIndex(UserEntry.COLUMN_USER_FIRST_TIME);
        int userPasscodeColumnIndex = data.getColumnIndex(UserEntry.COLUMN_USER_PASSCODE);

        String id = data.getString(userIdColumnIndex);
        String name = data.getString(userNameColumnIndex);
        String email = data.getString(userEmailColumnIndex);
        String status = data.getString(userStatusColumnIndex);
        String firstTime = data.getString(userFirstTimeColumnIndex);
        String passcode = data.getString(userPasscodeColumnIndex);

        return new User(id, name, email, status, firstTime, passcode);
    }

    private ContentValues convertUserToContentValues(User user) {
        ContentValues ret = new ContentValues();

        ret.put(UserEntry.COLUMN_ID, user.get_id());
        ret.put(UserEntry.COLUMN_USER_NAME, user.getUser_name());
        ret.put(UserEntry.COLUMN_USER_EMAIL, user.getUser_email());
        ret.put(UserEntry.COLUMN_USER_STATUS, user.getUser_status());
        ret.put(UserEntry.COLUMN_USER_FIRST_TIME, user.getUser_first_time());
        ret.put(UserEntry.COLUMN_USER_PASSCODE, user.getUser_passcode());

        return ret;
    }

    public HashMap<String, Object> convertUserToHashMap(User user) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(UserEntry.COLUMN_ID, user.get_id());
        hashMap.put(UserEntry.COLUMN_USER_NAME, user.getUser_name());
        hashMap.put(UserEntry.COLUMN_USER_EMAIL, user.getUser_email());
        hashMap.put(UserEntry.COLUMN_USER_STATUS, user.getUser_status());
        hashMap.put(UserEntry.COLUMN_USER_FIRST_TIME, user.getUser_first_time());
        hashMap.put(UserEntry.COLUMN_USER_PASSCODE, user.getUser_passcode());
        return hashMap;
    }

}
