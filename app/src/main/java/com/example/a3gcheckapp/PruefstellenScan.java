package com.example.a3gcheckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;


public class PruefstellenScan extends AppCompatActivity {

    private CodeScanner pruefCodeScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruefstellen_scan);

        CodeScannerView codeScannerView = findViewById(R.id.scanner_view);
        pruefCodeScan = new CodeScanner(this, codeScannerView);
        pruefCodeScan.setFormats(CodeScanner.TWO_DIMENSIONAL_FORMATS);
        pruefCodeScan.setScanMode(ScanMode.SINGLE);

        pruefCodeScan.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(PruefstellenScan.this, result.getText(), Toast.LENGTH_SHORT).show();
                        pruefCodeScan.stopPreview();

                        //PopUp Layout Inflater
                    }
                });
            }
        });
        codeScannerView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){pruefCodeScan.startPreview();}
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        pruefCodeScan.startPreview();
    }

    @Override
    protected void onPause() {
        pruefCodeScan.releaseResources();
        super.onPause();
    }
}