package net.mrmdi.pmp.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ResultActivity extends AppCompatActivity {

    // declare variables
    private int weight;
    private int height;
    private double bmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // add listener to buttonConfirm (close the activity)
        findViewById(R.id.buttonConfirm).setOnClickListener(v -> finish());

        // get weight and height
        weight = getIntent().getIntExtra("weight", 0);
        height = getIntent().getIntExtra("height", 0);

        calculateBMI();
        // display BMI

        List<String> bmiCategories = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.bmi_categories)));

        if (bmi > 0.0f) {
            String bmiText = String.format(Locale.getDefault(), "%.2f", bmi); // format to 2 decimal places
            String category = getCategory((ArrayList<String>) bmiCategories);

            int color = getColor();
            ((android.widget.TextView) findViewById(R.id.textViewBMIResult)).setText(bmiText);
            ((android.widget.TextView) findViewById(R.id.textViewBMICategory)).setText(category);
            ((android.widget.TextView) findViewById(R.id.textViewBMICategory)).setTextColor(color);
        }
    }

    // calculate BMI
    private void calculateBMI() {
        float heightInMeters = (float) height / 100;
        try {
            bmi = (double) weight / (heightInMeters * heightInMeters);
        } catch (Exception e) { // to handle divide by zero
            bmi = 0;
            // display error in popup
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Please enter valid weight and height")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private String getCategory(ArrayList<String> bmiCategories) {
        if (bmi == 0.0) {
            return bmiCategories.get(0);
        } else if (bmi < 18.5) {
            return bmiCategories.get(1);
        } else if (bmi < 24.9) {
            return bmiCategories.get(2);
        } else if (bmi < 29.9) {
            return bmiCategories.get(3);
        } else {
            return bmiCategories.get(4);
        }
    }

    private int getColor() {
        int color;
        if (bmi < 18.5) {
            return Color.BLUE;
        } else if (bmi <= 24.9) {
            return Color.GREEN;
        } else if (bmi <= 29.9) {
            return Color.rgb(255, 165, 0); // Orange
        } else if (bmi < 40) {
            return Color.RED;
        }
            return Color.rgb(139, 0, 0); // Dark red
    }
}