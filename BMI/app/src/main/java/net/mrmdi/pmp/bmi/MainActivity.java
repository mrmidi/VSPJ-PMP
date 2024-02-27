package net.mrmdi.pmp.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /*
    * BMI Calculator
    * Height	Weight Range	BMI	Considered
    * 5′ 9″	124 lbs or less	Below 18.5	Underweight
    * 125 lbs to 168 lbs	18.5 to 24.9	Healthy weight
    * 169 lbs to 202 lbs	25.0 to 29.9	Overweight
    * 203 lbs or more	30 or higher	Obesity
    * 271 lbs or more	40 or higher	Class 3 Obesity
    * values from https://www.cdc.gov/obesity/basics/adult-defining.html
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // process button click
        findViewById(R.id.buttonCalculate).setOnClickListener(v -> {
            int weight = 0;
            int height = 0;
            // get weight and height
            try {
                weight = Integer.parseInt(((EditText) findViewById(R.id.editTextWeight)).getText().toString());
                height = Integer.parseInt(((EditText) findViewById(R.id.editTextHeight)).getText().toString());
            } catch (NumberFormatException e) {
                // display error in popup
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Please enter valid weight and height")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }
//            int weight = Integer.parseInt(((EditText) findViewById(R.id.editTextWeight)).getText().toString());
//            int height = Integer.parseInt(((EditText) findViewById(R.id.editTextHeight)).getText().toString());

            // calculate BMI
            float heightInMeters = (float) height / 100;
            double bmi = 0.0;
            try {
                bmi = (double) weight / (heightInMeters * heightInMeters);
            } catch (ArithmeticException e) {
                // display error in popup
                new android.app.AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Please enter valid weight and height")
                        .setPositiveButton("OK", null)
                        .show();
                return;
            }

            // if bmi is not zero, display chart
            if (bmi != 0.0) {
                // display BMI in popup
                new android.app.AlertDialog.Builder(this)
                        .setTitle("BMI")
                        .setMessage("Your BMI is " + bmi)
                        .setPositiveButton("OK", null)
                        .show();

// Format the BMI value to 2 decimal places
                String bmiText = String.format(Locale.US, "%.2f", bmi);

// Determine the BMI category and corresponding color
                String category;
                int color;
                if (bmi < 18.5) {
                    category = "Underweight";
                    color = Color.BLUE;
                } else if (bmi <= 24.9) {
                    category = "Normal";
                    color = Color.GREEN;
                } else if (bmi <= 29.9) {
                    category = "Overweight";
                    color = Color.rgb(255, 165, 0); // Orange
                } else if (bmi < 40) {
                    category = "Obesity";
                    color = Color.RED;
                } else {
                    category = "Class 3 Obesity";
                    color = Color.rgb(139, 0, 0); // Dark red
                }

// Get references to the TextViews
                TextView textViewBMIResult = findViewById(R.id.textViewBMIResult);
                TextView textViewBMICategory = findViewById(R.id.textViewBMICategory);

// Set the text and color for the BMI value
                textViewBMIResult.setText(bmiText);
                textViewBMIResult.setTextColor(color);

// Set the text for the BMI category label
                textViewBMICategory.setText(category);


            }


        });
    }


}