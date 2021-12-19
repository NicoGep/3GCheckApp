package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;

//The class CheckpointMainActivity contains the main logic of our "Prüfstelle" application.
public class CheckpointMainActivity extends AppCompatActivity {

    private ImageButton informationBtn;
    private ImageView informationBackground;
    private ImageButton checkButton;

    /**
     * The method generates the overview page of the application "Prüfstelle".
     *
     * @param savedInstanceState The saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint);

        informationBtn = (ImageButton) findViewById(R.id.checkInformationButton);
        informationBtn.setOnClickListener(v -> openInformation());

        informationBackground = (ImageView) findViewById(R.id.checkInformationBackground);

        checkButton = (ImageButton) findViewById(R.id.certificateCheckButton);
        checkButton.setOnClickListener(v -> openValidation());
    }

    /**
     * The method opens the CitizenInformation page.
     */
    public void openInformation(){
        Intent intent = new Intent(this, CheckpointInformation.class);
        startActivity(intent);

    }

    /**
     * The method opens the scan page
     */
    public void openValidation(){
        Intent intent = new Intent(this, CheckpointScan.class);
        startActivity(intent);
    }
}