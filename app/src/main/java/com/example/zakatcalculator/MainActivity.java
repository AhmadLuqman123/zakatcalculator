package com.example.zakatcalculator;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    EditText goldWeightEditText, goldValueEditText;
    RadioGroup typeRadioGroup;
    Button calculateButton, aboutButton, themeButton, shareButton, clearButton;  // Added clearButton
    TextView totalValueTextView, zakatPayableTextView, totalZakatTextView, errorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply the theme before setting the layout
        applySavedTheme();

        setContentView(R.layout.activity_main);

        goldWeightEditText = findViewById(R.id.goldWeightKeepEditText);
        goldValueEditText = findViewById(R.id.goldValueEditText);
        calculateButton = findViewById(R.id.calculateButton);
        aboutButton = findViewById(R.id.aboutButton);
        themeButton = findViewById(R.id.themeButton);
        shareButton = findViewById(R.id.shareButton);  // Initialize the shareButton
        clearButton = findViewById(R.id.clearButton);  // Initialize the clearButton
        totalValueTextView = findViewById(R.id.totalValueTextView);
        errorMessageTextView = findViewById(R.id.errorMessageTextView);  // Find error message TextView

        calculateButton.setOnClickListener(v -> calculateZakat());

        aboutButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        themeButton.setOnClickListener(v -> changeTheme());

        // Set onClickListener for the share button
        shareButton.setOnClickListener(v -> shareApp());

        // Set onClickListener for the clear button
        clearButton.setOnClickListener(v -> clearInputs());
    }

    private void applySavedTheme() {
        int currentTheme = getSharedPreferences("themePrefs", MODE_PRIVATE)
                .getInt("theme", R.style.Theme_Zakatcalculator); // Default theme is light
        setTheme(currentTheme);
    }

    private void changeTheme() {
        // Existing theme changing logic
    }

    @SuppressLint("SetTextI18n")
    private void calculateZakat() {
        String weightKeepStr = goldWeightEditText.getText().toString();
        String weightWearStr = goldWeightEditText.getText().toString();
        String valueStr = goldValueEditText.getText().toString();

        // Validate the inputs
        if (weightKeepStr.isEmpty() || weightWearStr.isEmpty() || valueStr.isEmpty()) {
            // Show the error message TextView
            errorMessageTextView.setVisibility(View.VISIBLE);
            return; // Stop further execution if any field is empty
        } else {
            // Hide the error message if all inputs are valid
            errorMessageTextView.setVisibility(View.GONE);
        }

        // Convert input strings to double
        double goldWeightKeep = Double.parseDouble(weightKeepStr);
        double goldWeightWear = Double.parseDouble(weightWearStr);
        double goldValue = Double.parseDouble(valueStr);

        // Constants for uruf thresholds
        double urufKeep = 85.0; // Threshold for keeping
        double urufWear = 200.0; // Threshold for wearing

        // Calculations for gold keeping
        double goldValueKeep = goldWeightKeep * goldValue;
        double zakatPayableKeep = (goldWeightKeep > urufKeep) ? (goldWeightKeep - urufKeep) * goldValue : 0;
        double totalZakatKeep = zakatPayableKeep * 0.025;

        // Calculations for gold wearing
        double goldValueWear = goldWeightWear * goldValue;
        double zakatPayableWear = (goldWeightWear > urufWear) ? (goldWeightWear - urufWear) * goldValue : 0;
        double totalZakatWear = zakatPayableWear * 0.025;

        // Display results
        String result = "Gold Keeping:\n" +
                "Total Value: RM " + goldValueKeep + "\n" +
                "Zakat Payable: RM " + zakatPayableKeep + "\n" +
                "Total Zakat: RM " + totalZakatKeep + "\n\n" +
                "Gold Wearing:\n" +
                "Total Value: RM " + goldValueWear + "\n" +
                "Zakat Payable: RM " + zakatPayableWear + "\n" +
                "Total Zakat: RM " + totalZakatWear;

        totalValueTextView.setText(result);
    }

    // Method to clear the input fields and hide the error message
    private void clearInputs() {
        // Clear the input fields
        goldWeightEditText.setText("");
        goldWeightEditText.setText("");
        goldValueEditText.setText("");


        // Hide the error message if it's visible
        errorMessageTextView.setVisibility(View.GONE);

        // Optionally, reset any displayed results
        totalValueTextView.setText("");
    }

    // Method to share the app
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String shareBody = "Check out this awesome app for calculating zakat: " +
                "https://github.com/AhmadLuqman123/zakatcalculator.git";  // Replace with your actual app link
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Zakat Calculator App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }
}

