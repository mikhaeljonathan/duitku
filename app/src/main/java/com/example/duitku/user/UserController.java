package com.example.duitku.user;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.duitku.database.DuitkuContract.UserEntry;

public class UserController {

    private final Context context;

    public UserController(Context context){
        this.context = context;
    }

    public void setPasscode(String passcode){
        User user = getUser();
        user.setPasscode(passcode);
        ContentValues values = convertUserToContentValues(user);

        context.getContentResolver().update(UserEntry.CONTENT_URI, values, null, null);
    }

    public User getUser(){
        User ret = null;

        Cursor data = context.getContentResolver().query(UserEntry.CONTENT_URI, getFullProjection(), null, null, null);
        if (data.moveToFirst()){
            ret = convertCursorToUser(data);
        }

        return ret;
    }

    public String[] getFullProjection(){
        return new String[]{UserEntry.COLUMN_ID,
                UserEntry.COLUMN_USER_NAME,
                UserEntry.COLUMN_USER_EMAIL,
                UserEntry.COLUMN_USER_STATUS,
                UserEntry.COLUMN_USER_FIRST_TIME,
                UserEntry.COLUMN_USER_PASSCODE};
    }

    private User convertCursorToUser(Cursor data){
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

    private ContentValues convertUserToContentValues(User user){
        ContentValues ret = new ContentValues();

        ret.put(UserEntry.COLUMN_ID, user.getId());
        ret.put(UserEntry.COLUMN_USER_NAME, user.getName());
        ret.put(UserEntry.COLUMN_USER_EMAIL, user.getEmail());
        ret.put(UserEntry.COLUMN_USER_STATUS, user.getStatus());
        ret.put(UserEntry.COLUMN_USER_FIRST_TIME, user.getFirstTime());
        ret.put(UserEntry.COLUMN_USER_PASSCODE, user.getPasscode());

        return ret;
    }

}
