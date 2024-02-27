package net.mrmdi.pmp.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ResultActivity extends AppCompatActivity {

    // declare variables
    private int weight;
    private int height;
    private double bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // get weight and height
        weight = getIntent().getIntExtra("weight", 0);
        height = getIntent().getIntExtra("height", 0);


    }

    // calculate BMI
    private void calculateBMI() {
        float heightInMeters = (float) height / 100;
        bmi = (double) weight / (heightInMeters * heightInMeters);
    }
}