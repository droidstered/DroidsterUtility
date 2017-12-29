package me.droidsterutility;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static void customLogV(String tag, String logStatement) {
        Log.v(tag, "" + logStatement);
    }

    public static void customToast(Context context, String toastStatement) {
        Toast.makeText(context, toastStatement, Toast.LENGTH_SHORT).show();
    }

    public static void storeStateOfString(SharedPreferences mediaPrefs,
                                          String prefsKeys, String prefsValue) {
        SharedPreferences.Editor prefEditor = mediaPrefs.edit();
        prefEditor.putString(prefsKeys, prefsValue);
        prefEditor.commit();
    }

    public static void storeStateOfInteger(SharedPreferences mediaPrefs,
                                           String prefsKeys, int prefsValue) {
        SharedPreferences.Editor prefEditor = mediaPrefs.edit();
        prefEditor.putInt(prefsKeys, prefsValue);
        prefEditor.commit();
    }

    public static boolean validMail(String emailstring) {
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(emailstring);
        return emailMatcher.matches();
    }

    public static void showAlertDialog(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title.length() > 0) {
            builder.setTitle(title);
        }

        builder.setMessage(message).setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public static void hideSoftKeyboard(Activity activity) {

        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), 0);
        } else {
        }

    }

    public static ArrayList<String> splitString(String text) {
        return splitString(text, 1000);
    }

    public static ArrayList<String> splitString(String text, int sliceSize) {
        ArrayList<String> textList = new ArrayList<String>();
        String aux;
        int left = -1, right = 0;
        int charsLeft = text.length();
        while (charsLeft != 0) {
            left = right;
            if (charsLeft >= sliceSize) {
                right += sliceSize;
                charsLeft -= sliceSize;
            } else {
                right = text.length();
                aux = text.substring(left, right);
                charsLeft = 0;
            }
            aux = text.substring(left, right);
            textList.add(aux);
        }
        return textList;
    }

    public static void splitAndLog(String tag, String text) {
        ArrayList<String> messageList = Util.splitString(text);
        for (String message : messageList) {
            Log.d(tag, message);
        }
    }

    public static String getFormatedDate(long seconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified
        // format.

        long newmillisec = seconds * 1000;

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in
        // milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(newmillisec);
        return formatter.format(calendar.getTime());
    }

    public static String getDeviceId(Context con) {
        String DeviceId = "";
        DeviceId = Settings.Secure.getString(con.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return DeviceId;

    }

    public static String getIndianCurrencyFormat(String str) {

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        String result = format.format(new BigDecimal(str));
        result = result.replace("Rs.", "");
        result = result.replace(".00", "");
        result = result.replace("₹", "");

        return result.trim();

    }

    public static void expand(final View v) {
        v.measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(800);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setDuration(800);
        v.startAnimation(a);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static boolean checkInternetConnection(Context context, String network_error_message) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Toast.makeText(context, network_error_message, Toast.LENGTH_LONG);
            return false;
        }
    }

}

