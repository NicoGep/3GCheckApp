package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

//The class detail generates the detailed view of the scanned certificates.
public class detail extends AppCompatActivity {

    private ImageButton backButton;

    @Override
    // The method generates the page including the detailed view of the scanned certificates with a back button.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openMain());

    }
    // The method opens the class MainActivity.
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}