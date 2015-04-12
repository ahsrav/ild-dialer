package com.ahsrav.dialer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends ActionBarActivity {

    private static final String TAG = "SettingsActivity";
    private static final int SETUP_REQUEST_CODE = 1;
    private List<ProviderObject> providers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Get providers data from JSON file
        List<String> providerNames = new ArrayList<>();
        try {
            providers = getProvidersList();
        } catch (JSONException e) {
            Log.e(TAG, "JSONException", e);
        }

        // Get providers names to display in ListView
        for (int i = 0; i < providers.size(); i++) {
            providerNames.add(providers.get(i).name);
        }

        ListView providersList = (ListView) findViewById(R.id.providersListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, providerNames);
        providersList.setAdapter(adapter);

        providersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Store info in SharedPrefs
                SharedPrefsUtil.storeInSharedPrefs(getApplicationContext(), providers.get(position));

                // Start SetupCompleteActivity
                Intent intent = new Intent(getApplicationContext(), SetupCompleteActivity.class);
                intent.putExtra("providerName", providers.get(position).name);
                startActivityForResult(intent, SETUP_REQUEST_CODE);
            }
        });


        Button doneButton = (Button) findViewById(R.id.doneButton);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Store info in SharedPrefs
                EditText accessNumber = (EditText) findViewById(R.id.accessNumber);
                EditText languageNumber = (EditText) findViewById(R.id.languageNumber);
                ProviderObject provider = new ProviderObject();
                provider.accessNumber = accessNumber.getText().toString();
                provider.languageNumber = languageNumber.getText().toString();

                if (provider.accessNumber.length() > 5 && provider.accessNumber.length() < 13) {
                    SharedPrefsUtil.storeInSharedPrefs(getApplicationContext(), provider);

                    // Start SetupCompleteActivity
                    Intent intent = new Intent(getApplicationContext(), SetupCompleteActivity.class);
                    intent.putExtra("providerName", "Custom");
                    startActivityForResult(intent, SETUP_REQUEST_CODE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
        }
    }

    /**
     * This method converts the string JSON to an ArrayList of type ProviderObject
     * @return ArrayList of providers
     * @throws JSONException
     */
    private List<ProviderObject> getProvidersList() throws JSONException {
        JSONObject obj = new JSONObject(loadJSONFromAsset());
        JSONArray jsonArray = obj.getJSONArray("providers");
        List<ProviderObject> providersList= new ArrayList<>();
        ProviderObject provider;

        for (int i = 0; i < jsonArray.length(); i++)
        {
            provider = new ProviderObject();
            JSONObject jo_inside = jsonArray.getJSONObject(i);
            provider.name = jo_inside.getString("name");
            provider.accessNumber = jo_inside.getString("accessNumber");
            provider.languageNumber = jo_inside.getString("languageNumber");

            providersList.add(provider);
        }

        return providersList;
    }

    /**
     * This method loads the JSON file from the assets package
     * @return json in String format
     */
    private String loadJSONFromAsset() {
        String json;
        try {

            InputStream is = getAssets().open("providersList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
}
