package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

//The class information generates the pages including privacy policy and contact information of the "Bürger" application.
public class information extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    //The method generates the information page containing the two options privacy policy and contact information with a back button.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openMain());
    }
    //The method opens the class MainActivity.
        public void openMain(){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
}