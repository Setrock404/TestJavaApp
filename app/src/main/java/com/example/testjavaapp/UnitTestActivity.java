package com.example.testjavaapp;

import android.os.Bundle;

import com.example.testjavaapp.util.BitmapResizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class UnitTestActivity extends AppCompatActivity {

    private EditText et_viewSize;
    private EditText et_bitmapSize;
    private TextView tv_result;

    int viewSize, bitmapSize;
    String viewSizeText;
    String bitmapSizeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_test);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        initUI();
    }

    private void initUI() {
        et_viewSize = findViewById(R.id.et_view_size);
        et_bitmapSize = findViewById(R.id.et_bitmap_size);
        tv_result = findViewById(R.id.tv_res_size);
    }

    // FUNCTIONS THAT VALIDATES INPUT AND SELECTS MIN NUMBER
    public void calculateBitmapSize(View view) {
        if (checkInput()) {
            String res = getString(R.string.result_size_text) + BitmapResizer.resizeBitmap(viewSize, bitmapSize);
            tv_result.setText(res);
        }
    }

    private boolean checkInput() {
        viewSizeText = et_viewSize.getText().toString();
        bitmapSizeText = et_bitmapSize.getText().toString();
        if (inputIsCorrect(viewSizeText) && inputIsCorrect(bitmapSizeText)) {
            viewSize = Integer.parseInt(viewSizeText);
            bitmapSize = Integer.parseInt(bitmapSizeText);
            return true;
        } else {
            tv_result.setText(R.string.wrong_input);
            return false;
        }
    }

    private boolean inputIsCorrect(String text) {
        return (!text.trim().isEmpty() && Integer.parseInt(text) > 0);
    }
}
