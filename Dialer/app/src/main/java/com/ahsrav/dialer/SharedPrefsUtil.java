package com.ahsrav.dialer;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsUtil {

    private static final String TAG = "SharedPrefsUtil";

    public static void storeInSharedPrefs(Context context, ProviderObject provider) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DialingNumbers", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("accessNumber", provider.accessNumber);
        editor.putString("languageNumber", provider.languageNumber);
        editor.apply();
    }
}
