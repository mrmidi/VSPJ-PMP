package net.mrmdi.pmp.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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

            // display ResultActivity using intent

            // Intent is a messaging object you can use to request an action from another app component.

            Intent bmiIntent = new Intent(this, ResultActivity.class);
            bmiIntent.putExtra("weight", weight);
            bmiIntent.putExtra("height", height);
            startActivity(bmiIntent); // start the activity


        });
    }



}