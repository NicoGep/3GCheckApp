package com.example.a3gcheckapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

//The class detail generates the detailed view of the scanned certificates.
public class detail extends AppCompatActivity {

    private ImageButton backButton;
    private ImageView trashIcon;

    @Override
    // The method generates the page including the detailed view of the scanned certificates with a back button.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openMain());

        trashIcon = (ImageView) findViewById(R.id.trashIcon);
        trashIcon.setOnClickListener(v -> deleteCertificate());
        Certificate cert;

        //receive the passed certificate object
        Gson gson = new Gson();

        cert = gson.fromJson(getIntent().getStringExtra("vaxCert"), Impfnachweis.class);
        if (cert == null) {
            cert = gson.fromJson(getIntent().getStringExtra("testCert"), Testnachweis.class);
        }
        if (cert == null) {
            cert = gson.fromJson(getIntent().getStringExtra("recCert"), Genesenennachweis.class);
        }


        //check for the type of the certificate and create the respecting views
        if(cert instanceof Impfnachweis){
            Impfnachweis vaxCert = (Impfnachweis) cert;
            createImpfView(vaxCert);
        } else if (cert instanceof Testnachweis){
            Testnachweis testCert = (Testnachweis) cert;
            createTestView(testCert);
        } else if (cert instanceof Genesenennachweis){
            Genesenennachweis recCert = (Genesenennachweis) cert;
            createRecView(recCert);
        }



    }

    private void createRecView(Genesenennachweis recCert) {
    }

    private void createTestView(Testnachweis testCert) {
    }

    private void createImpfView(Impfnachweis vaxCert) {

        //missing TextView for Name

        TextView vaxTextView = new TextView(this);
        vaxTextView.setId(R.id.textView);
        //vaxTextView.setBackgroundResource(R.drawable.certificate_small);
        vaxTextView.setPadding(100 , 123, 0, 330);
        vaxTextView.setTextSize(40);
        vaxTextView.setLineSpacing(75, 0);
        vaxTextView.setText("IMPFNACHWEIS\n");
        vaxTextView.append("Impfdatum: " + vaxCert.getVaccDate() +  "\n");
        vaxTextView.append("Ausgestellt: " + vaxCert.getIssuedate() + "\n");
        vaxTextView.append("Impfstoff: " + "SARS-CoV-2 mRNA" + "\n");
        vaxTextView.append("Hersteller: " + vaxCert.getVaxname() + "\n");
        vaxTextView.append("Impfung: " + vaxCert.getVaxnb() + " ");
    }

    // The method opens the class MainActivity.
    public void openMain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void deleteCertificate(){
        //missing: file deletion

        openMain();

    }

}