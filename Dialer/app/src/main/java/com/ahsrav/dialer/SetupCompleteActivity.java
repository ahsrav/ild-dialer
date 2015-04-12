package com.ahsrav.dialer;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class SetupCompleteActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_complete);

        String providerName = getIntent().getStringExtra("providerName");
        TextView setupCompleteTV = (TextView) findViewById(R.id.setup_complete_text);
        setupCompleteTV.setText(providerName + getString(R.string.setup_complete_1) +
                getString(R.string.setup_complete_2));

        Button doneButton = (Button) findViewById(R.id.setup_done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }

}
