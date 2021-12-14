package com.example.a3gcheckapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;


public class PruefstellenScan extends AppCompatActivity {

    private CodeScanner pruefCodeScan;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruefstellen_scan);

        backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> openMain());

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


                        openMain();

                        /**
                        //PopUp Layout Inflate
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View popupView = inflater.inflate(R.layout.validate_popup, null);

                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        boolean focusable = true;
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                        popupWindow.showAtLocation(findViewById(R.id.scanner_view), Gravity.CENTER, 0, 0);

                        popupView.setOnTouchListener(new View.OnTouchListener() {

                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                popupWindow.dismiss();
                                return true;
                            }
                        });
                         **/
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

    public void openMain(){
        Intent intent = new Intent(this, pruefstelle.class);
        startActivity(intent);
    }
}