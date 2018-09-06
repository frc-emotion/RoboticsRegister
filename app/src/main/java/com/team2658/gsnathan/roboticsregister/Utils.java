package com.team2658.gsnathan.roboticsregister;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import org.apache.commons.lang3.ArrayUtils;

import java.io.File;



public class Utils {

    //basically show a toast message on android
    public static void showToast(String msg, int time, Context context) {
        CharSequence text = msg;
        int duration = time;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    //navigate to url
    public static Intent webIntent(String url) {
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }

    //navigate to other pages
    public static Intent actIntent(Context context, Class<?> cls) {
        return new Intent(context, cls);
    }

    //merge two arrays to one
    public static String[] mergeArrays(String[] first, String[] last) {
        return ArrayUtils.addAll(first, last);
    }

    //create an email
    public static Intent emailIntent(String emailAddress, String subject, String text) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        return Intent.createChooser(email, "Send email...");
    }

    //create an email with an attachment
    public static Intent emailIntent(String emailAddress, String subject, String text, File file) {
        Uri path = Uri.fromFile(file);
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        email.putExtra(Intent.EXTRA_STREAM, path);
        return Intent.createChooser(email, "Send email...");
    }

    //create an email with an attachments
    public static Intent emailIntent(String emailAddress, String subject, String text, File[] attachments) {
        Uri path1 = Uri.fromFile(attachments[0]);
        Uri path2 = Uri.fromFile(attachments[1]);
        Intent email = new Intent(Intent.ACTION_SEND);
        email.setType("text/email");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, text);
        email.putExtra(Intent.EXTRA_STREAM, path1);
        email.putExtra(Intent.EXTRA_STREAM, path2);
        return Intent.createChooser(email, "Send email...");
    }

}
