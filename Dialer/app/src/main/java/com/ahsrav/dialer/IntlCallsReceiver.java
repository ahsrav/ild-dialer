package com.ahsrav.dialer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class IntlCallsReceiver extends BroadcastReceiver {

    private static final String TAG = "IntlCallsReceiver";
    private Context context;
    private static final String INTL_NUMBER_PREFIX = "011";
    private static final String DEFAULT_NUMBER = "0000000000";

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            Log.i(TAG, "Outgoing call received");
            this.context = context;

            String originalNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            // Only handle international calls
            if (originalNumber.startsWith("+") && !originalNumber.startsWith("+1")) {
                String processedNumber = processNumber(originalNumber);
                setResultData(getNumberToDial(processedNumber));
            } else {
                setResultData(originalNumber);
            }
        }
    }

    /**
     * This method replaces the leading '+' in international numbers with '011' which is the prefix
     * used by most long distance providers
     * @param originalNumber number with or without leading '+'
     * @return processedNumber
     */
    private String processNumber(String originalNumber) {

        String processedNumber;
        if (originalNumber.startsWith("+")) {
            originalNumber = originalNumber.substring(1);
        }
        processedNumber = INTL_NUMBER_PREFIX.concat(originalNumber);
        return processedNumber;
    }

    /**
     * This method adds the access number and language number (if any) to the phone number
     * @param processedNumber
     * @return number with accessNumber and languageNumber from shared preferences
     */
    private String getNumberToDial(String processedNumber) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DialingNumbers", Context.MODE_PRIVATE);
        String accessNumber = sharedPreferences.getString("accessNumber", DEFAULT_NUMBER);
        String languageNumber = sharedPreferences.getString("languageNumber", DEFAULT_NUMBER);
        if (languageNumber.equals(DEFAULT_NUMBER) || languageNumber.equals("")) {
            return (accessNumber + "," + processedNumber);
        }
        return (accessNumber + "," + languageNumber + "," + processedNumber);
    }
}
