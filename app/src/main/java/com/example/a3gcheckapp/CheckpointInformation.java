package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

/**
 * The class CheckpointInformation generates the pages including privacy policy and contact CitizenInformation of the "Prüfstelle" application.
 */
public class CheckpointInformation extends AppCompatActivity {

    private ImageButton back;

    /**
     * The method generates the CitizenInformation page containing the two options privacy policy and contact CitizenInformation with a back button.
     *
     * @param savedInstanceState The saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkpoint_information);

        back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(v -> openCheckpoint());
    }

    /**
     * The method opens the class CitizenMainActivity.
     */
    public void openCheckpoint() {
        Intent intent = new Intent(this, CheckpointMainActivity.class);
        startActivity(intent);
    }
}