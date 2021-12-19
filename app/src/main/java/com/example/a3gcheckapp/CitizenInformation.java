package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

//The class CitizenInformation generates the pages including privacy policy and contact information of the "Bürger" application.
public class CitizenInformation extends AppCompatActivity {

    private ImageButton backButton;

    /**
     * The method generates the CitizenInformation page containing the two options privacy policy and contact information with a back button.
     *
     * @param savedInstanceState The saved state of the instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openCitizenMainActivity());
    }

    /**
     * The method opens the class CitizenMainActivity.
     */
        public void openCitizenMainActivity(){
            Intent intent = new Intent(this, CitizenMainActivity.class);
            startActivity(intent);
        }
}