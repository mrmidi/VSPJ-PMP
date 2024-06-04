package net.mrmidi.pmp.factorialthreads;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText numberInput;
    private Button calculateButton;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        numberInput = findViewById(R.id.numberInput);
        calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.resultTextView);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateFactorial();
            }
        });
    }

    private void calculateFactorial() {
        String inputStr = numberInput.getText().toString();
        if (!inputStr.isEmpty()) {
            try {
                int number = Integer.parseInt(inputStr);
                final boolean[] calculating = {true};

                // Thread for updating the "Calculating..." message
                Thread updateMessageThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int dotCount = 0;
                        while (calculating[0]) {
                            int finalDotCount = dotCount;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    resultTextView.setText("Calculating" + new String(new char[finalDotCount % 4]).replace("\0", "."));
                                }
                            });
                            try {
                                Thread.sleep(300); // Wait for 1 second
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            dotCount++;
                        }
                    }
                });
                updateMessageThread.start();

                // Thread for factorial calculation
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        long result = factorial(number);
                        calculating[0] = false; // Stop the "Calculating..." message thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                resultTextView.setText(String.format(Locale.getDefault(), "Factorial: %d", result));
                            }
                        });
                    }
                }).start();
            } catch (NumberFormatException e) {
                resultTextView.setText("Invalid input");
            }
        } else {
            resultTextView.setText("Please enter a number");
        }
    }


    private long factorial(int n) {
        if (n == 0) {
            return 1;
        }
        long result = 1;
        for (int i = 1; i <= n; i++) {
            result *= i;
            // artificially slow down the calculation to make the "Calculating..." message visible
            try {
                Thread.sleep(100); // Wait for 0.1 second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}