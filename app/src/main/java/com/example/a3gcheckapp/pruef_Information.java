package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

//The class information generates the pages including privacy policy and contact information of the "Prüfstelle" application.
public class pruef_Information extends AppCompatActivity {

    private ImageButton back;

    @Override
    //The method generates the information page containing the two options privacy policy and contact information with a back button.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruef_information);

        back = (ImageButton) findViewById(R.id.backButton);
        back.setOnClickListener(v -> openPruef());
    }

    //The method opens the class MainActivity.
    public void openPruef(){
        Intent intent = new Intent(this, pruefstelle.class);
        startActivity(intent);
    }
}